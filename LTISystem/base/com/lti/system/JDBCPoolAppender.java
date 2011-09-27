package com.lti.system;

import java.sql.SQLException;

import org.apache.log4j.jdbc.JDBCAppender;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JDBCPoolAppender extends JDBCAppender{

	@Override
	protected java.sql.Connection getConnection() throws java.sql.SQLException{
		ComboPooledDataSource ds=(ComboPooledDataSource)ContextHolder.getInstance().getApplicationContext().getBean("dataSource");
		java.sql.Connection conn= ds.getConnection();
		return conn;
	}
	
	@Override
	protected void closeConnection(java.sql.Connection con){
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
