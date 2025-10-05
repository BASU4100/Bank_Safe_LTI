package com.wecp.progressive.jwt;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorization = request.getHeader("Authorization");
        if (authorization==null || authorization.isEmpty() || !authorization.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authorization.substring(7);
        String username = jwtUtil.extractUsername(token);
        if (!jwtUtil.validateToken(token, userDetailsService.loadUserByUsername(username))) {
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken uPass = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
        uPass.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(uPass);;
        filterChain.doFilter(request, response);
    } 
}
