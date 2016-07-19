package me.noahandrews.mediaplayersync.javafx;

import javafx.scene.media.Media;
import javafx.util.Duration;
import rx.Observable;
import rx.observables.JavaFxObservable;

/**
 * MIT License
 * <p>
 * Copyright (c) 2016 Noah Andrews
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

public class MediaPlayerJfxAdapter implements MediaPlayer {
    private javafx.scene.media.MediaPlayer mediaPlayer;

    public MediaPlayerJfxAdapter(String uri) {
        Media media = new Media(uri);
        mediaPlayer = new javafx.scene.media.MediaPlayer(media);
    }

    @Override
    public void dispose() {
        mediaPlayer.dispose();
    }

    @Override
    public int getCurrentTimeMs() {
        return (int) mediaPlayer.getCurrentTime().toMillis();
    }

    @Override
    public Observable<Integer> currentTimeMsObservable() {
        return JavaFxObservable.fromObservableValue(mediaPlayer.currentTimeProperty())
                .map(duration -> (int) duration.toMillis());
    }

    @Override
    public int getDurationMs() {
        return (int) mediaPlayer.getMedia().getDuration().toMillis();
    }

    @Override
    public void play() {
        mediaPlayer.play();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public void stop() {
        mediaPlayer.stop();
    }

    @Override
    public void seekMs(int timestamp) {
        mediaPlayer.seek(Duration.millis(timestamp));
    }

    @Override
    public void seekPercent(double percent) {
        mediaPlayer.seek(Duration.millis(getDurationMs() * (percent / 100.0)));
    }

    @Override
    public void setVolumePercent(double percent) {
        mediaPlayer.setVolume(percent / 100.0);
    }

    @Override
    public double getVolume() {
        return mediaPlayer.getVolume();
    }

    @Override
    public Status getStatus() {
        return convertStatus(mediaPlayer.getStatus());
    }

    @Override
    public Observable<Status> statusObservable() {
        return JavaFxObservable.fromObservableValue(mediaPlayer.statusProperty())
                .filter(status -> status != null)
                .map(this::convertStatus);
    }

    private Status convertStatus(javafx.scene.media.MediaPlayer.Status status) {
        if (status == null) {
            return null;
        }
        switch (status) {
            case UNKNOWN:
                return Status.INITIALIZING;
            case READY:
                return Status.READY;
            case PAUSED:
                return Status.PAUSED;
            case PLAYING:
                return Status.PLAYING;
            case STOPPED:
                return Status.STOPPED;
            case STALLED:
                return Status.BUFFERING;
            case HALTED:
                return Status.ERRORED;
            case DISPOSED:
                return Status.DISPOSED;
        }
        throw new RuntimeException("Unhandled JavaFX MediaPlayer status.");
    }
}
