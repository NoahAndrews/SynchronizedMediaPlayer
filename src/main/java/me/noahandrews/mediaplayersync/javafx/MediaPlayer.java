package me.noahandrews.mediaplayersync.javafx;

import rx.Observable;

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

public interface MediaPlayer {
    void dispose();

    int getCurrentTimeMs();

    Observable<Integer> currentTimeMsObservable();

    int getDurationMs();

    void play();

    void pause();

    void stop();

    void seekMs(int timestamp);

    void seekPercent(double percent);

    void setVolumePercent(double percent);

    /**
     * @return the volume on a scale from 0.0 to 1.0
     */
    double getVolume();

    Status getStatus();

    Observable<Status> statusObservable();

    enum Status {
        INITIALIZING,
        READY,
        PLAYING,
        PAUSED,
        STOPPED,
        BUFFERING,
        ERRORED,
        DISPOSED
    }
}
