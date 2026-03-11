package lol.caresapo.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lol.caresapo.dto.ProfileDto;
import lol.caresapo.dto.SummonerDetailDto;
import lol.caresapo.dto.SummonerDto;
import lol.caresapo.dto.AccountDto;
import lol.caresapo.service.LeagueService;
import lol.caresapo.service.SummonerService;
import lol.caresapo.util.AppConstants;

@Service
public class SummonerServiceImpl implements SummonerService{

    private final RestTemplate restTemplate = new RestTemplate();
    private final LeagueService leagueService;

    @Value("${riot.api.key}")
    private String apiKey;
    
    public SummonerServiceImpl(LeagueService leagueService) {
    	this.leagueService = leagueService;
    }
    
	@Override
	@Cacheable(value = "summonerCache", key = "#server")
	public SummonerDetailDto getSummoner(String server) {
		
		ProfileDto profile = this.getProfile(server);
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("X-Riot-Token", apiKey);
	    HttpEntity<Void> entity = new HttpEntity<>(headers);
	    
	    SummonerDetailDto summoner = new SummonerDetailDto();

        String accountUrl = String.format(AppConstants.API_URL, profile.getRegion()) +
                "/riot/account/v1/accounts/by-puuid/" + profile.getPuuid();

        ResponseEntity<AccountDto> accountRes =
                restTemplate.exchange(accountUrl, HttpMethod.GET, entity, AccountDto.class);
        
        summoner.setPuuid(accountRes.getBody().getPuuid());
        summoner.setGameName(accountRes.getBody().getGameName());
        summoner.setTagLine(accountRes.getBody().getTagLine());
        
        this.getSummonerInfo(profile,summoner);
        
        summoner.setLeagues(this.leagueService.getLeague(server));
        
        return summoner;
        
	}
	
	private void getSummonerInfo(ProfileDto profile, SummonerDetailDto summoner) {
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("X-Riot-Token", apiKey);
	    HttpEntity<Void> entity = new HttpEntity<>(headers);

        String summonerUrl = String.format(AppConstants.API_URL, profile.getPlatform()) +
                "/lol/summoner/v4/summoners/by-puuid/" + profile.getPuuid();

        ResponseEntity<SummonerDto> summonerRes =
                restTemplate.exchange(summonerUrl, HttpMethod.GET, entity, SummonerDto.class);
        
        summoner.setProfileIconId(summonerRes.getBody().getProfileIconId());
        summoner.setSummonerLevel(summonerRes.getBody().getSummonerLevel());
		
	}

	private ProfileDto getProfile(String server) {
		
		return AppConstants.PROFILES.stream()
				.filter(p -> p.getServer().equals(server)).findFirst().get();
	}
	
}
