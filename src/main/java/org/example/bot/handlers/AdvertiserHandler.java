package org.example.bot.handlers;

import lombok.SneakyThrows;
import org.example.enumators.StadiumState;
import org.example.enumators.UserState;
import org.example.model.Slot;
import org.example.model.Stadium;
import org.example.model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.example.bot.MyBot.*;
import static org.example.bot.MyBot.slotService;

public class AdvertiserHandler {

    public SendMessage nameStadium(Message message, User currentUser) {
        currentUser.setState(UserState.StadiumPage);
        Stadium add = stadiumService.add(new Stadium(currentUser.getId(), message.getText(), new ArrayList<>(), null, null));
        currentUser.setUpdateStadiumId(add.getId());
        userService.update(currentUser);
        return new SendMessage(message.getChatId().toString(), "Stadion rasmlarinini yuboring :");
    }

    @SneakyThrows
    public SendMessage stadiumPage(Message message, User currentUser) {
        if(message.hasPhoto()){
            currentUser.setState(UserState.LocationStadium);
            Stadium oldStadium = stadiumService.findByStadiumId(currentUser.getUpdateStadiumId());

            List<PhotoSize> photoSizes = message.getPhoto();
            String fileId = photoSizes.stream().sorted(Comparator.comparing(PhotoSize::getFileSize).reversed()).findFirst().orElse(null).getFileId();

            List<String> photos = oldStadium.getStadiumPage();
            photos.add(fileId);

            oldStadium.setStadiumPage(photos);
            stadiumService.update(oldStadium);
            userService.update(currentUser);
            SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "Stadion location  ");
            sendMessage.setReplyMarkup(buttons.requestLocation());
            return sendMessage;
        }
        return new SendMessage(message.getChatId().toString(), "Rasmlarni YUBOR DEYILDIKUU :");
    }

    public SendMessage locationStadium(Message message, User currentUser) {
        Long chatId = message.getChatId();
        if (message.hasLocation()) {
            Location location = message.getLocation();
            currentUser.setState(UserState.Price);
            userService.update(currentUser);
            Stadium byStadiumId = stadiumService.findByStadiumId(currentUser.getUpdateStadiumId());
            byStadiumId.setStadiumLocation(location);
            stadiumService.update(byStadiumId);
            SendMessage sendMessage = new  SendMessage(chatId.toString(), "1 soatlik narxini kiriting ");
            ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
            sendMessage.setReplyMarkup(remove);
            return sendMessage;
        }
        return new SendMessage(chatId.toString(), "please send your location");
    }

    public SendMessage priceStadium(Message message, User currentUser){
        currentUser.setState(UserState.LocalDate);
        userService.update(currentUser);
        Stadium byStadiumId = stadiumService.findByStadiumId(currentUser.getUpdateStadiumId());
        byStadiumId.setPrice(message.getText());
        stadiumService.update(byStadiumId);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "Sanani tanla ");
        sendMessage.setReplyMarkup(dateButtons.getWeek());
        return sendMessage;
    }



    public SendMessage localDate(Message message, User currentUser) {
        currentUser.setState(UserState.FromTo);
        String text = message.getText();
        LocalDate localDate = LocalDate.parse(text, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Slot add = slotService.add(new Slot(currentUser.getId(),
                currentUser.getUpdateStadiumId(), localDate,
                null, null));
        currentUser.setUpdateSlotId(add.getId());
        userService.update(currentUser);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "QAYSI VAQTLAR ORALIGIDA🧐 ");
        ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
        sendMessage.setReplyMarkup(timeButtons.getTimes());
        return sendMessage;
    }

    public SendMessage fromTo(Message message,  User currentUser){
        currentUser.setState(UserState.YesAndNo);
        userService.update(currentUser);
        Slot bySlotId = slotService.findBySlotId(currentUser.getUpdateSlotId());
        bySlotId.setFromTo(message.getText());
        bySlotId.setState(StadiumState.NotBooked);
        slotService.update(bySlotId);
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "Yana Vaqt kiritasizmi ");
        sendMessage.setReplyMarkup(buttons.getYesAndNo());
        return sendMessage;
    }


    public SendMessage yesAndNo(Message message,  User currentUser){

        if(message.getText().equals("NO")){
            currentUser.setState(UserState.MAIN_MENU);
            userService.update(currentUser);
            return new SendMessage(message.getChatId().toString(), "E'LON QUYGANINGIZ UCHUN RAHMAT👌 ");
        }
        if(message.getText().equals("YES")){
            currentUser.setState(UserState.PlusTime);
            userService.update(currentUser);
            SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "QAYSI VAQTLAR ORALIGIDA🧐 ");
            ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
            sendMessage.setReplyMarkup(timeButtons.getTimes());
            return sendMessage;
        }
        return new SendMessage(message.getChatId().toString(), "NOTUGRI BUYRUQ❌");
    }

    public SendMessage plusTime(Message message, User currentUser){
        currentUser.setState(UserState.MAIN_MENU);
        userService.update(currentUser);

        Slot bySlotId = slotService.findBySlotId(currentUser.getUpdateSlotId());
        slotService.add(new Slot(currentUser.getId(), bySlotId.getStadiumId(), bySlotId.getDate()
                , message.getText(), StadiumState.NotBooked ));
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "E'LON QUYGANINGIZ UCHUN RAHMAT👌 ");
        ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
        sendMessage.setReplyMarkup(remove);
        return sendMessage;
    }
}
