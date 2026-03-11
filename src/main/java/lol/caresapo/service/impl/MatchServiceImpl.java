package lol.caresapo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lol.caresapo.dto.MatchDto;
import lol.caresapo.dto.ParticipantDto;
import lol.caresapo.dto.ProfileDto;
import lol.caresapo.service.MatchService;
import lol.caresapo.util.AppConstants;

@Service
public class MatchServiceImpl implements MatchService {

    private final RestTemplate restTemplate = new RestTemplate();
    
    private final ChampionService championService;
    
    private final SummonerSpellService summonerSpellService;
    
    @Value("${riot.api.key}")
    private String apiKey;
    
    public MatchServiceImpl(ChampionService championService, SummonerSpellService summonerSpellService) {
    	this.championService = championService;
    	this.summonerSpellService = summonerSpellService;
    }
    
    @Cacheable(value = "matchCache", key = "#server")
    public List<MatchDto> getMatches(String server) {
    	
		ProfileDto profile = this.getProfile(server);
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("X-Riot-Token", apiKey);
	    HttpEntity<Void> entity = new HttpEntity<>(headers);
	    
        String leagueUrl = String.format(AppConstants.API_URL, profile.getRegion()) +
                "/lol/match/v5/matches/by-puuid/" + profile.getPuuid() + 
                "/ids?queue=420&type=ranked&start=0&count=5";

        ResponseEntity<List<String>> response =
                restTemplate.exchange(leagueUrl, HttpMethod.GET, entity, 
                		new ParameterizedTypeReference<List<String>>() {});
        
        List<MatchDto> matches = new ArrayList<>();
        
        for (String idMatch : response.getBody()) {
			
        	matches.add(this.getMatch(idMatch,profile));
		}
        
        return matches;
        
    }
    
    private MatchDto getMatch(String id, ProfileDto profile) {
    	
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("X-Riot-Token", apiKey);
	    HttpEntity<Void> entity = new HttpEntity<>(headers);

        String matchUrl = String.format(AppConstants.API_URL, profile.getRegion()) +
                "/lol/match/v5/matches/" + id;

        ResponseEntity<MatchDto> matchRes =
                restTemplate.exchange(matchUrl, HttpMethod.GET, entity, MatchDto.class);
        
	    Map<Long, String> map = championService.getChampionMap();
	    Map<Integer, String> mapSpell = summonerSpellService.getSpellMap();
	    
	    for (ParticipantDto participant : matchRes.getBody().getInfo().getParticipants()) {
			
	    	participant.setChampionImageUrl(this.getChampionImage(map, participant.getChampionId()));
	    	participant.setSummoner1Img(this.getSpellImage(mapSpell, participant.getSummoner1Id()));
	    	participant.setSummoner2Img(this.getSpellImage(mapSpell, participant.getSummoner2Id()));
		}
    	
    	return matchRes.getBody();
    }
    
	private ProfileDto getProfile(String server) {
		
		return AppConstants.PROFILES.stream()
				.filter(p -> p.getServer().equals(server)).findFirst().get();
	}
	
	private String getChampionImage(Map<Long, String> map, Long championId) {

	    String championName = map.get(championId);

	    return "https://ddragon.leagueoflegends.com/cdn/16.5.1/img/champion/"
	            + championName + ".png";
	}
	
	public String getSpellImage(Map<Integer, String> map, int spellId) {

	    String spellName = map.get(spellId);

	    return "https://ddragon.leagueoflegends.com/cdn/16.5.1/img/spell/"
	            + spellName + ".png";
	}
}
