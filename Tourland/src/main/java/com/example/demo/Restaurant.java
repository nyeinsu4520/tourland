package com.example.demo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int restaurant_id;
    private String title;
    private String description;
    private String location;
	private String image;
    private String restaurantType;
    private String address;
    
    
    @Transient
    private Double averageRating;




	@ManyToOne
    @JoinColumn(name = "trip_package_id")
    private TripPackage tripPackage;
    
    

	public Double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}



	@OneToMany(mappedBy = "restaurant" ,cascade=CascadeType.ALL)
	private List<ReviewRestaurant> reviewsrestaurant;
    
	
	 public List<ReviewRestaurant> getReviewsrestaurant() {
	        return reviewsrestaurant;
	    }
	 
    

	@ManyToOne(fetch = FetchType.LAZY) // Assuming each Hotel belongs to one Member
    @JoinColumn(name = "member_id") // Assuming you have a column named member_id in your card (hotel) table
    private Member member;

    
    
  //constructors
  	public Restaurant() {}
  	
  	public Restaurant(String title, String description, String location, String image) {
  		this.title=title;
  		this.description=description;
  		this.location=location;
  		this.image=image;
  	
  	}
    
    
    // Getters and Setters

	public int getRestaurant_id() {
		return restaurant_id;
	}

	public void setRestaurant_id(int restaurant_id) {
		this.restaurant_id = restaurant_id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}



	public String getRestaurantType() {
		return restaurantType;
	}

	public void setRestaurantType(String restaurantType) {
		this.restaurantType = restaurantType;
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

	public void setReviewsrestaurant(List<ReviewRestaurant> reviewsrestaurant) {
		this.reviewsrestaurant = reviewsrestaurant;
	}
	
    
    public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
    
}
