package me.noahandrews.mediaplayersync.javafx;

import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rx.schedulers.JavaFxScheduler;

import static me.noahandrews.mediaplayersync.javafx.HostConfigPresenter.ServerButtonState.START_SERVER;
import static me.noahandrews.mediaplayersync.javafx.HostConfigPresenter.ServerButtonState.STOP_SERVER;

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

public class HostConfigPresenter {
    private final HostConfigView hostConfigView;
    private final MediaService mediaService;
    private final Logger logger = LogManager.getLogger();
    private ServerButtonState serverButtonState = START_SERVER;

    public HostConfigPresenter(HostConfigView hostConfigView, MediaService mediaService) {

        this.hostConfigView = hostConfigView;
        this.mediaService = mediaService;

        hostConfigView.getServerButtonObservable().subscribe(event -> {
            if (serverButtonState == START_SERVER) {
                mediaService.establishConnectionAsHost()
                        .observeOn(JavaFxScheduler.getInstance())
                        .subscribe(hostConfigView::setServerStatusText);
                hostConfigView.setServerButtonText("Stop Server");
                serverButtonState = STOP_SERVER;
            } else if (serverButtonState == STOP_SERVER) {
                mediaService.tearDownNetworkConnection();
                hostConfigView.setServerButtonText("Start Server");
                serverButtonState = START_SERVER;
            }
        });
    }

    public Pane getPane() {
        return hostConfigView.getPane();
    }

    enum ServerButtonState {
        START_SERVER,
        STOP_SERVER
    }
}
