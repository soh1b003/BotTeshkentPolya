package org.example.service;

import org.example.model.Slot;
import org.example.model.Stadium;
import org.example.repository.SlotRepository;

import java.util.ArrayList;
import java.util.Objects;

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
}
