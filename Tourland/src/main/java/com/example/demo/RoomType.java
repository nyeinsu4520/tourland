package com.example.demo;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class RoomType {
	
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String type;
	    private BigDecimal price;
	    private String imageFilename; 
	    
	    
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "hotel_id")
	    private Card card;
		

	    // getters and setters
	    public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public BigDecimal getPrice() {
			return price;
		}
		public void setPrice(BigDecimal price) {
			this.price = price;
		}
		public String getImageFilename() {
			return imageFilename;
		}
		public void setImageFilename(String imageFilename) {
			this.imageFilename = imageFilename;
		}
		public Card getCard() {
			return card;
		}
		public void setCard(Card card) {
			this.card = card;
		}
		
	    
	}


