package com.vanpine.mcpclient.config;

import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/*
*  redis记忆存储Bean配置
*
* */
@Configuration
public class MemoryConfig {
    // 从配置文件中获取redis 主机ip配置
    @Value("${spring.ai.memory.redis.host}")
    private String redisHost;

    // 从配置文件中获取redis 隐射端口配置
    @Value("${spring.ai.memory.redis.port}")
    private int redisPort;


    @Bean
    public RedisChatMemoryRepository redisChatMemoryRepository() {
        return RedisChatMemoryRepository.builder()
                .host(redisHost)
                .port(redisPort)
                .build();
    }
}
