package com.example.demo;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int booking_id;
    
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package pkg;
    
    private String special_requests;
    private int numberofpeople;
    private LocalDate travel_date;


    // Getters and Setters
    
	public int getBooking_id() {
		return booking_id;
	}

	public void setBooking_id(int booking_id) {
		this.booking_id = booking_id;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Package getPkg() {
		return pkg;
	}

	public void setPkg(Package pkg) {
		this.pkg = pkg;
	}

	public String getSpecial_requests() {
		return special_requests;
	}

	public void setSpecial_requests(String special_requests) {
		this.special_requests = special_requests;
	}
	
	public int getNumberofpeople() {
		return numberofpeople;
	}

	public void setNumberofpeople(int numberofpeople) {
		this.numberofpeople = numberofpeople;
	}

	public LocalDate getTravel_date() {
		return travel_date;
	}

	public void setTravel_date(LocalDate travel_date) {
		this.travel_date = travel_date;
	}


  
   
}
