package org.yflyud.projects.websearch.engine.config;

import org.yflyud.projects.websearch.engine.MessageUtil;
import org.yflyud.projects.websearch.engine.WebSearchEngine;
import org.yflyud.projects.websearch.engine.WebSearchEngine.WebSearchEngineAcceptor;
import org.yflyud.projects.websearch.engine.WebSearchEngineException;
import org.yflyud.projects.websearch.engine.config.beans.EngineConfiguration;
import org.yflyud.projects.websearch.engine.config.beans.SourceConfiguration;
import org.yflyud.projects.websearch.engine.source.SourceDefinition;
import org.yflyud.projects.websearch.engine.source.handlers.ISourceHandler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;

public class WebSearchEngineConfigurator {
    private WebSearchEngineAcceptor acceptor;

    public WebSearchEngineConfigurator(WebSearchEngineAcceptor acceptor) {
        this.acceptor = acceptor;
    }

    // TODO localize all messages.
    public void configure(WebSearchEngine engine, EngineConfiguration engineConfiguration)
            throws ConfigurationException {
        acceptor.getSources().clear();
        acceptor.getWebClients().clear();
        Boolean shareBrowser = engineConfiguration.getShareBrowser();
        BrowserVersion browserVersion = engineConfiguration.getBrowserVersion();
        Boolean javaScriptEnabled = engineConfiguration.getJavaScriptEnabled();
        WebClient webClient = null;
        if (shareBrowser) {
            webClient = initWebClient(browserVersion, javaScriptEnabled);
        } else {
            if (browserVersion != null) {
                throw new RuntimeException(
                        "Browser version should not be specified when browser is not shared.");
            }
            if (javaScriptEnabled != null) {
                throw new RuntimeException(
                        "Enable JavaScript option should not be specified when browser is not shared.");
            }
        }
        if (engineConfiguration.getSources() == null
                || engineConfiguration.getSources().size() == 0) {
            throw new RuntimeException("At least one source configuration is required.");
        }
        for (SourceConfiguration sourceConfiguration : engineConfiguration.getSources()) {
            webClient = handleSourceConfiguration(shareBrowser, webClient, sourceConfiguration);
        }
    }

    private WebClient handleSourceConfiguration(Boolean shareBrowser, WebClient webClient,
            SourceConfiguration sourceConfiguration) throws ConfigurationException {
        BrowserVersion browserVersion;
        Boolean javaScriptEnabled;
        String sourceName = sourceConfiguration.getSourceName();
        checkObjectAvailability(sourceName, "exception.configuration.missing.source");
        browserVersion = sourceConfiguration.getBrowserVersion();
        javaScriptEnabled = sourceConfiguration.getJavaScriptEnabled();
        if (shareBrowser) {
            if (browserVersion != null) {
                throw new RuntimeException(
                        "Browser version for specific source is not allowed in 'Share Browser' mode.");
            }
            if (javaScriptEnabled != null) {
                throw new RuntimeException(
                        "Enable JavaScript option should not be specified when browser is not shared.");
            }

        } else {
            webClient = initWebClient(browserVersion, javaScriptEnabled);
        }
        if (acceptor.getWebClients().put(sourceName, webClient) != null) {
            throw new RuntimeException("Two sources with identical names are not allowed.");
        }
        SourceDefinition sourceDefinition = new SourceDefinition();
        sourceDefinition.setHomePageURI(sourceConfiguration.getHomePageURI());
        checkObjectAvailability(sourceDefinition.getHomePageURI(),
                "exception.configuration.missing.homepageuri", sourceName);
        sourceDefinition.setSourceHandler(getSourceHandler(sourceName,
                sourceConfiguration.getSourceHandlerClass()));
        acceptor.getSources().put(sourceName, sourceDefinition);
        return webClient;
    }

    // TODO move to utility class
    private void checkObjectAvailability(Object object, String errorMessageCode, Object... args) {
        if (object == null
                || ((object instanceof String) && object.toString().trim().length() == 0)) {
            throw new WebSearchEngineException(MessageUtil.formatMessage(errorMessageCode, args));
        }
    }

    private ISourceHandler getSourceHandler(String sourceName, String sourceHandlerClass)
            throws ConfigurationException {
        checkObjectAvailability(sourceHandlerClass,
                "exception.configuration.missing.sourcehandlerlazz", sourceName);

        try {
            return (ISourceHandler) (Class.forName(sourceHandlerClass)).newInstance();
        } catch (InstantiationException e) {
            throw new ConfigurationException(MessageUtil.formatMessage(
                    "exception.configuration.handlercreationfailure", sourceHandlerClass), e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationException(MessageUtil.formatMessage(
                    "exception.configuration.handlercreationfailure", sourceHandlerClass), e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException(MessageUtil.formatMessage(
                    "exception.configuration.classnotfound", sourceHandlerClass), e);
        }
    }

    private WebClient initWebClient(BrowserVersion browserVersion, Boolean javaScriptEnabled) {
        WebClient webClient;
        webClient = new WebClient(browserVersion == null ? BrowserVersion.getDefault()
                : browserVersion);
        if (javaScriptEnabled != null) {
            webClient.getOptions().setJavaScriptEnabled(javaScriptEnabled);
        }
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(false);
        return webClient;
    }
}
