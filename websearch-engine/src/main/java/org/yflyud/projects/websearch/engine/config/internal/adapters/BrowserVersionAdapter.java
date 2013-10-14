package org.yflyud.projects.websearch.engine.config.internal.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.yflyud.projects.websearch.engine.config.beans.BrowserVersionEnum;

import com.gargoylesoftware.htmlunit.BrowserVersion;

public class BrowserVersionAdapter extends XmlAdapter<String, BrowserVersion> {
    // @Override
    public BrowserVersion unmarshal(String s) {
        return BrowserVersionEnum.valueOf(s).toBrowserVersion();
    }

    // @Override
    public String marshal(BrowserVersion browserVersion) {
        return browserVersion == null ? null : BrowserVersionEnum.valueOf(browserVersion)
                .toString();
    }
}
