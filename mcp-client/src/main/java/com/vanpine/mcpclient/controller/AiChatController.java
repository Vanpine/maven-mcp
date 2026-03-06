package com.vanpine.mcpclient.controller;

import com.vanpine.mcpclient.Pojo.ChatPO;
import com.vanpine.mcpclient.service.DatabaseQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AiChatController {

    private final ChatClient dashScopeChatClient;
    private final VectorStore vectorStore;
    private final DatabaseQueryService databaseQueryService;

    @PostMapping(value = "/chat", produces = "text.html/utf-8")
    public Flux<String> chat(@RequestBody ChatPO chatPO) {
        Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.50)
                        .vectorStore(vectorStore)
                        .build())
                .build();

        String userMessage = chatPO.getMessage();
        String dbContext = databaseQueryService.getDatabaseContext(userMessage);
        String promptMessage = dbContext.isEmpty()
                ? userMessage
                : dbContext + "\n\n用户问：" + userMessage + "\n请根据以上数据库查询结果用自然语言回答，不要编造数据。";

        return dashScopeChatClient.prompt()
                .user(promptMessage)
                .advisors(a -> a
                        .param("CONVERSATION_ID", chatPO.getConversationId())
                        .advisors(retrievalAugmentationAdvisor)
                ).stream()
                .content();
    }
}
