package com.ani.home.controller;

import com.ani.home.service.CertificateNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CertificateNumberController {

    @Autowired
    private CertificateNumberService certificateNumberService;
    @GetMapping("/api/check-certificate")
    public ResponseEntity<?> checkCertificateNumber(@RequestParam("certificateNumber") String certificateNumber) {
        try {
            boolean isValid = certificateNumberService.isCertificateNumberValid(certificateNumber);
            if (isValid) {
                boolean isActivated = certificateNumberService.activateCertificateNumber(certificateNumber);
                if (isActivated) {
                    return ResponseEntity.ok().body("Certificate number was inactive and is now activated.");
                } else {
                    return ResponseEntity.ok().body("Certificate number is already active.");
                }
            } else {
                return ResponseEntity.ok().body("Invalid or inactive certificate number.");
            }
        } catch (Exception e) {
            // Log the exception and return an internal server error
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }

    @PostMapping("/api/register-certificate")
    public ResponseEntity<?> registerCertificateNumber(@RequestParam("certificateNumber") String certificateNumber) {
        try {
            boolean activated = certificateNumberService.activateCertificateNumber(certificateNumber);
            if (activated) {
                return ResponseEntity.ok("Certificate number activated successfully");
            } else {
                return ResponseEntity.badRequest().body("Certificate number is either already active or does not exist");
            }
        } catch (Exception e) {
            // Log the exception and return an internal server error
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }

}
