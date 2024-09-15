package com.ani.home.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ani.home.model.RatingsAndReviews;
import com.ani.home.repo.RatingsAndReviewRepo;

@Service
public class RatingsAndReviewServiceImpl {

    @Autowired
    private RatingsAndReviewRepo repository;

    public void addRating(RatingsAndReviews ratingAndReview) {
        repository.save(ratingAndReview);
    }
}
