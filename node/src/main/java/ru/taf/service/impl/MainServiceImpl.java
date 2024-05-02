package ru.taf.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.taf.entity.RawData;
import ru.taf.repository.RawDataRepository;
import ru.taf.service.MainService;
import ru.taf.service.ProducerService;

@Service
public class MainServiceImpl implements MainService {
    private final RawDataRepository rawDataRepository;
    private final ProducerService producerService;

    public MainServiceImpl(RawDataRepository rawDataRepository, ProducerService producerService) {
        this.rawDataRepository = rawDataRepository;
        this.producerService = producerService;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRowData(update);

        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Hello motherfucker");
        producerService.produceAnswer(sendMessage);
    }

    private void saveRowData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();

        rawDataRepository.save(rawData);

    }
}
