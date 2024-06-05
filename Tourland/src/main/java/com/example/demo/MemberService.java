package com.example.demo;


import java.util.Optional;


public interface MemberService {

    Optional<Member> findById(Integer id);

    String findIdByUsername(String username);

    Member findByUsername(String name);

    // You can add more methods here for other operations related to members
}

