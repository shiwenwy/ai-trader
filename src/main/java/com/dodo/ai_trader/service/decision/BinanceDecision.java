package com.dodo.ai_trader.service.decision;

import cn.hutool.json.JSONUtil;
import com.dodo.ai_trader.service.config.BizConfig;
import com.dodo.ai_trader.service.model.CommonPosition;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BinanceDecision {

    @Autowired
    private ChatClient chatClient;
    @Autowired
    private BizConfig bizConfig;

    @Value("classpath:/prompts/binance-system-message.mustache")
    private Resource systemResource;

    @Value("classpath:/prompts/binance-user-message.mustache")
    private Resource userResource;

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


    public Prompt buildUserPrompt(long minutesElapsed, Map<String, Object> btcData,
                                  Map<String, Object> ethData, Map<String, Object> solData,
                                  Map<String, Object> bnbData, double returnPct,
                                  double sharpeRatio, double cashAvailable, double accountValue,
                                  List<CommonPosition> currPositions) {
        Map<String, Object> variables = new HashMap<>();
        // 填充时间信息
        variables.put("minutes_elapsed", minutesElapsed);

        // 填充账户信息
        variables.put("return_pct", returnPct);
        variables.put("sharpe_ratio", sharpeRatio);
        variables.put("cash_available", cashAvailable);
        variables.put("account_value", accountValue);
        variables.put("current_positions", currPositions == null || currPositions.isEmpty() ? "" : JSONUtil.toJsonStr(currPositions));

        // 填充BTC数据
        if (btcData != null) {
            variables.putAll(btcData);
        }

        // 填充ETH数据
        if (ethData != null) {
            variables.putAll(ethData);
        }

        // 填充SOL数据
        if (solData != null) {
            variables.putAll(solData);
        }

        // 填充BNB数据
        if (bnbData != null) {
            variables.putAll(bnbData);
        }

        PromptTemplate template = new PromptTemplate(userResource);

        return template.create(variables);
    }

    public String test(){

        Prompt systemPrompt = buildSystemPrompt(10000);

        return systemPrompt.getContents();
    }

}
