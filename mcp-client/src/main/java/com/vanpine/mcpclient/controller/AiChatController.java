package com.vanpine.mcpclient.controller;


import com.vanpine.mcpclient.Pojo.ChatPO;
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

    // AI医生对话
    @PostMapping(value = "/chat", produces = "text.html/utf-8")
    public Flux<String> chat(@RequestBody ChatPO chatPO) {
        /*
         *  Rag检索增强
         * */
        Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.50)
                        .vectorStore(vectorStore)
                        .build())
                .build();


        return dashScopeChatClient.prompt()
                .user(chatPO.getMessage())
                .advisors(a -> a
                        .param("CONVERSATION_ID", chatPO.getConversationId()) // 对话记忆
                        .advisors(retrievalAugmentationAdvisor) // RAG检索增强
                ).stream()
                .content();
    }

}
