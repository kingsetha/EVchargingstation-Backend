package com.ani.home.repo;

import com.ani.home.model.CertificateNumber;

public interface CertificateNumberRepo {

    boolean existsByCertificateNumber(String certificateNumber);

    CertificateNumber findByCertificateNumber(String certificateNumber);

    void save(CertificateNumber certificateNumber);
}
