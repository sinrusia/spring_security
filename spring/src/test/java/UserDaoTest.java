import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.CoreMatchers.*;

import edu.dao.UserDao;
import edu.vo.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext-hsqldb.xml")
public class UserDaoTest {

	@Autowired
	UserDao dao;
	
	@Before
	public void setUp(){
		
	}
	
	@Test
	public void addAndGet() throws SQLException {
		
		dao.deleteAll();
		
		User user = new User();
		user.setId("user01");
		user.setName("∞Ì¿Á«–");
		user.setPassword("user01");
		dao.add(user);
		
		User user2 = dao.get(user.getId());
		
		assertThat(user.getId(), is(user2.getId()));
		
		assertThat(dao.getCount(), is(1));
	}
	
	@After
	public void reset(){
		
	}
}
