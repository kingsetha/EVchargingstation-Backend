package com.ani.home.service;

import java.util.List;
import java.util.Optional;

import com.ani.home.model.Admin;
import com.ani.home.model.StationAdmin;




public interface AdminService {
	Optional<Admin> authenticate(String email, String password);
 

}
