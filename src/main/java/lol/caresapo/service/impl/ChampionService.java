package lol.caresapo.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lol.caresapo.dto.ChampionDataDto;

@Service
public class ChampionService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String VERSION = "16.5.1";

    public Map<Long, String> getChampionMap() {

        String url = "https://ddragon.leagueoflegends.com/cdn/" +
                VERSION +
                "/data/en_US/champion.json";

        ChampionDataDto response =
                restTemplate.getForObject(url, ChampionDataDto.class);

        Map<Long, String> map = new HashMap<>();

        response.getData().values().forEach(champion -> {
            map.put(Long.parseLong(champion.getKey()), champion.getId());
        });

        return map;
    }
}
