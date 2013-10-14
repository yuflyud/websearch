package org.yflyud.projects.websearch.engine;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.yflyud.projects.websearch.engine.config.ConfigurationException;
import org.yflyud.projects.websearch.engine.config.WebSearchEngineConfigurator;
import org.yflyud.projects.websearch.engine.config.beans.EngineConfiguration;
import org.yflyud.projects.websearch.engine.source.SourceDefinition;
import org.yflyud.projects.websearch.engine.source.SourceExecutionException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WebSearchEngine {
    // TODO it's not good to have two maps here and keep them both in sync.
    private final Map<String, SourceDefinition> sources;
    private final Map<String, WebClient> webClients;
    private final WebSearchEngineConfigurator configurator;

    public WebSearchEngine() {
        sources = new LinkedHashMap<String, SourceDefinition>();
        webClients = new HashMap<String, WebClient>();
        configurator = new WebSearchEngineConfigurator(new WebSearchEngineAcceptor());
    }

    public void init(String configPath) throws ConfigurationException {
        EngineConfiguration engineConfiguration = null;
        try {
            JAXBContext context = JAXBContext.newInstance(EngineConfiguration.class);

            Unmarshaller marshaller = context.createUnmarshaller();
            engineConfiguration = (EngineConfiguration) marshaller.unmarshal(WebSearchEngine.class
                    .getResourceAsStream(configPath));
        } catch (JAXBException e) {
            throw new ConfigurationException(e);
        }
        init(engineConfiguration);
    }

    public void init() throws ConfigurationException {
        this.init("/se_config.xml");
    }

    public void init(EngineConfiguration engineConfiguration) throws ConfigurationException {
        configurator.configure(this, engineConfiguration);
    }

    public Result findResult(String sourceName, String filter) throws SourceExecutionException {
        WebClient webClient = webClients.get(sourceName);
        SourceDefinition sourceDefinition = sources.get(sourceName);
        checkObjectAvailability(webClient, "exception.missing.webclient", sourceName);
        checkObjectAvailability(sourceDefinition, "exception.missing.sourcedef", sourceName);
        HtmlPage page = null;
        try {
            page = getPage(webClient, sourceDefinition.getHomePageURI());
            return sourceDefinition.getSourceHandler().findResult(page, filter);
        } finally {
            webClient.closeAllWindows();
        }
    }

    public Map<String, Result> findResults(String filter) throws SourceExecutionException {
        Map<String, Result> results = new LinkedHashMap<String, Result>();
        for (String sourceName : webClients.keySet()) {
            Result result = findResult(sourceName, filter);
            if (result != null) {
                results.put(sourceName, result);
            }
        }
        return results;
    }

    // TODO move to utility class
    private void checkObjectAvailability(Object object, String errorMessageCode, Object... args) {
        if (object == null
                || ((object instanceof String) && object.toString().trim().length() == 0)) {
            throw new WebSearchEngineException(MessageUtil.formatMessage(errorMessageCode, args));
        }
    }

    private HtmlPage getPage(WebClient webClient, URI pageUri) {
        HtmlPage page = null;
        try {
            page = webClient.getPage(pageUri.toString());
        } catch (IOException e) {
            throw new WebSearchEngineException(e);
        }
        return page;
    }

    public class WebSearchEngineAcceptor {
        private WebSearchEngineAcceptor() {
        };

        public Map<String, SourceDefinition> getSources() {
            return sources;
        }

        public Map<String, WebClient> getWebClients() {
            return webClients;
        }
    }
}
