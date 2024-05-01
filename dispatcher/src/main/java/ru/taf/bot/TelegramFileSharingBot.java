package ru.taf.bot;



import lombok.extern.java.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component

public class TelegramFileSharingBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;
    private static final Logger log = LogManager.getLogger(TelegramFileSharingBot.class);

    public TelegramFileSharingBot(@Value("${bot.token}") String botToken){
        super(botToken);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        var originalMessage = update.getMessage();
        log.info(originalMessage.getText());

        SendMessage response = new SendMessage();
        response.setChatId(originalMessage.getChatId());
        response.setText("АААААААААААА МАСЛИНУ ПОЙМАЛ");

        sendAnswerMessage(response);
    }

    public void sendAnswerMessage(SendMessage message){
        if(message != null){
            try {
                execute(message);
            } catch (TelegramApiException e){
                log.error(e);
            }
        }
    }
}
