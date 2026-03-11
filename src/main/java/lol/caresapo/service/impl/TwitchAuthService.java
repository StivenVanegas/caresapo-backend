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
}
