package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.Adoptions;
import pro.sky.telegrambot.repository.AdoptionsRepository;
import pro.sky.telegrambot.repository.CustomersRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class AdoptionsService {

    Logger logger = LoggerFactory.getLogger(AdoptionsService.class);
    @Autowired
    private final TelegramBot telegramBot;
    @Autowired
    private final AdoptionsRepository adoptionsRepository;
    @Autowired
    private CustomersRepository customersRepository;

    public AdoptionsService(TelegramBot telegramBot, AdoptionsRepository adoptionsRepository, CustomersRepository customersRepository) {
        this.telegramBot = telegramBot;
        this.adoptionsRepository = adoptionsRepository;
        this.customersRepository = customersRepository;
    }

    public Adoptions createAdoptions(Adoptions adoptions) {
        logger.debug("Вызван метод createAdoptions");
        return adoptionsRepository.save(adoptions);
    }

    public Adoptions getAdoptionsById(Long adoptionsId) {
        logger.debug("Вызван метод getAdoptionsById");
        return adoptionsRepository.findById(adoptionsId).get();
    }

    public Collection<Adoptions> getAllAdoptions() {
        logger.debug("Вызван метод getAdoptionsById");
        return adoptionsRepository.findAll();
    }

    public Adoptions updateAdoptions(Long id, Adoptions adoptions) {
        logger.debug("Вызван метод updateAdoptions");
        adoptions.setId(id);
        adoptionsRepository.save(adoptions);
        return adoptions;
    }

    public void deleteAdoptions(Long adoptionsId) {
        logger.debug("Вызван метод deleteAdoptions");
        adoptionsRepository.deleteById(adoptionsId);
    }

    public Adoptions trialEndPlus14Days(Long adoptionsId) {
        logger.debug("Вызван метод trialEndPlus14Days");
        Adoptions adoptions = adoptionsRepository.findById(adoptionsId).get();
        LocalDateTime trialEnd = adoptions.getTrialEnd();
        adoptions.setTrialEnd(trialEnd.plusDays(14));
        adoptionsRepository.save(adoptions);
        return adoptions;
    }

    public Adoptions trialEndPlus30Days(Long adoptionsId) {
        logger.debug("Вызван метод trialEndPlus30Days");
        Adoptions adoptions = adoptionsRepository.findById(adoptionsId).get();
        LocalDateTime trialEnd = adoptions.getTrialEnd();
        adoptions.setTrialEnd(trialEnd.plusDays(30));
        adoptionsRepository.save(adoptions);
        return adoptions;
    }

    public Adoptions trialEndSuccess(Long adoptionsId) {
        logger.debug("Вызван метод trialEndSuccess");
        Adoptions adoptions = adoptionsRepository.findById(adoptionsId).get();
        adoptions.setTrialSuccess(true);
        adoptionsRepository.save(adoptions);
        Long chatId = customersRepository.findById(adoptions.getCustomerId()).get().getChatId();
        String text = "Поздравляем с успешным прохождением испытательного срока!";
        telegramBot.execute(new SendMessage(chatId, text));

        return adoptions;
    }
}
