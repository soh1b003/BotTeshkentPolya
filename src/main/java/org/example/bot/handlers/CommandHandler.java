package org.example.bot.handlers;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.bot.MyBot;
import org.example.enumators.UserState;
import org.example.exception.DataNotFoundException;
import org.example.model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

import static org.example.bot.MyBot.*;
import static org.example.bot.MyBot.advertiserHandler;

@Slf4j
public class CommandHandler extends MyBot {
    public static RegistrationHandler registrationHandler = new RegistrationHandler();
    public SendMessage handle(Message message) throws TelegramApiException {
        String text = message.getText();
        Long chatId = message.getChatId();

        if(Objects.equals(text, "/start")) {
            return handleStart(message, message.getFrom());
        }

        User currentUser = userService.findByChatId(chatId);
        switch (currentUser.getState()) {
            case SHARE_CONTACT -> {
                return registrationHandler.handleContact(message, currentUser);
            }
            case SHARE_LOCATION -> {
                return registrationHandler.handleLocation(message, currentUser);
            }
            case UserRole, MAIN_MENU ->{
                return registrationHandler.chooseUserRole(message, currentUser);
            }


            //user
            case LocationAndDate -> {
                return userHandler.locationAndDate(message, currentUser);
            }
            //Time
            case EnterDate -> {
                return userHandler.enterDate(message, currentUser);
            }
            case EnterTime ->{
                 return userHandler.enterTime(message, currentUser);
            }

            case OlishOlmaslik -> {
                return userHandler.oliwOlmaslik(message, currentUser);
            }
            case PAYMENT -> {
                return userHandler.payment(message, currentUser);
            }
            // LocationOrPolya
            case SHARE_LOCATION_POLYA -> {
                SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "Qidirilmoqda");
                ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
                sendMessage.setReplyMarkup(remove);
                execute(sendMessage);
                return userHandler.shareLocationPolya(message, currentUser);
            }
            case EnterYesAndNoCallBack -> {
                return userHandler.enterYesAndNoCallBack(message, currentUser);
            }
            case EnterDateCallBack -> {
                return userHandler.enterDateCallBack(message, currentUser);
            }
            case EnterTimeCallBack -> {
                return userHandler.enterTimeCallBack(message, currentUser);
            }





            ///ADvertiser
            case NameStadium ->{
                return advertiserHandler.nameStadium(message, currentUser);
            }
            case StadiumPage -> {
                return advertiserHandler.stadiumPage(message, currentUser);
            }
            case YesAndNoStadiumPage -> {
                return advertiserHandler.yesAndNoStadiumPage(message, currentUser);
            }
            case LocationStadium -> {
                if(message.hasPhoto()){
                    return advertiserHandler.stadiumPage(message, currentUser);
                }
                    return advertiserHandler.locationStadium(message, currentUser);
            }
            case Price -> {
                return advertiserHandler.priceStadium(message, currentUser);
            }
            case LocalDate -> {
                return advertiserHandler.localDate(message, currentUser);
            }
            case FromTo -> {
                return advertiserHandler.fromTo(message, currentUser);
            }
            case YesAndNo -> {
                return advertiserHandler.yesAndNo(message, currentUser);
            }
            case PlusTime -> {
                return advertiserHandler.plusTime(message, currentUser);
            }
        }
        return new SendMessage(message.getChatId().toString(), "NIMASAN SAN❌❌❌");
    }


    public SendMessage handleStart(Message message, org.telegram.telegrambots.meta.api.objects.User from) {
        Long chatId = message.getChatId();
        User user = null;
        try{
            user = userService.findByChatId(chatId);
            user.setState(UserState.MAIN_MENU);
            userService.update(user);
            user.setState(UserState.MAIN_MENU);
            SendMessage sendMessage = new SendMessage(chatId.toString(),
                    String.format("Welcome back %s, choose currency \n\n\n NIMA QILMOQCHISIZ ? 🧐", user.getFirstName()));
            sendMessage.setReplyMarkup(buttons.getUserRole());
            return sendMessage;
        }
        catch (DataNotFoundException e){
            log.info(e.getMessage(), chatId);
            User newUser = User.builder()
                    .firstName(from.getFirstName())
                    .lastName(from.getLastName())
                    .username(from.getUserName())
                    .state(UserState.SHARE_CONTACT)
                    .chatId(chatId).build();
            user = userService.add(newUser);
        }
        SendMessage sendMessage = new SendMessage(chatId.toString(),
                String.format("Welcome %s, please share your number", user.getFirstName()));
        sendMessage.setReplyMarkup(buttons.requestContact());
        return sendMessage;
        }


    public SendMessage callbackHandle(CallbackQuery callbackQuery) throws TelegramApiException {
        MaybeInaccessibleMessage message = callbackQuery.getMessage();
        User currentUser = userService.findByChatId(message.getChatId());
        switch (currentUser.getState()){
            case SendStadiumPageCallBack -> {
                String data = callbackQuery.getData();
                execute(userHandler.callBackStadiumPage(data, currentUser));
                execute(userHandler.callBackStadiumLocation(data, currentUser));
                currentUser.setState(UserState.EnterYesAndNoCallBack);
                userService.update(currentUser);
                SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "BRON QILASIZMI? 🧐 ");
                sendMessage.setReplyMarkup(buttons.getYesAndNo());
                return sendMessage;
            }
            case ToChooseStadium -> {
                String data = callbackQuery.getData();
                execute(userHandler.toChooseStadium(data, currentUser));
                execute(userHandler.sendLocation(data, currentUser));
                return new SendMessage(message.getChatId().toString(), "BRON QILASIZMI");
            }
        }
        return new SendMessage(message.getChatId().toString(), "Notugri buyruq!");
    }
}
