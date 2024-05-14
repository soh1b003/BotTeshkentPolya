package org.example.model;

import lombok.*;
import org.example.enumators.UserRole;
import org.example.enumators.UserState;
import org.telegram.telegrambots.meta.api.objects.Location;

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
}
