package com.ylzinfo.test.performance.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class DBFunctionalTestCase extends DataInit {
	private static final String ProductJDBC_URL = "jdbc:oracle:thin:@10.102.1.54:1521:mswappdb";
	private static final String JDBC_URL = "jdbc:oracle:thin:@10.10.30.165:1521:ORCL";
	private static final String DRIVER = "oracle.jdbc.OracleDriver";
	protected static Connection conn = null;
	protected static Connection productConn = null;
	private static Logger LOGGER = Logger.getLogger(DBFunctionalTestCase.class);
	private static List<Connection> conns = new ArrayList<Connection>();
	private static List<Statement> stmts = new ArrayList<Statement>();

	// @Before
	public static void initConnection(String username, String pwd)
			throws Exception {
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(JDBC_URL, username, pwd);
			conns.add(conn);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e, e);
			throw e;
		}
	}
	
	public static void productInitConnection(String username, String pwd)
			throws Exception {
		try {
			Class.forName(DRIVER);
			productConn = DriverManager.getConnection(ProductJDBC_URL, username, pwd);
			conns.add(conn);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e, e);
			throw e;
		}
	}

	// @After
	public static void destroyConnection() {
		for (Statement s : stmts) {
			if (s != null) {
				try {
					s.close();
				} catch (SQLException e) {
					s = null;
					LOGGER.error(e, e);
				}
			}
		}
		stmts.clear();
		for (Connection ss : conns) {
			if (ss != null) {
				try {
					ss.close();
				} catch (SQLException e) {
					ss = null;
					LOGGER.error(e, e);
				}
			}
		}
		conns.clear();
	}

	protected static Statement casGetStatement() throws Exception {
		initConnection(casUsername, casPassword);
		Statement stmt = conn.createStatement();
		stmts.add(stmt);
		return stmt;
	}
	
	protected static Statement mswGetStatement() throws Exception {
		initConnection("sicard", "sicard");
		Statement stmt = conn.createStatement();
		stmts.add(stmt);
		return stmt;
	}
	
	protected static Statement pcGetStatement() throws Exception {
		initConnection(pcUsername, pcPassword);
		Statement stmt = conn.createStatement();
		stmts.add(stmt);
		return stmt;
	}
	
	protected static Statement productGetStatement() throws Exception {
		productInitConnection("mswapp", "mswapp");
		Statement stmt = productConn.createStatement();
		stmts.add(stmt);
		return stmt;
	}

	protected static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				rs = null;
				LOGGER.error(e, e);
			}
		}
	}
}
