// package com.wecp.progressive.jwt;

// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.Collection;

// import javax.servlet.FilterChain;
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;

// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.AuthorityUtils;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import io.jsonwebtoken.Claims;

// @Component
// public class JwtRequestFilter extends OncePerRequestFilter {
//     private final UserDetailsService userDetailsService;
//     private final JwtUtil jwtUtil;

//     public JwtRequestFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
//         this.userDetailsService = userDetailsService;
//         this.jwtUtil = jwtUtil;
//     }

//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//             throws ServletException, IOException {
//         final String authorization = request.getHeader("Authorization");
//         if (authorization==null || authorization.isEmpty() || !authorization.startsWith("Bearer")) {
//             filterChain.doFilter(request, response);
//             return;
//         }

//         final String token = authorization.substring(7);
//         String username = jwtUtil.extractUsername(token);
//         UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//         if (!jwtUtil.validateToken(token, userDetails)) {
//             filterChain.doFilter(request, response);
//             return;
//         }
//         else {
//             Claims claims = jwtUtil.extractAllClaims(token);
//             Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList((String) claims.get("role"));
//             UsernamePasswordAuthenticationToken uPass = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
//             SecurityContextHolder.getContext().setAuthentication(uPass);;
//             filterChain.doFilter(request, response);
//         }
//     } 
// }

package com.wecp.progressive.jwt;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtRequestFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
                     SignatureException | IllegalArgumentException e) {
                // Handle invalid tokens here if needed
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                Claims claims = jwtUtil.extractAllClaims(jwt);
                Collection<? extends GrantedAuthority> authorities =
                        AuthorityUtils.createAuthorityList((String) claims.get("role"));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}