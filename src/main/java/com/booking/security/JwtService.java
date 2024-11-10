package com.booking.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire.refresh}")

    private int refreshTokenDelay;
    @Value("${jwt.expire.access}")
    private int accesTokenDelay;



    public String buildAccesToken(UserDetails userDetails){
        return  buildToken(userDetails,accesTokenDelay);
    }
   public String buildRefreshToken(UserDetails userDetails){

        return buildToken(userDetails,refreshTokenDelay);
    }



public <T> T extractClaim(String token,Function<Claims,T> mapper){


        return mapper.apply(extractAllClaims(token));

}
public Date exractExpiration(String token){



        return extractClaim(token,Claims::getExpiration);

}
public String subject(String token){


        return extractClaim(token,Claims::getSubject);

}

public boolean isValidAndNoExpierd(UserDetails user,String token){

        return user.getUsername().equals(getSubject(token))&&getExpiredAt(token).after(new Date());

}
   public String getSubject(String token){

        return extractClaim(token,Claims::getSubject);

   }


   public Date getExpiredAt(String token){

        return extractAllClaims(token).getExpiration();
   }


    private Claims extractAllClaims(String token) {
        //THIS BY DEFAULT VALIDATE TOKEN
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

    }


  private String buildToken(UserDetails userDetails,int time){

        Map<String,Object> extraClaims=new HashMap();

        extraClaims.put("authorities",userDetails.getAuthorities());



        return  Jwts.builder().setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+time*60*1000L))
                .signWith(this.getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }


    private Key getSignInKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }



}

