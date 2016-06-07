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

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    private Stage primaryStage;

    private MediaPlayer mediaPlayer;
    private MediaBar mediaBar;

    private boolean stopRequested = false;
    private boolean atEndOfMedia = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Synchronized media player");

        mediaBar = new MediaBar();
        mediaBar.setEventHandler(new MediaBarInputListener());

        BorderPane primaryPane = new BorderPane();
        primaryPane.setCenter(mediaBar);
        primaryPane.setTop(getMenuBar());
        primaryStage.setMinWidth(600);
        primaryPane.setPrefWidth(600);

        //Scene scene = new Scene(primaryPane, 570, 60);
        Scene scene = new Scene(primaryPane);


        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();

        primaryStage.setMaxHeight(primaryStage.getHeight());
        primaryStage.setMinHeight(primaryStage.getHeight());

        primaryStage.setOnCloseRequest(event -> exit());
    }

    private MenuBar getMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("_File");
        MenuItem openItem = new MenuItem("_Open...");
        openItem.setOnAction(event -> openFile());
        MenuItem exitItem = new MenuItem("E_xit");
        exitItem.setOnAction(event -> exit());
        fileMenu.getItems().addAll(openItem, exitItem);

        Menu helpMenu = new Menu("_Help");
        MenuItem aboutItem = new MenuItem("_About...");
        aboutItem.setOnAction(event -> new AlertBox("Version 0.2-SNAPSHOT", "About").showAndWait());
        helpMenu.getItems().addAll(aboutItem);

        menuBar.getMenus().addAll(fileMenu, helpMenu);

        return menuBar;
    }

    private void exit() {
        System.out.println("Exiting...");
        mediaPlayer.dispose();
        primaryStage.close();
    }

    private void openFile() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
        File file = fc.showOpenDialog(null);
        if (file == null) {
            return;
        }
        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        setupPlayer();
    }

    private void setupPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.setAutoPlay(false);

            mediaPlayer.currentTimeProperty().addListener(observable -> {
                mediaBar.updateTimes(mediaPlayer.getCurrentTime(), mediaPlayer.getMedia().getDuration());
            });

            mediaPlayer.setOnPlaying(() -> {
                if (stopRequested) {
                    mediaPlayer.pause();
                    stopRequested = false;
                    mediaBar.setStatus(PlaybackStatus.PAUSED);
                } else {
                    mediaBar.setStatus(PlaybackStatus.PLAYING);
                }
            });

            mediaPlayer.setOnReady(() -> {
                mediaBar.updateTimes(mediaPlayer.getCurrentTime(), mediaPlayer.getMedia().getDuration());
                mediaBar.updateVolumeLevel(mediaPlayer.getVolume());
            });

            mediaPlayer.setCycleCount(1);
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaBar.setStatus(PlaybackStatus.PAUSED);
                stopRequested = true;
                atEndOfMedia = true;
            });

            mediaPlayer.setOnPaused(() -> {
                System.out.println("onPaused");
                mediaBar.setStatus(PlaybackStatus.PAUSED);
            });
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private class MediaBarInputListener implements MediaBar.InputEventHandler {
        @Override
        public void play() {
            MediaPlayer.Status status = mediaPlayer.getStatus();
            if (status == MediaPlayer.Status.UNKNOWN || status == MediaPlayer.Status.HALTED) {
                return;
            }

            //TODO: Handle STALLED and DISPOSED states
            if (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.READY || status == MediaPlayer.Status.STOPPED) {
                if (atEndOfMedia) {
                    mediaPlayer.seek(mediaPlayer.getStartTime());
                    atEndOfMedia = false;
                }
                mediaPlayer.play();
                mediaBar.setStatus(PlaybackStatus.PLAYING);
            }
        }

        @Override
        public void pause() {
            MediaPlayer.Status status = mediaPlayer.getStatus();
            if (status == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                mediaBar.setStatus(PlaybackStatus.PAUSED);
            }
        }

        @Override
        public void timeSliderReleased(double position) {
            mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(position / 100.0));
        }

        @Override
        public void volumeSliderChanged(double oldValue, double newValue, boolean isPressed) {
            if (isPressed) {
                mediaPlayer.setVolume(newValue / 100.0);
            }
        }
    }
}
