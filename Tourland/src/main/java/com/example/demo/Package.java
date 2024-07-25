package com.example.demo;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.persistence.JoinColumn;

@Entity
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int package_id;
    private String title;
    private String description;
    private String about;
    



	private String image;
    private int price;
    private String location;
    
    @Transient
    private Double averageRating;


	public Double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}



	@OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();
    
   
    
    
    
    @OneToMany(mappedBy = "packageObj" ,cascade=CascadeType.ALL)
	private List<ReviewPackage> reviewspackage;
    
	
	 public List<ReviewPackage> getReviewspackage() {
	        return reviewspackage;
	    }
	 
    

	@ManyToOne(fetch = FetchType.LAZY) // Assuming each Hotel belongs to one Member
    @JoinColumn(name = "member_id") // Assuming you have a column named member_id in your card (hotel) table
    private Member member;
 

    
    // Getters and Setters
	public int getPackage_id() {
		return package_id;
	}

	public void setPackage_id(int package_id) {
		this.package_id = package_id;
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public void setReviewspackage(List<ReviewPackage> reviewspackage) {
		this.reviewspackage = reviewspackage;
	}

    
	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}	
	  
}



