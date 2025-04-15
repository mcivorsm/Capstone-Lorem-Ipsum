package learn.ligr.security;

import learn.ligr.data.ProfileRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import learn.ligr.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtConverter {

    // 1. Signing key
    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // 2. "Configurable" constants
    private final String ISSUER = "bug-safari";
    private final int EXPIRATION_MINUTES = 15;
    private final int EXPIRATION_MILLIS = EXPIRATION_MINUTES * 60 * 1000;

    @Autowired
    ProfileRepository profileRepository;

    public String getTokenFromUser(User user) {

        String authorities = user.getAuthorities().stream()
                .map(i -> i.getAuthority())
                .collect(Collectors.joining(","));

        // 3. Use JJWT classes to build a token.
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(user.getUsername())
                .claim("profileId", user.getProfile().getProfileId())
                .claim("authorities", authorities)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))
                .signWith(key)
                .compact();
    }

    public User getUserFromToken(String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        try {
            // 4. Use JJWT classes to read a token.
            Jws<Claims> jws = Jwts.parserBuilder()
                    .requireIssuer(ISSUER)
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.substring(7));


            //JWT TOKEN CONTENTS
            String username = jws.getBody().getSubject();
            String authStr = (String) jws.getBody().get("authorities");
            int userId = (int) jws.getBody().get("userId");
            int profileId = (int) jws.getBody().get("profileId");

            String email = (String) jws.getBody().get("email");
            List<GrantedAuthority> authorities = Arrays.stream(authStr.split(","))
                    .map(i -> new SimpleGrantedAuthority(i))
                    .collect(Collectors.toList());

          boolean adminFlag = authorities.get(0).equals("ROLE_ADMIN");

          //START PROFILE ASSIGNMENT HERE?
          return new User(userId, profileRepository.findById(profileId), username, email, adminFlag);

        } catch (JwtException e) {
            // 5. JWT failures are modeled as exceptions.
            System.out.println(e);
        }

        return null;
    }
}