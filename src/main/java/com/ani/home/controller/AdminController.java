package com.ani.home.controller;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import jakarta.mail.MessagingException;
import java.util.*;
import com.ani.home.model.StationAdmin;
import com.ani.home.model.StationAdmin.Status;
import com.ani.home.service.EmailService;
import com.ani.home.service.StationAdminService;


import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/superadmin")
public class AdminController {
	
	 private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private StationAdminService stationAdminService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/pending")
    public ResponseEntity<List<StationAdmin>> getPendingAdmins() {
        List<StationAdmin> pendingAdmins = stationAdminService.getPendingAdmins();
        return ResponseEntity.ok(pendingAdmins);
    }
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Integer>> getAdminStatistics() {
        int pendingCount = stationAdminService.getCountByStatus(Status.PENDING);
        int approvedCount = stationAdminService.getCountByStatus(Status.APPROVED);
        int rejectedCount = stationAdminService.getCountByStatus(Status.REJECTED);

        Map<String, Integer> stats = Map.of(
            "pending", pendingCount,
            "approved", approvedCount,
            "rejected", rejectedCount
        );

        return ResponseEntity.ok(stats);
    }
    @PostMapping("/approve/{id}")
    public ResponseEntity<String> approveAdmin(@PathVariable int id) throws MessagingException {
        try {
            StationAdmin admin = stationAdminService.getAdminById(id);
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
            }

            if (admin.getStatus() != Status.PENDING) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin request has already been processed");
            }

            stationAdminService.approveAdmin(id);
            sendApprovalEmail(admin);
            return ResponseEntity.ok("Admin approved successfully");
        } catch (Exception e) {
            // Log the exception for debugging purposes
            logger.error("Failed to approve admin with ID " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to approve admin: " + e.getMessage());
        }
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<String> rejectAdmin(@PathVariable int id) throws MessagingException {
        try {
            StationAdmin admin = stationAdminService.getAdminById(id);
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
            }

            if (admin.getStatus() != Status.PENDING) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin request has already been processed");
            }

            stationAdminService.rejectAdmin(id);
            sendRejectionEmail(admin);
            return ResponseEntity.ok("Admin rejected successfully");
        } catch (Exception e) {
            // Log the exception for debugging purposes
            logger.error("Failed to reject admin with ID " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to reject admin: " + e.getMessage());
        }
    }


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

    private void sendApprovalEmail(StationAdmin admin) {
        String subject = "Your Admin Request has been Approved";
        String body = "Dear " + admin.getName() + ",\n\nYour request to be a station admin has been approved.";
        try {
            emailService.sendEmail(admin.getEmail(), subject, body, null);
        } catch (MessagingException | IOException e) {
            System.err.println("Failed to send approval email: " + e.getMessage());
        }
    }

    private void sendRejectionEmail(StationAdmin admin) {
        String subject = "Your Admin Request has been Rejected";
        String body = "Dear " + admin.getName() + ",\n\nWe regret to inform you that your request to be a station admin has been rejected.";
        try {
            emailService.sendEmail(admin.getEmail(), subject, body, null);
        } catch (MessagingException | IOException e) {
            System.err.println("Failed to send rejection email: " + e.getMessage());
        }
    }
    @GetMapping("/allStation")
   	public List<StationAdmin> getAllStations()
   	{
   		return stationAdminService.getAllStations();
   	}
    @GetMapping("/blocked")
    public List<StationAdmin> findBlockedUsers(@RequestParam boolean blocked) {
        return stationAdminService.findBlockedUsers(blocked);
    }

//    @PostMapping("/block/{id}")
//    public ResponseEntity<String> blockUser(@PathVariable int stationId) {
//        String response = stationAdminService.blockUser(stationId);
//        if (response.equals("Station blocked successfully")) {
//            return ResponseEntity.ok(response);
//        } else {
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//    @PostMapping("/unblock/{id}")
//    public ResponseEntity<String> unblockUser(@PathVariable int stationId) {
//        String response = stationAdminService.unblockUser(stationId);
//        if (response.equals("Station unblocked successfully")) {
//            return ResponseEntity.ok(response);
//        } else {
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
    @PostMapping("/block/{id}")
    public ResponseEntity<String> blockUser(@PathVariable int id) {
        try {
            String response = stationAdminService.blockUser(id);
            if ("Station blocked successfully".equals(response)) {
                return ResponseEntity.ok(response); // Return 200 OK for success
            } else {
                return ResponseEntity.badRequest().body(response); // Return 400 Bad Request for failure
            }
        } catch (Exception e) {
            logger.error("Failed to block station with ID " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to block station: " + e.getMessage());
        }
    }

    @PostMapping("/unblock/{id}")
    public ResponseEntity<String> unblockUser(@PathVariable int id) {
        try {
            String response = stationAdminService.unblockUser(id);
            if ("Station unblocked successfully".equals(response)) {
                return ResponseEntity.ok(response); // Return 200 OK for success
            } else {
                return ResponseEntity.badRequest().body(response); // Return 400 Bad Request for failure
            }
        } catch (Exception e) {
            logger.error("Failed to unblock station with ID " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to unblock station: " + e.getMessage());
        }
    }
    @DeleteMapping("/deleteStation/{id}")
    public ResponseEntity<String> deleteStation(@PathVariable int id) {
        try {
            // Call service method to perform the deletion
            boolean isDeleted = stationAdminService.deleteStation(id);

            if (isDeleted) {
                return ResponseEntity.ok("Station deleted successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to delete station: Station not found");
            }
        } catch (Exception e) {
            logger.error("Failed to delete station with ID " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete station: " + e.getMessage());
        }
    }



}
