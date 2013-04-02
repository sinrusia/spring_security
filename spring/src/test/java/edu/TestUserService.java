package edu;

import edu.vo.User;

public class TestUserService extends UserServiceImpl {

	public TestUserService(String id) {
		this.id = id;
	}

	private String id = "";
	
	protected void upgradeLevel(User user){
		if(user.getId().equals(this.id))throw new TestUserServiceException();
		super.upgradeLevel(user);
	}
	
}
