package lol.caresapo.dto;

import lombok.Data;

@Data
public class LeagueDto {

	private String leagueId;
	private String queueType;
	private String tier;
	private String rank;
	private String puuid;
	private Integer leaguePoints;
	private Integer wins;
	private Integer losses;
	private Boolean veteran;
	private Boolean inactive;
	private Boolean freshBlood;
	private Boolean hotStreak;
}
