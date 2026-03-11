package lol.caresapo.util;

import java.util.List;

import lol.caresapo.dto.ProfileDto;

public final class AppConstants {

	private AppConstants() {
		
	}
	
	public static final String API_URL = "https://%s.api.riotgames.com";
	
	public static final String REGION_AMERICAS = "americas";
	public static final String REGION_EUROPE = "europe";
	
	//tM62sWst4ZMm-biO_A0Rrxn7195xc3iZ_m-2sEB66V9ZBDrZWTE5zwWKAhYb1HWKpKHdYx7DR0edpg
	//zxLTt02hjwZwllqBzpNfn8hey0ltGdj3NUH8JHW1QpHNQ9rFksJQvCVt_ZOCJ8XhVkB0FWhsoOkqtw
	public static final String PUUID_LAN = "tM62sWst4ZMm-biO_A0Rrxn7195xc3iZ_m-2sEB66V9ZBDrZWTE5zwWKAhYb1HWKpKHdYx7DR0edpg";
	public static final String PUUID_LAS = "b6CTkcNmeIwuaEDWNUpY7wgpgv7HubgD7jUsclQsuNnE3Qq2OQBUYvxTgPdZPABlACxQxhbAIp5ZHg";
	public static final String PUUID_NA = "BQWjXt6W07vsnKF4Jm5a2bOWBqSp3pRMGzC0xuyFvCSh3wgthMVTlVmozvCcO0b5FxYfJoA7gCqXqA";
	public static final String PUUID_EUW = "seeeYV6pIlfGOMjtTT8xeX7cJnm9pYi6tnMiGVOHn5hHKtAz8gACRzKrXJBO23vitMaHEXL0YVMkWg";
	
	public static final List<ProfileDto> PROFILES = List.of(
		new ProfileDto("LAN",PUUID_LAN,REGION_AMERICAS,"la1"),
		new ProfileDto("LAS",PUUID_LAS,REGION_AMERICAS,"la2"),
		new ProfileDto("NA",PUUID_NA,REGION_AMERICAS,"na1"),
		new ProfileDto("EUW",PUUID_EUW ,REGION_EUROPE,"euw1")
	);
}
