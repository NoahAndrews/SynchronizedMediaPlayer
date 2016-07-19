package me.noahandrews.mediaplayersync.javafx;

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

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NetworkConfigViewJfx implements NetworkConfigView {
    private Stage stage;
    private VBox vBox;
    private Pane childPane;

    private ModeSelectorView modeSelectorView;

    private static final int WIDTH = 325;

    public NetworkConfigViewJfx() {
        stage = new Stage();
        vBox = new VBox();

        stage.setTitle("Network Configuration");
        stage.setResizable(false);
        modeSelectorView = new ModeSelectorViewJfx();

        vBox.getChildren().add(modeSelectorView.getJfxView());
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(10));
        Scene scene = new Scene(vBox, WIDTH, 60);
        stage.setScene(scene);
    }

    public ModeSelectorView getModeSelectorView() {
        return modeSelectorView;
    }

    @Override
    public void setScene(Scene scene) {
        stage.setScene(scene);
    }

    @Override
    public void setHeight(int height) {
        stage.setMaxHeight(height);
    }

    @Override
    public void setChildPane(Pane pane) {
        if (childPane != null) {
            vBox.getChildren().remove(childPane);
        }
        childPane = pane;
        vBox.getChildren().add(childPane);
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
