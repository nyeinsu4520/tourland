package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.example.demo.Package;
import com.example.demo.Booking.Status;

@Entity
public class TripPackageBooking {



	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trip_package_id")
    private TripPackage tripPackage;
    
    

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Card hotel;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;
    
    
    public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	private int participants;
    private LocalDate bookingDate;
    private String email;
    private String phone;
    
    public enum Status {
        PENDING, CONFIRMED, REJECTED
    }

    private Status status = Status.PENDING;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TripPackage getTripPackage() {
        return tripPackage;
    }

    public void setTripPackage(TripPackage tripPackage) {
        this.tripPackage = tripPackage;
    }

    public Card getHotel() {
        return hotel;
    }

    public void setHotel(Card hotel) {
        this.hotel = hotel;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
    
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
    
}
