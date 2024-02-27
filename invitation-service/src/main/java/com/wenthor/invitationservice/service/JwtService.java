package com.wenthor.invitationservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${security.jwt.secret}")
    private String SECRET_KEY;

    private Key getKey() {
        byte [] key = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }

    private <T> T exportToken(String token, Function<Claims, T> claimsTFunction) {
        final JwtParser jwtParser = Jwts.parser().setSigningKey(getKey()).build();
        final Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claimsTFunction.apply(claims);
    }
    public  String findByAccountEmail(String token){
        return exportToken(token, Claims::getSubject);
    }
}
