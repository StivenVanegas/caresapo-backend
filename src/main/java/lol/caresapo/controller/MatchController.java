package lol.caresapo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lol.caresapo.dto.MatchDto;
import lol.caresapo.service.MatchService;

@RestController
@RequestMapping("/api/match")
public class MatchController {

	private final MatchService matchService;
	
	public MatchController(MatchService matchService) {
		this.matchService = matchService;
	}
	
    @GetMapping("/{server}")
    public List<MatchDto> getMatches(@PathVariable String server) {
    	
    	return matchService.getMatches(server);
    }
}
