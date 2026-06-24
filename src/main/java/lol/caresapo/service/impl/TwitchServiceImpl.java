package lol.caresapo.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
    
    private final String broadcasterId = "149107396";

    private final String moderatorId = "261639224";

    public TwitchServiceImpl(TwitchAuthService authService) {
        this.authService = authService;
    }
    
    private final Random random = new Random();

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
    
    @Override
    public String timeoutRandomChatter() {
    	//String token = authService.getAccessToken();
        HttpHeaders headers = buildHeaders();

        // 1. Obtener chatters
        String chattersUrl = "https://api.twitch.tv/helix/chat/chatters"
            + "?broadcaster_id=" + broadcasterId
            + "&moderator_id=" + moderatorId;

        ResponseEntity<Map> chattersResponse = restTemplate.exchange(
            chattersUrl,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            Map.class
        );

        List<Map<String, String>> chatters =
            (List<Map<String, String>>) chattersResponse.getBody().get("data");

        if (chatters == null || chatters.isEmpty()) {
            return "No hay chatters disponibles.";
        }

        // Filtrar al moderador/bot de la lista
        chatters = chatters.stream()
            .filter(c -> !c.get("user_id").equals(moderatorId))
            .toList();

        // 2. Elegir uno al azar
        Map<String, String> chosen = chatters.get(random.nextInt(chatters.size()));
        String userId = chosen.get("user_id");
        String userName = chosen.get("user_login");

        // 3. Aplicar timeout de 10 segundos
        String banUrl = "https://api.twitch.tv/helix/moderation/bans"
            + "?broadcaster_id=" + broadcasterId
            + "&moderator_id=" + moderatorId;

        Map<String, Object> banBody = Map.of(
            "data", Map.of(
                "user_id", userId,
                "duration", 300,
                "reason", "¡Seleccionado por el bot del canal! 🎰"
            )
        );

        restTemplate.exchange(
            banUrl,
            HttpMethod.POST,
            new HttpEntity<>(banBody, headers),
            Void.class
        );

        // 4. Anunciar en el chat
        //sendChatMessage("@" + userName + " ha sido alcanzado por una bala perdida o7");

        return userName;
    }

    private void sendChatMessage(String message) {
        HttpHeaders headers = buildHeaders();

        Map<String, String> body = Map.of(
            "broadcaster_id", broadcasterId,
            "sender_id", moderatorId,
            "message", message
        );

        restTemplate.exchange(
            "https://api.twitch.tv/helix/chat/messages",
            HttpMethod.POST,
            new HttpEntity<>(body, headers),
            Void.class
        );
    }

    private HttpHeaders buildHeaders() {
        // Usar getUserAccessToken() en vez del token de client credentials
        String token = authService.getUserAccessToken();

        if (token == null) {
            throw new IllegalStateException(
                "No hay token de usuario. Visita /twitch/authorize primero."
            );
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Client-Id", clientId);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
