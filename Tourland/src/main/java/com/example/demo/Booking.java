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
    private int id;
    private String customer_name;
    private String customer_email;
    private String customer_phone;
    private String special_requests;
    private LocalDate travel_date;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package pkg;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getCustomer_email() {
		return customer_email;
	}

	public void setCustomer_email(String customer_email) {
		this.customer_email = customer_email;
	}

	public String getCustomer_phone() {
		return customer_phone;
	}

	public void setCustomer_phone(String customer_phone) {
		this.customer_phone = customer_phone;
	}

	public String getSpecial_requests() {
		return special_requests;
	}

	public void setSpecial_requests(String special_requests) {
		this.special_requests = special_requests;
	}

	public LocalDate getTravel_date() {
		return travel_date;
	}

	public void setTravel_date(LocalDate travel_date) {
		this.travel_date = travel_date;
	}

	public Package getPkg() {
		return pkg;
	}

	public void setPkg(Package pkg) {
		this.pkg = pkg;
	}

    // Getters and Setters
    // Constructors
    // Other methods as needed


  
   
}
