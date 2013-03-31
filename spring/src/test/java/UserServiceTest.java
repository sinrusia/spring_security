import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import edu.TestUserService;
import edu.UserService;
import edu.UserServiceImpl;
import edu.UserServiceTx;
import edu.dao.UserDao;
import edu.domain.Level;
import edu.mail.MailSender;
import edu.mail.MockMailSender;
import edu.vo.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext-hsqldb.xml")
public class UserServiceTest {

	@Autowired
	UserDao userDao;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Autowired
	MailSender mailSender;

	@Autowired
	UserService userServiceImpl;

	private List<User> users;

	@Before
	public void setUp() {
		users = new ArrayList<User>();
		users.add(new User("id01", "È«±æµ¿", "pwd01", 49, 1, Level.valueOf(1),
				"abcd1@wemb.co.kr"));
		users.add(new User("id02", "Àå¹ßÀå", "pwd02", 51, 1, Level.valueOf(1),
				"abcd2@wemb.co.kr"));
		users.add(new User("id03", "ÃáÇâÀÌ", "pwd03", 3, 1, Level.valueOf(1),
				"abcd3@wemb.co.kr"));
		users.add(new User("id04", "±è»ñ°«", "pwd04", 101, 2, Level.valueOf(2),
				"abcd4@wemb.co.kr"));
		users.add(new User("id05", "±è»ñ°«", "pwd05", 4, 1, Level.valueOf(1),
				"abcd4@wemb.co.kr"));
	}

	@Test
	public void upgradeAllOrNothing() throws Exception {
		TestUserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(userDao);
		testUserService.setMailSender(mailSender);
		
		UserServiceTx txUserService = new UserServiceTx();
		txUserService.setTransactionManager(transactionManager);
		txUserService.setUserService(testUserService);
		
		userDao.deleteAll();
		
		for(User user : users) userDao.add(user);
		
		try{
			txUserService.upgradeLevels();
			fail("testUserService Exception expected");
		}catch(Exception e){
			
		}
	}

	@Test
	@DirtiesContext
	public void upgradeLevels() throws Exception {
		userDao.deleteAll();
		for (User user : users)
			userDao.add(user);

		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		
		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
		
		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(1).getEmail()));
		assertThat(request.get(1), is(users.get(3).getEmail()));
	}

	private void checkLevelUpgraded(User user, boolean b) {
		// TODO Auto-generated method stub
		
	}

}
