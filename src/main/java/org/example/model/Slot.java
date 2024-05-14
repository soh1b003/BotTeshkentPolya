package org.example.model;

import org.example.enumators.StadiumState;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Slot {
    private UUID userId;
    private UUID stadiumId;
    private LocalDate date;
    private LocalDateTime from;
    private LocalDateTime to;
    private StadiumState state;
}
