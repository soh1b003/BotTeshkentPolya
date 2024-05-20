package org.example.repository;

import org.example.model.Stadium;
import org.example.model.User;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class StadiumRepository extends BaseRepository<Stadium> {
    public StadiumRepository() {
        super.path = "src/main/resources/stadium.json";
        super.type = Stadium.class;
    }
}
