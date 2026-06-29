package lol.caresapo.service.impl;

import java.time.LocalDateTime;
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
    
    private String refreshToken;
    private LocalDateTime tokenExpiration;

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
        if (isTokenExpired()) {
            refreshAccessToken();
        }
        return userAccessToken;
    }
    
    private void refreshAccessToken() {
        if (refreshToken == null) {
            throw new IllegalStateException(
                "No hay refresh token. Visita /twitch/authorize nuevamente."
            );
        }

        String url = "https://id.twitch.tv/oauth2/token"
            + "?client_id=" + clientId
            + "&client_secret=" + clientSecret
            + "&refresh_token=" + refreshToken
            + "&grant_type=refresh_token";

        Map response = restTemplate.postForObject(url, null, Map.class);
        saveTokenData(response);
    }

    private void saveTokenData(Map response) {
        userAccessToken = (String) response.get("access_token");
        refreshToken = (String) response.get("refresh_token");

        // Twitch devuelve expires_in en segundos (normalmente 14400 = 4 horas)
        int expiresIn = (int) response.get("expires_in");

        // Guardamos con 5 minutos de margen para renovar antes de que expire
        tokenExpiration = LocalDateTime.now().plusSeconds(expiresIn).minusMinutes(5);
    }

    private boolean isTokenExpired() {
        return userAccessToken == null
            || tokenExpiration == null
            || LocalDateTime.now().isAfter(tokenExpiration);
    }
}
