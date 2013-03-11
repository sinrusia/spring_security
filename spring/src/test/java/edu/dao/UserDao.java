package edu.dao;

import java.sql.SQLException;

import edu.ConnectionMaker;
import eud.vo.User;

public class UserDao {

	private ConnectionMaker connectionMaker;
	
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

}
