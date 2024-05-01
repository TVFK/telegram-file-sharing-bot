package ru.taf.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.taf.bot.TelegramFileSharingBot;
import ru.taf.service.UpdateProducerImpl;
import ru.taf.util.MessageUtils;

import static ru.taf.RabbitQueue.*;


@Component
@Log4j2
public class UpdateController {
    private TelegramFileSharingBot sharingBot;
    private final MessageUtils messageUtils;
    private final UpdateProducerImpl updateProducer;

    public UpdateController(MessageUtils messageUtils, UpdateProducerImpl updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }

    public void registerBot(TelegramFileSharingBot sharingBot){
        this.sharingBot = sharingBot;
    }

    public void processUpdate(Update update){
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if(update.getMessage() != null){
            distributeMessageByType(update);
        } else{
            log.error("Received unsupported message type" + update);
        }
    }

    private void distributeMessageByType(Update update) {
        Message message = update.getMessage();
        
        if(message.getText() != null){
            processTextMessage(update);
        } else if(message.getDocument() != null){
            processDocMessage(update);
        } else if(message.getPhoto() != null){
            processPhotoMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setUnsupportedMessageTypeView(Update update) {
        SendMessage sendMessage = messageUtils.generateSendMessageWithText(update, "Неподерживаемый тип сообщения!");
        setView(sendMessage);
    }


    private void setFileIsReceivedView(Update update) {
        SendMessage sendMessage = messageUtils.generateSendMessageWithText(update, "Обрабатывается...");
        setView(sendMessage);
    }

    private void setView(SendMessage sendMessage){
        sharingBot.sendAnswerMessage(sendMessage);
    }


    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE.getVal(), update);
        setFileIsReceivedView(update);
    }

    private void processDocMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE.getVal(), update);
        setFileIsReceivedView(update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE.getVal(), update);
        setFileIsReceivedView(update);
    }
}
