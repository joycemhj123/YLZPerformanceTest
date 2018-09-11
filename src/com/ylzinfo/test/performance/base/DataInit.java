package com.ylzinfo.test.performance.base;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataInit {
	// 数据库用户
	protected static String casUsername = "ylz_cas";
	protected static String casPassword = "ylz_cas";

	protected static String mswUsername = "ylz_msw";
	protected static String mswPassword = "ylz_msw";
	
	protected static String pcUsername = "ylz_payment";
	protected static String pcPassword = "ylz_payment";

	// 脚本数据
	protected static String registFileName = "/registSSO.txt";
	protected static String loginFileName = "/casLogin.txt";
	protected static String searchFileName = "/search.txt";
	protected static String viewAppFileName = "/viewApp.txt";
	protected static String runAppFileName = "/runApp.txt";
	protected static String buyAppFileName = "/buyApp.txt";
	protected static String productYDFileName = "/productYD.txt";
	protected static String productJGFileName = "/productJG.txt";

	// Excel数据
	protected static String excelPath = "../copy/result.xlsx";
	protected static int rows, cells = 8;

	// Excel XSSFWorkbook初始化
	protected static XSSFWorkbook getWorkbook() throws IOException {
		InputStream is = new FileInputStream(excelPath);
		XSSFWorkbook wb = new XSSFWorkbook(is);
		return wb;
	}

	// Excel XSSFSheet初始化
	protected static XSSFSheet getSheet(XSSFWorkbook wb, String sheetName, int sheetNumber, int cells) throws IOException {
		if (sheetNumber == 0) {
			int totalSheetNumbers = wb.getNumberOfSheets();
			// 只有一张表时没办法删除
			for (int i = 0; i < totalSheetNumbers - 1; i++) {
				wb.removeSheetAt(0);
			}
			// 先新建一张表，然后把之前最后无法删除的表删除掉
			wb.createSheet();
			wb.removeSheetAt(0);
			wb.setSheetName(0, sheetName);
		} else {
			wb.createSheet(sheetName);
		}
		XSSFSheet sheet = wb.getSheet(sheetName);
		sheet.setColumnWidth(0, 6000);
		for (int i = 1; i < cells; i++) {
			sheet.setColumnWidth(i, 4000);
		}
		return sheet;
	}

	// 样式初始化
	@SuppressWarnings("static-access")
	public static XSSFCellStyle setCellStyle(XSSFWorkbook wb, Color color) {
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFillForegroundColor(new XSSFColor(color));
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		cellStyle.setFillPattern(cellStyle.SOLID_FOREGROUND);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		return cellStyle;
	}
}
