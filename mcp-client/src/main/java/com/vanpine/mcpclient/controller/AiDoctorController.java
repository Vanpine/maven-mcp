package com.vanpine.mcpclient.controller;


import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import com.vanpine.mcpclient.Pojo.ChatPO;
import com.vanpine.mcpclient.config.SystemChatClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AiDoctorController {

    private final ChatClient dashScopeChatClient;

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
