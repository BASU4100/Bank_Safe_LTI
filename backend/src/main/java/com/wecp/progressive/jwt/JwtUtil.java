package com.wecp.progressive.jwt;

// import java.security.Key;
import java.util.Date;
// import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.wecp.progressive.exception.AccountNotFoundException;
import com.wecp.progressive.repository.CustomerRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
    @Autowired
    private CustomerRepository customerRepository;

    private final String secret = "HelloWorld_is_not_same_in_all_languages_it_is_different_in_programming.";
    private final int expiration = 86400;

    public String generateToken(String username) {
        if (customerRepository.findByUsername(username)==null) {
            throw new AccountNotFoundException("Account does not exist with username: "+username);
        }
        return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis()+expiration))
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).getBody();
    }

    public String extractUsername(String token) {
        Claims claim = extractAllClaims(token);
        return claim.getSubject();
    }

    public boolean isTokenExpired(String token) {
        Claims claim = extractAllClaims(token);
        return claim.getExpiration().before(new Date(System.currentTimeMillis()));
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return (userDetails!=null && extractUsername(token).equalsIgnoreCase(userDetails.getUsername()) && isTokenExpired(token));
    }
}