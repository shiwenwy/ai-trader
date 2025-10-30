package com.dodo.ai_trader.service.decision;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BinanceDecision {

    private ChatClient chatClient;

    public BinanceDecision(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Value("classpath:/prompts/system-message.mustache")
    private Resource systemResource;

    private static String jsonFormat = "{\n" +
            "  \"signal\": \"buy_to_enter\" | \"sell_to_enter\" | \"hold\" | \"close\",\n" +
            "  \"coin\": \"BTC\" | \"ETH\" | \"SOL\" | \"BNB\",\n" +
            "  \"quantity\": <float>,\n" +
            "  \"leverage\": <integer 1-20>,\n" +
            "  \"profit_target\": <float>,\n" +
            "  \"stop_loss\": <float>,\n" +
            "  \"invalidation_condition\": \"<string>\",\n" +
            "  \"confidence\": <float 0-1>,\n" +
            "  \"risk_usd\": <float>,\n" +
            "  \"justification\": \"<string>\"\n" +
            "}";

    public String test(){
        Map<String, Object> variables = new HashMap<>();
        variables.put("initialFunding", 10000);
        variables.put("altcoinMin", 50);
        variables.put("altcoinMax", 150);
        variables.put("altcoinLeverage", 5);
        variables.put("btcMin", 200);
        variables.put("btcMax", 600);
        variables.put("btcLeverage", 3);
        variables.put("jsonFormat", jsonFormat);
        SystemPromptTemplate template = new SystemPromptTemplate(systemResource);
        String systemPrompt = template.render(variables);
        return systemPrompt;
    }

    public static void main(String[] args) {

    }
}
