package com.example.demo;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Favorite {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package pkg;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "trip_package_id") // Specify unique name for TripPackage join column
    private TripPackage tripPackage;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Card card;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    
    private String type;

    public Favorite() {
        // Default constructor required by JPA
    }

    public Favorite(Package pkg, String type) {
        this.pkg = pkg;
        this.type = type;
    }

    public Favorite(Package pkg, String type, Member member) {
        this.pkg = pkg;
        this.type = type;
        this.member = member;
    }
    
 // Constructor for TripPackage favorite
    public Favorite(TripPackage tripPackage, String type, Member member) {
        this.tripPackage = tripPackage;
        this.type = type;
        this.member = member;
    }

    // Constructor for Card favorite
    public Favorite(Card card, String type, Member member) {
        this.card = card;
        this.type = type;
        this.member = member;
    }

    // Constructor for Restaurant favorite
    public Favorite(Restaurant restaurant, String type, Member member) {
        this.restaurant = restaurant;
        this.type = type;
        this.member = member;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Package getPkg() {
		return pkg;
	}

	public void setPkg(Package pkg) {
		this.pkg = pkg;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TripPackage getTripPackage() {
		return tripPackage;
	}

	public void setTripPackage(TripPackage tripPackage) {
		this.tripPackage = tripPackage;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

    

}
