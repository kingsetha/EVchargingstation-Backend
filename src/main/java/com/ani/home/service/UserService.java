package com.ani.home.service;

import com.ani.home.model.User;

import java.util.*;

public interface UserService {

	public String addUser(User user);

	Optional<User> authenticate(String email, String password);
	List<User> getUsers(Map<String, String> params);

    public void deleteUser(int id);

//     public void updateUser(int id, User user);
     public Optional<User> getUserById(int id);
     public List<User> getAllUsers() ;
     String getPassword(String email);
     void sendPasswordToUser(String email);

	List<User> getUsersSortedByRegistrationDate();

	void updateUser(User user);

	List<String> getAllUserEmails();
}
