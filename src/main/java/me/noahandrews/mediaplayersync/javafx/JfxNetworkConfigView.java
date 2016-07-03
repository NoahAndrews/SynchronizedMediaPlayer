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

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class JfxNetworkConfigView implements NetworkConfigView {
    private static final int WIDTH = 325;
    private ModeSelector modeSelector;

    private Stage stage;

    public JfxNetworkConfigView() {
        super();
        stage = new Stage();

        stage.setTitle("Network Configuration");
        stage.setResizable(false);
        modeSelector = new ModeSelector();

        StackPane initialSceneStackPane = new StackPane();
        initialSceneStackPane.getChildren().add(modeSelector);
        initialSceneStackPane.setAlignment(Pos.TOP_CENTER);
        initialSceneStackPane.setPadding(new Insets(10));
        Scene initialScene = new Scene(initialSceneStackPane, WIDTH, 60);
        stage.setScene(initialScene);

        modeSelector.setHostButtonListener(event -> {
            stage.setScene(new HostConfigScene(modeSelector, WIDTH, 60));
        });

        modeSelector.setGuestButtonListener(event -> {
            stage.setScene(new GuestConfigView(modeSelector, WIDTH, 100).scene);
        });
    }

    @Override
    public void show() {
        stage.show();
    }

    @Override
    public void toFront() {
        stage.toFront();
    }

    @Override
    public void close() {
        stage.close();
    }
}
