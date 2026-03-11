package lol.caresapo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lol.caresapo.dto.LeagueDto;
import lol.caresapo.dto.ProfileDto;
import lol.caresapo.service.LeagueService;
import lol.caresapo.util.AppConstants;

@Service
public class LeagueServiceImpl implements LeagueService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${riot.api.key}")
    private String apiKey;

	@Override
	@Cacheable(value = "leagueCache", key = "#server")
	public List<LeagueDto> getLeague(String server) {
		
		ProfileDto profile = this.getProfile(server);
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("X-Riot-Token", apiKey);
	    HttpEntity<Void> entity = new HttpEntity<>(headers);
	    
        String leagueUrl = String.format(AppConstants.API_URL, profile.getPlatform()) +
                "/lol/league/v4/entries/by-puuid/" + profile.getPuuid();

        ResponseEntity<List<LeagueDto>> leagueRes =
                restTemplate.exchange(leagueUrl, HttpMethod.GET, entity, 
                		new ParameterizedTypeReference<List<LeagueDto>>() {});
	    
        return leagueRes.getBody();
	
	}
	
	private ProfileDto getProfile(String server) {
		
		return AppConstants.PROFILES.stream()
				.filter(p -> p.getServer().equals(server)).findFirst().get();
	}
}
