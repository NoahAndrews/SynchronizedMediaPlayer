package me.noahandrews.mediaplayersync.javafx;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
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

public class MainViewJfx implements MainView {
    private Stage stage;

    private MainViewEventHandler eventHandler;

    private MediaBar mediaBar;
    private Button networkSetupButton;
    private Tooltip networkSetupButtonTooltip;

    public MainViewJfx(Stage stage) {
        this.stage = stage;
        stage.setTitle("Synchronized Media Player");

        eventHandler = new NullMainViewEventHandler();

        mediaBar = new MediaBar();
        mediaBar.setEventHandler(new MediaBar.InputEventHandler() {
            @Override
            public void play() {
                eventHandler.onPlayButtonPressed();
            }

            @Override
            public void pause() {
                eventHandler.onPauseButtonPressed();
            }

            @Override
            public void timeSliderReleased(double position) {
                eventHandler.onTimeSliderMoved(position);
            }

            @Override
            public void volumeSliderChanged(double oldPosition, double newPosition, boolean isPressed) {
                eventHandler.onVolumeSliderMoved(oldPosition, newPosition, isPressed);
            }
        });

        networkSetupButton = new Button("Setup network");
        networkSetupButton.setOnAction(event -> {
            eventHandler.onNetworkSetupButtonPressed();
        });

        StackPane networkSetupButtonContainer = new StackPane(); // A container is required to display a tooltip on a disabled control

        networkSetupButtonTooltip = new Tooltip("A file must be loaded before you can configure a network connection.");
        networkSetupButtonContainer.getChildren().add(networkSetupButton);
        Tooltip.install(networkSetupButtonContainer, networkSetupButtonTooltip);
        mediaBar.getChildren().add(networkSetupButtonContainer);

        BorderPane primaryPane = new BorderPane();
        primaryPane.setCenter(mediaBar);
        primaryPane.setTop(getMenuBar());
        primaryPane.setMinWidth(600); // This may actually need to use a reference to the stage
        primaryPane.setPrefWidth(600);

        Scene scene = new Scene(primaryPane);

        stage.setScene(scene);
        stage.sizeToScene();

        stage.setOnCloseRequest(event -> eventHandler.onCloseRequest());
    }

    @Override
    public void setEventHandler(MainViewEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    private MenuBar getMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("_File");
        MenuItem openItem = new MenuItem("_Open...");
        openItem.setOnAction(event -> eventHandler.onOpenMenuItemSelected());
        MenuItem exitItem = new MenuItem("E_xit");
        exitItem.setOnAction(event -> eventHandler.onExitMenuItemSelected());
        fileMenu.getItems().addAll(openItem, exitItem);

        Menu helpMenu = new Menu("_Help");
        MenuItem aboutItem = new MenuItem("_About...");
        aboutItem.setOnAction(event -> eventHandler.onAboutMenuItemSelected());
        helpMenu.getItems().addAll(aboutItem);

        menuBar.getMenus().addAll(fileMenu, helpMenu);

        return menuBar;
    }

    @Override
    public void show() {
        stage.show();
        stage.setMaxHeight(stage.getHeight());
        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());
    }

    @Override
    public void toFront() {
        stage.toFront();
    }

    @Override
    public void close() {
        stage.close();
    }

    @Override
    public void updateTimes(Duration currentTime, Duration duration) {
        mediaBar.updateTimes(currentTime, duration);
    }

    @Override
    public void disableNetworkSetupButton() {
        networkSetupButton.setDisable(true);
    }

    @Override
    public void enableNetworkSetupButton() {
        networkSetupButton.setDisable(false);
    }

    @Override
    public void setPlaybackStatus(PlaybackStatus status) {
        mediaBar.setStatus(status);
    }
}
