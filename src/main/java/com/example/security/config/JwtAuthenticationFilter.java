package com.example.security.config;

import com.example.security.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor // use to create a constructor for final fields
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  @Autowired
  private final JwtService jwtService;

  private UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    //The first step we do is checking whether the token is existed or not
    final String authHeader = request.getHeader("authorization");
    logger.info("authHeader value: " + authHeader);
    final String jwt;
    final String userEmail;
    if (authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    jwt = authHeader.substring(7);
    userEmail = jwtService.extractUsername(jwt);
    if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
      if(jwtService.isTokenValid(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        ); //This object is needed for spring and SecurityContextHolder to verify whether the user is logged yet
         authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
         logger.info("The information inside authToken details: " + authToken.getDetails());
         SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request,response);
  }
}
