package com.ylzinfo.test.performance;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestDataGet {

	public static void main(String[] args) throws Exception {
		InputStream is = new FileInputStream(
				"D:\\YiLianZhong\\code\\docs\\产品部\\产品部测试数据\\testdata\\城镇职工社保五险查询\\城镇五保之医保\\参保缴费情况_参保缴费详情(1).xlsx");
		XSSFWorkbook wb = new XSSFWorkbook(is);
		XSSFSheet sheet = wb.getSheetAt(0);
		String sUser = "";
		String sName = "";
		String sEmail = "";
		for (int i = 1; i < sheet.getLastRowNum(); i++) {
			if (!sUser.contains(sheet.getRow(i).getCell(1).toString().replace(" ", ""))) {
				sUser += sheet.getRow(i).getCell(1).toString().replace(" ", "");
				sUser += ",";
				sUser += sheet.getRow(i).getCell(3).toString().replace(" ", "");
				String sNameF = sheet.getRow(i).getCell(2).toString();
				sNameF = sNameF.replace(" ", "");
				sName += URLEncoder.encode(sNameF, "utf-8");
				// sName += sheet.getRow(i).getCell(2).toString();
				StringBuffer array = new StringBuffer();
				String ss = "abcdefghijklmnopqrstuvwxyz1234567890";
				for (int i1 = 0; i1 < 7; i1++) {
					array.append(ss.charAt((int) (Math.random() * ss.length())));
				}
				array.append("@");
				for (int i2 = 0; i2 < 7; i2++) {
					array.append(ss.charAt((int) (Math.random() * ss.length())));
				}
				array.append(".com");
				String email = array.toString();
				sEmail += email;
				sUser += "\n";
				sName += "\n";
				sEmail += "\n";
			}
		}
		String filepathUser = "../testData/" + "/registUser.txt";
		String filepathName = "../testData/" + "/registName.txt";
		String filepathEmail = "../testData/" + "/registEmail.txt";
		FileWriter fwUser = new FileWriter(filepathUser);
		FileWriter fwName = new FileWriter(filepathName);
		FileWriter fwEmail = new FileWriter(filepathEmail);
		fwUser.write(sUser);
		fwUser.close();
		fwName.write(sName);
		fwName.close();
		fwEmail.write(sEmail);
		fwEmail.close();
	}
}
