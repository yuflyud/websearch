package org.yflyud.projects.websearch.client.excell;

import java.net.URI;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

public final class WorkbookUtil {

    private WorkbookUtil() {
    }

    public static void setCellHyperlinkValue(Cell cell, URI uri) {
        Workbook workbook = cell.getSheet().getWorkbook();
        CellStyle hlink_style = workbook.createCellStyle();
        Font hlink_font = workbook.createFont();
        hlink_font.setUnderline(Font.U_SINGLE);
        hlink_font.setColor(IndexedColors.BLUE.getIndex());
        hlink_style.setFont(hlink_font);

        CreationHelper createHelper = workbook.getCreationHelper();
        Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_URL);
        link.setAddress(uri.toString());

        cell.setHyperlink(link);
        cell.setCellStyle(hlink_style);

        cell.setCellValue(uri.toString());
    }

    public static void setCellForeground(Cell cell, short colorIndex) {
        CellStyle style = cell.getSheet().getWorkbook().createCellStyle();

        style.setFillForegroundColor(colorIndex);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cell.setCellStyle(style);
        cell.setCellStyle(style);
    }

    public static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        String value = null;
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_BOOLEAN:
            value = String.valueOf(cell.getBooleanCellValue());
            break;
        case Cell.CELL_TYPE_NUMERIC:
            value = String.valueOf(cell.getNumericCellValue());
            break;
        case Cell.CELL_TYPE_STRING:
            value = cell.getStringCellValue();
            break;
        }
        return value;
    }
}
