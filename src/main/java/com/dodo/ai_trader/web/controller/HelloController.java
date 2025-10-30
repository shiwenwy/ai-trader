package com.dodo.ai_trader.web.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private ChatClient chatClient;

    public HelloController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "input", defaultValue = "讲一个笑话") String input) {
        return chatClient.prompt(input).call().content();
    }
}
