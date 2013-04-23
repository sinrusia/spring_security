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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import edu.ConnectionMaker;
import edu.domain.Level;
import edu.vo.User;

public class UserDaoJdbc implements UserDao {

	public UserDaoJdbc() {

	}

	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private JdbcContext jdbcContext;

	/**
	 * @return the jdbcContext
	 */
	public JdbcContext getJdbcContext() {
		return jdbcContext;
	}

	/**
	 * @param jdbcContext
	 *            the jdbcContext to set
	 */
	public void setJdbcContext(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}

	public void add(final User user) {
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con
						.prepareStatement("INSERT INTO users (id,name,password,level,login,recommend,email) VALUES (?,?,?,?,?,?,?)");
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
				String query = "UPDATE users SET name=?, password=?,level=?,login=?,recommend=?,email=? WHERE id=?";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1, user.getName());
				ps.setString(2, user.getPassword());
				ps.setInt(3, user.getLevel().intValue());
				ps.setInt(4, user.getLogin());
				ps.setInt(5, user.getRecommend());
				ps.setString(6, user.getEmail());
				ps.setString(7, user.getId());
				return ps;
			}
		});
	}

	public User get(String id) {
		return this.jdbcTemplate.queryForObject(
				"select * from users where id = ?", new Object[] { id },
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
				return con.prepareStatement("delete from users");
			}
		});
	}

	public int getCount() {
		return jdbcTemplate.query(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				return con.prepareStatement("select count(*) from users");
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

	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users order by id",
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
	public void upgradeLevel() {
		// TODO Auto-generated method stub
		
	}

}
