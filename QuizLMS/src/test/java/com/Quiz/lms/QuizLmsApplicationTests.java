package com.Quiz.lms;

import com.Quiz.lms.domain.Member;
import com.Quiz.lms.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuizLmsApplicationTests {

	@Autowired
	MemberRepository memberRepository;

	@Test
	void contextLoads() {
		Member member = new Member();
		member.setId(1L);
		member.setPassword("1234");
		member.setName("test0");
		member.setUsername("test0");

		memberRepository.save(member);

	}

}
