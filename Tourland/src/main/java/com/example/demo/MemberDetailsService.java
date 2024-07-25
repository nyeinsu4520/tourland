package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MemberDetailsService implements UserDetailsService{
	
	@Autowired
	MemberRepository memberRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
	Member member=memberRepo.findByUsername(username);
	if(member==null) {
		throw new UsernameNotFoundException("Could not find user!");
	}
	
		return new MemberDetails(member);
	}
	
	
}
