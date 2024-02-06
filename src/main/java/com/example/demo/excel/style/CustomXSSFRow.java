package com.example.demo.excel.style;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

public class CustomXSSFRow {
    private XSSFRow xssfRow;
    private CellStyle defaultCellStyle;
    private int currentColumnIndex = 0;

    public CustomXSSFRow(XSSFRow xssfRow) {
        this.xssfRow = xssfRow;
        currentColumnIndex = 1;
        this.defaultCellStyle = null;
    }

    public int getCurrentColumnIndex() {
        return currentColumnIndex;
    }

    public CustomXSSFRow(XSSFRow xssfRow, CellStyle defaultCellStyle) {
        this.xssfRow = xssfRow;
        currentColumnIndex = 1;
        this.defaultCellStyle = defaultCellStyle;
    }

    public XSSFCell createCell(int columnIndex) {
        return xssfRow.createCell(columnIndex);
    }

    public XSSFCell createCell(CellType type) {

        if (this.defaultCellStyle != null)
            return createCell(type, defaultCellStyle);

        return xssfRow.createCell(currentColumnIndex++, type);
    }

    public XSSFCell createCell(CellType type, CellStyle cellStyle) {
        XSSFCell cell = xssfRow.createCell(currentColumnIndex++, type);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    public XSSFCell createCell(CellType type, CellStyle cellStyle, String cellValue) {
        XSSFCell cell = xssfRow.createCell(currentColumnIndex++, type);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(cellValue);
        return cell;
    }

    public XSSFCell createCell(CellType type, CellStyle cellStyle, double cellValue) {
        XSSFCell cell = xssfRow.createCell(currentColumnIndex++, type);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(cellValue);
        return cell;
    }

    public XSSFCell createCell(CellType type, CellStyle cellStyle, int cellValue) {
        XSSFCell cell = xssfRow.createCell(currentColumnIndex++, type);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(cellValue);
        return cell;
    }

    public void setHeight(short height) {
        xssfRow.setHeight(height);
    }

    public void setRowStyle(CellStyle style) {
        xssfRow.setRowStyle(style);
    }
}
