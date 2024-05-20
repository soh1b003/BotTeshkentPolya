package org.example.bot.buttons;

import org.example.enumators.StadiumState;
import org.example.model.Slot;
import org.example.model.Stadium;
import org.telegram.telegrambots.meta.api.objects.ReplyParameters;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static org.example.bot.MyBot.slotService;
import static org.example.bot.MyBot.stadiumService;

public class Buttons {
    public ReplyKeyboardMarkup getUserRole() {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("E'lon quwiw");
        row1.add("E'lonlarni kuriw");

        keyboardRows.add(row1);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getYesAndNo() {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("YES");
        row1.add("NO");

        keyboardRows.add(row1);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }


    public ReplyKeyboardMarkup requestContact() {
        KeyboardButton button = new KeyboardButton("\uD83D\uDCDEShare your number");
        button.setRequestContact(true);
        ReplyKeyboardMarkup replyKeyboardMarkup =
                new ReplyKeyboardMarkup(List.of(new KeyboardRow(List.of(button))));
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup requestLocation() {
        KeyboardButton button = new KeyboardButton("\uD83D\uDCCDShare your location");
        button.setRequestLocation(true);
        ReplyKeyboardMarkup replyKeyboardMarkup =
                new ReplyKeyboardMarkup(List.of(new KeyboardRow(List.of(button))));
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getStadiumNumber(){
        List<KeyboardRow> buttons = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        List<Stadium> all = stadiumService.getAll();
        int i = 0;
        for (Stadium stadium : all) {
            row1.add(String.valueOf(++i));
        }


        buttons.add(row1);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }


    public ReplyKeyboardMarkup getSlotNumber(int size){
        List<KeyboardRow> buttons = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();

        for (int i = 1; i <= size; i++) {
            row1.add(String.valueOf(i));
        }

        buttons.add(row1);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getLocationAndDate() {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("TIME");
        row1.add("LOCATION");

        keyboardRows.add(row1);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboard arraySize(int size) {
        List<KeyboardRow> buttons = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();

        for (int i = 1; i <= size; i++) {
            row1.add(String.valueOf(i));
        }
        buttons.add(row1);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboard menuAdvertiser() {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("E'lonlarim");
        row1.add("E'lon Quwiw");

        keyboardRows.add(row1);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }
}
