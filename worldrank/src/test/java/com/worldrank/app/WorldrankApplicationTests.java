package com.worldrank.app;

import com.worldrank.app.auth.security.JwtProvider;
import com.worldrank.app.user.domain.Usuario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WorldrankApplicationTests {

	@Autowired
	private JwtProvider jwtProvider;

	@Test
	void contextLoads() {
	}

	@Test
	void generateNonExpiringJwt() {
		String userId = "628d2a03-0f49-46fe-b069-ec3c48554256";
		Usuario user = new Usuario();
		user.setId(java.util.UUID.fromString(userId));
		user.setEmail("elturcofarid@gmail.com");
		user.setUsername("elturcofarid");
		String profileId = "f7fb2a99-d1dd-4da6-84e1-2d7205f98782";
		String token = jwtProvider.generateNonExpiringToken(user, profileId);
		System.out.println("Non-expiring JWT: " + token);
	}

}
