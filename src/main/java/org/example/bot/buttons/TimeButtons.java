package org.example.bot.buttons;

import org.example.enumators.StadiumState;
import org.example.model.Slot;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDate;
import java.util.*;

import static org.example.bot.MyBot.slotService;

public class TimeButtons {
    public ReplyKeyboardMarkup getTimes() {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        KeyboardRow row5 = new KeyboardRow();
        KeyboardRow row6 = new KeyboardRow();
        row1.add("17:00 vs 18:00");
        row2.add("18:00 vs 19:00");
        row3.add("19:00 vs 20:00");
        row4.add("20:00 vs 21:00");
        row5.add("21:00 vs 22:00");
        row6.add("22:00 vs 23:00");

        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardRows.add(row3);
        keyboardRows.add(row4);
        keyboardRows.add(row5);
        keyboardRows.add(row6);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getTimesUserHandler(LocalDate date) {
        ArrayList<Slot> slots = slotService.getAll();

        HashSet<String> newSlots = new HashSet<>();

        for (Slot slot : slots) {
            if(Objects.equals(slot.getDate(), date) && Objects.equals(slot.getState(), StadiumState.NotBooked)){
                newSlots.add(slot.getFromTo());
            }
        }
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        for (String slot : newSlots) {
            KeyboardRow row = new KeyboardRow();
            row.add(slot);
            keyboardRows.add(row);
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;

    }

    public ReplyKeyboardMarkup getTimesUserCallBack(LocalDate localDate, UUID updateStadiumId) {
        ArrayList<Slot> slots = slotService.getAll();

        HashSet<String> newSlots = new HashSet<>();

        for (Slot slot : slots) {
            if(Objects.equals(slot.getDate(), localDate) && Objects.equals(slot.getState(), StadiumState.NotBooked)
              && Objects.equals(slot.getStadiumId(), updateStadiumId)){
                newSlots.add(slot.getFromTo());
            }
        }
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        for (String slot : newSlots) {
            KeyboardRow row = new KeyboardRow();
            row.add(slot);
            keyboardRows.add(row);
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

}
