package me.noahandrews.mediaplayersync.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Copyright (C) 2016 Noah Andrews
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

class AlertBox extends Stage {

    AlertBox(String message, String title) {
        initModality(Modality.APPLICATION_MODAL);
        setTitle(title);
        setMinWidth(250);

        Label label = new Label(message);

        Button okBtn = new Button("OK");
        okBtn.setOnAction(event -> close());

        VBox pane = new VBox(10);
        pane.setPadding(new Insets(7.0));
        pane.getChildren().addAll(label, okBtn);
        pane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(pane);
        setScene(scene);
    }
}
