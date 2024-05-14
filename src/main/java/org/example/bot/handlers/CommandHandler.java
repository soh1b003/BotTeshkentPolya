package org.example.bot.handlers;

import lombok.extern.slf4j.Slf4j;
import org.example.enumators.UserState;
import org.example.exception.DataNotFoundException;
import org.example.model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Objects;

import static org.example.bot.MyBot.buttons;
import static org.example.bot.MyBot.userService;
import static org.example.enumators.UserState.*;

@Slf4j
public class CommandHandler {
    public static RegistrationHandler registrationHandler = new RegistrationHandler();
    public SendMessage handle(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();

        if(Objects.equals(text, "/start")) {
            return handleStart(chatId, message.getFrom());
        }

        User currentUser = userService.findByChatId(chatId);

        switch (currentUser.getState()) {
            case SHARE_CONTACT -> {
                return registrationHandler.handleContact(message, currentUser);
            }
            case SHARE_LOCATION -> {
                return registrationHandler.handleLocation(message, currentUser);
            }
            case REGISTERED -> {
                return registrationHandler.chooseUserRole(message, currentUser);
            }
        }
        return new SendMessage(chatId.toString(), "Wrong command or something went wrong");
    }

    private SendMessage handleStart(Long chatId, org.telegram.telegrambots.meta.api.objects.User from) {
        User user = null;
        try{
            user = userService.findByChatId(chatId);
            user.setState(UserState.MAIN_MENU);
            userService.update(user);
            user.setState(UserState.MAIN_MENU);
            SendMessage sendMessage = new SendMessage(chatId.toString(),
                    String.format("Welcome back %s, choose currency", user.getFirstName()));
//            sendMessage.setReplyMarkup(buttons.getUserRole());
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
