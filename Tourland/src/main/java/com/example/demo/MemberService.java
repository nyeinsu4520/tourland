package com.example.demo;


import java.util.Optional;


public interface MemberService {

    Optional<Member> findById(Integer id);

    String findIdByUsername(String username);

    Member findByUsername(String name);
    
    Integer getLoggedInMemberId();

    // You can add more methods here for other operations related to members
    String createPasswordResetTokenForMember(Member member);

    Optional<Member> getMemberByPasswordResetToken(String token);

    void changeMemberPassword(Member member, String newPassword);
}

