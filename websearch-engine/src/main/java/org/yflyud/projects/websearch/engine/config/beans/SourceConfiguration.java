package org.yflyud.projects.websearch.engine.config.beans;

import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.yflyud.projects.websearch.engine.config.internal.adapters.BrowserVersionAdapter;

import com.gargoylesoftware.htmlunit.BrowserVersion;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SourceConfiguration {
    @XmlJavaTypeAdapter(BrowserVersionAdapter.class)
    private BrowserVersion browserVersion;
    private Boolean javaScriptEnabled;
    private String sourceName;
    private URI homePageURI;
    private String sourceHandlerClass;

    public BrowserVersion getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(BrowserVersion browserVersion) {
        this.browserVersion = browserVersion;
    }

    public Boolean getJavaScriptEnabled() {
        return javaScriptEnabled;
    }

    public void setJavaScriptEnabled(Boolean javaScriptEnabled) {
        this.javaScriptEnabled = javaScriptEnabled;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public URI getHomePageURI() {
        return homePageURI;
    }

    public void setHomePageURI(URI homePageURI) {
        this.homePageURI = homePageURI;
    }

    public String getSourceHandlerClass() {
        return sourceHandlerClass;
    }

    public void setSourceHandlerClass(String sourceHandlerClass) {
        this.sourceHandlerClass = sourceHandlerClass;
    }

}
