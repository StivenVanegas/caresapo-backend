package lol.caresapo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lol.caresapo.dto.LeagueDto;
import lol.caresapo.service.LeagueService;

@RestController
@RequestMapping("/api/league")
public class LeagueController {

	private final LeagueService leagueService;
	
	public LeagueController(LeagueService leagueService) {
		this.leagueService = leagueService;
	}
	
    @GetMapping("/{server}")
    public List<LeagueDto> getLeague(@PathVariable String server) {
    	
        return leagueService.getLeague(server);
    }
}
