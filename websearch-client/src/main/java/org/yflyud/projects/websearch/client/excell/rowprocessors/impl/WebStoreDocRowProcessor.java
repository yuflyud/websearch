package org.yflyud.projects.websearch.client.excell.rowprocessors.impl;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.yflyud.projects.websearch.client.MessageUtil;
import org.yflyud.projects.websearch.client.PropertyKeys;
import org.yflyud.projects.websearch.client.excell.WorkbookUtil;
import org.yflyud.projects.websearch.client.excell.rowprocessors.IExcelRowProcessor;
import org.yflyud.projects.websearch.engine.Result;
import org.yflyud.projects.websearch.engine.WebSearchEngine;
import org.yflyud.projects.websearch.engine.source.SourceExecutionException;

public class WebStoreDocRowProcessor implements IExcelRowProcessor {
    private static final Log LOGGER = LogFactory.getLog(WebStoreDocRowProcessor.class);

    private final WebSearchEngine engine;

    private final boolean useFullSearch;
    private final boolean initArticleColumn;
    private final int vendorColumnIndex;
    private final int nameColumnIndex;
    private final int articleColumnIndex;
    private final int modelColumnIndex;
    private final boolean yandexMarketEnabled;
    private final int yandexMarketURIColumnIndex;
    private final long requestDelay;

    public WebStoreDocRowProcessor(WebSearchEngine engine, Properties properties) {
        this.engine = engine;
        useFullSearch = Boolean.valueOf(properties
                .getProperty(PropertyKeys.ROW_PROCESSOR_USE_FULL_SEARCH));
        requestDelay = Integer.valueOf(properties
                .getProperty(PropertyKeys.ROW_PROCESSOR_REQUEST_DELAY));
        initArticleColumn = Boolean.valueOf(properties
                .getProperty(PropertyKeys.ROW_PROCESSOR_INIT_ARTICLE_COLUMN));
        vendorColumnIndex = Integer.valueOf(properties
                .getProperty(PropertyKeys.ROW_PROCESSOR_VENDOR_COLUMN));
        nameColumnIndex = Integer.valueOf(properties
                .getProperty(PropertyKeys.ROW_PROCESSOR_NAME_COLUMN));
        articleColumnIndex = Integer.valueOf(properties
                .getProperty(PropertyKeys.ROW_PROCESSOR_ARTICLE_COLUMN));
        modelColumnIndex = Integer.valueOf(properties
                .getProperty(PropertyKeys.ROW_PROCESSOR_MODEL_COLUMN));
        yandexMarketEnabled = Boolean.valueOf(properties
                .getProperty(PropertyKeys.ROW_PROCESSOR_YANDEX_MARKET_ENABLED));
        yandexMarketURIColumnIndex = Integer.valueOf(properties
                .getProperty(PropertyKeys.ROW_PROCESSOR_YANDEX_MARKET_URI_COLUMN));
    }

    // @Override
    public void processRow(Row row) {
        String itemNameFull = combineCellValues(row, vendorColumnIndex, modelColumnIndex);

        if (initArticleColumn) {
            initArticleColumn(row);
        }

        String itemNameShort = WorkbookUtil.getCellValueAsString(row.getCell(articleColumnIndex));
        if (itemNameFull.length() == 0 && itemNameShort.length() == 0) {
            LOGGER.debug(MessageUtil.formatMessage("processing.emptyname", row.getRowNum()));
            return;
        }

        if (yandexMarketEnabled) {
            Result result = null;
            result = getResult(itemNameFull, result);
            if (result == null && useFullSearch) {
                LOGGER.debug(MessageUtil.formatMessage("processing.searchingbyarticle",
                        itemNameFull, itemNameShort));
                result = getResult(itemNameShort, result);
            }
            if (result != null) {
                Cell cell = row.createCell(yandexMarketURIColumnIndex);
                WorkbookUtil.setCellHyperlinkValue(cell, result.getResultURI());
            } else {
                LOGGER.debug(MessageUtil.formatMessage("processing.yandex_market.noresult",
                        itemNameFull));
            }
        }
    }

    private void initArticleColumn(Row row) {
        Cell cell = row.getCell(articleColumnIndex);
        if (cell == null) {
            cell = row.createCell(articleColumnIndex);
        }
        String val = WorkbookUtil.getCellValueAsString(cell);
        if (val == null || val.trim().length() == 0) {
            val = combineCellValues(row, vendorColumnIndex, nameColumnIndex);
            if (val.trim().length() != 0) {
                WorkbookUtil.setCellForeground(cell, IndexedColors.YELLOW.getIndex());
            }
        }
        cell.setCellValue(val);
    }

    private String combineCellValues(Row row, int cellIndex1, int cellIndex2) {
        String val1 = WorkbookUtil.getCellValueAsString(row.getCell(cellIndex1));
        String val2 = WorkbookUtil.getCellValueAsString(row.getCell(cellIndex2));
        StringBuilder sb = new StringBuilder(val1 == null ? "" : val1.trim());
        if (val2 != null && val2.trim().length() != 0) {
            sb.append(" ").append(val2.trim());
        }
        return sb.toString();
    }

    private Result getResult(String itemName, Result result) {
        try {
            try {
                Thread.sleep(requestDelay);
            } catch (InterruptedException e) {
                // No need to log error.
            }
            result = engine.findResult("yandex_market", itemName);
        } catch (SourceExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

}
