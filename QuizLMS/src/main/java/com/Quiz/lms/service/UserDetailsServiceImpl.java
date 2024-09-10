package com.Quiz.lms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Quiz.lms.domain.Member;
import com.Quiz.lms.domain.UserRole;
import com.Quiz.lms.repository.MemberRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

//        return new User(member.getUsername(), member.getPassword(), new ArrayList<>()); // 유저 정보를 반환
     // 사용자 권한 리스트 생성
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : member.getRoles()) {
            // 각 역할을 GrantedAuthority로 변환하여 추가
            authorities.add(new SimpleGrantedAuthority(role.name())); // Enum의 이름을 사용
        }
        
        return org.springframework.security.core.userdetails.User
                .withUsername(member.getUsername())
                .password(member.getPassword())
                .authorities(authorities) 
//                .roles(member.getRoles().toArray(new String[0]))
                .build();
    }
}

