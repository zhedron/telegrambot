package store.telegrambot.bot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.vdurmont.emoji.EmojiParser;

import store.telegrambot.config.BotConfig;

@Component
public class TelegramBot extends TelegramLongPollingBot {
	
	private final BotConfig bot;
	
	static final String HELP_COMMAND = "Welcome! Enter the following commands: /start getting to with bot\nEnter the command /store 5" + "to know the product" 
	+ "\nFind out your /balance";
	
	
	public TelegramBot (BotConfig bot) {
		this.bot = bot;
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			String fromUser = update.getMessage().getText();
			Long chatId = update.getMessage().getChatId();
			
			
			switch (fromUser) {
			case "/start":
				startCommand (chatId, update.getMessage().getChat().getFirstName());
				break;
			case "/help":
				helpCommand (chatId);
				break;
			case "/store":
				inlineCommand(chatId);
				break;
			case "/balance":
				Scanner sc = new Scanner(System.in);
				
				String balance = sc.next();
				balance(chatId, "Ваш баланс: " + balance + "$");
				break;
				default:
					startCommand(chatId, "We don't understand you. To contact me, use the /help command");
			}
		} else if (update.hasCallbackQuery()) {
			String callData = update.getCallbackQuery().getData();
			Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
			Long chatId = update.getCallbackQuery().getMessage().getChatId();
			
			if (callData.equals("Computers")) {
				EditMessageText edit = new EditMessageText();
				
				edit.setChatId(chatId);
				edit.setMessageId(messageId);
				edit.setText("You have selected computers. Now choose the computer you want to buy");
				
				try {
					execute (edit);
				} catch (TelegramApiException e) {}
			} else if (callData.equals("Laptops")) {
				EditMessageText edit = new EditMessageText();
				edit.setChatId(chatId);
				edit.setMessageId(messageId);
				edit.setText("You have selected laptops. Now choose the laptop you want to buy");
				
				try {
					execute (edit);
				} catch (TelegramApiException e) {}
			} else if (callData.equals("Phones")) {
				EditMessageText edit = new EditMessageText();
				edit.setChatId(chatId);
				edit.setMessageId(messageId);
				edit.setText("You have selected phones. Now choose the phone you want to buy");
				try {
					execute (edit);
				} catch (TelegramApiException e) {}
			}
		}
	}

	@Override
	public String getBotUsername() {
		return bot.getBotName();
	}
	
	@Override
	public String getBotToken () {
		return bot.getBotToken();
	}
	
	public SendMessage startCommand (Long chat_id, String text) {
		SendMessage message = new SendMessage ();
		message.setChatId(chat_id);
		message.setText("Hello, " + text + ", i am bot that shows the product");
		
		ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
		markup.setOneTimeKeyboard(true);
		markup.setResizeKeyboard(true);
		
		List<KeyboardRow> listrow = new ArrayList<>();
		
		KeyboardButton button = new KeyboardButton();
		
		KeyboardRow row = new KeyboardRow();
		
		button.setText("/start");
		
		KeyboardButton button2 = new KeyboardButton ();
		button2.setText("/help");
		KeyboardButton button3 = new KeyboardButton ();
		button3.setText ("/store");
		
		row.add(button);
		row.add(button2);
		row.add(button3);
		
		listrow.add(row);
		
		markup.setKeyboard(listrow);
		message.setReplyMarkup(markup);
		
		try {
			execute (message);
		} catch (TelegramApiException e) {}
		
		return message;
	}
	
	private SendMessage helpCommand (Long chat_id) {
		SendMessage message = new SendMessage ();
		message.setChatId(chat_id);
		message.setText(HELP_COMMAND);
		
		try {
			execute (message);
		} catch (TelegramApiException e) {
		}
		
		return message;
	}
	
	private SendMessage inlineCommand (Long chat_id) {
		SendMessage message = new SendMessage ();
		message.setChatId(chat_id);
		message.setText("Select a product from the list");
		
		InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
		
		List<List<InlineKeyboardButton>> listbuttons = new ArrayList<>();
		
		List<InlineKeyboardButton> rowbutton = new ArrayList<>();
		
		InlineKeyboardButton button = new InlineKeyboardButton();
		
		button.setText(EmojiParser.parseToUnicode(":desktop_computer:" + " computers"));
		button.setCallbackData("Computers");
		
		InlineKeyboardButton button2 = new InlineKeyboardButton();
		
		button2.setText(EmojiParser.parseToUnicode(":computer:" + " laptops"));
		button2.setCallbackData("Laptops");
		
		InlineKeyboardButton button3 = new InlineKeyboardButton();
		
		button3.setText(EmojiParser.parseToUnicode(":iphone:" + "phones"));
		button3.setCallbackData("Phones");
		
		rowbutton.add(button3);
		rowbutton.add(button2);
		rowbutton.add(button);
		
		listbuttons.add(rowbutton);
		
		markup.setKeyboard(listbuttons);
		message.setReplyMarkup(markup);
		
		try {
			execute (message);
		} catch (TelegramApiException e) {}
		return message;
	}
	
	private void balance (Long chat_id, String balance) {
		SendMessage message = new SendMessage();
		message.setChatId(chat_id);
		
		message.setText(balance);
		
		try {
			execute (message);
		} catch (TelegramApiException e) {}
	}
}
