package edu.vo;

import edu.domain.Level;

public class User {
	private String id;
	private String name;
	private String password;
	private int login;
	private int recommend;
	private Level level;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getLogin() {
		return login;
	}

	public void setLogin(int login) {
		this.login = login;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	/**
	 * @return the level
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(Level level) {
		this.level = level;
	}

	public void upgradeLevel() {
		Level nextLevel = this.getLevel().nextLevel();
		
		if(nextLevel == null){
			throw new IllegalStateException(this.getLevel() + "은 업그레이드가 불가능합니다.");
		}else{
			this.setLevel(nextLevel);
		}
	}

}
