package com.ani.home.repo;

import java.util.List;

import com.ani.home.model.RatingsAndReviews;

public interface RatingsAndReviewRepo {
    void save(RatingsAndReviews ratingAndReview);

	List<RatingsAndReviews> findByChargingStation_Id(int stationId);

	List<RatingsAndReviews> findAllReview();
}
