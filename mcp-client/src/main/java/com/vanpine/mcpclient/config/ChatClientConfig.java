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
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {

    private final RedisChatMemoryRepository redisChatMemoryRepository;

    /**
     * 构建自定义会话记忆（带Redis存储和消息窗口限制）
     */
    @Bean
    public MessageWindowChatMemory customMessageWindowChatMemory() {
        // 正确赋值并返回自定义会话记忆实例
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(redisChatMemoryRepository)
                .maxMessages(SystemChatClientConfig.MAX_MESSAGE) // 引用外部静态配置
                .build();
    }

    /**
     * 构建最终的 ChatClient 实例
     */
    @Bean
    public ChatClient chatClient(DashScopeChatModel model ,
                                 MessageWindowChatMemory customChatMemory,
                                 ToolCallbackProvider tools) {
        // 获取自定义会话记忆实例
        // MessageWindowChatMemory customChatMemory = customMessageWindowChatMemory();

        // 构建最终 ChatClient 实例
        return  ChatClient
                .builder(model)
                .defaultSystem(SystemChatClientConfig.SYSTEM_PROMPT) // 优先使用配置文件中的提示词
                .defaultToolCallbacks(tools.getToolCallbacks()) // mcp服务端工具
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(), // 日志打印顾问
                        MessageChatMemoryAdvisor.builder(customChatMemory).build() // 注入自定义会话记忆
                )
                .build();
    }
}
