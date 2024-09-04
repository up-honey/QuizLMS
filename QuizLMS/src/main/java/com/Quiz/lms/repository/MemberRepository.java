package com.Quiz.lms.repository;

import com.Quiz.lms.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
