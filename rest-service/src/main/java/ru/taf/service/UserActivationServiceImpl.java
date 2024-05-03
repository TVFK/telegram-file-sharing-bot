package ru.taf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.taf.repository.TgUserRepository;
import ru.taf.utils.CryptoTool;

@RequiredArgsConstructor
@Service
public class UserActivationServiceImpl implements UserActivationService {

    private final TgUserRepository tgUserRepository;

    private final CryptoTool cryptoTool;

    @Override
    public boolean activation(String cryptoUserId) {
        var userId = cryptoTool.idOf(cryptoUserId);
        var optional = tgUserRepository.findById(userId);
        if (optional.isPresent()) {
            var user = optional.get();
            user.setIsActive(true);
            tgUserRepository.save(user);
            return true;
        }
        return false;
    }
}