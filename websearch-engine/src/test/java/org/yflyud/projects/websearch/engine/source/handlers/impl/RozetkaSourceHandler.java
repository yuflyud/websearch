package org.yflyud.projects.websearch.engine.source.handlers.impl;

import java.io.IOException;

import org.yflyud.projects.websearch.engine.Result;
import org.yflyud.projects.websearch.engine.source.handlers.ISourceHandler;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class RozetkaSourceHandler implements ISourceHandler {
    public Result findResult(HtmlPage startPage, String filter) {
        final HtmlForm form = startPage.getFirstByXPath("//form[@class='search']");
        final HtmlButton button = form.getFirstByXPath("//button[@type='submit']");
        final HtmlTextInput textField = form.getInputByName("text");
        textField.setValueAttribute(filter);

        HtmlPage page2 = null;
        try {
            page2 = button.click();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HtmlAnchor anchor = page2
                .getFirstByXPath("//div[@class='goods list']/table[contains(@class, 'item')]//div[@class='title']/a");

        HtmlPage page3 = null;
        try {
            page3 = anchor.click();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HtmlDivision div = page3.getFirstByXPath("//div[@class='pp-tab-characteristics']");
        Result r = new Result();
        r.addCharacteristic("rawCharacteristic", div.asText());
        return r;
    }
}
