package org.example.bot.handlers;

import org.example.enumators.StadiumState;
import org.example.enumators.UserRole;
import org.example.enumators.UserState;
import org.example.model.Slot;
import org.example.model.Stadium;
import org.example.model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.example.bot.MyBot.*;

public class UserHandler {


    public SendMessage locationAndDate(Message message, User currentUser){
        if(Objects.equals(message.getText(), "TIME")){
            currentUser.setState(UserState.EnterDate);
            userService.update(currentUser);

            SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "QAYSI SANADA QIDIRMOQCHISIZ? 🧐 ");
            ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
            sendMessage.setReplyMarkup(dateButtons.getWeek());
            return sendMessage;
        }

        if(Objects.equals(message.getText(), "LOCATION")){
            currentUser.setState(UserState.SHARE_LOCATION_POLYA);
            userService.update(currentUser);

            SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "LOCATION🧐 ");
            ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
            sendMessage.setReplyMarkup(buttons.requestLocation());
            return sendMessage;
        }

        return new SendMessage(message.getChatId().toString(), "NIMASAN SEN❌❌❌");

    }


    public SendMessage enterTime(Message message, User currentUser) {
        LocalDate date = currentUser.getDate();
        currentUser.setFromTo(message.getText());
        String fromTo = message.getText();
        ArrayList<Slot> timeSlots = slotService.getDateAndTime(date, fromTo);
        LinkedHashMap<Slot, UUID> map = new LinkedHashMap<>();
        for (Slot slot : timeSlots) {
            map.put(slot, slot.getStadiumId());
        }
        String newMessage = "";
        int i = 0;
        for (Slot timeSlot : timeSlots) {
            Stadium byStadiumId = stadiumService.findByStadiumId(timeSlot.getStadiumId());
            newMessage = newMessage + ++i + ".  " + byStadiumId.getNameStadium() + "   SOATIGA -> " + byStadiumId.getPrice() + "\n\n";
        }
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), newMessage);
        sendMessage.setReplyMarkup(inlineButton.getSlotNumber(map));
        currentUser.setState(UserState.ToChooseStadium);
        userService.update(currentUser);
        return sendMessage;
    }


    public SendMediaGroup toChooseStadium(String message, User currentUser){

        Stadium stadium = stadiumService.findByStadiumId(UUID.fromString(message));

        currentUser.setState(UserState.SendLocation);
        currentUser.setUpdateStadiumId(stadium.getId());
        userService.update(currentUser);

        List<InputMedia> medias = new ArrayList<>();

        if(stadium.getStadiumPage().size() == 1){
            InputMediaPhoto inputMediaPhoto = new InputMediaPhoto(stadium.getStadiumPage().get(0));
            medias.add(inputMediaPhoto);
        }
        for (int i = 0; i < stadium.getStadiumPage().size(); i++) {
            medias.add(new InputMediaPhoto(stadium.getStadiumPage().get(i)));
        }

        return new SendMediaGroup(currentUser.getChatId().toString(), medias);
    }

    public SendMessage enterDate(Message message, User currentUser){
        currentUser.setState(UserState.EnterTime);

        LocalDate localDate = LocalDate.parse(message.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        currentUser.setDate(localDate);
        userService.update(currentUser);
        if(slotService.checkDataSlot(localDate).isEmpty()){
            currentUser.setState(UserState.EnterDate);
            userService.update(currentUser);
            SendMessage sendMessage = new SendMessage(message.getChatId().toString(), """
                    BU SANALARDA BUSH POLYA YUQ🤦‍♂️

                    YANGI DATE KIRITING""");
            sendMessage.setReplyMarkup(dateButtons.getWeek());
            return sendMessage;
        }
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "QAYSI SOATLARDA KK");
        sendMessage.setReplyMarkup(timeButtons.getTimesUserHandler(localDate));
        return sendMessage;
    }

    public SendMessage payment(Message message, User currentUser){
        if(message.hasPhoto()){
            currentUser.setState(UserState.MAIN_MENU);
            Slot slot = slotService.findByDataAndFromTo(currentUser.getDate(), currentUser.getFromTo()).get();
            slot.setStadiumId(currentUser.getUpdateStadiumId());
            slotService.update(slot);
            slot.setState(StadiumState.Booked);
            slotService.update(slot);
            userService.update(currentUser);
            return new SendMessage(message.getChatId().toString(), "Maroqli Hordiq tilayman!!!👍");
        }
        return new SendMessage(message.getChatId().toString(), "Senga Chekni tawla dedm🧐🧐");
    }

    public SendLocation sendLocation(String  message, User currentUser) {
        currentUser.setState(UserState.OlishOlmaslik);
        userService.update(currentUser);
        Stadium stadium = stadiumService.findByStadiumId(currentUser.getUpdateStadiumId());
        SendLocation sendLocation = new SendLocation(currentUser.getChatId().toString(),
                stadium.getStadiumLocation().getLatitude(), stadium.getStadiumLocation().getLongitude() );
        ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
        sendLocation.setReplyMarkup(buttons.getYesAndNo());
        return sendLocation;
    }

    public SendMessage oliwOlmaslik(Message message, User currentUser) {
        if(message.getText().equals("NO")){
            currentUser.setState(UserState.LocationAndDate);
            userService.update(currentUser);
            SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "QAYSI TARTIBDA QIDIRMOQCHISIZ? 🧐 ");
            ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
            sendMessage.setReplyMarkup(buttons.getLocationAndDate());
            return sendMessage;
        }
        if(message.getText().equals("YES")){
            currentUser.setState(UserState.PAYMENT);
            userService.update(currentUser);
            Stadium stadium = stadiumService.findByStadiumId(currentUser.getUpdateStadiumId());
            SendMessage sendMessage  = new SendMessage(message.getChatId().toString(), stadium.getNameStadium() +
                    "   SOATIGA -> " + stadium.getPrice() + "\n\n\nTo'lov chekini Tawlang {RASM}");
            ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
            sendMessage.setReplyMarkup(remove);
            return sendMessage;
        }
        return new SendMessage(message.getChatId().toString(), "NIMASAN SAN❌❌❌");
    }

    public SendMessage shareLocationPolya(Message message, User currentUser) {
        if(message.hasLocation()){
            currentUser.setState(UserState.Stadium_Selection);
            ArrayList<Stadium> stadiums = stadiumService.getAll();

            LinkedHashMap<String, Stadium> map = new LinkedHashMap<>();
            for (Stadium stadium : stadiums) {
                double distance1 = distance.distance(stadium.getStadiumLocation().getLatitude(), stadium.getStadiumLocation().getLongitude(),
                        message.getLocation().getLatitude(), message.getLocation().getLongitude(), "K");
                if(distance1 <= 5){
                    map.put(String.valueOf(distance1).substring(0, 4), stadium);
                }
            }

            if(map.isEmpty()){
                currentUser.setState(UserState.LocationAndDate);
                SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "Sizga 5 Radiusda polya yuq🤦‍♂️");
                ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
                sendMessage.setReplyMarkup(buttons.getLocationAndDate());
                return sendMessage;
            }
            String newMessage = "";
            int i = 0;

            for (Map.Entry<String , Stadium> entry : map.entrySet()) {
                newMessage = newMessage + ++i + " :   => " + entry.getValue().getNameStadium() + "  :  SOATIGA ->  " + entry.getValue().getPrice()
                        + "\nSizdan ->   " + entry.getKey() + " : KM   UZOQLIKDA. " + "\n\n\n";
            }

            SendMessage sendMessage  = new SendMessage(message.getChatId().toString(), newMessage);
            ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
            sendMessage.setReplyMarkup(remove);
            sendMessage.setReplyMarkup(inlineButton.getStadiumSelection(map));
            return sendMessage;
        }
        return new SendMessage(message.getChatId().toString(), "NIMASAN SEN❌ \n Location Tawla dedm");
    }


    public SendMessage enterDateCallBack(Message message, User currentUser) {
        currentUser.setState(UserState.EnterTimeCallBack);
        LocalDate localDate = LocalDate.parse(message.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        currentUser.setDate(localDate);
        userService.update(currentUser);
        if(slotService.checkDataSlotCllBack(localDate, currentUser.getUpdateStadiumId()).isEmpty()){
            currentUser.setState(UserState.EnterDateCallBack);
            userService.update(currentUser);
            SendMessage sendMessage = new SendMessage(message.getChatId().toString(), """
                    BU SANALARDA  POLYA BAND🤦‍♂️

                    YANGI DATE KIRITING""");
            sendMessage.setReplyMarkup(dateButtons.getWeek());
            return sendMessage;
        }

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "QAYSI SOATLARDA KK");
        sendMessage.setReplyMarkup(timeButtons.getTimesUserCallBack(localDate, currentUser.getUpdateStadiumId()));
        return sendMessage;
    }

    public SendMessage enterTimeCallBack(Message message, User currentUser) {
        currentUser.setFromTo(message.getText());
        currentUser.setState(UserState.PAYMENT);
        userService.update(currentUser);
        if(slotService.checkOpenedOrder(currentUser.getUpdateStadiumId(), currentUser.getDate(), currentUser.getFromTo())){
            currentUser.setState(UserState.PAYMENT);
            userService.update(currentUser);

            SendMessage sendMessage  = new SendMessage(message.getChatId().toString(), "To'lov chekini Tawlang {RASM}");
            ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
            sendMessage.setReplyMarkup(remove);
            return sendMessage;
        }
        currentUser.setState(UserState.EnterTimeCallBack);
        userService.update(currentUser);
        SendMessage sendMessage  = new SendMessage(message.getChatId().toString(), "Bu vaqtta polya band\n Bowqa vaqt tanlang ");
        ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
        sendMessage.setReplyMarkup(timeButtons.getTimes());
        return sendMessage;
    }

    public SendMessage enterYesAndNoCallBack(Message message, User currentUser) {
        if(message.getText().equals("NO")){
            currentUser.setState(UserState.LocationAndDate);
            userService.update(currentUser);
            SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "QAYSI TARTIBDA QIDIRMOQCHISIZ? 🧐 ");
            ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
            sendMessage.setReplyMarkup(buttons.getLocationAndDate());
            return sendMessage;
        }

        if(message.getText().equals("YES")){
            currentUser.setState(UserState.EnterDateCallBack);
            userService.update(currentUser);
            SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "QAYSI SANADA KERAK SIZGA ");
            sendMessage.setReplyMarkup(dateButtons.getWeek());
            return sendMessage;
        }
        return new SendMessage(message.getChatId().toString(), "NOTUGRI BUYRUQ❌");
    }

    public SendMediaGroup callBackStadiumPage(String data, User currentUser) {
        Stadium stadium = stadiumService.findByStadiumId(UUID.fromString(data));
        currentUser.setUpdateStadiumId(stadium.getId());
        userService.update(currentUser);
        List<InputMedia> medias = new ArrayList<>();
        if(stadium.getStadiumPage().size() == 1){
            InputMediaPhoto inputMediaPhoto = new InputMediaPhoto(stadium.getStadiumPage().get(0));
            medias.add(inputMediaPhoto);
        }
        for (int i = 0; i < stadium.getStadiumPage().size(); i++) {
            medias.add(new InputMediaPhoto(stadium.getStadiumPage().get(i)));
        }

        return new SendMediaGroup(currentUser.getChatId().toString(), medias);
    }

    public SendLocation callBackStadiumLocation(String data, User currentUser) {
        Stadium stadium = stadiumService.findByStadiumId(UUID.fromString(data));
        return new SendLocation(currentUser.getChatId().toString(),
                stadium.getStadiumLocation().getLatitude(), stadium.getStadiumLocation().getLongitude() );
    }
}
