package lol.caresapo.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lol.caresapo.dto.CurrentGameDto;
import lol.caresapo.dto.CurrentGameParticipantDto;
import lol.caresapo.dto.ProfileDto;
import lol.caresapo.service.SpectatorService;
import lol.caresapo.util.AppConstants;

@Service
public class SpectatorServiceImpl implements SpectatorService {

    private final RestTemplate restTemplate = new RestTemplate();
    
    private final ChampionService championService;
    
    @Value("${riot.api.key}")
    private String apiKey;
    
    public SpectatorServiceImpl(ChampionService championService) {
    	this.championService = championService;
    }
	
	@Override
	@Cacheable("spectatorCache")
	public CurrentGameDto getCurrentGame() {
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("X-Riot-Token", apiKey);
	    HttpEntity<Void> entity = new HttpEntity<>(headers);
	    
	    CurrentGameDto currentGame = null;
	    
	    for (ProfileDto profile : AppConstants.PROFILES) {
	    	
	        String spectatorUrl = String.format(AppConstants.API_URL, profile.getPlatform()) +
	                "/lol/spectator/v5/active-games/by-summoner/" + profile.getPuuid();
	        
	        try {

		        ResponseEntity<CurrentGameDto> gameRes =
		                restTemplate.exchange(spectatorUrl, HttpMethod.GET, entity, CurrentGameDto.class);
		        
		        currentGame = gameRes.getBody();
		        break;
		        
	        } catch (HttpClientErrorException e) {
	        	currentGame = null;
			}
	        
		}
	    
	    if(currentGame == null) {
	    	throw new RuntimeException("NOT FOUND");
	    }
	    
	    Map<Long, String> map = championService.getChampionMap();
	    
	    for (CurrentGameParticipantDto participant : currentGame.getParticipants()) {
			
	    	participant.setChampionImageUrl(this.getChampionImage(map, participant.getChampionId()));
		}
	    
		return currentGame;
	}
	
	private String getChampionImage(Map<Long, String> map, Long championId) {

	    String championName = map.get(championId);

	    return "https://ddragon.leagueoflegends.com/cdn/16.5.1/img/champion/"
	            + championName + ".png";
	}

}
