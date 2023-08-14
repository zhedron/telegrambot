package store.telegrambot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class BotApp {
	@Autowired
	TelegramBot bot;
	
	@EventListener ({ContextRefreshedEvent.class})
	public void init () throws TelegramApiException {
		TelegramBotsApi telegram = new TelegramBotsApi(DefaultBotSession.class);
		
		try {
			telegram.registerBot(bot);
		} catch (TelegramApiRequestException e) {
			log.error("Failed to register bot", e);
		}
	}
}
