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
import javafx.stage.Stage;

public class MainApplication extends Application {

    private boolean stopRequested = false;
    private boolean atEndOfMedia = false;

    private ApplicationComponent applicationComponent;

    private MediaModule mediaModule;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mediaModule = new MediaModule();

        applicationComponent = DaggerApplicationComponent.builder()
                .mainViewModule(new MainViewModule(primaryStage, this, mediaModule))
                .networkConfigViewModule(new NetworkConfigViewModule(this))
                .mediaModule(mediaModule)
                .build();

        applicationComponent.mainViewPresenter().show();
    }


    public void exit() {
        System.out.println("Exiting...");
        applicationComponent.mediaService().tearDown();
        applicationComponent.mainViewPresenter().close();
        applicationComponent.networkConfigPresenter().close();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
