package com.ani.home.serviceimpl;


import com.ani.home.model.Reminder;
import com.ani.home.model.User;
import com.ani.home.repo.ReminderRepo;
import com.ani.home.repo.UserRepo;
import com.ani.home.service.EmailService;
import com.ani.home.service.ReminderService;
import com.ani.home.service.UserService;

import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ReminderServiceImpl implements ReminderService {

 @Autowired
 private ReminderRepo reminderRepository;

 @Autowired
 private UserRepo userRepository;
 
 @Autowired
 private EmailService emailService;
 
 @Autowired
 private UserService userService;

 private static final Logger logger = LoggerFactory.getLogger(ReminderService.class);

 @Override
 public Optional<Reminder> getReminderByUserId(int userId) {
     return reminderRepository.findByUserId(userId);
 }

 @Override
 public Reminder createOrUpdateReminder(int userId, Reminder reminder) {
     // Fetch the User entity from the database
     Optional<User> userOptional = userRepository.findById(userId);

     if (userOptional.isEmpty()) {
         throw new RuntimeException("User not found with id: " + userId);  
     }

     User user = userOptional.get();  

     return reminderRepository.findByUserId(userId)
             .map(existingReminder -> {
                 existingReminder.setReminderEnabled(reminder.isReminderEnabled());
                 existingReminder.setDaysBefore(reminder.getDaysBefore());
                 LocalDate reminderDate = LocalDate.now().plusDays(reminder.getDaysBefore());
                 existingReminder.setReminderDate(reminderDate);
                 existingReminder.setUser(user);
                 return reminderRepository.save(existingReminder);
             })
             .orElseGet(() -> {
                 reminder.setUser(user);  // Set the fetched User entity
                 LocalDate reminderDate = LocalDate.now().plusDays(reminder.getDaysBefore());
                 reminder.setReminderDate(reminderDate);
                 return reminderRepository.save(reminder);
             });
 }

 @Scheduled(cron = "0 * * * * ?")
 public void sendReminders() {
     logger.info("Scheduled task running...");
     List<Reminder> reminders = reminderRepository.findAll();
     LocalDateTime now = LocalDateTime.now();
     LocalDate currentDate = now.toLocalDate();
     LocalTime currentTime = now.toLocalTime();

     logger.info("Current date: {} Current time: {}", currentDate, currentTime);

     for (Reminder reminder : reminders) {
         LocalDate reminderDate = reminder.getReminderDate();
         LocalTime reminderTime = reminder.getReminderTime();

         logger.info("Processing reminder ID {}: Date {}, Time {}", reminder.getId(), reminderDate, reminderTime);

         if (reminder.isReminderEnabled() &&
             reminderDate.equals(currentDate) &&
             reminderTime != null &&
             isTimeWithinRange(currentTime, reminderTime, 1)) { 

             User user = userService.getUserById(reminder.getUser().getId()).orElse(null);
             if (user != null) {
                 String email = user.getEmail();
                 logger.info("Sending email to {} for reminder ID {}", email, reminder.getId());
                 try {
                     sendReminderEmail(user, reminder);
                 } catch (MessagingException e) {
                     logger.error("Failed to send email for reminder ID {}: {}", reminder.getId(), e.getMessage(), e);
                 } catch (IOException e) {
                     logger.error("IO error occurred while sending email for reminder ID {}: {}", reminder.getId(), e.getMessage(), e);
                 }
             } else {
                 logger.error("User not found for reminder ID {}", reminder.getId());
             }
         } else {
             logger.info("No matching reminder conditions for reminder ID {}", reminder.getId());
         }
     }
 }


 private boolean isTimeWithinRange(LocalTime currentTime, LocalTime reminderTime, int minutes) {
	    LocalTime lowerBound = reminderTime.minusMinutes(minutes);
	    LocalTime upperBound = reminderTime.plusMinutes(minutes);

	    logger.info("Checking time range: Current time = {}, Reminder time = {}, Lower bound = {}, Upper bound = {}",
	        currentTime, reminderTime, lowerBound, upperBound);

	    return !currentTime.isBefore(lowerBound) && !currentTime.isAfter(upperBound);
	}


 protected void sendReminderEmail(User user, Reminder reminder) throws MessagingException, IOException {
     String subject = "Reminder Notification";

     String text = String.format(
         "Dear %s,%n%n" +
         "This is a reminder that your next action is due in %d days.%n" +
         "Reminder Date: %s%n%n" +
         "Best regards,%n" +
         "EV charging station",
         user.getFullName(),
         reminder.getDaysBefore(),
         reminder.getReminderDate()
     );

     emailService.sendEmail(user.getEmail(), subject, text, null); // No attachment
     logger.info("Reminder email sent to {}", user.getEmail());
 }


}
