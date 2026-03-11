package lol.caresapo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lol.caresapo.dto.SummonerDetailDto;
import lol.caresapo.service.SummonerService;

@RestController
@RequestMapping("/api/summoner")
public class SummonerController {

	private final SummonerService summonerService;
	
	public SummonerController(SummonerService summonerService) {
		this.summonerService = summonerService;
	}
	
    @GetMapping("/{server}")
    public SummonerDetailDto getPlayer(@PathVariable String server) {
    	
        return summonerService.getSummoner(server);
    }
}
