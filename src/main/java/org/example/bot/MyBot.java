package org.example.bot;

import lombok.extern.slf4j.Slf4j;
import org.example.bot.buttons.*;
import org.example.bot.handlers.AdvertiserHandler;
import org.example.bot.handlers.CommandHandler;
import org.example.bot.handlers.RegistrationHandler;
import org.example.bot.handlers.UserHandler;
import org.example.repository.SlotRepository;
import org.example.repository.StadiumRepository;
import org.example.repository.UserRepository;
import org.example.service.SlotService;
import org.example.service.StadiumService;
import org.example.service.UserService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class MyBot extends TelegramLongPollingBot {
    public static SlotService slotService = new SlotService(new SlotRepository());
    public static StadiumService stadiumService = new StadiumService(new StadiumRepository());
    public static UserService userService = new UserService(new UserRepository());
    public static UserButtons userButtons = new UserButtons();
    public static AdvertiserButtons advertiserButtons = new AdvertiserButtons();
    public static Buttons buttons = new Buttons();
    public static CommandHandler commandHandler = new CommandHandler();
    public static DateButtons dateButtons = new DateButtons();
    public static UserHandler userHandler = new UserHandler();
    public static AdvertiserHandler advertiserHandler = new AdvertiserHandler();
    public static TimeButtons timeButtons = new TimeButtons();
    public MyBot(String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = commandHandler.handle(update.getMessage());
        send(sendMessage);
    }

    private void send(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "TashkentPolya_bot";
    }
}
