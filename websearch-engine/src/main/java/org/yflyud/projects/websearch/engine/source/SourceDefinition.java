package org.yflyud.projects.websearch.engine.source;

import java.net.URI;

import org.yflyud.projects.websearch.engine.source.handlers.ISourceHandler;

public class SourceDefinition {
    private URI homePageURI;
    private ISourceHandler sourceHandler;

    public URI getHomePageURI() {
        return homePageURI;
    }

    public void setHomePageURI(URI homePageURI) {
        this.homePageURI = homePageURI;
    }

    public ISourceHandler getSourceHandler() {
        return sourceHandler;
    }

    public void setSourceHandler(ISourceHandler sourceHandler) {
        this.sourceHandler = sourceHandler;
    }
}
