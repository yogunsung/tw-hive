/** 
 * Project Name:tw-hive 
 * File Name:Jdbc2hiveserver.java 
 * Package Name:cn.com.talkweb.hive.jdbc 
 * Date:2015年2月4日 下午9:00:43 
 * Copyright (c) 2015, TalkWeb All Rights Reserved. 
 * 
 */  
package cn.com.talkweb.hive.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** 
 * @author songyg 
 * @version  
 * @since JDK 1.6 
 */
public class JdbcHiveServer {
	private static String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";
	private static String url = "jdbc:hive://10.157.142.163:10000/default";
	
	public static void main(String[] args) {
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			Connection conn = DriverManager.getConnection("url");
			Statement stmt = conn.createStatement();
			
			String sql = "show tables";
		  ResultSet rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
