package com.example.security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private static final String SECRET_KEY =
      "1PEp8SmRe/esUViJkusdUZP1rpHzp/RDbdKyCnvUbZAPy25S52iMRVOGUzZd25FY3f8K3lct3bGKCpIZfC/igQ==";

  public String extractUsername(String jwt) {

    return extractClaim(jwt, Claims::getSubject);
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

   /*
   Hàm này để lấy ra từng phần của claims (Claims một khối các dữ liệu trong token),
   Hàm này nhận 2 parameter là token và Function<Claims,T>
  */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims); //Hàm này có chức năng áp dụng cái function được truyền vào từ argument để xử lí claim
  }                                     // hàm được áp dụng cụ thể ở đây là Claims::getSubject


  //Hàm này lấy tất cả các claims trong token
  public Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSignInKey()) //Sign in key là một cách thức để lưu dữ liệu trong quá trình xử lý cũng như là để bảo mật
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    String username = extractUsername(token);
    return userDetails.getUsername().equals(username) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }


}
