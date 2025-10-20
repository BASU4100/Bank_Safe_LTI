// package com.wecp.progressive.jwt;

// // import java.security.Key;
// import java.util.Date;
// // import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.stereotype.Component;

// import com.wecp.progressive.exception.AccountNotFoundException;
// import com.wecp.progressive.repository.CustomerRepository;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;

// @Component
// public class JwtUtil {
//     @Autowired
//     private CustomerRepository customerRepository;

//     private final String secret = "HelloWorld+is+not+same+in+all+languages+it+is+different+in+programming.";
//     private final int expiration = 86400*60;

//     public String generateToken(String username) {
//         if (customerRepository.findByUsername(username)==null) {
//             throw new AccountNotFoundException("Account does not exist with username: "+username);
//         }
//         return Jwts.builder()
//                     .setSubject(username)
//                     .setIssuedAt(new Date(System.currentTimeMillis()))
//                     .setExpiration(new Date(System.currentTimeMillis()+expiration))
//                     .signWith(SignatureAlgorithm.HS256, secret)
//                     .compact();
//     }

//     public Claims extractAllClaims(String token) {
//         return Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).getBody();
//     }

//     public String extractUsername(String token) {
//         Claims claim = extractAllClaims(token);
//         return claim.getSubject();
//     }

//     public boolean isTokenExpired(String token) {
//         Claims claim = extractAllClaims(token);
//         return claim.getExpiration().before(new Date(System.currentTimeMillis()));
//     }

//     public boolean validateToken(String token, UserDetails userDetails) {
//         return (userDetails!=null && extractUsername(token).equalsIgnoreCase(userDetails.getUsername()) && isTokenExpired(token));
//     }
// }

package com.wecp.progressive.jwt;


import com.wecp.progressive.entity.Customers;
import com.wecp.progressive.repository.CustomerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private CustomerRepository customerRepository;

    @Autowired
    public JwtUtil(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    private final String secret = "secretKey000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

    private final int expiration = 86400;

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);
        Customers user = customerRepository.findByUsername(username);

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);

        // Assign role based on user type
        claims.put("role", user.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expirationDate.before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}