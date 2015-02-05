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

import cn.com.talkweb.hive.jdbc.util.HiveJdbcUtils;

/** 
 * @author songyg 
 * @version  
 * @since JDK 1.6 
 */
public class JdbcHiveServer {
	
	public static void main(String[] args) throws SQLException {
		String hql = "show tables";
		Connection conn = HiveJdbcUtils.getConnection();
		Statement stmt = conn.createStatement();
	  ResultSet rs = HiveJdbcUtils.excuteHql(conn, stmt, hql);
	  while(rs.next()){
		  System.out.println(rs.getString(1));
	  }
	}
}
