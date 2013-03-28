package edu;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import edu.dao.UserDao;
import edu.vo.User;

public class UserService {

	private DataSource dataSource;
	
	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private UserDao userDao;

	public void upgradeLevels(){
		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
		
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try{
			List<User> users = userDao.getAll();
			
			for(User user : users){
				if(canUpgradeLevel(user)){
					upgradeLevel(user);
				}
			}
			transactionManager.commit(status);
		} catch (RuntimeException e) {
			transactionManager.rollback(status);
			throw e;
		}
	}

	private void upgradeLevel(User user) {
		// TODO Auto-generated method stub
		
	}

	private boolean canUpgradeLevel(User user) {
		// TODO Auto-generated method stub
		return false;
	}

}
