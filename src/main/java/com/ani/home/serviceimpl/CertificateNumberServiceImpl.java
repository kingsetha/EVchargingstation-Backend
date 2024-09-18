package com.ani.home.serviceimpl;

import com.ani.home.model.CertificateNumber;
import com.ani.home.repo.CertificateNumberRepo;
import com.ani.home.service.CertificateNumberService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificateNumberServiceImpl implements CertificateNumberService {

	@Autowired
    private CertificateNumberRepo certificateNumberRepo;

    public boolean isCertificateNumberValid(String certificateNumber) {
        CertificateNumber cert = certificateNumberRepo.findByCertificateNumber(certificateNumber);
        return cert != null && !cert.isActive();  
        
    }

    @Transactional  
    public boolean activateCertificateNumber(String certificateNumber) {
        CertificateNumber cert = certificateNumberRepo.findByCertificateNumber(certificateNumber);
        if (cert != null && !cert.isActive()) {
            cert.setActive(true);
            certificateNumberRepo.save(cert);  
            return true;  
        }
        return false;  
    }

}
