package me.noahandrews.mediaplayersync.javafx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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

class GuestConfigViewJfx implements GuestConfigView {
    private VBox pane;
    private HBox connectionBox;

    private final TextField hostnameField;
    private final Button connectButton;

    private final int initialConnectionBoxChildCount;

    public GuestConfigViewJfx() {
        pane = new VBox(20);
//        pane.setPadding(new Insets(10));

        connectionBox = new HBox(10);
        connectionBox.setAlignment(Pos.CENTER_LEFT);
        Label hostnameLabel = new Label("Hostname");
        hostnameLabel.setMinWidth(55);
        hostnameField = new TextField("127.0.0.1");
        connectButton = new Button("Connect");
        connectButton.setMinWidth(60);
        connectionBox.getChildren().addAll(hostnameLabel, hostnameField, connectButton);
        HBox.setHgrow(hostnameField, Priority.ALWAYS);
        initialConnectionBoxChildCount = connectionBox.getChildren().size();

        pane.getChildren().clear();
        pane.getChildren().add(connectionBox);
    }

    @Override
    public Pane getPane() {
        return pane;
    }

    @Override
    public String getHostname() {
        return hostnameField.getText();
    }

    @Override
    public Observable<ActionEvent> connectButtonObservable() {
        return JavaFxObservable.fromNodeEvents(connectButton, ActionEvent.ACTION);
    }

    @Override
    public void showSpinner() {
        Platform.runLater(() -> {
            ProgressIndicator spinner = new ProgressIndicator();
            spinner.setProgress(-1);
            spinner.setMinHeight(connectionBox.getHeight());
            spinner.setMaxHeight(connectionBox.getHeight());
            spinner.setMaxWidth(connectionBox.getHeight());
            spinner.setMinWidth(connectionBox.getHeight());
            removeExtraConnectionBoxItem();
            connectionBox.getChildren().add(initialConnectionBoxChildCount, spinner);
        });
    }

    @Override
    public void showSuccess() {
        Platform.runLater(() -> {
            ImageView imageView = new ImageView("success.png");
            imageView.setFitHeight(connectionBox.getHeight());
            imageView.setPreserveRatio(true);
            removeExtraConnectionBoxItem();
            connectionBox.getChildren().add(initialConnectionBoxChildCount, imageView);
        });
    }

    @Override
    public void showError(String message) {
        Platform.runLater(() -> {
            ImageView imageView = new ImageView("error.png");
            imageView.setFitHeight(connectionBox.getHeight());
            imageView.setPreserveRatio(true);
            Tooltip errorTooltip = new Tooltip(message);
            Tooltip.install(imageView, errorTooltip);
            removeExtraConnectionBoxItem();
            connectionBox.getChildren().add(initialConnectionBoxChildCount, imageView);
        });
    }

    private void removeExtraConnectionBoxItem() {
        try {
            connectionBox.getChildren().remove(initialConnectionBoxChildCount);
        } catch (IndexOutOfBoundsException e) {
            // We're removing the 4th item from the connectionBox. If it doesn't exist, no harm done.
        }
    }
}
