package ru.taf.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.taf.entity.AppDocument;
import ru.taf.entity.AppPhoto;

public interface FileService {
    AppDocument processDoc(Message telegraMessage);
    AppPhoto processPhoto(Message telegraMessage);
}
