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
	 * �޼ҵ� ������ ����
	 */
	@Override
	public void add(User user) {
		this.userService.add(user);
	}

	/**
	 * �޼ҵ� ����
	 */
	@Override
	public void upgradeLevels() {
		// �ΰ���� ����
		TransactionStatus status = this.transactionManager
				.getTransaction(new DefaultTransactionDefinition());
		try {
			
			// ����
			userService.upgradeLevels();
			
			// �ΰ���� ����
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
