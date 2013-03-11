package edu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

public class ConnectionMaker {

	public Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("org.hsqldb.jdbcDriver");
		Connection c = DriverManager.getConnection("jdbc:hsqldb:MyDB", "sa", "" );
		return c;
	}
}
