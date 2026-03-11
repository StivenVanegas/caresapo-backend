package lol.caresapo.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lol.caresapo.dto.SpellDataDto;

@Service
public class SummonerSpellService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String VERSION = "16.5.1";

    public Map<Integer, String> getSpellMap() {

        String url = "https://ddragon.leagueoflegends.com/cdn/"
                + VERSION
                + "/data/en_US/summoner.json";

        SpellDataDto response =
                restTemplate.getForObject(url, SpellDataDto.class);

        Map<Integer, String> map = new HashMap<>();

        response.getData().values().forEach(spell -> {
            map.put(Integer.parseInt(spell.getKey()), spell.getId());
        });

        return map;
    }
}
