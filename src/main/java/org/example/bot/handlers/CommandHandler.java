package org.example.bot.handlers;

import lombok.extern.slf4j.Slf4j;
import org.example.enumators.UserState;
import org.example.exception.DataNotFoundException;
import org.example.model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Objects;

import static org.example.bot.MyBot.*;
import static org.example.bot.MyBot.advertiserHandler;

@Slf4j
public class CommandHandler {
    public static RegistrationHandler registrationHandler = new RegistrationHandler();
    public SendMessage handle(Message message) {
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
            case ToChoose ->{
                return userHandler.toChoose(message, currentUser);
            }
            case PAYMENT -> {
                return userHandler.payment(message, currentUser);
            }

            ///ADvertiser
            case NameStadium ->{
                return advertiserHandler.nameStadium(message, currentUser);
            }
            case StadiumPage -> {
                return advertiserHandler.stadiumPage(message, currentUser);
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
        return new SendMessage(message.getChatId().toString(), "Xato Xabar!!!");
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
                    String.format("Welcome back %s, choose currency", user.getFirstName()));
//            if(Objects.equals(user.getRole(), org.example.enumators.UserRole.USER)){
//                userMenu(message, user);
//            }
//
//            if(Objects.equals(user.getRole(), org.example.enumators.UserRole.ADVERTISER)){
//                advertiserMenu(message, user);
//
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
    }
