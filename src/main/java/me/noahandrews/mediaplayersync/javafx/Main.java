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

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Synchronized media player");

        MediaControl mediaControl = new MediaControl();

        BorderPane primaryPane = new BorderPane();
        primaryPane.setCenter(mediaControl);
        primaryPane.setTop(getMenuBar());

        Scene scene = new Scene(primaryPane, 570, 60);

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    private MenuBar getMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("_File");
        MenuItem openItem = new MenuItem("_Open...");
        MenuItem exitItem = new MenuItem("E_xit");
        fileMenu.getItems().addAll(openItem, exitItem);

        Menu helpMenu = new Menu("_Help");
        MenuItem aboutItem = new MenuItem("_About...");
        aboutItem.setOnAction(event -> new AlertBox("Version 0.2-SNAPSHOT", "About").showAndWait());
        helpMenu.getItems().addAll(aboutItem);

        menuBar.getMenus().addAll(fileMenu, helpMenu);

        return menuBar;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
