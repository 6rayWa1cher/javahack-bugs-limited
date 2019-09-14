package com.a6raywa1cher.javahackbugslimited.component;

import com.a6raywa1cher.javahackbugslimited.config.AppConfigProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtComponent {
	private static final String ISSUER = "javahack";
	private final JWTVerifier verifier;
	private final Algorithm algorithm;

	@Autowired
	public JwtComponent(AppConfigProperties properties) {
		this.algorithm = Algorithm.HMAC256(properties.getJwtTokenKey());
		this.verifier = JWT.require(algorithm)
				.withIssuer(ISSUER)
				.build();
	}

	public String create(String purpose, String additionalInformation) {
		try {
			UUID uuid = UUID.randomUUID();

			LocalDateTime now = LocalDateTime.now();
			LocalDateTime exp = now.plusMonths(1);

			Date issuedDate = new Date();
			issuedDate.setTime(Timestamp.valueOf(now.minusSeconds(1)).getTime());

			Date expDate = new Date();
			expDate.setTime(Timestamp.valueOf(exp).getTime());

			String token = JWT.create()
					.withIssuer(ISSUER)
					.withIssuedAt(issuedDate)
					.withNotBefore(issuedDate)
					.withExpiresAt(expDate)
					.withAudience(purpose)
					.withClaim("ai", additionalInformation)
					.withJWTId(uuid.toString())
					.sign(algorithm);
			return token;
		} catch (JWTCreationException e) {
			return null;
		}
	}

	public Optional<String> decode(String purpose, String token) {
		try {
			DecodedJWT jwtDecoded = JWT.require(algorithm)
					.withIssuer(ISSUER)
					.withAudience(purpose)
					.build()
					.verify(token);
			return Optional.of(jwtDecoded.getClaim("ai").asString());
		} catch (JWTVerificationException e) {
			return Optional.empty();
		}
	}
}
