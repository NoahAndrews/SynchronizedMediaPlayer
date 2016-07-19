package me.noahandrews.mediaplayersync.javafx;

import javafx.scene.control.Alert;
import javafx.util.Duration;

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

public class MainViewPresenter {
    private final MainView mainView;

    private final NetworkConfigPresenter networkConfigPresenter;

    private final MainApplication mainApplication;

    private final MediaService mediaService;

    private Duration duration;


    public MainViewPresenter(MainView mainView, NetworkConfigPresenter networkConfigPresenter, MainApplication mainApplication, MediaService mediaService) {
        this.mainView = mainView;
        this.networkConfigPresenter = networkConfigPresenter;
        this.mainApplication = mainApplication;
        this.mediaService = mediaService;

        mainView.setEventHandler(new ViewEventHandler());

        mainView.updateTimes(Duration.ZERO, Duration.UNKNOWN);

        mainView.disableNetworkSetupButton();

        mediaService.setEventHandler(new MediaServiceEventHandler());
    }

    public void close() {
        mainView.close();
        //TODO: Determine if there's any more cleanup that needs to be done for the presenter
    }

    public void show() {
        mainView.show();
    }

    private class ViewEventHandler implements MainViewEventHandler {


        @Override
        public void onNetworkSetupButtonPressed() {
            networkConfigPresenter.show();
        }

        @Override
        public void onPlayButtonPressed() {
            mediaService.play();
        }

        @Override
        public void onPauseButtonPressed() {
            mediaService.pause();
        }

        @Override
        public void onTimeSliderMoved(double position) {
            mediaService.seekPercentage(position);
        }

        @Override
        public void onVolumeSliderMoved(double oldPosition, double newPosition, boolean isPressed) {
            mediaService.setVolumePercentage(newPosition);
        }

        @Override
        public void onOpenMenuItemSelected() {
            mediaService.loadNewMedia();
        }

        @Override
        public void onAboutMenuItemSelected() {
            new Alert(Alert.AlertType.INFORMATION, "Version 0.3-SNAPSHOT").show();
        }

        @Override
        public void onExitMenuItemSelected() {
            mainApplication.exit();
        }

        @Override
        public void onCloseRequest() {
            mainApplication.exit();
        }
    }

    private class MediaServiceEventHandler implements MediaService.EventHandler {

        @Override
        public void onMediaLoaded(int durationMs) {
            duration = Duration.millis(durationMs);
            mainView.updateTimes(Duration.ZERO, duration);
            mainView.enableNetworkSetupButton();
        }

        @Override
        public void onPlay() {
            mainView.setPlaybackStatus(PlaybackStatus.PLAYING);
        }

        @Override
        public void onPause() {
            mainView.setPlaybackStatus(PlaybackStatus.PAUSED);
        }

        @Override
        public void onPlaybackAdvanced(int ms) {
            if (duration == null) {
                duration = Duration.UNKNOWN;
            }
            mainView.updateTimes(Duration.millis(ms), duration);
        }

        @Override
        public void onStop() {
            mainView.setPlaybackStatus(PlaybackStatus.PAUSED);
        }
    }
}
