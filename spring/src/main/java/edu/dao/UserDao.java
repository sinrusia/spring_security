package edu.dao;

import java.util.List;

import edu.vo.User;

public interface UserDao {

	void add(User user);
	User get(String id);
	List<User> getAll();
	int getCount();
	void deleteAll();
	
	void upgradeLevel();
	void update(User user);
}
