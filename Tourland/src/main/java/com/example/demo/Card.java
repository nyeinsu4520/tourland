package com.example.demo;

import java.time.LocalDate;
import java.util.List;

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
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hotel_id;
    private String title;
    private String description;
    private String location;
    private String image;
	private int price;
	private String roomType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
	
    

    @ManyToOne
    @JoinColumn(name = "trip_package_id")
    private TripPackage tripPackage;
	

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}



	public List<ReviewHotel> getReviewHotel() {
		return ReviewHotel;
	}

	public void setReviewHotel(List<ReviewHotel> reviewHotel) {
		ReviewHotel = reviewHotel;
	}

	@OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<ReviewHotel> ReviewHotel;




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

	public Card(String title, String description, String location, String image, int price) {
		this.title=title;
		this.description=description;
		this.location=location;
		this.image=image;
		this.price=price;
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
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	
    
}
