package com.Quiz.lms.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Quiz.lms.domain.Member;
import com.Quiz.lms.domain.UserRole;
import com.Quiz.lms.dto.MemberDTO;
import com.Quiz.lms.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        
        //권한 설정
        Set<UserRole> roles = new HashSet<>();
        if("ADMIN".equalsIgnoreCase(memberDTO.getUsername())) {
            roles.add(UserRole.ROLE_ADMIN);
            log.info("Adding ADMIN role for user: {}", memberDTO.getUsername());
        }
        else {
        	roles.add(UserRole.ROLE_USER);
        }
        member.setRoles(roles);
        
        memberRepository.save(member);
        
        System.out.println("유저 권한" + member.getRoles());
    }

    public Optional<Member> login(String username, String password) {
        Optional<Member> member = memberRepository.findByUsername(username);
        if (member.isPresent() && passwordEncoder.matches(password, member.get().getPassword())) {
            return member;
        }
        return Optional.empty();
    }

    //권한 관련
    public boolean isAdmin(String username) {
        Member user = memberRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getRoles().contains(UserRole.ROLE_ADMIN);
    }

    public void addAdminRole(String username) {
    	Member user = memberRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.getRoles().add(UserRole.ROLE_ADMIN);
        memberRepository.save(user);
    }
}
