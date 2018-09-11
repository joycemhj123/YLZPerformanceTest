package com.ylzinfo.test.performance;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ylzinfo.test.performance.base.DataInit;

public class SheetMerge extends DataInit {

	public static void getFirstCells(XSSFSheet sheet, XSSFSheet sheetold,
			int rowNum) {
		for (int i = 1; i < rowNum; i++) {
			XSSFCell cell = sheet.createRow(i).createCell(0);
			cell.setCellStyle(sheetold.getRow(i).getCell(0).getCellStyle());
			cell.setCellValue(sheetold.getRow(i).getCell(0)
					.getStringCellValue());
		}

	}

	public static void getFirstAndLastRows(XSSFWorkbook wb, XSSFSheet sheet,
			int cellNum, int rowNumLast) {
		XSSFRow row = sheet.createRow(0);
		XSSFRow rowLast = sheet.createRow(rowNumLast);
		for (int i = 1; i < cellNum; i++) {
			XSSFCell cell = row.createCell(i);
			XSSFCell cellLast = rowLast.createCell(i);
			cell.setCellStyle(wb.getSheetAt(0).getRow(0).getCell(i)
					.getCellStyle());
			cellLast.setCellStyle(wb.getSheetAt(0).getRow(rowNumLast)
					.getCell(i).getCellStyle());
			cell.setCellValue(wb.getSheetName(i - 1));
		}
		getSingleCellValueAndStyle(rowLast, wb.getSheetAt(0).getRow(rowNumLast)
				.getCell(0));
		getSingleCellValueAndStyle(row, wb.getSheetAt(0).getRow(0).getCell(0));
	}

	public static void getSingleCellValueAndStyle(XSSFRow row, XSSFCell cell) {
		row.createCell(0).setCellValue(cell.getStringCellValue());
		row.getCell(0).setCellStyle(cell.getCellStyle());
	}

	public static void main(String[] args) throws IOException {
		int totalThreadNumber = Integer.valueOf(args[0]);
		int cellNum = totalThreadNumber + 1;
		int sheetNum = cells;
		InputStream is = new FileInputStream(excelPath);
		XSSFWorkbook wb = new XSSFWorkbook(is);
		int rowNum = wb.getSheetAt(0).getLastRowNum();
		for (int i = 0; i < sheetNum - 1; i++) {
			int sheetNewNum = totalThreadNumber + i;
			String sheetName = wb.getSheetAt(0).getRow(0).getCell(i + 1)
					.getStringCellValue().toString();
			XSSFSheet sheetNew = getSheet(wb, sheetName, sheetNewNum, cellNum);
			getFirstCells(sheetNew, wb.getSheetAt(0), rowNum);
			getFirstAndLastRows(wb, sheetNew, cellNum, rowNum);
			for (int j = 0; j < totalThreadNumber; j++) {
				for (int k = 1; k < rowNum + 1; k++) {
					XSSFRow rowNew = sheetNew.getRow(k);
					XSSFCell cellNew = rowNew.createCell(j + 1);
					String cellValue = wb.getSheetAt(j).getRow(k)
							.getCell(i + 1).getStringCellValue();
					XSSFCellStyle cellLastStyle = setCellStyle(wb, Color.white);
					cellNew.setCellValue(cellValue);
					cellNew.setCellStyle(cellLastStyle);
				}
			}
			for (int j = 0; j < sheetNew.getRow(0).getLastCellNum(); j++) {
				XSSFCell cellLast = sheetNew.getRow(rowNum).getCell(j);
				XSSFCellStyle cellLastStyle = setCellStyle(wb, Color.red);
				cellLast.setCellStyle(cellLastStyle);
			}
		}
		FileOutputStream fileOut = new FileOutputStream(excelPath);

		wb.write(fileOut);

		fileOut.close();
	}
}
