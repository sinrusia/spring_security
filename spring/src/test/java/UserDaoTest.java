import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
		
		
	}
	
	@After
	public void reset(){
		
	}
}
