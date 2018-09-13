package com.ylzinfo.test.performance;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ylzinfo.test.performance.base.DataInit;

public class FromHtmlToExcel extends DataInit {
//fsfstttttttbfsfsafsfsaffsdfasfsdfsafas
	protected static XSSFWorkbook wb;
	protected static XSSFSheet sheet;

	@SuppressWarnings("deprecation")
	protected static void writeHtmlToExcel(String htmlURL, String threadNumber,
			int sheetNumber) throws MalformedURLException, IOException {
		String sheetName = threadNumber + "个线程";
		int space = Integer.valueOf(threadNumber) + 3;
		
		wb = getWorkbook();
		sheet = getSheet(wb, sheetName, sheetNumber, cells);

		Source source = new Source(new URL(htmlURL));
		List<Element> elementList = source
				.findAllElements(HTMLElementName.TABLE);
		Element element = elementList.get(2);
		List<Element> trLists = element.findAllElements(HTMLElementName.TR);
		rows = trLists.size();
		List<Element> thLists = trLists.get(0).findAllElements(
				HTMLElementName.TH);
		XSSFRow row0 = sheet.createRow(0);
		for (int i = 0; i < cells; i++) {
			XSSFCellStyle cellStyle = setCellStyle(wb, Color.GREEN);
			XSSFCell cell = row0.createCell(i);
			cell.setCellValue(thLists.get(i).getContent().toString());
			cell.setCellStyle(cellStyle);
		}
		int add = 1;
		for (int i = 2 + space; i < rows; i += space) {
			XSSFRow row = sheet.createRow(add);
			add++;
			List<Element> tdLists = trLists.get(i).findAllElements(
					HTMLElementName.TD);
			for (int j = 0; j < cells; j++) {
				XSSFCell cells = row.createCell(j);
				XSSFCellStyle cellStyle = setCellStyle(wb, Color.WHITE);
				cells.setCellValue(tdLists.get(j).getContent().toString());
				cells.setCellStyle(cellStyle);
				if (j == 0) {
					XSSFCellStyle cellStyle1 = setCellStyle(wb,
							Color.LIGHT_GRAY);
					cells.setCellStyle(cellStyle1);
				}
			}
		}
		Element element1 = elementList.get(1);
		List<Element> trLists1 = element1.findAllElements(HTMLElementName.TR);
		rows = trLists.size();
		List<Element> tdLists1 = trLists1.get(1).findAllElements(
				HTMLElementName.TD);
		XSSFRow rowE = sheet.createRow(add);
		XSSFCell cell0 = rowE.createCell(0);
		XSSFCellStyle cellStyle = setCellStyle(wb, Color.red);
		cell0.setCellValue("Summary");
		cell0.setCellStyle(cellStyle);
		for (int i = 1; i < cells; i++) {
			XSSFCell cellE = rowE.createCell(i);
			XSSFCellStyle cellStyle1 = setCellStyle(wb, Color.red);
			cellE.setCellValue(tdLists1.get(i - 1).getContent().toString());
			cellE.setCellStyle(cellStyle1);
		}
		FileOutputStream fileOut = new FileOutputStream(excelPath);

		wb.write(fileOut);

		fileOut.close();
	}

	public static void main(String[] args) throws IOException {
		writeHtmlToExcel(args[0], args[1], Integer.valueOf(args[2]));
	}

}
