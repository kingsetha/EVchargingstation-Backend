package com.ani.home.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ani.home.model.StationAdmin;
import com.ani.home.model.User;
import com.ani.home.repo.StationAdminRepo;
import com.ani.home.repo.UserRepo;

import java.io.IOException;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private final String fromEmail = "noreply@yourdomain.com"; 

    private final String adminEmail = "animuthu03@gmail.com"; 

    public void sendEmailToAdmin(String subject, String text, MultipartFile attachment) throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(fromEmail);
        helper.setTo(adminEmail); 
        helper.setSubject(subject);
        helper.setText(text, true); 

        if (attachment != null && !attachment.isEmpty()) {
            helper.addAttachment(attachment.getOriginalFilename(), new ByteArrayResource(attachment.getBytes()));
        }

        javaMailSender.send(mimeMessage);
        System.out.println("Mail sent to admin");
    }
    
    public void sendEmail(String to, String subject, String text, MultipartFile attachment) throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); 

        if (attachment != null && !attachment.isEmpty()) {
            helper.addAttachment(attachment.getOriginalFilename(), new ByteArrayResource(attachment.getBytes()));
        }

        javaMailSender.send(mimeMessage);
        System.out.println("Mail sent");
    }
//    public void sendEmailToMultiple(List<String> recipients, String subject, String text, MultipartFile attachment) throws MessagingException, IOException {
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//
//        helper.setFrom(fromEmail);
//        helper.setTo(recipients.toArray(new String[0])); // Convert List to Array
//        helper.setSubject(subject);
//        helper.setText(text, true); // true indicates HTML content
//
//        if (attachment != null && !attachment.isEmpty()) {
//            helper.addAttachment(attachment.getOriginalFilename(), new ByteArrayResource(attachment.getBytes()));
//        }
//
//        javaMailSender.send(mimeMessage);
//        System.out.println("Mail sent to " + recipients.size() + " recipients");
//    }
//    private final String fromEmail = "noreply@yourdomain.com"; 

    public void sendEmailToMultiple(List<String> recipients, String subject, String text, MultipartFile attachment) throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(fromEmail);
        helper.setTo(recipients.toArray(new String[0])); // Convert List to Array
        helper.setSubject(subject);
        helper.setText(text, true); // true indicates HTML content

        if (attachment != null && !attachment.isEmpty()) {
            helper.addAttachment(attachment.getOriginalFilename(), new ByteArrayResource(attachment.getBytes()));
        }

        javaMailSender.send(mimeMessage);
        System.out.println("Mail sent to " + recipients.size() + " recipients");
    }
    
    public void sendEmailToMultipleConfirm(List<String> recipients, String subject, String body, MultipartFile attachment) throws MessagingException, IOException {
        // Create a new MimeMessage
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        // Set the sender's email address
        helper.setFrom(fromEmail);
        // Set multiple recipient email addresses
        helper.setTo(recipients.toArray(new String[0])); // Convert List to Array
        // Set the subject of the email
        helper.setSubject(subject);
        // Set the body of the email
        helper.setText(body, true); // true indicates HTML content

        // Add attachment if provided
        if (attachment != null && !attachment.isEmpty()) {
            helper.addAttachment(attachment.getOriginalFilename(), new ByteArrayResource(attachment.getBytes()));
        }

        // Send the email
        javaMailSender.send(mimeMessage);
        System.out.println("Mail sent to " + recipients.size() + " recipients");
    }
    public void sendPaymentConfirmationEmail(String to, String bookingId, String slotId, double amountWithTax, String deviceType, String paymentMethod) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Payment Confirmation");

        // Format the email body with the provided arguments
        String emailBody = String.format(
            "Dear Customer,%n%n" +
            "Thank you for your payment.%n%n" +
            "Booking ID: %s%n" +
            "Slot ID: %s%n" +
            "Amount Paid (with tax): $%.2f%n" +
            "Device Type: %s%n" +
            "Payment Method: %s%n%n" +
            "Best regards,%n" +
            "Your Company",
            bookingId, 
            slotId, 
            amountWithTax, 
            deviceType, 
            paymentMethod
        );

        message.setText(emailBody);
        javaMailSender.send(message);
    }
    public void sendEmailWithMap(String to, String subject, String text, String mapLocationUrl) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);

        // Construct the email body with the map location URL
        String htmlContent = String.format(
            "<html><body>" +
            "<p>%s</p>" +
            "<p>Click <a href=\"%s\">here</a> to view the map location.</p>" +
            "</body></html>",
            text,
            mapLocationUrl
        );

        helper.setText(htmlContent, true); // true indicates HTML content

        javaMailSender.send(mimeMessage);
        System.out.println("Mail sent with map location URL");
    }
    
    public void sendReminderEmail(String to, int daysBefore) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject("Reminder: Vehicle Recharge Due Soon");

        String text = String.format(
            "Dear User,<br><br>" +
            "This is a friendly reminder that your vehicle is due for recharge in %d days.<br><br>" +
            "Best regards,<br>Your Company", daysBefore);

        helper.setText(text, true); // true indicates HTML content

        javaMailSender.send(mimeMessage);
        System.out.println("Reminder email sent to " + to);
    }
    
    public void sendFeedbackRequestEmail(String toEmail, int slotId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Request for Feedback on Your Charging Session");
        message.setText("Dear User,\n\nThank you for using our charging service. Your slot (ID: " + slotId + ") has been completed. We value your feedback, and we would appreciate it if you could take a moment to provide your feedback on our service.\n\nBest regards,\nCharging Service Team");
        javaMailSender.send(message);
    }
    
    
    
    @Autowired
    private StationAdminRepo stationAdminRepo;
    public String getAdminEmailByStationId(String stationId) {
        // Fetch the station admin by stationId
        StationAdmin admin = stationAdminRepo.findByStationId(stationId);
        System.out.println(stationId);
        return admin != null ? admin.getEmail() : null;
    }


}
