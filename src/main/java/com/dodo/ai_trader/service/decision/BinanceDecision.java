package com.dodo.ai_trader.service.decision;

import cn.hutool.json.JSONUtil;
import com.dodo.ai_trader.service.config.BizConfig;
import com.dodo.ai_trader.service.config.TradeConfig;
import com.dodo.ai_trader.service.model.CommonPosition;
import com.dodo.ai_trader.service.utils.LogUtil;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BinanceDecision {

    @Autowired
    private ChatClient chatClient;
    @Autowired
    private TradeConfig tradeConfig;

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
            "  \"entry_price\": <float>,\n" +
            "  \"justification\": \"<string>\"\n" +
            "}";

    private static String waitJson = "[\n" +
            "  {\n" +
            "    \"symbol\": \"MARKET\",\n" +
            "    \"action\": \"wait\",\n" +
            "    \"reasoning\": \"市场震荡，无明确信号，等待更好机会\"\n" +
            "  }\n" +
            "]";

    public static void main(String[] args) {
        System.out.println(jsonFormat);
    }

    public Message buildSystemMessage(Integer initialFunding) {

        Map<String, Object> variables = new HashMap<>();
        variables.put("initialFunding", initialFunding);
        variables.put("altcoinMin", tradeConfig.getAltcoinMin());
        variables.put("altcoinMax", tradeConfig.getAltcoinMax());
        variables.put("altcoinLeverage", tradeConfig.getAltcoinLeverage());
        variables.put("btcMin", tradeConfig.getBtcMin());
        variables.put("btcMax", tradeConfig.getBtcMax());
        variables.put("btcLeverage", tradeConfig.getBtcLeverage());
        variables.put("jsonFormat", jsonFormat);
        variables.put("waitJson", waitJson);
        SystemPromptTemplate template = new SystemPromptTemplate(systemResource);
        return template.createMessage(variables);
    }

    public Message buildUserMessage(long minutesElapsed, Map<String, Object> btcData,
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
        return template.createMessage(variables);
    }


    public Flux<String> getDecision(long minutesElapsed, Map<String, Object> btcData,
                                    Map<String, Object> ethData, Map<String, Object> solData,
                                    Map<String, Object> bnbData, double returnPct,
                                    double sharpeRatio, double cashAvailable, double accountValue,
                                    List<CommonPosition> currPositions, Integer initialFunding) {
        Message systemMessage = buildSystemMessage(initialFunding);
        Message userMessaget = buildUserMessage(minutesElapsed, btcData, ethData, solData, bnbData,
                returnPct, sharpeRatio, cashAvailable, accountValue, currPositions);

        Prompt prompt = new Prompt(systemMessage, userMessaget);
        LogUtil.monitorLog("prompt: \n" + prompt.getContents());

        return null;
//        return chatClient.prompt(prompt).stream().content();
    }

}
