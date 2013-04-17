import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import sun.security.action.GetLongAction;

import edu.TestUserService;
import edu.UserService;
import edu.UserServiceImpl;
import edu.UserServiceTx;
import edu.dao.MockUserDao;
import edu.dao.UserDao;
import edu.domain.Level;
import edu.handler.TransactionHandler;
import edu.mail.MailSender;
import edu.mail.MockMailSender;
import edu.service.TxProxyFactoryBean;
import edu.vo.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class UserServiceTest {
	
	@Autowired
	ApplicationContext context;

	@Autowired
	UserDao userDao;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Autowired
	MailSender mailSender;

	@Autowired
	UserService userService;
	
	@Autowired
	UserService testUserService;

	private List<User> users;

	@Before
	public void setUp() {
		users = new ArrayList<User>();
		users.add(new User("id01", "»´±Êµø", "pwd01", 49, 1, Level.valueOf(1),
				"abcd1@wemb.co.kr"));
		users.add(new User("joytouch", "¿Âπﬂ¿Â", "pwd02", 51, 1, Level.valueOf(1),
				"abcd2@wemb.co.kr"));
		users.add(new User("id03", "√·«‚¿Ã", "pwd03", 3, 1, Level.valueOf(1),
				"abcd3@wemb.co.kr"));
		users.add(new User("madnite1", "±ËªÒ∞´", "pwd04", 101, 2,
				Level.valueOf(2), "abcd4@wemb.co.kr"));
		users.add(new User("id05", "±ËªÒ∞´", "pwd05", 4, 1, Level.valueOf(1),
				"abcd4@wemb.co.kr"));
	}
	
	@Test
	public void addAndGet() throws SQLException {
		
		userService.deleteAll();
		
		User user = new User();
		user.setId("user01");
		user.setName("∞Ì¿Á«–");
		user.setPassword("user01");
		user.setLevel(Level.BASIC);
		userService.add(user);
		
		User user2 = userService.get(user.getId());
		
		assertThat(user.getId(), is(user2.getId()));
		
		assertThat(userService.getCount(), is(1));
	}

	@Test(expected=DataAccessException.class)
	public void upgradeAllOrNothing() throws Exception {
		
		userDao.deleteAll();

		for (User user : users)
			userDao.add(user);

		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (Exception e) {

		}
		
		checkLevelUpgraded(users.get(1), false);
	}


	@Test
	@DirtiesContext
	public void upgradeLevels() throws Exception {
		userService.upgradeLevels();
	}

	private void checkUserAndLevel(User updated, String expectedId,
			Level expectedLevel) {
		assertThat(updated.getId(), is(expectedId));
		assertThat(updated.getLevel(), is(expectedLevel));
	}

	private void checkLevelUpgraded(User user, boolean b) {
		User userUpdate = userDao.get(user.getId());

	}
	
	@Test
	public void advisorAutoProxyCreator(){
		assertThat(testUserService, is(java.lang.reflect.Proxy.class));
	}

}
