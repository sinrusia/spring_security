package edu;

import edu.mail.MailSender;
import edu.mail.MockMailSender;
import edu.vo.User;

public interface UserService {

	void add(User user);
	void upgradeLevels();
	void setMailSender(MailSender mockMailSender);
}
