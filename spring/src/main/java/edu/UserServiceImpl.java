package edu;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import edu.dao.UserDao;
import edu.mail.MailSender;
import edu.vo.User;

public class UserServiceImpl implements UserService {

	private DataSource dataSource;

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	private MailSender mailSender;

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void upgradeLevels() {
		upgradeLevelsInternal();
	}

	private void upgradeLevelsInternal() {
		
		List<User> users = userDao.getAll();
		
		for (User user : users) {
			if (canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		}
	}

	private void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
		sendUpgradeEMail(user);
	}

	private void sendUpgradeEMail(User user) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("sinrusia@wemb.co.kr");
		mailMessage.setSubject("upgrade 안내");
		mailMessage.setText("사용자님의 등급이 " + user.getLevel().name()
				+ "로 업그레이드 되었습니다.");
		this.mailSender.send(mailMessage);
	}

	private boolean canUpgradeLevel(User user) {
		return user.canUpgradeLevel();
	}

	@Override
	public void add(User user) {
		// TODO Auto-generated method stub

	}

}
