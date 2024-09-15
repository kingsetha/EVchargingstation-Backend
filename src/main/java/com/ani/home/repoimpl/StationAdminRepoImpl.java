
package com.ani.home.repoimpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ani.home.model.StationAdmin;
import com.ani.home.model.StationAdmin.Status;
import com.ani.home.repo.StationAdminRepo;


import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class StationAdminRepoImpl implements StationAdminRepo {

    private final EntityManager em;

    public StationAdminRepoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public String save(StationAdmin user) {
        if (user != null) {
            em.merge(user);
            return "Success";
        }
        return "Failure";
    }

    @Override
    public Optional<StationAdmin> findByEmailAndPassword(String email, String password) {
        TypedQuery<StationAdmin> query = em.createQuery(
                "SELECT u FROM StationAdmin u WHERE u.email = :email AND u.password = :password", StationAdmin.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
        StationAdmin admin = query.getResultStream().findFirst().orElse(null);
        return Optional.ofNullable(admin);
    }

    @Override
    public List<StationAdmin> findByStatus(StationAdmin.Status status) {
        TypedQuery<StationAdmin> query = em.createQuery(
                "SELECT u FROM StationAdmin u WHERE u.status = :status", StationAdmin.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public void updateStatus(int id, StationAdmin.Status status) {
        StationAdmin admin = em.find(StationAdmin.class, id);
        if (admin != null) {
            admin.setStatus(status);
            em.merge(admin);
        }
    }
    @Override
    public Optional<StationAdmin> findById(int id) {
        try {
            StationAdmin admin = em.find(StationAdmin.class, id);
            return Optional.ofNullable(admin);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    @Override
    public Optional<StationAdmin> findByEmail(String email) {
        try {
            String query = "SELECT a FROM StationAdmin a WHERE a.email = :email";
            StationAdmin admin = em.createQuery(query, StationAdmin.class)
                                   .setParameter("email", email)
                                   .getSingleResult();
            return Optional.ofNullable(admin);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public int countByStatus(Status status) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(u) FROM StationAdmin u WHERE u.status = :status", Long.class);
        query.setParameter("status", status);
        return query.getSingleResult().intValue();
    }
    @Override
    public List<StationAdmin> findByLatitudeBetweenAndLongitudeBetweenAndVehicleType(
            BigDecimal latMin, BigDecimal latMax, BigDecimal lonMin, BigDecimal lonMax) {
        String queryStr = "SELECT s FROM StationAdmin s WHERE s.latitude BETWEEN :latMin AND :latMax " +
                          "AND s.longitude BETWEEN :lonMin AND :lonMax " ;
        TypedQuery<StationAdmin> query = em.createQuery(queryStr, StationAdmin.class);
        query.setParameter("latMin", latMin);
        query.setParameter("latMax", latMax);
        query.setParameter("lonMin", lonMin);
        query.setParameter("lonMax", lonMax);
//        query.setParameter("vehicleType", vehicleType);
        return query.getResultList();
    }

	@Override
	public List<StationAdmin> findAll() {
		String hql = "from StationAdmin";
		Query query = em.createQuery(hql);
		return query.getResultList();
	}

	@Override
	public List<StationAdmin> findByBlocked(Boolean blocked) {
		Query query = em.createQuery("SELECT u FROM StationAdmin u WHERE u.blocked = :blocked");
        query.setParameter("blocked", blocked);
        return query.getResultList();
	}
	public StationAdmin findByStationId(String stationId) {
	    // Use JPQL to fetch the StationAdmin by stationId
	    String jpql = "SELECT a FROM StationAdmin a WHERE a.id = :id";
	    TypedQuery<StationAdmin> query = em.createQuery(jpql, StationAdmin.class);
	    query.setParameter("id", stationId); // Set the parameter name to 'id'
	    List<StationAdmin> results = query.getResultList();
	    return results.isEmpty() ? null : results.get(0);
	}

	 @Override
	    public boolean existsById(int id) {
	        String query = "SELECT COUNT(sa) FROM StationAdmin sa WHERE sa.id = :id";
	        Long count = em.createQuery(query, Long.class)
	                                 .setParameter("id", id)
	                                 .getSingleResult();
	        return count > 0;
	    }

	    @Override
	    public StationAdmin save1(StationAdmin stationAdmin) {
	        if (stationAdmin.getId() == 0) {
	            em.persist(stationAdmin);
	            return stationAdmin;
	        } else {
	            return em.merge(stationAdmin);
	        }
	    }

	    @Override
	    public void deleteById(int id) {
	        try {
	            StationAdmin admin = em.find(StationAdmin.class, id);
	            if (admin != null) {
	                em.remove(admin);
	            }
	        } catch (Exception e) {
	            e.printStackTrace(); // Handle any exceptions that occur during deletion
	        }
	    }

	
}
