package lol.caresapo.dto;

import lombok.Data;

@Data
public class ParticipantDto {

	private Integer assists;
	private Long championId;
	private String championName;
	private Integer deaths;
	private Integer kills;
	private String puuid;
	private String riotIdGameName;
	private String riotIdTagline;
	private Boolean win;
	private String championImageUrl;
	private Integer teamId;
	
	private Integer item0;
	private Integer item1;
	private Integer item2;
	private Integer item3;
	private Integer item4;
	private Integer item5;
	private Integer item6;
	
	private Integer summoner1Id;
	private Integer summoner2Id;
	
	private String summoner1Img;
	private String summoner2Img;
}
