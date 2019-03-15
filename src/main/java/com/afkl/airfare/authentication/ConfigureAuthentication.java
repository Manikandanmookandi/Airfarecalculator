package com.afkl.airfare.authentication;

import org.springframework.web.client.RestTemplate;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

public class ConfigureAuthentication {

	private static String accessTokenUri="http://localhost:8080/oauth/token";
	private static String oAuth2ClientId="travel-api-client";
	private static String oAuth2ClientSecret="psw";
	
	
	
	public RestTemplate oAuthRestTemplate() {
		System.out.println("in configure authentication");
	    ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();	    
	    resourceDetails.setGrantType("client_credentials");
	    resourceDetails.setClientId(oAuth2ClientId);
	    resourceDetails.setClientSecret(oAuth2ClientSecret);
	    resourceDetails.setAccessTokenUri(accessTokenUri);
	    
	    OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails, new DefaultOAuth2ClientContext());

	    return restTemplate;
	}
}
