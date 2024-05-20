package org.example.bot.buttons;

import org.example.model.Slot;
import org.example.model.Stadium;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

public class InlineButton {
    public InlineKeyboardMarkup getStadiumSelection(LinkedHashMap<String, Stadium> map){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();

        Collection<Stadium> values = map.values();

        int i = 1;
        for (Stadium value : values) {
            List<InlineKeyboardButton> list = new ArrayList<>();

            InlineKeyboardButton button = new InlineKeyboardButton(i++ + "");
            button.setCallbackData(value.getId().toString());
            list.add(button);
            lists.add(list);
        }
        markup.setKeyboard(lists);
        return markup;
    }

    public ReplyKeyboard getSlotNumber(LinkedHashMap<Slot, UUID> map) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();

        Collection<UUID> values = map.values();

        int i = 1;
        for (UUID value : values) {
            List<InlineKeyboardButton> list = new ArrayList<>();

            InlineKeyboardButton button = new InlineKeyboardButton(i++ + "");
            button.setCallbackData(value.toString());
            list.add(button);
            lists.add(list);
        }
        markup.setKeyboard(lists);
        return markup;
    }
}
