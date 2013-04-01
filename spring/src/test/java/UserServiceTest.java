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
import edu.dao.MockUserDao;
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
	UserService userService;

	private List<User> users;

	@Before
	public void setUp() {
		users = new ArrayList<User>();
		users.add(new User("id01", "ȫ�浿", "pwd01", 49, 1, Level.valueOf(1),
				"abcd1@wemb.co.kr"));
		users.add(new User("joytouch", "�����", "pwd02", 51, 1, Level.valueOf(1),
				"abcd2@wemb.co.kr"));
		users.add(new User("id03", "������", "pwd03", 3, 1, Level.valueOf(1),
				"abcd3@wemb.co.kr"));
		users.add(new User("madnite1", "���", "pwd04", 101, 2, Level.valueOf(2),
				"abcd4@wemb.co.kr"));
		users.add(new User("id05", "���", "pwd05", 4, 1, Level.valueOf(1),
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
		// ���� �׽�Ʈ������ �׽�Ʈ ��� ������Ʈ�� ���� �����ϸ� �ȴ�.
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		// �� ������Ʈ�� ���� UserDao�� ���� DI���ش�.
		MockUserDao mockUserDao = new MockUserDao(this.users);
		userServiceImpl.setUserDao(mockUserDao);
		
		// ���� �߼� ���� Ȯ���� ���� �� ������Ʈ DI
		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		
		// MockuserDao�κ��� ������Ʈ ����� �����´�.
		List<User> updated = mockUserDao.getUpdated();
		assertThat(updated.size(), is(2));
		checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
		checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);
		
		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(1).getEmail()));
		assertThat(request.get(1), is(users.get(3).getEmail()));
	}
	
	private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel){
		assertThat(updated.getId(), is(expectedId));
		assertThat(updated.getLevel(), is(expectedLevel));
	}

	private void checkLevelUpgraded(User user, boolean b) {
		User userUpdate = userDao.get(user.getId());
		
		
		
	}

}
