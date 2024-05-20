package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;
import org.example.enumators.UserRole;
import org.example.enumators.UserState;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User extends Base{
    private String firstName;
    private String lastName;
    private String username;
    private UserRole role;
    private UserState state;
    private Long chatId;
    private String phoneNumber;
    private Location locationUser;
    private UUID updateStadiumId;
    private UUID updateSlotId;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;
    private String fromTo;
}
