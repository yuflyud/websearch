package org.yflyud.projects.websearch.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.yflyud.projects.websearch.client.excell.ExcelSheetWrapper;
import org.yflyud.projects.websearch.client.excell.IRowProcessedListener;
import org.yflyud.projects.websearch.client.excell.rowprocessors.impl.WebStoreDocRowProcessor;
import org.yflyud.projects.websearch.engine.WebSearchEngine;
import org.yflyud.projects.websearch.engine.config.ConfigurationException;

public class AppRunner {
    private static final Log LOGGER = LogFactory.getLog(AppRunner.class);
    private static final String APP_DEFAULT_PROPERTIES_PATH = "/app_default.properties";

    public static void main(String[] args) throws IOException, ConfigurationException {
        LOGGER.info("Application started. Initializing components...");
        // Start the watch
        StopWatch watch = new StopWatch();
        watch.start();

        // Read application arguments
        AppArgs appArgs = new AppArgs(args);

        final WebSearchEngine engine = new WebSearchEngine();
        // Read configuration data from /se_config.properties
        engine.init();

        // Read application properties
        Properties properties = readProperties(appArgs.getPropertiesArg());

        // Get input file path
        String inputFilePath = getInputFilePath(appArgs.getInputArg(), properties);

        // Initialize sheet processing
        ExcelSheetWrapper sheetWrapper = new ExcelSheetWrapper(new FileInputStream(inputFilePath),
                Integer.valueOf(properties.getProperty(PropertyKeys.SHEET_INDEX)));
        sheetWrapper.setFirstRowHeader(Boolean.valueOf(properties
                .getProperty(PropertyKeys.SHEET_CONTAINS_HEADER)));
        sheetWrapper.setRowProcessedListener(new RowProcessedListener());

        LOGGER.info("Components initialized. Application starts to process the input file.");
        // Process each sheet row
        sheetWrapper.processRows(new WebStoreDocRowProcessor(engine, properties));

        LOGGER.info("Processing done. Saving data...");
        // Store changes to new workbook
        File inputFile = new File(inputFilePath);
        String fileName = getNewFileName(properties, inputFile);
        sheetWrapper.save(new FileOutputStream(new File(inputFile.getParentFile(), fileName)));
        LOGGER.info(fileName + " was successfully saved to disk.");
        watch.stop();

        String timeString = getElapsedTime(watch);
        LOGGER.info("Done! Total elapsed time is: " + timeString);
    }

    private static String getElapsedTime(StopWatch watch) {
        Date date = new Date(watch.getTime());
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSSS", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    private static String getInputFilePath(String inputFilePath, Properties properties) {
        if (inputFilePath == null || inputFilePath.isEmpty()) {
            inputFilePath = properties.getProperty(PropertyKeys.APP_ARGS_DEFAULTS_INPUT);
        }
        return inputFilePath;
    }

    private static Properties readProperties(String propertiesArg) throws IOException,
            FileNotFoundException {
        Properties properties = new Properties();

        properties.load(AppRunner.class.getResourceAsStream(APP_DEFAULT_PROPERTIES_PATH));

        if (propertiesArg != null && !propertiesArg.isEmpty()) {
            Properties user_properties = new Properties();
            user_properties.load(new FileInputStream(propertiesArg));
            properties.putAll(user_properties);
        }
        return properties;
    }

    private static String getNewFileName(Properties properties, File inputFile) {
        Matcher matcher = Pattern.compile(".?\\w+$").matcher(inputFile.getName());
        String fileExtension = "";
        if (matcher.find()) {
            fileExtension = matcher.group();
        }
        String fileName = matcher.replaceFirst("");
        fileName = fileName + properties.getProperty(PropertyKeys.WORKBOOK_ONSAVE_SUFFIX)
                + fileExtension;
        return fileName;
    }
}

class RowProcessedListener implements IRowProcessedListener {
    private static final Log LOGGER = LogFactory.getLog(RowProcessedListener.class);

    public RowProcessedListener() {
    }

    // @Override
    public void rowProcessed(Row row) {
        LOGGER.debug(String.format("Processed row: %d of %d", row.getRowNum(), row.getSheet()
                .getLastRowNum()));
    }

}
