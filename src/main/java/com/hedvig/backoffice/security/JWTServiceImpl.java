package com.hedvig.backoffice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class JWTServiceImpl implements JWTService {

    @Value("${jwt.expiration:864000000}")
    private long expiration;

    @Value("${jwt.secret:secret}")
    private String secret;

    @Value("${jwt.header:Authorization}")
    private String header;

    @Override
    public JWTDTO createTokenForUser(String email) {
        String jwtToken = Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        return new JWTDTO(email, jwtToken);
    }

    @Override
    public void addTokenToHeader(String jwtToken, HttpServletResponse response) {
        response.addHeader(header, jwtToken);
    }

    @Override
    public String resolveUserFromToken(HttpServletRequest request) throws JWTInvalidTokenException {
        String token = request.getHeader(header);
        if (StringUtils.isNotBlank(token)) {
            try {
                return Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();
            } catch (Exception ex) {
                throw new JWTInvalidTokenException(ex.getMessage());
            }
        }

        return null;
    }

}
