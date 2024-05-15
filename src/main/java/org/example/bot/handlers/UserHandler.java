package org.example.bot.handlers;

import org.example.enumators.UserState;
import org.example.model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class UserHandler {
    public SendMessage toChoose(Message message, User currentUser) {
        currentUser.setState(UserState.PAYMENT);
        // bazani kursatish
        return new SendMessage(message.getChatId().toString(), "Tulov Chekini tawlang :");
    }

    public SendMessage payment(Message message, User currentUser){
        if(message.hasPhoto()){
            currentUser.setState(UserState.MAIN_MENU);
            return new SendMessage(message.getChatId().toString(), "Maroqli Hordiq tilayman!!!👍");
        }
        return new SendMessage(message.getChatId().toString(), "Senga Chekni tawla dedm🧐🧐");
    }
}
