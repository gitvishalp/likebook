package com.likebook.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;

@Component
public class JWTTokenUtil {

	public static final long JWT_TOKEN_VALIDITY = 60 * 60;

    private static String ENV;
    private static String JWT_SECRET = "lb[isfd-8zK9-7j2314dk{}+Qjnli;;cd0";
    
    public JWTTokenUtil(@NotNull Environment environment) {
        super();
        if (environment.getActiveProfiles().length > 0) {
            ENV = environment.getActiveProfiles()[0];
        } else {
            ENV = "";
        }
    }
    
    public String generateToken(String email, String userId, String phoneNumber, String password,String IpAddress) {
    	
    	Map<String, Object> claims = new HashMap<String, Object>();
    	claims.put("Email", email);
    	claims.put("UserId", userId);
    	claims.put("PhoneNumber", phoneNumber);
    	claims.put("Password", password);
    	claims.put("IpAddress", IpAddress);
    	claims.put("Env", ENV);
    	
    	String localSecret = ENV + JWT_SECRET;
    	
    	return Jwts.builder().setClaims(claims).setSubject(userId).setIssuedAt(new Date(System.currentTimeMillis()))
    			.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(Keys.hmacShaKeyFor(localSecret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
    
    public static boolean validateToken(String token) {
        try {
            // Check the token environment received
            String tokenEnv = getDataFromToken(token).getString("Env");
            if (tokenEnv == null || tokenEnv.trim().isEmpty() || !ENV.equals(tokenEnv)) {
                return false;
            }

            String localSecret = tokenEnv + JWT_SECRET;
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(localSecret.getBytes(StandardCharsets.UTF_8))).build().parseClaimsJws(token).getBody();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static JSONObject getDataFromToken(String token) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] parts = token.split("\\.");
        if (parts.length < 2) {
            return new JSONObject();
        }
        return new JSONObject(new String(decoder.decode(parts[1])));
    }
    
    public static String getUserIdFromToken(String token) {
        JSONObject jsonObject = getDataFromToken(token);
        return jsonObject.get("sub").toString();
    }
    
    public static String getEmailFromToken(String token) {
    	JSONObject jsonObject = getDataFromToken(token);
    	return jsonObject.getString("Email");
    }
}
