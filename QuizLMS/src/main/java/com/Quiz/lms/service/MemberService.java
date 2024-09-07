package com.Quiz.lms.service;

import com.Quiz.lms.domain.Member;
import com.Quiz.lms.dto.MemberDTO;
import com.Quiz.lms.repository.MemberRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(MemberDTO memberDTO) {
        System.out.println("Register method called");
        

        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setUsername(memberDTO.getUsername());
        member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));

        memberRepository.save(member);
    }

    public Optional<Member> login(String username, String password) {
        Optional<Member> member = memberRepository.findByUsername(username);
        if (member.isPresent() && passwordEncoder.matches(password, member.get().getPassword())) {
            return member;
        }
        return Optional.empty();
    }



}
