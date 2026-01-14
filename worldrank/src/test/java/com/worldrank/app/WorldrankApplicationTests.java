package com.worldrank.app;

import com.worldrank.app.auth.security.JwtProvider;
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
		String profileId = "f7fb2a99-d1dd-4da6-84e1-2d7205f98782";
		String token = jwtProvider.generateNonExpiringToken(userId, profileId);
		System.out.println("Non-expiring JWT: " + token);
	}

}
