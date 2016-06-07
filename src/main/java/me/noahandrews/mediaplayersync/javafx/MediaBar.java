package me.noahandrews.mediaplayersync.javafx;

/**
 * Copyright (C) 2016 Noah Andrews
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.util.Duration;

import static me.noahandrews.mediaplayersync.javafx.PlaybackStatus.PAUSED;

public class MediaBar extends HBox {
    private Button playButton;
    private Slider timeSlider;
    private Label elapsedTimeLabel;
    private Slider volumeSlider;

    private InputEventHandler registeredEventHandler;

    private PlaybackStatus status = PAUSED;

    public MediaBar() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(5, 10, 5, 10));
        setSpacing(10);

        Label timeLabel = new Label("Time: ");
        Label volumeLabel = new Label("Vol: ");

        playButton = new Button(">");
        playButton.setOnAction(event -> {
            switch (status) {
                case PAUSED:
                    registeredEventHandler.play();
                    break;
                case PLAYING:
                    registeredEventHandler.pause();
                    break;
            }
        });

        timeSlider = new Slider();
        setHgrow(timeSlider, Priority.ALWAYS);
        timeSlider.setMinWidth(50);
        timeSlider.setMaxWidth(Double.MAX_VALUE);
        timeSlider.setOnMouseReleased(event -> registeredEventHandler.timeSliderReleased(timeSlider.getValue()));

        elapsedTimeLabel = new Label("00:00/00:00");
        elapsedTimeLabel.setMinWidth(50);
        setMargin(elapsedTimeLabel, new Insets(0, 15, 0, 0));

        volumeSlider = new Slider();
        volumeSlider.setPrefWidth(70);
        volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
        volumeSlider.setMinWidth(30);

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            registeredEventHandler.volumeSliderChanged(oldValue.doubleValue(), newValue.doubleValue(), volumeSlider.isPressed());
        });

        getChildren().addAll(
                playButton,
                timeLabel,
                timeSlider,
                elapsedTimeLabel,
                volumeLabel,
                volumeSlider);
    }

    void updateVolumeLevel(double volume) {
        if (volumeSlider != null) {
            Platform.runLater(() -> {
                if (!volumeSlider.isValueChanging()) {
                    volumeSlider.setValue((int) Math.round(volume * 100));
                }
            });
        }
    }

    void updateTimes(Duration currentTime, Duration duration) {
        if (elapsedTimeLabel != null && timeSlider != null) {
            Platform.runLater(() -> {
                elapsedTimeLabel.setText(formatTime(currentTime, duration));
                timeSlider.setDisable(duration.isUnknown());
                if (!timeSlider.isDisabled() && duration.greaterThan(Duration.ZERO) && !timeSlider.isPressed()) {
                    timeSlider.setValue(currentTime.divide(duration.toMillis()).toMillis() * 100.0);
                }
            });
        }
    }

    void setStatus(PlaybackStatus status) {
        this.status = status;
        switch (status) {
            case PAUSED:
                playButton.setText(">");
                break;
            case PLAYING:
                playButton.setText("||");
                break;
        }
    }

    public void setEventHandler(InputEventHandler registeredEventHandler) {
        this.registeredEventHandler = registeredEventHandler;
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
            int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;
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


    /**
     * The reason the time slider and volume sliders have different events associated with them is that the time slider
     * needs special handling because it is automatically advanced forward continually.
     */
    interface InputEventHandler {
        void play();

        void pause();

        void timeSliderReleased(double position);

        void volumeSliderChanged(double oldValue, double newValue, boolean isPressed);
    }
}
