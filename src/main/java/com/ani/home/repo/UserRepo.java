package com.ani.home.repo;

import java.util.*;

import org.springframework.data.domain.Sort;

import com.ani.home.model.User;





public interface UserRepo {

	public String save(User user);

	public Optional<User> findByEmailAndPassword(String email, String password);
//	public  List<User> findByfirstnameContainingAndAgeAndReligion(String firstname, Integer age, String religion);
	public void deleteById(int id);
	public Optional<User> findById(int id);
	public List<User> findAll();
	public List<User> findByfirstnameContainingAndAgeAndReligion(Integer userid,String firstname, Integer age, String gender, String religion, String maritalstatus, String occupation);
	 public List<User> findByBlocked(Boolean blocked);
	 public String getPasswordByEmail(String email);
	 Optional<User> findByEmail(String email);

	public List<User> findAllSort(Sort by);

	public User findByBookingId(String bookingId);

	String getUserEmailByBookingId(String bookingId);

	
}
