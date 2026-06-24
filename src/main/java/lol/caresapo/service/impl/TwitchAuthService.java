package lol.caresapo.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TwitchAuthService {

	@Value("${twitch.client.id}")
    private String clientId;

    @Value("${twitch.client.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    private String accessToken;
    
    @Value("${twitch.redirect-uri}")
    private String redirectUri;
    
    private String userAccessToken;

    public String getAccessToken() {

        if (accessToken != null) {
            return accessToken;
        }

        String url = "https://id.twitch.tv/oauth2/token" +
                "?client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&grant_type=client_credentials";

        Map<?, ?> response = restTemplate.postForObject(url, null, Map.class);

        accessToken = (String) response.get("access_token");

        return accessToken;
    }
    
 // URL a la que debes redirigir al moderador para autorizar
    public String buildAuthorizationUrl() {
        return "https://id.twitch.tv/oauth2/authorize"
            + "?client_id=" + clientId
            + "&redirect_uri=" + redirectUri
            + "&response_type=code"
            + "&scope=moderator:read:chatters+moderator:manage:banned_users+user:write:chat";
    }

    // Intercambiar el code por un access_token de usuario
    public void exchangeCodeForToken(String code) {
        String url = "https://id.twitch.tv/oauth2/token"
            + "?client_id=" + clientId
            + "&client_secret=" + clientSecret
            + "&code=" + code
            + "&grant_type=authorization_code"
            + "&redirect_uri=" + redirectUri;

        Map response = restTemplate.postForObject(url, null, Map.class);
        userAccessToken = (String) response.get("access_token");
    }
    
    public String getUserAccessToken() {
        return userAccessToken;
    }
}
