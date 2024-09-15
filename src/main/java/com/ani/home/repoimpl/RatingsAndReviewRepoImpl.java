package com.ani.home.repoimpl;

import org.springframework.beans.factory.annotation.Autowired;
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
            // Insert new record
            entityManager.persist(ratingAndReview);
        } else {
            // Update existing record
            entityManager.merge(ratingAndReview);
        }
    }
}
