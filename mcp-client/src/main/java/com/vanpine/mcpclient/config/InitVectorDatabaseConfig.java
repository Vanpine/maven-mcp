package com.vanpine.mcpclient.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class InitVectorDatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(InitVectorDatabaseConfig.class);

    // 向量数据库的操作类
    private final VectorStore vectorStore;

    @Value("classpath:ops.txt")
    private Resource opsFile;

    @PostConstruct
    public void init() {
        // 1. 直接用 Resource 构造 TextReader
        TextReader textReader = new TextReader(opsFile);
        textReader.setCharset(StandardCharsets.UTF_8);

        // 2. 文件转换为向量（开启分词）
        List<Document> list = new TokenTextSplitter().transform(textReader.read());
        log.info("成功读取 ops.txt，分割为 {} 个文档片段", list.size());

        // 3. 写入向量数据库 redisStack
        vectorStore.add(list);
        log.info("成功将 {} 个文档片段写入 Redis Stack 向量库", list.size());
    }
}