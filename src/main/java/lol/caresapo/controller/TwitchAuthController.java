package lol.caresapo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lol.caresapo.service.impl.TwitchAuthService;

@RestController
@RequestMapping("/twitch")
public class TwitchAuthController {

	private final TwitchAuthService twitchService;

    public TwitchAuthController(TwitchAuthService twitchService) {
        this.twitchService = twitchService;
    }

    // Visita este endpoint en el navegador para iniciar la autorización
    @GetMapping("/authorize")
    public ResponseEntity<Void> authorize() {
        String url = twitchService.buildAuthorizationUrl();
        return ResponseEntity.status(HttpStatus.FOUND)
            .header("Location", url)
            .build();
    }

    // Twitch redirige aquí automáticamente con el code
    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam String code) {
    	twitchService.exchangeCodeForToken(code);
        return ResponseEntity.ok("✅ Autorización exitosa. Ya puedes usar el bot.");
    }
}
