package lol.caresapo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lol.caresapo.dto.TwitchUser;
import lol.caresapo.service.TwitchService;

@RestController
@RequestMapping("/api/twitch")
public class TwitchController {
	
	private final TwitchService twitchService;
	
	public TwitchController(TwitchService twitchService) {
		this.twitchService = twitchService;
	}
	
    @GetMapping("/live/{username}")
    public TwitchUser isLive(@PathVariable String username) {
        return twitchService.isStreamerLive(username);
    }

}
