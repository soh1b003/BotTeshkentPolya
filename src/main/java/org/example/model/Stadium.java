package org.example.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stadium extends Base {
    private UUID userId;
    private String nameStadium;
    private List<String> stadiumPage = new ArrayList<>();
    private Location stadiumLocation;
    private String price;
}
