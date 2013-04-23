import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import edu.TestUserService;
import edu.dao.UserDao;
import edu.domain.Level;
import edu.mail.MailSender;
import edu.mail.MockMailSender;
import edu.service.UserService;
import edu.service.UserServiceImpl;
import edu.service.UserServiceTx;
import edu.vo.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext-hsqldb.xml")
public class UserServiceTest {

	@Autowired
	UserDao dao;

	@Autowired
	private DataSource dataSource;	
	
	@Autowired
	PlatformTransactionManager transactionManager;

	@Autowired
	MailSender mailSender;

	@Autowired
	UserService userService;

	private List<User> users;

	@Test
	public void addAndGet() throws SQLException {
		
		dao.deleteAll();
		
		User user = new User();
		user.setId("user01");
		user.setName("∞Ì¿Á«–");
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
		user.setName("»´±Ê");
		user.setLevel(Level.BASIC);
		user.setPassword("user02");
		dao.add(user);
		
		dao.add(user);
	}
	
	@Test
	public void sqlExceptionTranslate(){
		dao.deleteAll();
		
		User user = new User();
		user.setId("user02");
		user.setName("»´±Ê");
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
	
	@Before
	public void setUp() {
		users = new ArrayList<User>();
		users.add(new User("bumjin", "»´±Êµø", "pwd01", 49, 1, Level.valueOf(1),
				"abcd1@wemb.co.kr"));
		users.add(new User("joytouch", "¿Âπﬂ¿Â", "pwd02", 51, 1, Level.valueOf(1),
				"abcd2@wemb.co.kr"));
		users.add(new User("erwins", "√·«‚¿Ã", "pwd03", 3, 1, Level.valueOf(1),
				"abcd3@wemb.co.kr"));
		users.add(new User("madnite1", "¿ÃªÛ»£", "pwd04", 101, 2, Level.valueOf(2),
				"abcd4@wemb.co.kr"));
		users.add(new User("green", "ø¿πŒ±‘", "pwd05", 4, 1, Level.valueOf(1),
				"abcd4@wemb.co.kr"));
	}

	@Test
	public void upgradeAllOrNothing() throws Exception {
		TestUserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(dao);
		testUserService.setMailSender(mailSender);
		
		UserServiceTx txUserService = new UserServiceTx();
		txUserService.setTransactionManager(transactionManager);
		txUserService.setUserService(testUserService);
		
		dao.deleteAll();
		
		for(User user : users) dao.add(user);
		
		try{
			txUserService.upgradeLevels();
			fail("testUserService Exception expected");
		}catch(Exception e){
			
		}
	}

	@Test
	@DirtiesContext
	public void upgradeLevels() throws Exception {
		dao.deleteAll();
		
		for (User user : users)
			userService.add(user);

		MockMailSender mockMailSender = new MockMailSender();
		userService.setMailSender(mockMailSender);
		
		userService.upgradeLevels();
		
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
