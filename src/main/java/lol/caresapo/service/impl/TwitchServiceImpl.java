package lol.caresapo.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lol.caresapo.dto.TwitchStreamResponse;
import lol.caresapo.dto.TwitchUser;
import lol.caresapo.dto.TwitchUserResponse;
import lol.caresapo.service.TwitchService;

@Service
public class TwitchServiceImpl implements TwitchService {

	@Value("${twitch.client.id}")
    private String clientId;

    private final RestTemplate restTemplate = new RestTemplate();
    private final TwitchAuthService authService;

    public TwitchServiceImpl(TwitchAuthService authService) {
        this.authService = authService;
    }

    @Override
    @Cacheable(value = "twitchCache", key = "#username")
    public TwitchUser isStreamerLive(String username) {

        String token = authService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientId);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://api.twitch.tv/helix/streams?user_login=" + username;

        ResponseEntity<TwitchStreamResponse> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        TwitchStreamResponse.class
                );

        Boolean online =  response.getBody() != null &&
               !response.getBody().getData().isEmpty();
        
        TwitchUser twitchUser = this.getTwitchUser(token, username);
        twitchUser.setOnline(online);
        
        this.getChannel(twitchUser.getId(), token, twitchUser);
        
        return twitchUser;
    }
    
    private TwitchUser getTwitchUser(String token, String username) {
    	
        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientId);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        String url = "https://api.twitch.tv/helix/users?login=" + username;
        
        ResponseEntity<TwitchUserResponse> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        TwitchUserResponse.class
                );
        
        return response.getBody().getData().get(0);
    	
    }
    
    private void getChannel(String id, String token,TwitchUser twitchUser) {
    	
        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientId);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        String url = "https://api.twitch.tv/helix/channels?broadcaster_id=" + id;
        
        ResponseEntity<TwitchUserResponse> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        TwitchUserResponse.class
                );
        TwitchUser channelInfo = response.getBody().getData().get(0);
        
        twitchUser.setGameName(channelInfo.getGameName());
        twitchUser.setTitle(channelInfo.getTitle());

    }
}
