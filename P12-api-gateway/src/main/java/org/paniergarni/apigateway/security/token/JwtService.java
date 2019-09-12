package org.paniergarni.apigateway.security.token;


import org.paniergarni.apigateway.security.auth.model.UserContext;

/**
 * Création, modification, et vérification de JWT
 * 
 * 
 * @author pichat morgan
 *
 * 20 Juillet 2019
 *
 */
public interface JwtService {
	
 /**
  * Création d'un token d'authentification
  * 
  * @param userContext --> pour les informations à injecté dans le token (username, liste de role)
  * @return token 
  */
  String createAuthToken(UserContext userContext);
 
 /**
  * Création d'un token de rafraichissement
  * 
  * @param userContext --> pour les informations à injecté dans le token (username)
  * @return token
  */
  String createRefreshToken(UserContext userContext);
 
 /**
  * Vérification de conformité d'un refresh token
  * 
  * @param token --> token à vérifier
  * @return claims
  */
  UserContext validateRefreshToken(JwtToken token);
 
 /**
  * Retire le préfixe précédent le token
  * 
  * @param header --> header complet du token (avec préfix)
  * @return token (sans préfix)
  */
  String extract(String header);
}
