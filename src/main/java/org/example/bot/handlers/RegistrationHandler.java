package org.example.bot.handlers;

import org.example.enumators.UserRole;
import org.example.enumators.UserState;
import org.example.model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Currency;
import java.util.Objects;

import static org.example.bot.MyBot.*;

public class RegistrationHandler {
    public SendMessage handleContact(Message message, User currentUser) {
        Long chatId = message.getChatId();
        if (message.hasContact()) {
            Contact contact = message.getContact();
            if (!Objects.equals(contact.getUserId(), chatId)) {
                return new SendMessage(chatId.toString(), "Please share your own number");
            }
            currentUser.setPhoneNumber(contact.getPhoneNumber());
            currentUser.setState(UserState.SHARE_LOCATION);
            userService.update(currentUser);
            SendMessage sendMessage = new SendMessage(chatId.toString(), "thank you, please share your location");
            sendMessage.setReplyMarkup(buttons.requestLocation());
            return sendMessage;
        }
        return new SendMessage(chatId.toString(), "please send your contact");
    }

    public SendMessage handleLocation(Message message, User currentUser) {
        Long chatId = message.getChatId();
        if (message.hasLocation()) {
            Location location = message.getLocation();
            currentUser.setLocationUser(location);
            currentUser.setState(UserState.UserRole);
            userService.update(currentUser);
            SendMessage sendMessage = new SendMessage(chatId.toString(), "NIMA QILMOQCHISIZ ??");
            sendMessage.setReplyMarkup(buttons.getUserRole());
            return sendMessage;
        }
        return new SendMessage(chatId.toString(), "please send your location");
    }


    public SendMessage chooseUserRole(Message message, User currentUser){
        if(Objects.equals(message.getText(), "E'lonlarni kuriw")){
            currentUser.setState(UserState.ToChoose);
            userService.updateRole(currentUser, UserRole.USER );
            return toChoose(message, currentUser);
        }

        if(Objects.equals(message.getText(), "E'lon quyiw")){
            currentUser.setState(UserState.NameStadium);
            userService.updateRole(currentUser, UserRole.ADVERTISER );
            return nameStadium(message, currentUser);
        }
        return new SendMessage(message.getChatId().toString(), "Wrong command or something went wrong");
    }


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

    public SendMessage nameStadium(Message message, User currentUser) {
        currentUser.setState(UserState.StadiumPage);
        return new SendMessage(message.getChatId().toString(), "Stadion nomini kiriting :");
    }

    public SendMessage stadiumPage(Message message, User currentUser) {
        currentUser.setState(UserState.LocationStadium);
        return new SendMessage(message.getChatId().toString(), "Stadion rasmni yuboring :");
    }

    public SendMessage locationStadium(Message message, User currentUser) {
        Long chatId = message.getChatId();
        if (message.hasLocation()) {
            Location location = message.getLocation();
            currentUser.setLocationUser(location);
            currentUser.setState(UserState.LocalDate);
            userService.update(currentUser);
            SendMessage sendMessage = new SendMessage(chatId.toString(), "Sanani tanla ");
            sendMessage.setReplyMarkup(dateButtons.getWeek());
            return sendMessage;

        }
        return new SendMessage(chatId.toString(), "please send your location");
    }


    public SendMessage localDate(Message message, User currentUser) {
        currentUser.setState(UserState.LocationStadium);

    }









}
