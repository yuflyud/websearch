package org.yflyud.projects.websearch.engine.config.beans;

import com.gargoylesoftware.htmlunit.BrowserVersion;

public enum BrowserVersionEnum {
    CHROME, FIREFOX_17, INTERNET_EXPLORER_8, INTERNET_EXPLORER_9;
    public BrowserVersion toBrowserVersion() {
        switch (this) {
        case CHROME:
            return BrowserVersion.CHROME;
        case FIREFOX_17:
            return BrowserVersion.FIREFOX_17;
        case INTERNET_EXPLORER_8:
            return BrowserVersion.INTERNET_EXPLORER_8;
        case INTERNET_EXPLORER_9:
            return BrowserVersion.INTERNET_EXPLORER_9;
        }
        return BrowserVersion.getDefault();
    }

    public static BrowserVersionEnum valueOf(BrowserVersion browserVersion) {
        if (BrowserVersion.CHROME.equals(browserVersion)) {
            return CHROME;
        } else if (BrowserVersion.FIREFOX_17.equals(browserVersion)) {
            return FIREFOX_17;
        } else if (BrowserVersion.INTERNET_EXPLORER_8.equals(browserVersion)) {
            return INTERNET_EXPLORER_8;
        } else if (BrowserVersion.INTERNET_EXPLORER_9.equals(browserVersion)) {
            return INTERNET_EXPLORER_9;
        }
        throw new IllegalArgumentException(String.format(
                "Browser version '%s' cannot be recognized.",
                browserVersion.getApplicationCodeName()));
    }
}
