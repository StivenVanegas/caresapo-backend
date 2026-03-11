package lol.caresapo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TwitchUser {

    private String id;
    
    @JsonProperty("display_name")
    private String displayName;
    
    private String description;
    
    @JsonProperty("profile_image_url")
    private String profileImageUrl;
    
    private Boolean online;
    
    @JsonProperty("game_name")
    private String gameName;
    
    private String title;
}
