package com.tweetapp.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class JWTUtil {

	public static final String SECRET_TOKEN = "sshNVBUSGBK12";

	public String createToken(String userName) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET_TOKEN);
			String token = JWT.create().withClaim("userId", userName)
					.withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 600)).sign(algorithm);
			return token;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JWTCreationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getUserfromToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET_TOKEN);
			JWTVerifier verifier = JWT.require(algorithm).build();
			DecodedJWT jwt = verifier.verify(token);
			return jwt.getClaim("userId").toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JWTCreationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Boolean isValidToken(String token) {
		String userId = this.getUserfromToken(token);
		return userId != null;
	}

}
