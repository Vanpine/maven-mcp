package com.vanpine.mcpclient.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class McpController {

    private final ChatClient dashScopeChatClient;

    @GetMapping(value = "/mcp" , produces = "text.html/utf-8")
    public Flux<String> chatMcp(@RequestParam(value = "msg" ,defaultValue = "深圳") String msg){
          System.out.println("msg:"+msg);
          return dashScopeChatClient.prompt()
                  .user(msg)
                  .stream()
                  .content();
    }
}
