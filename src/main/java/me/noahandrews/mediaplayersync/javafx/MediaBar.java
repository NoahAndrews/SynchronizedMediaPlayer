package me.noahandrews.mediaplayersync.javafx;

/**
    Copyright (C) 2016 Noah Andrews

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;

/**
 * Created by noah on 6/4/2016.
 */
public class MediaBar extends HBox {
    private Media media;
    private MediaPlayer mediaPlayer;
    private boolean stopRequested = false;
    private boolean atEndOfMedia = false;
    private Duration duration;
    private Button playButton;
    private Slider timeSlider;
    private Label playTime;
    private Slider volumeSlider;

    public MediaBar() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(5, 10, 5, 10));

        Label spacer1 = new Label("   ");
        Label spacer2 = new Label("   ");
        Label timeLabel = new Label("Time: ");
        Label volumeLabel = new Label("Vol: ");

        Button openButton = new Button("Open");
        openButton.setOnAction(event -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
            File file = fc.showOpenDialog(null);
            if (file == null) {
                return;
            }
            media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            setupPlayer();
        });

        playButton = new Button(">");

        timeSlider = new Slider();
        setHgrow(timeSlider, Priority.ALWAYS);
        timeSlider.setMinWidth(50);
        timeSlider.setMaxWidth(Double.MAX_VALUE);

        playTime = new Label();
        playTime.setPrefWidth(130);
        playTime.setMinWidth(50);

        volumeSlider = new Slider();
        volumeSlider.setPrefWidth(70);
        volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
        volumeSlider.setMinWidth(30);

        getChildren().addAll(
                openButton,
                spacer1,
                playButton,
                spacer2,
                timeLabel,
                timeSlider,
                playTime,
                volumeLabel,
                volumeSlider);
    }

    private void setupPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.setAutoPlay(false);

            mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    updateValues();
                }
            });

            mediaPlayer.setOnPlaying(() -> {
                if (stopRequested) {
                    mediaPlayer.pause();
                    stopRequested = false;
                } else {
                    playButton.setText("||");
                }
            });

            mediaPlayer.setOnReady(() -> {
                duration = mediaPlayer.getMedia().getDuration();
                updateValues();
            });

            mediaPlayer.setCycleCount(1);
            mediaPlayer.setOnEndOfMedia(() -> {
                playButton.setText(">");
                stopRequested = true;
                atEndOfMedia = true;
            });

            mediaPlayer.setOnPaused(() -> {
                System.out.println("onPaused");
                playButton.setText(">");
            });

            playButton.setOnAction(event -> {
                Status status = mediaPlayer.getStatus();
                if (status == Status.UNKNOWN || status == Status.HALTED) {
                    return;
                }

                if (status == Status.PAUSED || status == Status.READY || status == Status.STOPPED) {
                    if (atEndOfMedia) {
                        mediaPlayer.seek(mediaPlayer.getStartTime());
                        atEndOfMedia = false;
                    }
                    mediaPlayer.play();
                } else {
                    mediaPlayer.pause();
                }
            });

            timeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    if (timeSlider.isValueChanging()) {
                        mediaPlayer.seek(duration.multiply(timeSlider.getValue() / 100.0));
                    }
                }
            });

            volumeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    if (volumeSlider.isValueChanging()) {
                        mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
                    }
                }
            });
        }
    }

    protected void updateValues() {
        if (mediaPlayer != null && playTime != null && timeSlider != null && volumeSlider != null) {
            Platform.runLater(() -> {
                Duration currentTime = mediaPlayer.getCurrentTime();
                playTime.setText(formatTime(currentTime, duration));
                timeSlider.setDisable(duration.isUnknown());
                if (!timeSlider.isDisabled() && duration.greaterThan(Duration.ZERO) && !timeSlider.isValueChanging()) {
                    timeSlider.setValue(currentTime.divide(duration.toMillis()).toMillis() * 100.0);
                }
                if (!volumeSlider.isValueChanging()) {
                    volumeSlider.setValue((int) Math.round(mediaPlayer.getVolume() * 100));
                }
            });
        }
    }

    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 -
                    durationMinutes * 60;
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d", elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }
}
