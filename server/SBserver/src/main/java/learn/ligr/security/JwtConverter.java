package learn.ligr.security;

import learn.ligr.data.ProfileRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import learn.ligr.domain.ProfileService;
import learn.ligr.models.Profile;
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
    ProfileService profileService;

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
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))
                .signWith(key)
                .compact();
    }

    public User getUserFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        try {
            // Parse JWT token
            Jws<Claims> jws = Jwts.parserBuilder()
                    .requireIssuer(ISSUER)
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.substring(7));

            // Extract JWT token contents
            String username = jws.getBody().getSubject();
            String authStr = (String) jws.getBody().get("authorities");
            int userId = (int) jws.getBody().get("userId");
            int profileId = (int) jws.getBody().get("profileId");
            String email = (String) jws.getBody().get("email");

            // Convert the authorities string into a list of GrantedAuthorities
            List<GrantedAuthority> authorities = Arrays.stream(authStr.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // Check if the user has admin privileges
            boolean isAdmin = authorities.stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

            // Fetch profile details using profileId (ensure ProfileService is working as expected)
            Profile profile = profileService.findById(profileId);

            // Return the User object with the necessary information
            return new User(userId, profile, username, email, isAdmin);

        } catch (JwtException e) {
            // Handle JWT parsing exceptions
            System.out.println("Error parsing JWT: " + e.getMessage());
        }

        return null;
    }
}