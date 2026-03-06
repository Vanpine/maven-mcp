package com.vanpine.mcpclient.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {

    private final RedisChatMemoryRepository redisChatMemoryRepository;

    @Bean
    public MessageWindowChatMemory customMessageWindowChatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(redisChatMemoryRepository)
                .maxMessages(SystemChatClientConfig.MAX_MESSAGE)
                .build();
    }

    @Bean
    public ChatClient chatClient(DashScopeChatModel model,
                                 MessageWindowChatMemory customChatMemory,
                                 ToolCallbackProvider tools) {
        return ChatClient
                .builder(model)
                .defaultSystem(SystemChatClientConfig.SYSTEM_PROMPT)
                .defaultToolCallbacks(tools.getToolCallbacks())
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(customChatMemory).build()
                )
                .build();
    }
}
