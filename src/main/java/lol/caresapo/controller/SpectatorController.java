package lol.caresapo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lol.caresapo.dto.CurrentGameDto;
import lol.caresapo.service.impl.SpectatorServiceImpl;

@RestController
@RequestMapping("/api/spectator")
public class SpectatorController {
	
	private final SpectatorServiceImpl spectatorService;
	
	public SpectatorController(SpectatorServiceImpl spectatorService) {
		this.spectatorService = spectatorService;
	}
	
    @GetMapping()
    public CurrentGameDto getLeague() {
    	
        return spectatorService.getCurrentGame();
    }

}
