package com.dodo.ai_trader.service.decision;

import com.dodo.ai_trader.service.config.BizConfig;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BinanceDecision {

    @Autowired
    private ChatClient chatClient;
    @Autowired
    private BizConfig bizConfig;

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

    private Prompt buildSystemPrompt(Integer initialFunding) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("initialFunding", initialFunding);
        variables.put("altcoinMin", bizConfig.getAltcoinMin());
        variables.put("altcoinMax", bizConfig.getAltcoinMax());
        variables.put("altcoinLeverage", bizConfig.getAltcoinLeverage());
        variables.put("btcMin", bizConfig.getBtcMin());
        variables.put("btcMax", bizConfig.getBtcMax());
        variables.put("btcLeverage", bizConfig.getBtcLeverage());
        variables.put("jsonFormat", jsonFormat);
        SystemPromptTemplate template = new SystemPromptTemplate(systemResource);

        return template.create(variables);
    }

    public String test(){

        Prompt systemPrompt = buildSystemPrompt(10000);

        return systemPrompt.getContents();
    }

    public static void main(String[] args) {

    }
}
