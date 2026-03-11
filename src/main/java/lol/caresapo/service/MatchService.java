package lol.caresapo.service;

import java.util.List;

import lol.caresapo.dto.MatchDto;

public interface MatchService {

	List<MatchDto> getMatches(String server);
}
