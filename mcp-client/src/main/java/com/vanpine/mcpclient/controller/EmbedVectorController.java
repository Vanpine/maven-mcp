package com.vanpine.mcpclient.controller;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmbedVectorController {

    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    /*
     *  文本向量化
     * */
    @GetMapping("/text2embed")
    public EmbeddingResponse text2embed(String msg) {
        EmbeddingResponse embeddingResponse = embeddingModel.call(new EmbeddingRequest(List.of(msg),
                DashScopeEmbeddingOptions.builder()
                        .withModel("text-embedding-v3")
                        .build()));

        System.out.println(Arrays.toString(embeddingResponse.getResult().getOutput()));
        return embeddingResponse;
    }

    /*
     *   文本向量化存储RedisStack
     * */
    @GetMapping("/embedVector/add")
    public void add() {
        List<Document> documents = List.of(
                new Document("LLM"),
                new Document("Java")
        );
        vectorStore.add(documents);
    }
}
