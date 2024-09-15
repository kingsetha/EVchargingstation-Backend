package com.ani.home.repoimpl;

import com.ani.home.model.CertificateNumber;
import com.ani.home.repo.CertificateNumberRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class CertificateNumberRepoImpl implements CertificateNumberRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean existsByCertificateNumber(String certificateNumber) {
        String queryStr = "SELECT COUNT(c) FROM CertificateNumber c WHERE c.certificateNumber = :certificateNumber";
        TypedQuery<Long> query = entityManager.createQuery(queryStr, Long.class);
        query.setParameter("certificateNumber", certificateNumber);
        Long count = query.getSingleResult();
        return count > 0;
    }

    @Override
    public CertificateNumber findByCertificateNumber(String certificateNumber) {
        String queryStr = "SELECT c FROM CertificateNumber c WHERE c.certificateNumber = :certificateNumber";
        TypedQuery<CertificateNumber> query = entityManager.createQuery(queryStr, CertificateNumber.class);
        query.setParameter("certificateNumber", certificateNumber);
        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public void save(CertificateNumber certificateNumber) {
        if (certificateNumber.getId() == 0) {
            entityManager.persist(certificateNumber);
        } else {
            entityManager.merge(certificateNumber);
        }
    }
}
