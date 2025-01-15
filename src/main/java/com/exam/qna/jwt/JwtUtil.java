package com.exam.qna.jwt;


import com.exam.qna.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtUtil {

    // 1. JWT 데이터

    public static String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";

    private final long TOKEN_TIME = 60 * 60 * 1000L;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init(){
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 2. JWT 생성
    public String createToken(String username, UserRole role){
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }


    // 3. 생성된 JWT 를 Cookie 에 저장
    public void addJwtCookie(String token, HttpServletResponse res){
        try{
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+","%20");

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token);
            cookie.setPath("/");

            res.addCookie(cookie);
        }catch (UnsupportedEncodingException e){
            logger.error(e.getMessage());
        }
    }

    // 4. Cookie 에 들어있던 JWT 토큰 substring
    public String substringToken(String tokenValue){
        if(StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)){
            return tokenValue.substring(7);
        }
        logger.error("NOT Found Token");
        throw  new NullPointerException("Not Found Token");
    }

    // 5. JWT 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 6. JWT 에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // 7. HttpServletRequest 에서 Cookie  JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req){
        Cookie[] cookies = req.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(AUTHORIZATION_HEADER)){
                    try{
                        return URLDecoder.decode(cookie.getValue(), "UTF-8");
                    }catch (UnsupportedEncodingException e){
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
