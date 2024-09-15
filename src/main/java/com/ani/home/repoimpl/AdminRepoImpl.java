package com.ani.home.repoimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ani.home.model.Admin;
import com.ani.home.model.StationAdmin;
import com.ani.home.repo.AdminRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class AdminRepoImpl implements AdminRepo {

    private final EntityManager em;

    public AdminRepoImpl(EntityManager em) {
        this.em = em;
    }
    @Override
    public String save(Admin user) {
        if (user != null) {
            em.merge(user);
            return "Success";
        }
        return "Failure";
    }


    @Override
    public Optional<Admin> findByEmailAndPassword(String email, String password) {
        TypedQuery<Admin> query = em.createQuery(
                "SELECT u FROM Admin u WHERE u.email = :email AND u.password = :password", Admin.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
        Admin admin = query.getResultStream().findFirst().orElse(null);
        return Optional.ofNullable(admin);
    }
	
    

}
