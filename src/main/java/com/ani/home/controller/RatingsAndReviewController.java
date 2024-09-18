package com.ani.home.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ani.home.model.RatingsAndReviews;
import com.ani.home.serviceimpl.RatingsAndReviewServiceImpl;

@RestController
@RequestMapping("/Rating")
@CrossOrigin("*")
public class RatingsAndReviewController {

    
    public RatingsAndReviewController(RatingsAndReviewServiceImpl service) {
		super();
		this.service = service;
	}

	private RatingsAndReviewServiceImpl service;

    @PostMapping("/register")
    public String addUser(@RequestBody RatingsAndReviews ratingAndReview) {
        try {
            service.addRating(ratingAndReview);
            return "Registered Successfully";
        } catch (Exception e) {
            return "Failure";
        }
    }
    
    @GetMapping("/reviews/{stationId}")
    public List<RatingsAndReviews> getReviewsByStationId(@PathVariable int stationId) {
        return service.getReviewsByStationId(stationId);
    }
    
    @GetMapping("/all")
    public List<RatingsAndReviews> getAllFeedback() {
        return service.getAllReview();
    }
}
