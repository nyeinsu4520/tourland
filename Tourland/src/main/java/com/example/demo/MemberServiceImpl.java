package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

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

    // You can implement more methods here for other operations related to members
}
