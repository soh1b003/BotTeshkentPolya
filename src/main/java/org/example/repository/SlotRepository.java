package org.example.repository;

import org.example.enumators.StadiumState;
import org.example.model.Slot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class SlotRepository extends BaseRepository<Slot>{
    public SlotRepository() {
        super.path = "src/main/resources/slot.json";
        super.type = Slot.class;
    }


    public ArrayList<Slot> getTimeSlots(Slot slot1) {
        for (Slot slot : getAll()) {
            if(Objects.equals(slot.getFromTo(), slot1.getFromTo()) &&
                    Objects.equals(slot.getDate(), slot1.getDate())
                    &&  Objects.equals(slot.getState(), StadiumState.Booked)){
                return new ArrayList<Slot>();
            }
        }
        return (ArrayList<Slot>) getAll().stream().filter(slot -> (Objects.equals(slot.getFromTo(), slot1.getFromTo()) &&
                !Objects.equals(slot.getDate(), slot1.getDate())
                 &&  Objects.equals(slot.getState(), StadiumState.NotBooked))).collect(Collectors.toList());
    }




}
