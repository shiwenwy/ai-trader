package com.dodo.ai_trader;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "com.dodo.ai_trader")
@MapperScan("com.dodo.ai_trader.service.mapper")
public class AiTraderApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiTraderApplication.class, args);
		System.out.println("Ai Trader Application Started");
	}

}
