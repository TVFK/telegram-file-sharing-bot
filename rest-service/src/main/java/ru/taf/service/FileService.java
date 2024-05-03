package ru.taf.service;

import ru.taf.entity.AppDocument;
import ru.taf.entity.AppPhoto;

public interface FileService {

    AppDocument getDocument(String id);

    AppPhoto getPhoto(String id);
}