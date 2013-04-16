package edu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import edu.ConnectionMaker;
import edu.domain.Level;
import edu.user.SqlService;
import edu.vo.User;

public class UserDaoJdbc implements UserDao {

	public UserDaoJdbc() {

	}
	
	private SqlService sqlService;
	
	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}
	
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void add(final User user) {
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sqlService.getSql("userAdd"));
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				ps.setInt(4, user.getLevel().intValue());
				ps.setInt(5, user.getLogin());
				ps.setInt(6, user.getRecommend());
				ps.setString(7, user.getEmail());
				return ps;
			}
		});
	}
	
	@Override
	public void update(final User user) {
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				String query = sqlService.getSql("userUpdate");
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1, user.getName());
				ps.setString(2,	user.getPassword());
				ps.setInt(3, user.getLevel().intValue());
				ps.setInt(4, user.getLogin());
				ps.setInt(5, user.getRecommend());
				ps.setString(6, user.getEmail());
				ps.setString(7, user.getId());
				return ps;
			}
		});
	}

	
	public User get(String id){
		return this.jdbcTemplate.queryForObject(sqlService.getSql("userGet"),
				new Object[] {id},
			new RowMapper<User>() {
				@Override
				public User mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					User user = new User();
					user.setId(rs.getString("id"));
					user.setName(rs.getString("name"));
					user.setPassword(rs.getString("password"));
					return user;
				}
			});
	}

	public void deleteAll() {
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				return con.prepareStatement(sqlService.getSql("userDeleteAll"));
			}
		});
	}
	
	public int getCount()  {
		return jdbcTemplate.query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				return con.prepareStatement(sqlService.getSql("userGetCount"));
			}
			
		}, new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				rs.next();
				return rs.getInt(1);
			}
		});
	}
	
	public List<User> getAll(){
		return this.jdbcTemplate.query(sqlService.getSql("userGetAll"),
				new RowMapper<User>() {
					@Override
					public User mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						User user = new User();
						user.setId(rs.getString("id"));
						user.setName(rs.getString("name"));
						user.setPassword(rs.getString("password"));
						user.setLevel(Level.valueOf(rs.getInt("level")));
						user.setLogin(rs.getInt("login"));
						user.setRecommend(rs.getInt("recommend"));
						user.setEmail(rs.getString("email"));
						return user;
					}
				});
			
	}

	@Override
	public void upgradeLevels() {
		// TODO Auto-generated method stub
		
	}

}
