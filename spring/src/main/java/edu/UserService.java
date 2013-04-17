package edu;

import edu.mail.MailSender;
import edu.mail.MockMailSender;
import edu.vo.User;

public interface UserService {

	void deleteAll();
	User get(String id);
	void add(User user);
	void upgradeLevels();
	void setMailSender(MailSender mockMailSender);
	int getCount();
}
