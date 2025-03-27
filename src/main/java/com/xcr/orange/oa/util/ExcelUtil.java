package com.xcr.orange.oa.util;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.LinkedList;
import java.util.List;

public class ExcelUtil {

    public static List<List<String>> getSheetContent(XSSFSheet sheet, int columnNum) {
        List<List<String>> rows = new LinkedList<>();
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            List<String> line = new LinkedList<>();
            for (int j = 0; j < columnNum; j++) {
                XSSFCell cell = row.getCell(j);
                if (null != cell) {
                    cell.setCellType(CellType.STRING);
                    line.add(cell.toString());
                } else {
                    line.add(null);
                }
            }
            rows.add(line);
        }
        return rows;
    }
}
