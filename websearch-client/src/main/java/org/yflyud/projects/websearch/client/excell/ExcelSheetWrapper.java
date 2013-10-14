package org.yflyud.projects.websearch.client.excell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.yflyud.projects.websearch.client.excell.rowprocessors.IExcelRowProcessor;

public class ExcelSheetWrapper {
    private static final Log LOGGER = LogFactory.getLog(ExcelSheetWrapper.class);
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private IRowProcessedListener rowProcessedListener;

    // TODO detect headers dynamically
    private boolean isFirstRowHeader = true;

    public ExcelSheetWrapper(InputStream workbookInputStream) throws IOException {
        workbook = new XSSFWorkbook(workbookInputStream);
        if (workbook.getNumberOfSheets() >= 1) {
            LOGGER.warn("Number of sheets in specified workbook is greater than 1. The first sheet will be taken as a default one.");
        }
        switchSheet(0);
    }

    public ExcelSheetWrapper(InputStream workbookInputStream, int sheetIndex) throws IOException {
        workbook = new XSSFWorkbook(workbookInputStream);
        switchSheet(sheetIndex);
    }

    public ExcelSheetWrapper(InputStream workbookInputStream, String sheetName) throws IOException {
        workbook = new XSSFWorkbook(workbookInputStream);
        switchSheet(sheetName);
    }

    public void switchSheet(int sheetIndex) {
        sheet = workbook.getSheetAt(sheetIndex);
    }

    public void switchSheet(String sheetName) {
        sheet = workbook.getSheet(sheetName);
    }

    public void processRows(IExcelRowProcessor processor) {
        Iterator<Row> rowIterator = sheet.iterator();
        if (isFirstRowHeader && rowIterator.hasNext()) {
            rowIterator.next();
        }
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            processor.processRow(row);
            if (rowProcessedListener != null) {
                rowProcessedListener.rowProcessed(row);
            }
        }
    }

    public void setFirstRowHeader(boolean isFirstRowHeader) {
        this.isFirstRowHeader = isFirstRowHeader;
    }

    public boolean isFirstRowHeader() {
        return isFirstRowHeader;
    }

    public IRowProcessedListener getRowProcessedListener() {
        return rowProcessedListener;
    }

    public void setRowProcessedListener(IRowProcessedListener rowProcessedListener) {
        this.rowProcessedListener = rowProcessedListener;
    }

    public void save(OutputStream outputStream) throws IOException {
        try {
            workbook.write(outputStream);
        } finally {
            outputStream.close();
        }
    }
}
