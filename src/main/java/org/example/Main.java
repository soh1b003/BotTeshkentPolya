package org.example;

import org.example.bot.MyBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyBot("7193951173:AAG9LKGMDR5R_NFPka0WsM75CjANNoQtQOw"));
            System.out.println("GOOOOOOO");
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}