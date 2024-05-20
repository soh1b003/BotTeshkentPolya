package org.example.bot.handlers;

import lombok.SneakyThrows;
import org.example.enumators.UserRole;
import org.example.enumators.UserState;
import org.example.model.Slot;
import org.example.model.Stadium;
import org.example.model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Currency;
import java.util.List;
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
            SendMessage sendMessage = new SendMessage(chatId.toString(), "NIMA QILMOQCHISIZ ? 🧐");
            sendMessage.setReplyMarkup(buttons.getUserRole());
            return sendMessage;
        }
        return new SendMessage(chatId.toString(), "please send your location");
    }




    public SendMessage chooseUserRole(Message message, User currentUser){
        if(Objects.equals(message.getText(), "E'lonlarni kuriw")){
            currentUser.setState(UserState.LocationAndDate);
            currentUser.setRole(UserRole.USER);
            userService.update(currentUser);

            SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "QAYSI TARTIBDA QIDIRMOQCHISIZ? 🧐 ");
            ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
            sendMessage.setReplyMarkup(buttons.getLocationAndDate());
            return sendMessage;
        }

        if(Objects.equals(message.getText(), "E'lon quwiw")){
            currentUser.setState(UserState.NameStadium);
            currentUser.setRole(UserRole.ADVERTISER);
            userService.update(currentUser);
            SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "Stadion nomini kiriting :");
            ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
            sendMessage.setReplyMarkup(remove);
            return sendMessage;
        }

        return new SendMessage(message.getChatId().toString(), "Nima qilmoqcisan uzi");

    }





}
