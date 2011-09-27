package com.lti.rmi;

public interface UserService {
	public int newUser(UserBean userBean);
	
	public void deleteUser(int userId);
	
	public void updateUser(UserBean userBean);
}
