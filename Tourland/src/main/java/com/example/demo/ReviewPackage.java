package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ReviewPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    private String comment;
    private int rating;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package packageObj;

    @ManyToOne
    @JoinColumn(name = "id")
    private Member member;

    

    //constructors
  	public ReviewPackage() {}
  	
	//getters and setters

	public ReviewPackage(String comment, int rating) {
  		this.comment=comment;
  		this.rating=rating;
  	}



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



	public Package getPackageObj() {
		return packageObj;
	}



	public void setPackageObj(Package packageObj) {
		this.packageObj = packageObj;
	}



	public Member getMember() {
		return member;
	}



	public void setMember(Member member) {
		this.member = member;
	}

  
	
}
