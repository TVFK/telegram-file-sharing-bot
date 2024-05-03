package ru.taf.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.taf.entity.AppDocument;
import ru.taf.entity.AppPhoto;
import ru.taf.repository.AppDocumentRepository;
import ru.taf.repository.AppPhotoRepository;
import ru.taf.utils.CryptoTool;

@Log4j2
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final AppDocumentRepository appDocumentDAO;

    private final AppPhotoRepository appPhotoDAO;

    private final CryptoTool cryptoTool;

    @Override
    public AppDocument getDocument(String hash) {
        var id = cryptoTool.idOf(hash);
        if (id == null) {
            return null;
        }
        return appDocumentDAO.findById(id).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String hash) {
        var id = cryptoTool.idOf(hash);
        if (id == null) {
            return null;
        }
        return appPhotoDAO.findById(id).orElse(null);
    }
}