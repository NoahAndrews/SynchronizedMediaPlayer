package me.noahandrews.mediaplayersync.javafx;

/*
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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

class ModeSelectorViewJfx implements ModeSelectorView {
    private HBox hBox;

    private RadioButton hostButton, guestButton;

    public ModeSelectorViewJfx() {
        hBox = new HBox(25);
        hBox.setMaxHeight(40);
        hBox.setAlignment(Pos.CENTER_LEFT);

        Label modeLabel = new Label("Mode:");
        hostButton = new RadioButton("Host");
        guestButton = new RadioButton("Guest");

        ToggleGroup toggleGroup = new ToggleGroup();
        hostButton.setToggleGroup(toggleGroup);
        guestButton.setToggleGroup(toggleGroup);

        HBox.setMargin(modeLabel, new Insets(0, 30, 0, 0));

        hBox.getChildren().addAll(modeLabel, hostButton, guestButton);
    }

    @Override
    public Region getJfxView() {
        return hBox;
    }

    @Override
    public void setHostButtonListener(EventHandler<ActionEvent> eventHandler) {
        hostButton.setOnAction(eventHandler);
    }

    @Override
    public void setGuestButtonListener(EventHandler<ActionEvent> eventHandler) {
        guestButton.setOnAction(eventHandler);
    }
}
