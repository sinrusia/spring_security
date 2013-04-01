import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

	private List<User> users;

	@Before
	public void setUp() {
		users = new ArrayList<User>();
		users.add(new User("id01", "홍길동", "pwd01", 49, 1, Level.valueOf(1),
				"abcd1@wemb.co.kr"));
		users.add(new User("joytouch", "장발장", "pwd02", 51, 1, Level.valueOf(1),
				"abcd2@wemb.co.kr"));
		users.add(new User("id03", "춘향이", "pwd03", 3, 1, Level.valueOf(1),
				"abcd3@wemb.co.kr"));
		users.add(new User("madnite1", "김삿갓", "pwd04", 101, 2,
				Level.valueOf(2), "abcd4@wemb.co.kr"));
		users.add(new User("id05", "김삿갓", "pwd05", 4, 1, Level.valueOf(1),
				"abcd4@wemb.co.kr"));
	}

	@Test
	public void upgradeAllOrNothing() throws Exception {

		TestUserService testUserService = new TestUserService(users.get(3)
				.getId());
		testUserService.setUserDao(userDao);
		testUserService.setMailSender(mailSender);
		
		TxProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", TxProxyFactoryBean.class);
		
		txProxyFactoryBean.setTarget(testUserService);
		UserService txUserService = (UserService)txProxyFactoryBean.getObject();
		
		userDao.deleteAll();

		for (User user : users)
			userDao.add(user);

		try {
			txUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (Exception e) {

		}
		
		checkLevelUpgraded(users.get(1), false);
	}

	@Test
	public void mockUpgradeLevels() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();

		UserDao mockUserDao = mock(UserDao.class);
		when(mockUserDao.getAll()).thenReturn(this.users);
		userServiceImpl.setUserDao(mockUserDao);

		MailSender mockMailSender = mock(MailSender.class);
		userServiceImpl.setMailSender(mockMailSender);

		userServiceImpl.upgradeLevels();

		// verify(mockUserDao, times(2)).update(any(User.class));
		// verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao).update(users.get(1));
		assertThat(users.get(1).getLevel(), is(Level.SILVER));
		verify(mockUserDao).update(users.get(3));
		assertThat(users.get(3).getLevel(), is(Level.GOLD));

		ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor
				.forClass(SimpleMailMessage.class);
		verify(mockMailSender, times(2)).send(mailMessageArg.capture());
		List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
		assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
		assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));
	}

	@Test
	@DirtiesContext
	public void upgradeLevels() throws Exception {
		// 고립된 테스트에서는 테스트 대상 오브젝트를 직접 생성하면 된다.
		UserServiceImpl userServiceImpl = new UserServiceImpl();

		// 목 오브젝트로 만든 UserDao를 직접 DI해준다.
		MockUserDao mockUserDao = new MockUserDao(this.users);
		userServiceImpl.setUserDao(mockUserDao);

		// 메일 발송 여부 확인을 위해 목 오브젝트 DI
		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setMailSender(mockMailSender);

		userServiceImpl.upgradeLevels();

		// MockuserDao로부터 업데이트 결과를 가져온다.
		List<User> updated = mockUserDao.getUpdated();
		assertThat(updated.size(), is(2));
		checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
		checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);

		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(1).getEmail()));
		assertThat(request.get(1), is(users.get(3).getEmail()));
	}

	private void checkUserAndLevel(User updated, String expectedId,
			Level expectedLevel) {
		assertThat(updated.getId(), is(expectedId));
		assertThat(updated.getLevel(), is(expectedLevel));
	}

	private void checkLevelUpgraded(User user, boolean b) {
		User userUpdate = userDao.get(user.getId());

	}

}
