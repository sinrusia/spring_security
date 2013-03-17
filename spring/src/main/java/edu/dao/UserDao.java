package edu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import edu.ConnectionMaker;
import edu.vo.User;

public class UserDao {

	public UserDao() {

	}

	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private JdbcContext jdbcContext;
	
	/**
	 * @return the jdbcContext
	 */
	public JdbcContext getJdbcContext() {
		return jdbcContext;
	}

	/**
	 * @param jdbcContext the jdbcContext to set
	 */
	public void setJdbcContext(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}


	public void add(final User user) throws SQLException {
		this.jdbcContext.jdbcContextWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makePreparedStatement(Connection c)
					throws SQLException {
				PreparedStatement ps = c.prepareStatement("insert into users(id,name,password) values(?,?,?)");
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				return ps;
			}
		});
	}
	
	public User get(String id) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
		ps.setString(1, id);
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		User user = new User();
		user.setId(rs.getString("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));
		
		rs.close();
		ps.close();
		c.close();
		
		return user;
	}

	public void deleteAll() throws SQLException {
		this.jdbcContext.jdbcContextWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makePreparedStatement(Connection c)
					throws SQLException {
				PreparedStatement ps = c.prepareStatement("delete from users");
				return ps;
			}
		});
	}
	
	public int getCount() throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement ps = c.prepareStatement("select count(*) from users");
		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		
		rs.close();
		ps.close();
		c.close();
		
		return count;
	}


}
