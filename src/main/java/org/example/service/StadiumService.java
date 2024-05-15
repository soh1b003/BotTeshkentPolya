package org.example.service;

import org.example.model.Stadium;
import org.example.repository.StadiumRepository;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class StadiumService extends BaseService<Stadium, StadiumRepository> {
    public StadiumService(StadiumRepository repository) {
        super(repository);
    }

    public void update(Stadium updated) {
        ArrayList<Stadium> all = repository.getAll();
        Integer i = 0;
        for (Stadium stadium  : all) {
            if (Objects.equals(stadium.getId(), updated.getId())) {
                all.set(i, updated);
                break;
            }
            i++;
        }
        repository.writeData(all);
    }

    public Stadium findByStadiumId(UUID stadiumId){
        return getAll().stream().filter((stadium) -> Objects.equals(stadium.getId(), stadiumId)).findFirst().get();
    }
}
