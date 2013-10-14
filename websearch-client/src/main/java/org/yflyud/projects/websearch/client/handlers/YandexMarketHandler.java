package org.yflyud.projects.websearch.client.handlers;

import java.io.IOException;
import java.net.URISyntaxException;

import org.yflyud.projects.websearch.engine.Result;
import org.yflyud.projects.websearch.engine.source.SourceExecutionException;
import org.yflyud.projects.websearch.engine.source.handlers.ISourceHandler;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class YandexMarketHandler implements ISourceHandler {

    // @Override
    public Result findResult(HtmlPage startPage, String filter) throws SourceExecutionException {
        if (filter.trim().length() == 0) {
            return null;
        }
        HtmlTextInput searchInput = startPage.getHtmlElementById("search-input");
        searchInput.setValueAttribute(filter);

        HtmlSubmitInput searchButton = searchInput.getEnclosingElement("tr").getFirstByXPath(
                "td/span/input[@type='submit']");
        HtmlPage resultPage;
        try {
            resultPage = searchButton.click();
        } catch (IOException e) {
            throw new SourceExecutionException(startPage.getUrl().toString(), e);
        }
        HtmlDivision div = resultPage
                .getFirstByXPath("//div[@class=' js-metrika-offers-count' and @data-metrika-type='search']");
        if (div != null) {
            HtmlAnchor searchResultAnchor = resultPage
                    .getFirstByXPath("//div[contains(@class, 'b-offers_type_guru')]/div[@class='b-offers__desc']/h3/a");
            if (searchResultAnchor != null) {
                try {
                    resultPage = searchResultAnchor.click();
                } catch (IOException e) {
                    throw new SourceExecutionException(resultPage.getUrl().toString(),
                            new NullPointerException());
                }
            } else {
                return null;
            }
        } else {
            HtmlElement emptyResultPage = resultPage.getFirstByXPath("//p[@class='no-found']");
            if (emptyResultPage != null) {
                return null;
            }
        }
        HtmlAnchor characteristicsAnchor = resultPage
                .getFirstByXPath("//div[@class='b-model-tabs']/ul/li[2]/span/a");

        HtmlPage characteristicsPage;
        try {
            characteristicsPage = characteristicsAnchor.click();
        } catch (IOException e) {
            throw new SourceExecutionException(resultPage.getUrl().toString(), e);
        }
        Result result = new Result();
        try {
            result.setResultURI(characteristicsPage.getUrl().toURI());
        } catch (URISyntaxException e) {
            throw new SourceExecutionException(characteristicsPage.getUrl().toString(), e);
        }

        return result;
    }
}
