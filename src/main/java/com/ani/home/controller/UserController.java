package com.ani.home.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ani.home.dto.PaymentRequestDTO;
import com.ani.home.model.Admin;
import com.ani.home.model.AdminResponse;
import com.ani.home.model.LoginResponse;
import com.ani.home.model.StationAdmin;
import com.ani.home.model.StationAdminResponse;
import com.ani.home.model.User;
import com.ani.home.service.EmailService;
import com.ani.home.serviceimpl.AdminServiceImpl;
import com.ani.home.serviceimpl.PaymentServiceImpl;
import com.ani.home.serviceimpl.StationAdminServiceImpl;
import com.ani.home.serviceimpl.UserServiceImpl;


import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;


import org.springframework.web.bind.annotation.DeleteMapping;


@RestController
@RequestMapping("/User")
@CrossOrigin("*")
public class UserController {
    @Autowired
    UserServiceImpl service;
    @Autowired
    AdminServiceImpl Aservice;
    @Autowired
    StationAdminServiceImpl stationAdminServiceImpl;
    @Autowired
    PaymentServiceImpl paymentServiceImpl;
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    static final String SUCCESS = "Success";
    static final String FAILURE = "Failure";

    @PostMapping("/register")
    public String addUser(@RequestBody User user) {
        try {
            service.addUser(user);
            return "Registered Successfully";
        } catch (Exception e) {
            return "Failure";
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            // Authenticate user
            Optional<User> authenticatedUser = service.authenticate(user.getEmail(), user.getPassword());
            if (authenticatedUser.isPresent()) {
                User userDetails = authenticatedUser.get();
                String role = userDetails.getRole();
                int id = userDetails.getId();
                return ResponseEntity.ok(new LoginResponse(id, "LoginSuccess", role));
            }

            // Authenticate admin
            Optional<Admin> authenticatedAdmin = Aservice.authenticate(user.getEmail(), user.getPassword());
            if (authenticatedAdmin.isPresent()) {
                Admin adminDetails = authenticatedAdmin.get();
                String role = adminDetails.getRole();
                int id = adminDetails.getId();
                return ResponseEntity.ok(new AdminResponse(id, "LoginSuccess", role));
            }

            // Authenticate station admin
            Optional<StationAdmin> authenticatedStationAdmin = stationAdminServiceImpl.authenticate(user.getEmail(), user.getPassword());
            if (authenticatedStationAdmin.isPresent()) {
                StationAdmin stationAdminDetails = authenticatedStationAdmin.get();
                if (stationAdminDetails.getStatus() == StationAdmin.Status.APPROVED) {
                    String role = stationAdminDetails.getRole();
                    int id = stationAdminDetails.getId();
                    return ResponseEntity.ok(new StationAdminResponse(id, "LoginSuccess", role));
                } else {
                    // Return a specific message for pending approval
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new ErrorResponse("Your account is pending approval. Please contact support."));
                }
            }

            // If no user, admin, or station admin found
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Incorrect credentials. Please try again."));
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server error. Please try again later."));
        }
    }

    // Define ErrorResponse class if not already defined
    public class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        // Getter and Setter
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
    @GetMapping("/getAllUserEmails")
    public ResponseEntity<List<String>> getAllUserEmails() {
        try {
            List<String> emails = service.getAllUserEmails();
            return ResponseEntity.ok(emails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/all")
    public List<User> getUsers() {
        return service.getUsersSortedByRegistrationDate();
    }
//
//    @GetMapping("/all")
//	public List<User> getUsers()
//	{
//		return service.getAllUsers();
//	}

    @GetMapping
    public List<User> getUsers(@RequestParam Map<String, String> params) {
        return service.getUsers(params);
    }
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        try {
            service.deleteUser(id);
            return "User deleted successfully";
        } catch (Exception e) {
            return "Failure";
        }
    }

   



    @GetMapping("/getProfileData/{id}")
    public Optional<User> getUserById(@PathVariable int id) {
        return service.getUserById(id);
    }
    
    

    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        try {
            service.sendPasswordToUser(email);
            return ResponseEntity.ok("Password sent to your email");
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
        }
    }
   
    @Autowired
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
    private final String adminEmail = "animuthu03@gmail.com"; 
    @PostMapping("/sendEmailToAdmin")
    public ResponseEntity<String> sendEmail(
            @RequestParam String from,
            @RequestParam String subject,
            @RequestParam String body,
            @RequestParam(required = false) MultipartFile attachment
    ) {
        try {
            emailService.sendEmail(adminEmail, subject, body, attachment);
            return ResponseEntity.ok("Email sent successfully");
        } catch (MessagingException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
        }
    }
    @PostMapping("/sendEmailToAdminBy")
    public ResponseEntity<String> sendEmailAdmin(
            @RequestParam String stationId,  // Receive the stationId
            @RequestParam String subject,
            @RequestParam String body,
            @RequestParam(required = false) MultipartFile attachment
    ) {
        try {
            // Fetch the admin email based on the stationId
            String adminEmail = emailService.getAdminEmailByStationId(stationId);
            if (adminEmail == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin email not found for the given stationId");
            }

            // Send the email
            emailService.sendEmail(adminEmail, subject, body, attachment);
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
        	 System.out.println("Recipients: " + to);
            emailService.sendEmailToMultiple(to, subject, body, attachment); 
            return ResponseEntity.ok("Email sent successfully to all recipients");
        } catch (MessagingException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable("id") int id, @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Ensure the ID in the request body matches the ID in the URL
            if (user.getId() != id) {
                response.put("error", "ID in request body does not match URL ID");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Perform the update
            service.updateUser(user);
            
            // Prepare success response
            response.put("message", "User updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid input: {}", e.getMessage());
            response.put("error", "Invalid input: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            response.put("error", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Failed to update user: {}", e.getMessage(), e);
            response.put("error", "Failed to update user");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/sendPaymentConfirmationEmail")
    public ResponseEntity<String> sendPaymentConfirmationEmail(
            @RequestParam String bookingId,
            @RequestParam String slotId,
            @RequestParam double amountWithTax,
            @RequestParam String deviceType,
            @RequestParam String paymentMethod,
            @RequestParam(required = false) MultipartFile attachment) {
        try {
            // Retrieve the user's email based on the booking ID
            String userEmail = service.getUserEmailByBookingId(bookingId);
            
            if (userEmail == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User email not found for the given booking ID");
            }

            // Construct the email content
            String subject = "Payment Confirmation for Booking ID: " + bookingId;
            String body = String.format(
                "Dear User,\n\n" +
                "Your payment for booking ID %s has been successfully processed.\n\n" +
                "Details:\n" +
                "Slot ID: %s\n" +
                "Device Type: %s\n" +
                "Amount with Tax: $%.2f\n" +
                "Payment Method: %s\n\n" +
                "Thank you for using our service.\n\n" +
                "Best regards,\n" +
                "The Team",
                bookingId, slotId, deviceType, amountWithTax, paymentMethod
            );

            
            emailService.sendEmailToMultipleConfirm(
                List.of(userEmail), 
                subject,
                body,
                attachment 
            );

            return ResponseEntity.ok("Payment confirmation email sent successfully");
        } catch (MessagingException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }
 // Example of a PaymentController method to handle payment and send email
    @PostMapping("/processPayment")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequestDTO paymentRequest) {
        try {
            // Validate paymentRequest
            if (paymentRequest == null || paymentRequest.getBooking() == null) {
                return ResponseEntity.badRequest().body("Invalid payment request data.");
            }

            // Ensure deviceType is handled correctly
            String deviceType = paymentRequest.getDeviceType() != null ? paymentRequest.getDeviceType() : "Unknown";

            // Process the payment
            boolean paymentSuccessful = paymentServiceImpl.processPayment(paymentRequest);
            
            if (paymentSuccessful) {
                // Prepare parameters for sending email
                String bookingId = String.valueOf(paymentRequest.getBooking().getBookingId());
                String slotId = paymentRequest.getSlotId() != null ? String.valueOf(paymentRequest.getSlotId()) : "N/A";
                double amountWithTax = paymentRequest.getAmount() != null ? paymentRequest.getAmount().doubleValue() : 0.0;
                String paymentMethod = paymentRequest.getPaymentMethod() != null ? paymentRequest.getPaymentMethod().name() : "Unknown";
                
                // Call the email sending method
                sendPaymentConfirmationEmail(bookingId, slotId, amountWithTax, deviceType, paymentMethod, null);
                
                return ResponseEntity.status(HttpStatus.CREATED).body("Payment processed and confirmation email sent.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment failed.");
            }
        } catch (Exception e) {
            // Log the exception (consider using a logging framework)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing payment: " + e.getMessage());
        }
    }
    


}
