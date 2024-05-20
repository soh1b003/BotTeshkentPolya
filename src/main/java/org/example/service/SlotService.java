package org.example.service;

import org.example.enumators.StadiumState;
import org.example.model.Slot;
import org.example.repository.SlotRepository;

import java.io.ObjectStreamException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class SlotService extends BaseService<Slot, SlotRepository> {
    public SlotService(SlotRepository repository) {
        super(repository);
    }

    public void update(Slot updated) {
        ArrayList<Slot> all = repository.getAll();
        Integer i = 0;
        for (Slot slot  : all) {
            if (Objects.equals(slot.getId(), updated.getId())) {
                all.set(i, updated);
                break;
            }
            i++;
        }
        repository.writeData(all);
    }

    public Slot findBySlotId(UUID slotId){
        return getAll().stream().filter((slot) -> Objects.equals(slot.getId(), slotId)).findFirst().get();
    }

    public ArrayList<Slot> getTimeSlots(Slot slot) {
        return repository.getTimeSlots(slot);
    }


    public Optional<Slot> checkDataSlot(LocalDate localDate) {
        return getAll().stream().filter(slot -> Objects.equals(slot.getDate(), localDate)).findFirst();
    }

    public ArrayList<Slot> getDateAndTime(LocalDate date, String fromTo) {
        return (ArrayList<Slot>) getAll().stream().filter(slot -> (Objects.equals(slot.getDate(), date) &&
                Objects.equals(slot.getFromTo(), fromTo))).collect(Collectors.toList());
    }

    public Optional<Slot> findByDataAndFromTo(LocalDate date, String fromTo) {
        return getAll().stream().filter(slot -> (Objects.equals(slot.getDate(), date) && Objects.equals(slot.getFromTo(), fromTo))).findFirst();
    }

    public Optional<Slot> checkDataSlotCllBack(LocalDate localDate, UUID updateStadiumId) {
        return getAll().stream().filter(slot -> (Objects.equals(slot.getDate(), localDate) && Objects.equals(slot.getStadiumId(), updateStadiumId))).findFirst();
    }

    public boolean checkOpenedOrder(UUID updateStadiumId, LocalDate date, String fromTo) {
        for (Slot slot : getAll()) {
            if(Objects.equals(slot.getStadiumId(), updateStadiumId) &&
            Objects.equals(slot.getDate(), date) &&
            Objects.equals(slot.getFromTo(), fromTo)){
                if(Objects.equals(slot.getState(), StadiumState.Booked)){
                    return false;
                }
            }
        }
        return true;
    }
}
