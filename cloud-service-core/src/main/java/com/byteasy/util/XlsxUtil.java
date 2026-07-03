package com.byteasy.util;

import cn.hutool.poi.excel.ExcelWriter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

public class XlsxUtil {

    public static void setFontColor(ExcelWriter writer, int x, int y, short color, boolean bold) {
        Font font = writer.createFont();
        font.setBold(bold);
        font.setColor(color);

        CellStyle cellStyle = writer.createCellStyle(x, y);
        cellStyle.setFont(font);
        writer.setStyle(cellStyle, x, y);
    }

    public static void setBorder(ExcelWriter writer, int x, int y) {
        CellStyle cellStyle = writer.getOrCreateCellStyle(x, y);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        writer.setStyle(cellStyle, x, y);
    }
}
