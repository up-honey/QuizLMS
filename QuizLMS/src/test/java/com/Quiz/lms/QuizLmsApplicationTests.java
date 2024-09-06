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
		member.setId(2L);
		member.setUsername("test22");
		member.setPassword("qwer");
		member.setName("test12");

		memberRepository.save(member);

	}

}
