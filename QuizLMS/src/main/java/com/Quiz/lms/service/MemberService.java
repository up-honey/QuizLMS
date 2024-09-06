package com.Quiz.lms.service;

import com.Quiz.lms.domain.Member;
import com.Quiz.lms.dto.MemberDTO;
import com.Quiz.lms.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(MemberDTO memberDTO) {
        System.out.println("Register method called");
        // System.out.println(memberDTO.getEmail());

//        try {
//            memberService.register(memberDTO);
//            System.out.println("Registration successful");
//        } catch (IllegalArgumentException e) {
//            System.out.println(ResponseEntity.badRequest().body(e.getMessage()));
//        }

        //System.out.println(memberDTO.getEmail()+"1");

        if (memberRepository.findByUsername(memberDTO.getUsername()).isPresent()) {
            System.out.println(memberDTO.getUsername());
            System.out.println("Username already exists");
            throw new IllegalArgumentException("Username already exists");
        }
        if (memberRepository.findByEmail(memberDTO.getEmail()).isPresent()) {
            System.out.println("Useremail already exists");
            throw new IllegalArgumentException("Email already exists");
        }
        if (memberRepository.findByPhoneNumber(memberDTO.getPhoneNumber()).isPresent()) {
            System.out.println("UserPhone already exists");
            throw new IllegalArgumentException("Phone number already exists");
        }
        if (memberRepository.findByNickname(memberDTO.getNickname()).isPresent()) {
            System.out.println("UserNick already exists");
            throw new IllegalArgumentException("Nickname already exists");
        }

        Member member = new Member();
        member.setEmail(memberDTO.getEmail());
        member.setName(memberDTO.getName());
        member.setNickname(memberDTO.getNickname());
        member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        member.setPhoneNumber(memberDTO.getPhoneNumber());
        member.setUsername(memberDTO.getUsername());

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
