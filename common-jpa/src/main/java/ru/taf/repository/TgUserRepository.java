package ru.taf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.taf.entity.TgUser;

public interface TgUserRepository extends JpaRepository<TgUser, Long> {

   TgUser findTgUserByTelegramUserId(Long TelegramUserId);

}
