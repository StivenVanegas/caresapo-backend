package lol.caresapo.service;

import java.util.List;

import lol.caresapo.dto.LeagueDto;

public interface LeagueService {

	List<LeagueDto> getLeague(String server);
}
