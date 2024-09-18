package com.ani.home.repoimpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ani.home.model.RatingsAndReviews;
import com.ani.home.repo.RatingsAndReviewRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class RatingsAndReviewRepoImpl implements RatingsAndReviewRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(RatingsAndReviews ratingAndReview) {
        if (ratingAndReview.getReviewId() == 0) {
            
            entityManager.persist(ratingAndReview);
        } else {
            
            entityManager.merge(ratingAndReview);
        }
    }

//    @Override
//    public List<RatingsAndReviews> findByChargingStation_Id(int stationId) {
//        String query = "SELECT r FROM RatingsAndReviews r WHERE r.chargingStation.id = :stationId";
//        return entityManager.createQuery(query, RatingsAndReviews.class)
//                            .setParameter("stationId", stationId)
//                            .getResultList();
//    }
    @Override
    public List<RatingsAndReviews> findByChargingStation_Id(int stationId) {
        String query = "SELECT r FROM RatingsAndReviews r JOIN FETCH r.user JOIN FETCH r.chargingStation WHERE r.chargingStation.id = :stationId";
        return entityManager.createQuery(query, RatingsAndReviews.class)
                            .setParameter("stationId", stationId)
                            .getResultList();
    }

	@Override
	public List<RatingsAndReviews> findAllReview() {
		String query = "SELECT r FROM RatingsAndReviews r";
        return entityManager.createQuery(query, RatingsAndReviews.class).getResultList();
    
	}
}
