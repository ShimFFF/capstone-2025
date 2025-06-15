package naeilmolae;

import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class NaeilmolaeApplicationTests {

	@Autowired
	private MemberRepository memberRepository;

	@Test
	void contextLoads() {
	}

	@Test
	@Transactional
	void sqlInit() {
		List<Member> all = memberRepository.findAll();
		for (Member member : all) {
			System.out.println("member.getName() = " + member.getName());
		}
	}

}
