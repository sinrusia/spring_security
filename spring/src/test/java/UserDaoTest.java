import java.sql.SQLException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import edu.dao.UserDao;
import eud.vo.User;


public class UserDaoTest {

	
	@Test
	public void addAndGet() throws SQLException {
		
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDao dao = (UserDao) context.getBean("userDao");
		
		User user = new User();
		
		dao.add(user);
		
		
	}
}
