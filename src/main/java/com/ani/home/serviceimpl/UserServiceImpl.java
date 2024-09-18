package com.ani.home.serviceimpl;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ani.home.model.User;
import com.ani.home.repo.UserRepo;
import com.ani.home.service.UserService;

import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.data.domain.Sort;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepository;
    @Autowired
    private JavaMailSender mailSender;

   
    public UserServiceImpl(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String addUser(User user) {
        userRepository.save(user);
        return "User added successfully";
    }
    @Override
    public String getPassword(String email) {
        return userRepository.getPasswordByEmail(email);
    }

    @Override
    public void sendPasswordToUser(String email) {
        String password = getPassword(email);
        if (password == null) {
            throw new RuntimeException("User not found");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Password");
        message.setText("Your password is: " + password);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public Optional<User> authenticate(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
   
    public List<User> getUsers(Map<String, String> params) {
        
        String useridStr = params.get("userid");
        String firstname = params.get("firstname");

        Integer userid = null;
        if (useridStr != null && !useridStr.trim().isEmpty()) {
            try {
                userid = Integer.parseInt(useridStr);
            } catch (NumberFormatException e) {
                
                System.err.println("Invalid userid value: " + useridStr);
            }
        }

        Integer age = null;
        String ageStr = params.get("age");
        if (ageStr != null && !ageStr.trim().isEmpty()) {
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
               
                System.err.println("Invalid age value: " + ageStr);
            }
        }

        String gender = params.get("gender");
        String religion = params.get("religion");
        String maritalstatus = params.get("maritalstatus");
        String occupation = params.get("occupation");

        
        return userRepository.findByfirstnameContainingAndAgeAndReligion(
                userid, firstname, age, gender, religion, maritalstatus, occupation);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    
    @Override
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }
    @Override
	public List<User> getAllUsers() {

		return userRepository.findAll();
	}
    @Override
    public List<User> getUsersSortedByRegistrationDate() {
        return userRepository.findAllSort(Sort.by(Sort.Order.desc("registrationDate")));
    }
    @Override
    public void updateUser(User user) {
        
        User existingUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + user.getId()));

        
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setAddress(user.getAddress());
        existingUser.setVehicleType(user.getVehicleType());
        existingUser.setVehicleRegistrationNumber(user.getVehicleRegistrationNumber());
        existingUser.setVehicleMake(user.getVehicleMake());
        existingUser.setVehicleModel(user.getVehicleModel());
        existingUser.setRole(user.getRole());

       
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(user.getPassword()); 
        }

       
        userRepository.save(existingUser);
    }
    @Override
    public List<String> getAllUserEmails() {
        return userRepository.findAll()
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

    public String getUserEmailByBookingId(String bookingId) {
       
        User user = userRepository.findByBookingId(bookingId);
        if (user != null) {
            return user.getEmail();
        } else {
            return null;
        }
    }


    
   
   

}
