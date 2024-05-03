package ru.taf.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.taf.entity.enums.UserState;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_tg_user")
public class TgUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long telegramUserId;

    private String firstName;

    private String lastName;

    private String userName;

    private String email;

    private Boolean isActive;

    private LocalDateTime firstLoginDate;

    @Enumerated(EnumType.STRING)
    private UserState userState;
}
