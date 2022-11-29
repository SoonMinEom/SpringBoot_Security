package com.hospital.review.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
    public static String createToken(String userName, String key, long expireTime) {
        Claims clams = Jwts.claims();
        clams.put("userName",userName);
        return Jwts.builder()
                .setClaims(clams)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expireTime))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
}
