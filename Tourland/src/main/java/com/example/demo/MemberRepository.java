package com.example.demo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Integer> {

	public Member findByUsername(String username);
	
	   Member findByEmail(String email);
	   
	   boolean existsByUsername(String username);
	    boolean existsByEmail(String email);
	    
	  
	    Optional<Member> findByResetToken(String resetToken);
	    
	
	
}