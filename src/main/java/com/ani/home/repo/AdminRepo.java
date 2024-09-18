package com.ani.home.repo;

import java.util.Optional;

import com.ani.home.model.Admin;




public interface AdminRepo {
	public Optional<Admin> findByEmailAndPassword(String email, String password);
	public String save(Admin user);
	

}
