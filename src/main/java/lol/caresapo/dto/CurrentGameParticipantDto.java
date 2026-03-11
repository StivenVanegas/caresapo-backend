package lol.caresapo.dto;

import lombok.Data;

@Data
public class CurrentGameParticipantDto {

	private Long championId;
	private Long profileIconId;
	private Boolean bot;
	private Long teamId;
	private String puuid;
	private Long spell1Id;
	private Long spell2Id;
	private String riotId;
	private String championImageUrl;
}
