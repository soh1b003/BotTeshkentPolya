package org.example.repository;

import org.example.model.Slot;

public class SlotRepository extends BaseRepository<Slot>{
    public SlotRepository() {
        super.path = "src/main/resources/slot.json";
        super.type = Slot.class;
    }
}
