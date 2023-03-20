package bookstore.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    public static boolean allowRequest(HttpServletRequest request, String signingKey) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String role;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // yes, with the space after Bearer
            return false;
        }

        jwt = authHeader.substring(7); // 7 because "Bearer "
        Jws<Claims> claims;

        try {
            // parseJwt throws an exception if the token is invalid
            claims = parseJwt(jwt, signingKey);
        }catch (Exception e) {
            return false;
        }

        // if the token is valid, try to get the role from the token
        try {
            role = claims.getBody().get("role").toString();
        }catch (Exception e) {
           return false;
        }

        System.out.println(role);

        if (role.equals("ADMIN")) {
            return true;
        }else {
            return false;
        }
    }

    private static Jws<Claims> parseJwt(String token, String key) {
        try {
            Jws<Claims> jwt = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return jwt;
        }catch (Exception e) {
            throw e;
        }
    }
}
