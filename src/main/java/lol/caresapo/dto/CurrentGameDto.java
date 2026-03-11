package lol.caresapo.dto;

import java.util.List;

import lombok.Data;

@Data
public class CurrentGameDto {

	private Long gameId;
	private String gameType;
	private Long gameStartTime;
	private Long mapId;
	private Long gameLength;
	private String platformId;
	private String gameMode;
	//private bannedChampions;
	private Long gameQueueConfigId;
	private List<CurrentGameParticipantDto> participants;
}
