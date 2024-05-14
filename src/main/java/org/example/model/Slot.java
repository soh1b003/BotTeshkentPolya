package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.enumators.StadiumState;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Slot extends Base {
    private UUID userId;
    private UUID stadiumId;
    private LocalDate date;
    private LocalDateTime from;
    private LocalDateTime to;
    private StadiumState state;
}
