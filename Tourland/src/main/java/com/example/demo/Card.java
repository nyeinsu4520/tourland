package com.example.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private int hotel_id;
    private String title;
    private String description;
    private String location;
    private String image;
    private String address;
    private String phnum;
    private String has_foreign_key_reference;
	
    
 // Add this field to store the average rating
    @Transient
    private Double averageRating;

	
    

	public String getPhnum() {
		return phnum;
	}

	public void setPhnum(String phnum) {
		this.phnum = phnum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}

	@ManyToOne
    @JoinColumn(name = "trip_package_id")
    private TripPackage tripPackage;
	
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomType> roomTypes = new ArrayList<>();






	@OneToMany(mappedBy = "card" ,cascade=CascadeType.ALL)
	private List<ReviewHotel> reviewshotel;
    
	
	 public List<ReviewHotel> getReviewshotel() {
	        return reviewshotel;
	    }
	 
    @ManyToOne(fetch = FetchType.LAZY) // Assuming each Hotel belongs to one Member
    @JoinColumn(name = "member_id") // Assuming you have a column named member_id in your card (hotel) table
    private Member member;
 
	 
	//constructors
	public Card() {}
	
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Card(String title, String description, String location, String image) {
		this.title=title;
		this.description=description;
		this.location=location;
		this.image=image;
	}
	
    // Getters and Setters
    
	
	
	public String getTitle() {
		return title;
	}
	public int getHotel_id() {
		return hotel_id;
	}

	public void setHotel_id(int hotel_id) {
		this.hotel_id = hotel_id;
	}

	public void setReviewshotel(List<ReviewHotel> reviewshotel) {
		this.reviewshotel = reviewshotel;
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	public List<RoomType> getRoomTypes() {
		return roomTypes;
	}

	public void setRoomTypes(List<RoomType> roomTypes) {
		this.roomTypes = roomTypes;
	}

	public TripPackage getTripPackage() {
		return tripPackage;
	}

	public void setTripPackage(TripPackage tripPackage) {
		this.tripPackage = tripPackage;
	}

	public String getHas_foreign_key_reference() {
		return has_foreign_key_reference;
	}

	public void setHas_foreign_key_reference(String has_foreign_key_reference) {
		this.has_foreign_key_reference = has_foreign_key_reference;
	}

	
    
}
