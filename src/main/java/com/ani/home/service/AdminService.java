package com.ani.home.service;

import java.util.Optional;

import com.ani.home.model.Admin;





public interface AdminService {
	Optional<Admin> authenticate(String email, String password);
 

}
