package edu;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import edu.mail.MailSender;
import edu.vo.User;

public class UserServiceTx implements UserService {

	UserService userService;

	/**
	 * @param userService
	 *            the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	PlatformTransactionManager transactionManager;

	public void setTransactionManager(
			PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	/**
	 * 메소드 구현과 위임
	 */
	@Override
	public void add(User user) {
		this.userService.add(user);
	}

	/**
	 * 메소드 구현
	 */
	@Override
	public void upgradeLevels() {
		// 부가기능 수행
		TransactionStatus status = this.transactionManager
				.getTransaction(new DefaultTransactionDefinition());
		try {
			
			// 위임
			userService.upgradeLevels();
			
			// 부가기능 수행
			this.transactionManager.commit(status);
		} catch (RuntimeException e) {
			this.transactionManager.rollback(status);
			throw e;
		}
	}

	@Override
	public void setMailSender(MailSender mockMailSender) {
		// TODO Auto-generated method stub

	}
}
