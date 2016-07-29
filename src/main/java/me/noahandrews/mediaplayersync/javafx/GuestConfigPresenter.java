package me.noahandrews.mediaplayersync.javafx;

import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rx.schedulers.Schedulers;

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

public class GuestConfigPresenter {
    private GuestConfigView guestConfigView;
    private MediaService mediaService;
    private Logger logger = LogManager.getLogger();

    public GuestConfigPresenter(GuestConfigView guestConfigView, MediaService mediaService) {
        this.guestConfigView = guestConfigView;
        this.mediaService = mediaService;

        guestConfigView.connectButtonObservable()
                .observeOn(Schedulers.io())
                .flatMap(event -> {
                    guestConfigView.showSpinner();
                    guestConfigView.setConnectionButtonDisabled(true);
                    return mediaService.establishConnectionAsGuest(guestConfigView.getHostname()).toObservable();
                })
                .retry((retries, error) -> {
                    error.printStackTrace();
                    guestConfigView.showError(error.getMessage());
                    guestConfigView.setConnectionButtonDisabled(false);
                    return true;
                })
                .subscribe(v -> {
                    guestConfigView.showSuccess();
                    guestConfigView.setConnectionButtonDisabled(false);
                    guestConfigView.showDisconnectButton();
                }, Throwable::printStackTrace);

        guestConfigView.disconnectButtonObservable()
                .observeOn(Schedulers.io())
                .subscribe(event -> {
                    logger.debug("disconnect button pressed (subscribe)");
                    mediaService.tearDownNetworkConnection();
                    guestConfigView.removeExtraConnectionBoxItem();
                    guestConfigView.showConnectButton();
                });
    }

    public Pane getPane() {
        return guestConfigView.getPane();
    }
}
