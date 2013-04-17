import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import edu.dao.UserDao;
import edu.domain.Level;
import edu.vo.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext-hsqldb.xml")
public class UserDaoTest {

	@Autowired
	private UserDao dao;
	
	@Autowired
	private DataSource dataSource;
	
	@Before
	public void setUp(){
		
	}
	
	@Test
	public void addAndGet() throws SQLException {
		
		dao.deleteAll();
		
		User user = new User();
		user.setId("user01");
		user.setName("°íÀçÇÐ");
		user.setPassword("user01");
		user.setLevel(Level.BASIC);
		dao.add(user);
		
		User user2 = dao.get(user.getId());
		
		assertThat(user.getId(), is(user2.getId()));
		
		assertThat(dao.getCount(), is(1));
	}
	
	@Test(expected=DataAccessException.class)
	public void duplicateKey(){
		dao.deleteAll();
		
		User user = new User();
		user.setId("user02");
		user.setName("È«±æ");
		user.setPassword("user02");
		dao.add(user);
		
		dao.add(user);
	}
	
	@Test
	public void sqlExceptionTranslate(){
		dao.deleteAll();
		
		User user = new User();
		user.setId("user02");
		user.setName("È«±æ");
		user.setPassword("user02");
		
		try {
			dao.add(user);
			dao.add(user);
		} catch (DuplicateKeyException e) {
			SQLException sqlEx = (SQLException)e.getRootCause();
			SQLExceptionTranslator set = 
					new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
			assertThat(set.translate(null, null, sqlEx), is(DuplicateKeyException.class));
		}
	}
	
	@After
	public void reset(){
		
	}
}
