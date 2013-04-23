package edu.service;

import edu.mail.MailSender;
import edu.mail.MockMailSender;
import edu.vo.User;

public interface UserService {

	void add(User user);
	void upgradeLevels() throws Exception;
	void setMailSender(MailSender mockMailSender);
}
