package org.yflyud.projects.websearch.engine;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Result {
    private URI resultURI;
    private final Map<String, Object> properties;
    private final Map<String, Object> characteristics;

    public Result() {
        properties = new LinkedHashMap<String, Object>();
        characteristics = new LinkedHashMap<String, Object>();
    }

    public Result(URI resultURI) {
        this();
        this.resultURI = resultURI;
    }

    public URI getResultURI() {
        return resultURI;
    }

    public void setResultURI(URI resultURI) {
        this.resultURI = resultURI;
    }

    public Map<String, Object> getProperties() {
        return new HashMap<String, Object>(properties);
    }

    public Object addProperty(String propertyName, Object propertyValue) {
        return properties.put(propertyName, propertyValue);
    }

    public Object removeProperty(String propertyName) {
        return properties.remove(propertyName);
    }

    public Map<String, Object> getCharacteristics() {
        return new HashMap<String, Object>(characteristics);
    }

    public Object addCharacteristic(String characteristicName, Object characteristicValue) {
        return characteristics.put(characteristicName, characteristicValue);
    }

    public Object removeCharacteristic(String characteristicName) {
        return characteristics.remove(characteristicName);
    }

}
