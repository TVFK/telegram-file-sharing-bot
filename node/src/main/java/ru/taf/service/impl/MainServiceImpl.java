package ru.taf.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.taf.entity.AppDocument;
import ru.taf.entity.AppPhoto;
import ru.taf.entity.RawData;
import ru.taf.entity.TgUser;
import ru.taf.entity.enums.UserState;
import ru.taf.exceptions.UploadFileException;
import ru.taf.repository.RawDataRepository;
import ru.taf.repository.TgUserRepository;
import ru.taf.service.MainService;
import ru.taf.service.ProducerService;
import ru.taf.service.enums.ServiceCommand;

import static ru.taf.entity.enums.UserState.BASIC_STATE;
import static ru.taf.entity.enums.UserState.WAIT_FOR_EMAIL_STATE;
import static ru.taf.service.enums.ServiceCommand.*;

@Service
@Log4j2
public class MainServiceImpl implements MainService {
    private final RawDataRepository rawDataRepository;
    private final ProducerService producerService;
    private final TgUserRepository tgUserRepository;
    private final FileServiceImpl fileService;

    public MainServiceImpl(RawDataRepository rawDataRepository, ProducerService producerService, TgUserRepository tgUserRepository, FileServiceImpl fileService) {
        this.rawDataRepository = rawDataRepository;
        this.producerService = producerService;
        this.tgUserRepository = tgUserRepository;
        this.fileService = fileService;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRowData(update);
        TgUser tgUser = findOrSaveTgUser(update);
        UserState userState = tgUser.getUserState();
        String text = update.getMessage().getText();
        String output = "";

        var serviceCommand = ServiceCommand.fromValue(text);
        if (CANCEL.equals(serviceCommand)) {
            output = cancelProcess(tgUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(tgUser, text);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
            log.info("abobus");
        } else {
            log.error("Unknown user state: " + userState);
            output = "Неизвестная ошибка! Введите /cancel и попробуйте снова!";
        }
        
        Long chatId = update.getMessage().getChatId();
        sendAnswer(output, chatId);
    }

    @Override
    public void processDocMessage(Update update) {
        saveRowData(update);
        TgUser tgUser = findOrSaveTgUser(update);
        Long chatId = update.getMessage().getChatId();
        if(isNOtAllowToSetContent(chatId, tgUser)){
            return;
        }

        try {
            AppDocument doc = fileService.processDoc(update.getMessage());
            var answer = "Документ успешно загружен! "
                    + "Ссылка для скачивания: http://test.ru/get-doc/777";
            sendAnswer(answer, chatId);
        } catch (UploadFileException ex) {
            log.error(ex);
            String error = "К сожалению, загрузка файла не удалась. Повторите попытку позже.";
            sendAnswer(error, chatId);
        }
    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRowData(update);
        TgUser tgUser = findOrSaveTgUser(update);
        Long chatId = update.getMessage().getChatId();
        if(isNOtAllowToSetContent(chatId, tgUser)){
            return;
        }

        try {
            AppPhoto photo = fileService.processPhoto(update.getMessage());
            var answer = "Photo загружен! Ссылка для скачивания";
            sendAnswer(answer, chatId);
        } catch (UploadFileException ex) {
            log.error(ex);
            String error = "К сожалению, загрузка фото не удалась. Повторите попытку позже.";
            sendAnswer(error, chatId);
        }
    }

    private boolean isNOtAllowToSetContent(Long chatId, TgUser tgUser) {
        var userState = tgUser.getUserState();
        if(!tgUser.getIsActive()){
            var error = "Зарегайся, чушпан";
            sendAnswer(error, chatId);
            return true;
        } else if (!BASIC_STATE.equals(userState)) {
            var error = "Отмени текущую команду с помощью /cancel";
            sendAnswer(error, chatId);
            return true;
        }
        return false;
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.produceAnswer(sendMessage);
    }

    private String processServiceCommand(TgUser tgUser, String cmd){
        var serviceCommand = ServiceCommand.fromValue(cmd);
        if(REGISTRATION.equals(serviceCommand)){
            return "тут ничего нет";
        } else if(HELP.equals(serviceCommand)){
            return help();
        } else if(START.equals(serviceCommand)){
            return "Здарова, меченный, я с тобой в благодарности играть не буду, выполнишь для меня пару заданий и мы в расчёте. Задания в /help";
        } else {
            return "И что это? :|";
        }
    }

    private String help() {
        return "Список доступных команд: \n"
                + "/cancel - делает ГАЛЯ ОТМЕНА;\n" +
                "/registration - регистрация;\n";
    }

    private String cancelProcess(TgUser tgUser){
        tgUser.setUserState(BASIC_STATE);
        tgUserRepository.save(tgUser);
        return "Команда отменена!";
    }

    private TgUser findOrSaveTgUser(Update update){
        User telegramUser = update.getMessage().getFrom();

        TgUser persistentTgUser = tgUserRepository.findTgUserByTelegramUserId(telegramUser.getId());
        if(persistentTgUser == null){
            TgUser transientTgUser = TgUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .isActive(true)
                    .userState(BASIC_STATE)
                    .build();

            return tgUserRepository.save(transientTgUser);
        }
        return persistentTgUser;
    }

    private void saveRowData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();

        rawDataRepository.save(rawData);

    }
}
