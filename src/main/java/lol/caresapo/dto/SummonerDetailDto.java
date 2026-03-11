package lol.caresapo.dto;

import java.util.List;

import lombok.Data;

@Data
public class SummonerDetailDto {

	private String puuid;
	private String gameName;
	private String tagLine;
	private Integer profileIconId;
	private Long summonerLevel;
	private List<LeagueDto> leagues;
}
