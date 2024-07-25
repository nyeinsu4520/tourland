package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ReviewTripPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    private String comment;
    private int rating;

    @ManyToOne
    @JoinColumn(name = "trip_package_id")
    private TripPackage tripPackage;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // Constructors
    public ReviewTripPackage() {}

    public ReviewTripPackage(String comment, int rating) {
        this.comment = comment;
        this.rating = rating;
    }

    // Getters and setters
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public TripPackage getTripPackage() {
        return tripPackage;
    }

    public void setTripPackage(TripPackage tripPackage) {
        this.tripPackage = tripPackage;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
