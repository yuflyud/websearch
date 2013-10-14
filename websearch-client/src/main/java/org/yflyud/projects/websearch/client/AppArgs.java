package org.yflyud.projects.websearch.client;

import java.util.HashMap;
import java.util.Map;


public class AppArgs {
    private static final String PROPERTIES_ARG = "properties";
    private static final String INPUT_ARG = "input";
    private String inputArg;
    private String propertiesArg;

    public AppArgs(String[] args) {
        Map<String, String> argsMap = new HashMap<String, String>();
        for (String arg : args) {
            addArgumentToMap(argsMap, arg);
        }
        for (Map.Entry<String, String> argEntry : argsMap.entrySet()) {
            initArgFromEntry(argEntry);
        }

    }

    private void initArgFromEntry(Map.Entry<String, String> argEntry) {
        if (INPUT_ARG.equals(argEntry.getKey())) {
            inputArg = argEntry.getValue().trim();
        } else if (PROPERTIES_ARG.equals(argEntry.getKey())) {
            propertiesArg = argEntry.getValue().trim();
        } else {
            throw new IllegalArgumentException(MessageUtil.formatMessage(
                    "exception.properties.unknown", argEntry.getKey()));
        }
    }

    private void addArgumentToMap(Map<String, String> argsMap, String arg) {
        String[] keyValue = arg.split("=");
        if (keyValue.length != 2) {
            throw new IllegalArgumentException(MessageUtil.formatMessage(
                    "exception.properties.invalidformat", arg));
        }
        String key = keyValue[0].trim();
        String value = keyValue[1].trim();
        if (!key.startsWith("-D")) {
            throw new IllegalArgumentException(MessageUtil.formatMessage(
                    "exception.properties.invalidnameformat", key));
        }
        key = key.replaceFirst("-D", "");
        argsMap.put(key, value);
    }

    public String getInputArg() {
        return inputArg;
    }

    public String getPropertiesArg() {
        return propertiesArg;
    }
}