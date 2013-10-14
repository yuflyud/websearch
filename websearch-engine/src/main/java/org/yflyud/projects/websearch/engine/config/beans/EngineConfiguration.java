package org.yflyud.projects.websearch.engine.config.beans;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.yflyud.projects.websearch.engine.config.internal.adapters.BrowserVersionAdapter;

import com.gargoylesoftware.htmlunit.BrowserVersion;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EngineConfiguration {
    private Boolean shareBrowser;
    @XmlJavaTypeAdapter(BrowserVersionAdapter.class)
    private BrowserVersion browserVersion;
    private Boolean javaScriptEnabled;
    @XmlElementWrapper(name = "sources")
    @XmlElement(name = "source")
    private Set<SourceConfiguration> sources;

    public Boolean getShareBrowser() {
        return shareBrowser;
    }

    public void setShareBrowser(Boolean shareBrowser) {
        this.shareBrowser = shareBrowser;
    }

    public Boolean getJavaScriptEnabled() {
        return javaScriptEnabled;
    }

    public void setJavaScriptEnabled(Boolean javaScriptEnabled) {
        this.javaScriptEnabled = javaScriptEnabled;
    }

    public BrowserVersion getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(BrowserVersion browserVersion) {
        this.browserVersion = browserVersion;
    }

    public Set<SourceConfiguration> getSources() {
        return sources;
    }

    public void setSources(Set<SourceConfiguration> sources) {
        this.sources = sources;
    }

}
