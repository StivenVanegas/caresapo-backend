package lol.caresapo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileDto {
	
	private String server;
	private String puuid;
	private String region;
	private String platform;

}
