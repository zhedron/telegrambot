package store.telegrambot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import lombok.Data;

@Data
@Configuration
public class BotConfig {
	@Value ("${bot.name}")
	private String botName;
	@Value ("${bot.token}")
	private String botToken;
}
