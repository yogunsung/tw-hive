/** 
 * Project Name:tw-hive 
 * File Name:HiveJdbcUtils.java 
 * Package Name:cn.com.talkweb.hive.jdbc.util 
 * Date:2015年2月4日 下午9:30:11 
 * Copyright (c) 2015, TalkWeb All Rights Reserved. 
 * 
 */  
package cn.com.talkweb.hive.jdbc.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;


/** 
 * @author songyg 
 * @version  
 * @since JDK 1.6 
 */
public class HiveJdbcUtils {
	// 定义日志对象
	private static Logger log = Logger.getLogger(HiveJdbcUtils.class);

	private HiveJdbcUtils() {}

	// 定义hive连接信息
	private static String url;
	private static String username;
	private static String password;

	// url,user,pasword要写在配置文件中，并且只加载一次
	// 静态代码块不可以抛异常。
	static {
		try {
			// 读取配置文件jdbc.properties中获得user,password,url
			InputStream in = HiveJdbcUtils.class.getClassLoader()
					.getResourceAsStream("config.properties");
			Properties prop = new Properties();
			prop.load(in);
			// 从资源文件中读取配置信息
			url = prop.getProperty("hive.url");
			username = prop.getProperty("hive.username");
			password = prop.getProperty("hive.password");
			// 注册驱动
			String driverClass = prop.getProperty("hive.driver");
			Class.forName(driverClass);

		} catch (Exception e) {
			log.error(e.getMessage());
			// 抛出初始化异常。
			throw new ExceptionInInitializerError(e);
		}
	}

	// 获得数据库的连接
	public static Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(url, username, password);
		return conn;
	}

	// 释放资源的代码
	public static void release(Statement stmt, Connection conn, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				rs = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs = null;
		}
		if (stmt != null) {
			try {
				stmt.close();
				stmt = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmt = null;
		}
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}

	// 执行sql语句,使用时注意释放资源
	public static ResultSet excuteHql(Connection conn, Statement stmt,
			ResultSet rs, String hql) throws SQLException {
		try {
			log.info("执行hql语句：" + hql);
			rs = stmt.executeQuery(hql);
			log.info("hql语句：" + hql + "执行完毕！");
			return rs;
		} catch (SQLException e) {
			log.info(e.getMessage());
			rs = null;
			throw e;
		} finally {
			// 释放资源
			// release(stmt,conn,rs);
		}
	}

	/**
	 * 
	 * @param hql
	 * @return boolean
	 * @throws SQLException
	 */
	public static boolean excuteHql(String hql) throws SQLException {
		// 创建hive数据库操作基本对象
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// 获取hive数据库连接
			conn = getConnection();
			stmt = conn.createStatement();
			log.info("执行hql语句：" + hql);
			rs = stmt.executeQuery(hql);
			log.info("hql语句：" + hql + "执行完毕！");
			return true;
		} catch (SQLException e) {
			log.info(e.getMessage());
			throw e;
		} finally {
			// 释放资源
			release(stmt, conn, rs);
		}
	}
}
