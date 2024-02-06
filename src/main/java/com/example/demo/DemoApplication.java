package com.example.demo;

import com.example.demo.bot.Bot;
import com.example.demo.bot.Channel;
import com.example.demo.bot.Controller;
import com.example.demo.bot.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@EnableJpaRepositories
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		Bot bot = new Bot();
		try {
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(bot);
			new Channel(bot);
			Data.printMessage();
		} catch (TelegramApiException e) {
			Data.printError(e);
		}
	}
}