package lol.caresapo.service;

import lol.caresapo.dto.TwitchUser;

public interface TwitchService {

	TwitchUser isStreamerLive(String username);
}
