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
	public static final String PUUID_LAN = "DCMJ4rwiJ-9EJpu-pdDKxO80UbFhVl3mTjjZfPxgzPYAcSIiy4BLl27Bjn_SCOOn_HqOkd-GufQwXw";
	public static final String PUUID_LAS = "hC0FM2TQUlc8SJyYNZYKdlStUaoBa251_Zu40Y6NG5d3LKVCmaubTOxSn8EJRA2X4TR9IImlhPUbfQ";
	public static final String PUUID_NA = "yWjKBilcrGb78TK0OvDr4VYsogQcnfXCuLnEVd2APWErcF_GFjYYlzkdqvO150FLZLfsHldW_4fdRQ";
	public static final String PUUID_EUW = "oZtYFNiuGiwCwxcR07dk6O6s8GJNy89KVJYh4rthesGPflQlegFKuypP423NzH_Ju7ux-gb91mDJ2g";
	
	public static final List<ProfileDto> PROFILES = List.of(
		new ProfileDto("LAN",PUUID_LAN,REGION_AMERICAS,"la1"),
		new ProfileDto("LAS",PUUID_LAS,REGION_AMERICAS,"la2"),
		new ProfileDto("NA",PUUID_NA,REGION_AMERICAS,"na1"),
		new ProfileDto("EUW",PUUID_EUW ,REGION_EUROPE,"euw1")
	);
}
