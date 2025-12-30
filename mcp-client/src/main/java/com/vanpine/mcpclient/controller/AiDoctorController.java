package com.vanpine.mcpclient.controller;


import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import com.vanpine.mcpclient.Pojo.ChatPO;
import com.vanpine.mcpclient.config.AiDoctorProperties;
import com.vanpine.mcpclient.config.SystemChatClientConfig;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AiDoctorController {

    private final AiDoctorProperties aiDoctorProperties;
    private final ChatClient dashScopeChatClient;
    private final MessageWindowChatMemory messageWindowChatMemory;


    public AiDoctorController(
            AiDoctorProperties aiDoctorProperties,
            ChatClient.Builder builder,
            RedisChatMemoryRepository redisChatMemoryRepository
    ) {
        this.aiDoctorProperties = aiDoctorProperties;
        // 创建会话记忆实例
        this.messageWindowChatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(redisChatMemoryRepository)
                .maxMessages(SystemChatClientConfig.MAX_MESSAGE)
                .build();
        // 创建聊天实例
        this.dashScopeChatClient = builder
                .defaultSystem(SystemChatClientConfig.SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(messageWindowChatMemory).build()
                ).build();
    }

    // AI医生对话
    @PostMapping(value = "/chat", produces = "text.html/utf-8")
    public Flux<String> chat(@RequestBody ChatPO chatPO) {
        // String englishQuestion = "Please answer in English only. My question: " + chatPO.getMessage();
        return dashScopeChatClient.prompt()
                .user(chatPO.getMessage())
                .advisors(a -> a.param(CONVERSATION_ID, chatPO.getConversationId()))
                .stream()
                .content();
    }

}
