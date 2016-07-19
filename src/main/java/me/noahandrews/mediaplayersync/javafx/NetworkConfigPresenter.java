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

public class NetworkConfigPresenter {
    private final NetworkConfigView networkConfigView;
    private final MainApplication mainApplication;

    public NetworkConfigPresenter(NetworkConfigView networkConfigView, HostConfigPresenter hostConfigPresenter, GuestConfigPresenter guestConfigPresenter, MainApplication mainApplication) {
        this.networkConfigView = networkConfigView;
        this.mainApplication = mainApplication;

        ModeSelectorView modeSelectorView = networkConfigView.getModeSelectorView();

        modeSelectorView.setHostButtonListener(event -> {
            networkConfigView.setChildPane(hostConfigPresenter.getPane());
            //networkConfigView.setHeight(60);
        });

        modeSelectorView.setGuestButtonListener(event -> {
            networkConfigView.setChildPane(guestConfigPresenter.getPane());
            //networkConfigView.setHeight(100);
        });
    }

    public void show() {
        networkConfigView.show();
    }

    public void close() {
        networkConfigView.close();
    }
}
