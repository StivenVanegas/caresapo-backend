package lol.caresapo.config;

import java.time.Duration;
import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {

        CaffeineCache summonerCache = new CaffeineCache(
                "summonerCache",
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(10))
                        .maximumSize(1000)
                        .build()
        );

        CaffeineCache leagueCache = new CaffeineCache(
                "leagueCache",
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(10))
                        .maximumSize(1000)
                        .build()
        );
        
        CaffeineCache twitchCache = new CaffeineCache(
                "twitchCache",
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(1))
                        .maximumSize(1000)
                        .build()
        );
        
        CaffeineCache spectatorCache = new CaffeineCache(
                "spectatorCache",
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(1))
                        .maximumSize(1000)
                        .build()
        );
        
        CaffeineCache matchCache = new CaffeineCache(
                "matchCache",
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(5))
                        .maximumSize(1000)
                        .build()
        );

        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(List.of(summonerCache, leagueCache, twitchCache, spectatorCache, matchCache));

        return manager;
    }
}
