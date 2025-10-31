package com.dodo.ai_trader.service.config;

import com.binance.connector.client.common.configuration.ClientConfiguration;
import com.binance.connector.client.common.configuration.SignatureConfiguration;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.DerivativesTradingUsdsFuturesRestApiUtil;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.api.DerivativesTradingUsdsFuturesRestApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static com.dodo.ai_trader.service.utils.SHA256Util.decryptAESFromHex;

@Configuration
public class ExchangeConfig {

    private static final String SALT = "123456";

    @Value("${bn.api.key}")
    private String bnApiKey;
    @Value("${bn.api.secret}")
    private String bnSecretKey;

    @Bean
    public DerivativesTradingUsdsFuturesRestApi binanceFuturesRestApi() throws Exception {

        ClientConfiguration clientConfiguration = DerivativesTradingUsdsFuturesRestApiUtil.getClientConfiguration();
        SignatureConfiguration signatureConfiguration = new SignatureConfiguration();
        signatureConfiguration.setApiKey(decryptAESFromHex(bnApiKey, SALT));
        signatureConfiguration.setSecretKey(decryptAESFromHex(bnSecretKey, SALT));
        clientConfiguration.setSignatureConfiguration(signatureConfiguration);
        return new DerivativesTradingUsdsFuturesRestApi(clientConfiguration);
    }

}
