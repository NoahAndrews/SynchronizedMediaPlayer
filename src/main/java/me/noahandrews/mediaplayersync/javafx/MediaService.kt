package me.noahandrews.mediaplayersync.javafx

import me.noahandrews.savpp.MediaSynchronizationClient
import me.noahandrews.savpp.MediaSynchronizationServer
import me.noahandrews.savpp.MediaSynchronizer
import org.apache.logging.log4j.LogManager
import rx.Completable
import java.io.File
import java.net.URI
import java.security.MessageDigest
import javax.xml.bind.annotation.adapters.HexBinaryAdapter

/**
 * MIT License
 *
 *
 * Copyright (c) 2016 Noah Andrews
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

class MediaService(
        private val fileProvider: FileProvider,
        private val mediaPlayerProvider: MediaPlayerProvider,
        private val mediaSynchronizationClientProvider: MediaSynchronizationClientProvider,
        private val mediaSynchronizationServerProvider: MediaSynchronizationServerProvider
) {
    private var mediaPlayer: MediaPlayer? = null
    private var eventHandler: EventHandler? = null
    private var mediaSynchronizer: MediaSynchronizer? = null
    private val logger = LogManager.getLogger()

    fun loadNewMedia() {
        //TODO: We need to make sure any old network objects and media players get torn down when a new file is opened.
        val file = fileProvider.file
        if (file != null) {
            mediaPlayer?.dispose()
            val mediaPlayer = mediaPlayerProvider.getMediaPlayer(file)
            this.mediaPlayer = mediaPlayer
            setupMediaPlayerSubscriptions(mediaPlayer)

            //TODO: If a connection is established, tear it down and replace it
        }
    }

    fun establishConnectionAsGuest(hostname: String): Completable {
        //TODO: We need to make sure any old network objects and media players get torn down when a new connection is established.
        return Completable.fromAction {
            logger.info("Attempting to connect to server at $hostname")

            mediaSynchronizer = mediaSynchronizationClientProvider.getMediaSynchronizationClient(hostname)
            (mediaSynchronizer as MediaSynchronizationClient).connect(getMd5Hash())
        }
    }

    fun establishConnectionAsHost(): Completable {
        //TODO: We need to make sure any old network objects and media players get torn down when a new server is established.
        return Completable.fromAction {
            logger.info("Attempting to start server")

            mediaSynchronizer = mediaSynchronizationServerProvider.getMediaSynchronizationServer(getMd5Hash())
            (mediaSynchronizer as MediaSynchronizationServer).startListening()
        }
    }

    fun play() {
        mediaPlayer?.play()
        eventHandler?.onPlay()
    }

    fun pause() {
        mediaPlayer?.pause()
        eventHandler?.onPause()
    }

    fun seekPercentage(positionPercent: Double) {
        if (mediaPlayer != null) {
            mediaPlayer!!.seekPercent(positionPercent)
            val ms = percentToMs(positionPercent)
            eventHandler?.onPlaybackAdvanced(ms)
        }

    }

    fun setVolumePercentage(volume: Double) {
        mediaPlayer?.setVolumePercent(volume)
    }

    fun setEventHandler(eventHandler: EventHandler) {
        this.eventHandler = eventHandler
    }

    private fun percentToMs(percent: Double): Int {
        val duration = mediaPlayer?.durationMs ?: 0
        return ((percent / 100.0) * duration).toInt()
    }

    private fun getMd5Hash(): String {
        if (mediaPlayer == null) {
            throw IllegalStateException("No media file has been loaded.")
        }
        val mediaUri = URI(mediaPlayer!!.mediaUri)
        val mediaFileBytes = File(mediaUri).readBytes()
        val messageDigest = MessageDigest.getInstance("MD5")
        val md5Bytes = messageDigest.digest(mediaFileBytes) //TODO: If a file is really large, readBytes should be avoided.
        val md5String = HexBinaryAdapter().marshal(md5Bytes)

        logger.info("MD5 hash: $md5String")

        return md5String
    }

    private fun setupMediaPlayerSubscriptions(mediaPlayer: MediaPlayer) {
        mediaPlayer.statusObservable()
                .filter { it == MediaPlayer.Status.READY }
                .subscribe() {
                    eventHandler?.onMediaLoaded(mediaPlayer.durationMs)
                }
        mediaPlayer.currentTimeMsObservable()?.subscribe() { currentTimeMs ->
            if (mediaPlayer.status != MediaPlayer.Status.INITIALIZING) {
                eventHandler?.onPlaybackAdvanced(currentTimeMs)
            }
        }
        mediaPlayer.statusObservable()
                ?.filter { it == MediaPlayer.Status.STOPPED }
                ?.subscribe() { eventHandler?.onStop() }
    }

    interface EventHandler {
        fun onMediaLoaded(durationMs: Int): Unit
        fun onPlay(): Unit
        fun onPause(): Unit
        fun onStop(): Unit
        fun onPlaybackAdvanced(ms: Int): Unit
    }
}