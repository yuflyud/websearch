package org.yflyud.projects.websearch.engine;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class MessageUtil {
    private static ResourceBundle myResources = ResourceBundle.getBundle("messages/websearch_engine_messages");

    public static String formatMessage(String messageKey, Object... args) {
        MessageFormat mf = new MessageFormat(myResources.getString(messageKey));
        return mf.format(args);
    }
}