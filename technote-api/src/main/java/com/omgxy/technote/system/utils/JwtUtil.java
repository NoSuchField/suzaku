package com.omgxy.technote.system.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtUtil {

    private static final String SECRET = "!Q@W#E$R^Y&U";
    //token签发者
    private static final String ISSUERS = "HZSTYGZPT";
    //token过期时间
    public static final Long EXPIRE_DATE = 1000 * 60L;

    public static String createToken() {
        //当前时间
        Date now = new Date();
        //创建过期时间
        Calendar instance = Calendar.getInstance();
        instance.setTime(now);
        instance.add(Calendar.DATE, 7);  //7天过期
        //1. header
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT.create()
                .withIssuer(ISSUERS)
                .withIssuedAt(now)
                .withExpiresAt(instance.getTime())
                .sign(algorithm);
    }

    public static String createToken(Map<String, String> map) {
        //创建过期时间
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 7);  //7天过期

        //创建builder对象
        JWTCreator.Builder builder = JWT.create();
        //遍历map
        map.forEach(builder::withClaim);
        return builder.withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(SECRET));
    }

    public static boolean verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        try {
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            jwtVerifier.verify(token);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("auth failed");
        }
        return true;
    }

    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }
}

