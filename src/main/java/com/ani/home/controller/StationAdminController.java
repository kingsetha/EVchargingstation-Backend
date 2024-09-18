package com.ani.home.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ani.home.dto.StationAdminDTO;
import com.ani.home.model.Slot;
import com.ani.home.model.StationAdmin;
import com.ani.home.service.EmailService;
import com.ani.home.service.SlotService;
import com.ani.home.serviceimpl.StationAdminServiceImpl;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/StationAdmin")
@CrossOrigin("*")
public class StationAdminController {
	
	
    public StationAdminController(StationAdminServiceImpl stationAdminServiceImpl, SlotService slotService,
			EmailService emailService) {
		super();
		this.stationAdminServiceImpl = stationAdminServiceImpl;
		this.slotService = slotService;
		this.emailService = emailService;
	}



	StationAdminServiceImpl stationAdminServiceImpl;
	
	
	SlotService slotService;
	
	static final String SUCCESS = "Success";
    static final String FAILURE = "Failure";
    @PostMapping("/register")
    public String addUser(@RequestBody StationAdmin stationAdmin) {
        try {
            stationAdminServiceImpl.addUser(stationAdmin);
            return "Registration request sent to admin. You will be notified once approved.Once approved You can able to login.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failure";
        }
    }
    

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updateStationAdmin(
            @PathVariable("id") int id,
            @RequestBody StationAdmin stationAdmin) {
        try {
            StationAdmin updatedAdmin = stationAdminServiceImpl.updateStationAdmin(id, stationAdmin);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Station Admin updated successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Station Admin not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to update Station Admin");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    
    @GetMapping("/nearby")
    public ResponseEntity<List<StationAdminDTO>> getNearbyStations(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude)
          {
        try {
            List<StationAdminDTO> stations = stationAdminServiceImpl.findNearbyStations(latitude, longitude);
            return ResponseEntity.ok(stations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStationById(@PathVariable("id") int id) {
        try {
            Optional<StationAdmin> stationAdmin = stationAdminServiceImpl.findById(id);
            if (stationAdmin.isPresent()) {
                System.out.println("Fetched StationAdmin: " + stationAdmin.get());
                return ResponseEntity.ok(stationAdmin.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Station not found");
            }
        } catch (Exception e) {
            System.err.println("Error fetching station details: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch station details");
        }
    }
    @GetMapping("/api/{id}")
    public ResponseEntity<?> getStationByIdNew(@PathVariable("id") int id) {
        try {
            Optional<StationAdmin> stationAdmin = stationAdminServiceImpl.findById(id);
            if (stationAdmin.isPresent()) {
                StationAdmin admin = stationAdmin.get();
                List<Slot> slots = slotService.getSlotsByStationIdNew(id); // Ensure this method returns correct slots
                Map<String, Object> response = new HashMap<>();
                response.put("station", admin);
                response.put("slots", slots);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Station not found");
            }
        } catch (Exception e) {
            System.err.println("Error fetching station details: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch station details");
        }
    }
    

    @GetMapping("/getProfileData/{id}")
    public Optional<StationAdmin> getUserById(@PathVariable int id) {
        return stationAdminServiceImpl.getUserById(id);
    }
    
    
    
    private EmailService emailService;
    @PostMapping("/sendEmail")
    public ResponseEntity<String> sendEmail(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body,
            @RequestParam(required = false) MultipartFile attachment
    ) {
        try {

            emailService.sendEmail(to, subject, body, attachment);
            return ResponseEntity.ok("Email sent successfully");
        } catch (MessagingException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
        }
    }
    
    @PostMapping("/sendEmailToAll")
    public ResponseEntity<String> sendEmail(
            @RequestParam List<String> to, 
            @RequestParam String subject,
            @RequestParam String body,
            @RequestParam(required = false) MultipartFile attachment
    ) {
        try {
            emailService.sendEmailToMultiple(to, subject, body, attachment); 
            return ResponseEntity.ok("Email sent successfully to all recipients");
        } catch (MessagingException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
        }
    }

    @GetMapping("/getAllStationAdminEmails")
    public ResponseEntity<List<String>> getAllUserEmails() {
        try {
            List<String> emails = stationAdminServiceImpl.getAllAdminEmails();
            return ResponseEntity.ok(emails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}


