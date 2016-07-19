package me.noahandrews.mediaplayersync.javafx;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

    public GuestConfigViewJfx() {
        pane = new VBox(20);
        pane.setPadding(new Insets(10));

        connectionBox = new HBox(10);
        connectionBox.setAlignment(Pos.CENTER_LEFT);
        hostnameField = new TextField("127.0.0.1");
        connectButton = new Button("Connect");
        connectionBox.getChildren().addAll(new Label("Hostname"), hostnameField, connectButton);

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
}
