package com.example.demo;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.persistence.JoinColumn;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
public class TripPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
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

	@Transient
    private List<Integer> hotelIds;

    @Transient
    private List<Integer> restaurantIds;
    
    
    @OneToMany(mappedBy = "tripPackage", cascade = CascadeType.ALL)
    private List<ReviewTripPackage> reviewsTripPackage;
 

    public List<ReviewTripPackage> getReviewsTripPackage() {
		return reviewsTripPackage;
	}

	public void setReviewsTripPackage(List<ReviewTripPackage> reviewsTripPackage) {
		this.reviewsTripPackage = reviewsTripPackage;
	}

	@ManyToMany
    @JoinTable(
        name = "trip_package_hotel",
        joinColumns = @JoinColumn(name = "trip_package_id"),
        inverseJoinColumns = @JoinColumn(name = "hotel_id")
    )
    private Set<Card> hotels = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "trip_package_restaurant",
        joinColumns = @JoinColumn(name = "trip_package_id"),
        inverseJoinColumns = @JoinColumn(name = "restaurant_id")
    )
    private Set<Restaurant> restaurants = new HashSet<>();
    

    // Getters and setters

    public List<Integer> getHotelIds() {
        return hotelIds;
    }

    public void setHotelIds(List<Integer> hotelIds) {
        this.hotelIds = hotelIds;
    }

    public List<Integer> getRestaurantIds() {
        return restaurantIds;
    }

    public void setRestaurantIds(List<Integer> restaurantIds) {
        this.restaurantIds = restaurantIds;
    }

    public Set<Card> getHotels() {
        return hotels;
    }

    public void setHotels(Set<Card> hotels) {
        this.hotels = hotels;
    }

    public Set<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(Set<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}


    // Other getters and setters
    
    
}


    


