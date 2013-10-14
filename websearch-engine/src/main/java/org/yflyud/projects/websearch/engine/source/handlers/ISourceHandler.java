package org.yflyud.projects.websearch.engine.source.handlers;

import org.yflyud.projects.websearch.engine.Result;
import org.yflyud.projects.websearch.engine.source.SourceExecutionException;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public interface ISourceHandler {
    Result findResult(HtmlPage startPage, String filter) throws SourceExecutionException;
}
