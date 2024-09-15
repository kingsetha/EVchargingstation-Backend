package com.ani.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    @Autowired
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
}
