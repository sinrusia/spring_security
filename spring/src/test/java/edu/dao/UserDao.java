package edu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import edu.ConnectionMaker;
import eud.vo.User;

public class UserDao {

	private ConnectionMaker connectionMaker;
	
	private DataSource dataSource;
	
	public ConnectionMaker getConnectionMaker() {
		return connectionMaker;
	}

	public void setConnectionMaker(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
	}

	public UserDao(){
		
	}
	
	public UserDao(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
	}

	public void add(User user) {
		try {
			connectionMaker.getConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void deleteAll()throws SQLException {
		
		Connection c = null;
		PreparedStatement ps = null;
		try{
			c = dataSource.getConnection();
			ps = c.prepareStatement("delete from users");
			ps.execute();
		}catch(SQLException e){
			throw e;
		}finally{
			
		}
		
		
		ps.close();
		c.close();
	}

}
