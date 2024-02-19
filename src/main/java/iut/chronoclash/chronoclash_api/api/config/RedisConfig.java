package iut.chronoclash.chronoclash_api.api.config;

import iut.chronoclash.chronoclash_api.api.model.CacheNames;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;

// @Configuration
public class RedisConfig {

    @Value("${cache.config.entryTtl}")
    private int entryTtl;

    @Value("${cache.config.users.entryTtl}")
    private int usersEntryTtl;

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> builder
                .withCacheConfiguration(CacheNames.USERS,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(usersEntryTtl)));
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(entryTtl))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}