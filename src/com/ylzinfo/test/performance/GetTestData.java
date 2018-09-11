package com.ylzinfo.test.performance;

import java.io.FileWriter;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

import com.ylzinfo.test.performance.base.DBFunctionalTestCase;

public class GetTestData extends DBFunctionalTestCase {
	public static Statement stmt;

	// 产品部测试数据：医疗机构查询
	public static void productData(int number) throws Exception {
		String sqlYD = "select fwwdmc as aa from vw_sst_ddydjg where rownum<="
				+ number;
		String sqlJG = "select fwwdmc as aa from VW_YB_SST_DDYLJG where rownum<="
				+ number;
		getAndWriteProductData(productGetStatement(), sqlYD, productYDFileName);
		getAndWriteProductData(productGetStatement(), sqlJG, productJGFileName);
	}

	// 注册测试数据
	public static void RegistData(int number) throws Exception {
		StringBuffer buffer = new StringBuffer(
				"_0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
		Random r = new Random();
		int range = buffer.length();
		String s = "";
		for (int i = 0; i < number; i++) {
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < 18; j++) {
				sb.append(buffer.charAt(r.nextInt(range)));
			}
			s += sb;
			s += "\n";
		}
		writeData(registFileName, s);
	}

	// 登录测试数据
	public static void LoginData(int number) throws Exception {
		String sql = "select USER_NAME as aa from users where rownum<= "
				+ number;
		String s = mswGetAndWriteData(mswGetStatement(), sql, loginFileName);
		// 为之后购买用户登录加钱（购买用户登录用的是登录的数据）
		String sqlBuy = "update wallet set income='1000000' where user_id in (select user_id from Pc_USER where username in "
				+ s + ")";
		// 修改支付密码
		String sqlPwd = "update pc_user set PAYMENT_PASSWORD='bda7af550e8ca05b1c267740e82d4214'";
		// 修改密码输入错误次数，因为一天内密码输入错误次数=5时就不能再支付了。
		String sqlWrongTimes = "update PAYPWD_LIMIT set PAY_TIMES=0";
		pcGetStatement().execute(sqlBuy);
		pcGetStatement().execute(sqlPwd);
		pcGetStatement().execute(sqlWrongTimes);
	}

	// 搜索测试数据
	public static void searchData(int number) throws Exception {
		String sql = "select app_name as aa from (select APP_NAME from application where app_status='NORMAL' order by RECOMMENDED_ORDER asc , clicks desc,score desc) where rownum<="
				+ number;
		getAndWriteData(mswGetStatement(), sql, searchFileName);
	}

	// 查看单个应用展示页面数据
	public static void appViewData(int number) throws Exception {
		String sql = "select app_id as aa from (select app_id from application where app_status='NORMAL' order by RECOMMENDED_ORDER asc , clicks desc,score desc) where rownum<="
				+ number;
		getAndWriteData(mswGetStatement(), sql, viewAppFileName);
	}

	// 购买流程数据
	public static void buyAppData(int number) throws Exception {
		String sql = "select a.app_id||','||p.PAYTYPE as aa from application a ,app_price p where a.APP_ID=p.APP_ID and a.app_status='NORMAL' AND A.IS_FREE=0 AND P.PAYTYPE !='ONEOFF' and rownum <= "
				+ number;
		getAndWriteData(mswGetStatement(), sql, buyAppFileName);
		// 购买加钱
	}

	// 运行应用数据(运行免费应用)
	public static void runAppData(int number) throws Exception {
		String sql = "select app_id as aa from APPLICATION where app_status='NORMAL' and is_free=1 and rownum<="
				+ number;
		getAndWriteData(mswGetStatement(), sql, runAppFileName);
		// Statement stmt = mswGetStatement();
		// ResultSet rs = stmt.executeQuery(sql);
		// String s = "";
		// Statement stmt1 = mswGetStatement();
		// while (rs.next()) {
		// s += rs.getString("aa");
		// String sql1 =
		// "select app_id as aa from USER_APPLICATION where user_id="
		// + rs.getString("bb") + " and rownum=1";
		// ResultSet rs1 = stmt1.executeQuery(sql1);
		// rs1.next();
		// s += "," + rs1.getString("aa");
		// s += "\n";
		// rs1.close();
		// }
		// rs.close();
		// writeData(runAppFileName, s);
	}

	// 抽取从数据库取数据，写数据操作
	public static void getAndWriteData(Statement stmt, String sql,
			String fileName) throws Exception {
		ResultSet rs = stmt.executeQuery(sql);
		String s = getString(rs);
		closeResultSet(rs);
		writeData(fileName, s);
	}

	public static void getAndWriteProductData(Statement stmt, String sql,
			String fileName) throws Exception {
		ResultSet rs = stmt.executeQuery(sql);
		String s = getProductString(rs);
		closeResultSet(rs);
		writeData(fileName, s);
	}

	// 为购买用户加钱做准备
	public static String mswGetAndWriteData(Statement stmt, String sql,
			String fileName) throws Exception {
		ResultSet rs = stmt.executeQuery(sql);
		String[] sAll = getStringAndForBuy(rs);
		String s = sAll[0];
		closeResultSet(rs);
		writeData(fileName, s);
		return sAll[1];
	}

	// 执行sql得到的数据转化为字符串
	public static String getString(ResultSet rs) throws Exception {
		String s = "";
		while (rs.next()) {
			s += rs.getString("aa");
			s += "\n";
		}
		return s;
	}

	public static String getProductString(ResultSet rs) throws Exception {
		String s = "";
		while (rs.next()) {
			String ss = rs.getString("aa");
			s += URLEncoder.encode(ss, "utf-8");
			s += "\n";
		}
		return s;
	}

	// 执行sql得到的数据转化为字符串,为购买加钱做准备
	public static String[] getStringAndForBuy(ResultSet rs) throws Exception {
		String sBuy = "('";
		String s = "";
		int num = 0;
		while (rs.next()) {
			if (num != 0) {
				sBuy += "','";
			}
			num++;
			String ss = rs.getString("aa");
			sBuy += ss;
			s += ss;
			s += "\n";
		}
		sBuy += "')";
		String[] sAll = { s, sBuy };
		return sAll;
	}

	// 执行sql得到的数据转化为字符串后写入文件中
	public static void writeData(String fileName, String data) throws Exception {
		String filepath = "../testData/" + fileName;
		FileWriter fw = new FileWriter(filepath);
		fw.write(data);
		fw.close();
	}

	// esb
	public static void esbData(int number) throws Exception {
		String sql = "select AAC002,AAZ501 from AZ01 where rownum <="
				+ number;
		getAndWriteData(mswGetStatement(), sql, viewAppFileName);
	}

	
	// 执行以取得数据并写入数据
	public static void main(String[] args) throws Exception {
		// 得到build.xml传过来的数据
		// int number = Integer.valueOf(args[0]);
		int number = 1000;

		// 执行
		esbData(number);
		// LoginData(number);
		// searchData(number);
		// appViewData(number);
		// runAppData(number);
		// buyAppData(number);
//		productData(200);

		// 关闭数据库连接
		destroyConnection();
	}
}
