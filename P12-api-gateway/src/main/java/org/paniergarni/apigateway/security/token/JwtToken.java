package org.paniergarni.apigateway.security.token;

import io.jsonwebtoken.*;
import org.paniergarni.apigateway.security.exception.JwtExpiredTokenException;
import org.springframework.security.authentication.BadCredentialsException;


/**
 * Représention d'un JWT pour l'authentification
 * 
 * @author pichat morgan
 *
 * 20 Juillet 2019
 *
 */
public class JwtToken {

	private String token;

	public JwtToken(String token) {
		super();
		this.token = token;
	}

	public String getToken() {		
		return this.token;
	}
	
	 /**
     * Parses and validates JWT Token signature.
     * 
     * @throws BadCredentialsException
     * @throws JwtExpiredTokenException
     * 
     */
    public Jws<Claims> parseClaims(String token, String secret) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            throw new BadCredentialsException("jwt.invalid");
        } catch (ExpiredJwtException expiredEx) {       
            throw new JwtExpiredTokenException( "jwt.expired");
        }
    }
    
   
}
