package com.ani.home.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ani.home.model.Admin;
import com.ani.home.model.StationAdmin;
import com.ani.home.repo.AdminRepo;
import com.ani.home.repo.StationAdminRepo;
import com.ani.home.service.AdminService;


@Service
public class AdminServiceImpl implements AdminService{
	
	 private AdminRepo adminRepository;
	 

	    @Autowired
	    public AdminServiceImpl(AdminRepo userRepository) {
	        this.adminRepository = userRepository;
	        
	    }
	 @Override
	    public Optional<Admin> authenticate(String email, String password) {
	        return adminRepository.findByEmailAndPassword(email, password);
	    }
	

	    

}
