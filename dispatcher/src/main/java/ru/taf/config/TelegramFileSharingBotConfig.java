package ru.taf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.taf.bot.TelegramFileSharingBot;

@Configuration
public class TelegramFileSharingBotConfig {
    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramFileSharingBot sharingBot) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(sharingBot);
        return api;
    }
}
