package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;
    

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<Member> findById(Integer id) {
        return memberRepository.findById(id);
    }

    @Override
    public String findIdByUsername(String username) {
        Member member = memberRepository.findByUsername(username);
        return member != null ? String.valueOf(member.getId()) : null;
    }

    @Override
    public Member findByUsername(String name) {
        return memberRepository.findByUsername(name);
    }

    public Integer getLoggedInMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Member member = memberRepository.findByUsername(username);
            return member != null ? member.getId() : null;
        }
        return null;
    }
    

    @Override
    public String createPasswordResetTokenForMember(Member member) {
        String token = java.util.UUID.randomUUID().toString();
        member.setResetToken(token);
        memberRepository.save(member);
        return token;
    }

    @Override
    public Optional<Member> getMemberByPasswordResetToken(String token) {
        return memberRepository.findByResetToken(token);
    }

    @Override
    public void changeMemberPassword(Member member, String newPassword) {
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
    }
}
