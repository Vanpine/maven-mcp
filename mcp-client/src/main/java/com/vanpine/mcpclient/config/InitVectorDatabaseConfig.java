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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
@RequiredArgsConstructor
public class InitVectorDatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(InitVectorDatabaseConfig.class);

    // 向量数据库的操作类
    private final VectorStore vectorStore;
    private final StringRedisTemplate stringRedisTemplate;

    private final AtomicBoolean reloading = new AtomicBoolean(false);

    // 读取环境对应的 ops.txt 路径：本地用 classpath，线上用绝对路径
    @Value("${ops.file-path:classpath:ops.txt}")
    private Resource opsFile;

    @PostConstruct
    public void init() {
        reloadOpsToVectorStore();
    }

    /**
     * 每隔 10 秒重新加载 ops.txt 到向量库。
     * 注意：这是重建索引/写入向量的行为，可能比较耗时。
     */
    @Scheduled(fixedDelay = 10000)
    public void scheduledReloadOpsToVectorStore() {
        if (!reloading.compareAndSet(false, true)) {
            log.warn("上一轮 ops.txt 重加载尚未完成，跳过本次调度");
            return;
        }
        try {
            reloadOpsToVectorStore();
        } finally {
            reloading.set(false);
        }
    }

    private void reloadOpsToVectorStore() {
        // 1. 直接用 Resource 构造 TextReader
        TextReader textReader = new TextReader(opsFile);
        textReader.setCharset(StandardCharsets.UTF_8);
        // 2. 文件转换为向量（开启分词）
        List<Document> list = new TokenTextSplitter().transform(textReader.read());
        log.info("成功读取 ops.txt，分割为 {} 个文档片段", list.size());
        log.info("这是文件内容{}", list.stream().map(Document::getId).toList());

        // 删除旧向量
        deleteKeysWithCustomPrefix();
        // 4. 写入向量数据库 redisStack
        vectorStore.add(list);
        log.info("成功将 {} 个文档片段写入 Redis Stack 向量库", list.size());
    }

    /*
     *  每一次更新知识库的时候 进行删除旧的向量
     * */
    public long deleteKeysWithCustomPrefix() {
        // 1. 匹配所有以custom-prefix为前缀的Key（*是通配符，匹配任意后缀）
        Set<String> keys = stringRedisTemplate.keys("custom-prefix*");
        log.debug("{} \n", keys);
        if (keys == null || keys.isEmpty()) {
            System.out.println("未找到以custom-prefix为前缀的Key");
            return 0;
        }
        // 2. 批量删除匹配到的Key（delete支持批量删除Set集合）
        long deletedCount = stringRedisTemplate.delete(keys);
        System.out.println("成功删除 " + deletedCount + " 个以custom-prefix为前缀的Key");
        return deletedCount;
    }
}