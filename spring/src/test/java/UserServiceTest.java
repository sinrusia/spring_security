import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import edu.TestUserService;
import edu.UserService;
import edu.dao.UserDao;


public class UserServiceTest {

	@Autowired
	UserDao userDao;
	
	@Autowired
	PlatformTransactionManager transactionManager;
	
	@Test
	public void upgradeAllOrNothing() throws Exception {
		UserService testUserService = new  TestUserService();
		testUserService.setUserDao(userDao);
		testUserService.setTransactionManager(transactionManager);
	}

}
