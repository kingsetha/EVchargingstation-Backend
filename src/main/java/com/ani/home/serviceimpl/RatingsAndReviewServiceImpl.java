package com.ani.home.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ani.home.model.RatingsAndReviews;
import com.ani.home.model.StationAdmin;
import com.ani.home.model.User;
import com.ani.home.repo.RatingsAndReviewRepo;
import com.ani.home.repo.StationAdminRepo;
import com.ani.home.repo.UserRepo;

import java.util.List;


@Service
public class RatingsAndReviewServiceImpl {



    @Autowired
    private RatingsAndReviewRepo repository;

    @Autowired
    private StationAdminRepo stationAdminRepo;

    @Autowired
    private UserRepo userRepo;


    public void addRating(RatingsAndReviews ratingAndReview) {
        // Fetch user and station from the database
        User user = userRepo.findById(ratingAndReview.getUser().getId())
            .orElseThrow(() -> new RuntimeException("User with ID " + ratingAndReview.getUser().getId() + " not found"));

        StationAdmin station = stationAdminRepo.findById(ratingAndReview.getChargingStation().getId())
            .orElseThrow(() -> new RuntimeException("Charging Station with ID " + ratingAndReview.getChargingStation().getId() + " not found"));

        // Set user and station
        ratingAndReview.setUser(user);
        ratingAndReview.setChargingStation(station);

        // Save the review
        repository.save(ratingAndReview);
    }
    public List<RatingsAndReviews> getReviewsByStationId(int stationId) {
        return repository.findByChargingStation_Id(stationId);
    }
	public List<RatingsAndReviews> getAllReview() {
		 return repository.findAllReview();
	}
}
