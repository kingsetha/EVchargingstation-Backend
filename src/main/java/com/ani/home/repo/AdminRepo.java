package com.ani.home.repo;

import java.util.List;
import java.util.Optional;

import com.ani.home.model.Admin;
import com.ani.home.model.StationAdmin;



public interface AdminRepo {
	public Optional<Admin> findByEmailAndPassword(String email, String password);
	public String save(Admin user);
	

}
