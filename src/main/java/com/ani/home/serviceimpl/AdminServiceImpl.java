package com.ani.home.serviceimpl;


import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ani.home.model.Admin;

import com.ani.home.repo.AdminRepo;

import com.ani.home.service.AdminService;


@Service
public class AdminServiceImpl implements AdminService{
	
	 private AdminRepo adminRepository;
	 

	    
	    public AdminServiceImpl(AdminRepo userRepository) {
	        this.adminRepository = userRepository;
	        
	    }
	 @Override
	    public Optional<Admin> authenticate(String email, String password) {
	        return adminRepository.findByEmailAndPassword(email, password);
	    }
	

	    

}
