package ru.taf.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.taf.entity.AppDocument;

public interface FileService {
    AppDocument processDoc(Message externalMessage);
}
