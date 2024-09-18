package com.ani.home.controller;

import com.ani.home.model.Reminder;
import com.ani.home.model.User;
import com.ani.home.repo.ReminderRepo;
import com.ani.home.repo.UserRepo;
import com.ani.home.service.EmailService;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import java.util.Optional;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {


    private ReminderRepo reminderRepository;

   
    private UserRepo userRepository;  
    
    public ReminderController(ReminderRepo reminderRepository, UserRepo userRepository, EmailService emailService) {
		super();
		this.reminderRepository = reminderRepository;
		this.userRepository = userRepository;
		this.emailService = emailService;
	}

    private EmailService emailService; 
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Reminder> getReminder(@PathVariable int userId) {
        Optional<Reminder> reminder = reminderRepository.findByUserId(userId);
        return reminder.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Reminder> updateReminder(@PathVariable int userId, @RequestBody Reminder reminder) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); 
        }

        User user = userOptional.get();  

        return reminderRepository.findByUserId(userId)
                .map(existingReminder -> {
                    existingReminder.setReminderEnabled(reminder.isReminderEnabled());
                    existingReminder.setDaysBefore(reminder.getDaysBefore());
                    existingReminder.setReminderDate(reminder.getReminderDate()); 
                    existingReminder.setReminderTime(reminder.getReminderTime()); 
                    existingReminder.setUser(user);
                    reminderRepository.save(existingReminder);
                    return ResponseEntity.ok(existingReminder);
                })
                .orElseGet(() -> {
                    reminder.setUser(user);
                    reminderRepository.save(reminder);
                    return ResponseEntity.ok(reminder);
                });
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteReminder(@PathVariable int userId) {
        Optional<Reminder> reminderOptional = reminderRepository.findByUserId(userId);

        if (reminderOptional.isPresent()) {
            reminderRepository.delete(reminderOptional.get());
            return ResponseEntity.noContent().build(); 
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }

    protected void sendReminderEmail(User user, Reminder reminder) {
        String subject = "Reminder Notification";

        String text = String.format(
            "Dear %s,%n%n" +
            "This is a reminder that your next action is due in %d days.%n" +
            "Reminder Date: %s%n%n" +
            "Best regards,%n" +
            "Your Company",
            user.getFullName(),
            reminder.getDaysBefore(),
            reminder.getReminderDate()
        );

        try {
            emailService.sendEmail(user.getEmail(), subject, text, null); 
            System.out.println("Reminder email sent to " + user.getEmail());
        } catch (MessagingException | IOException e) {
            System.err.println("Failed to send reminder email to " + user.getEmail());
            e.printStackTrace();
        }
    }

}
