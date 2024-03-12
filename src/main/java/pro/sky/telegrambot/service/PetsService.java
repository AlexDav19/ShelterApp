package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.Pets;
import pro.sky.telegrambot.repository.PetsRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Optional;

@Service
public class PetsService {

    Logger logger = LoggerFactory.getLogger(PetsService.class);

    private final TelegramBot telegramBot;

    private final PetsRepository petsRepository;

    public PetsService(TelegramBot telegramBot, PetsRepository petsRepository) {
        this.telegramBot = telegramBot;
        this.petsRepository = petsRepository;
    }


    public Pets createPets(Pets pet) throws FileNotFoundException {
        logger.debug("Вызван метод createPets");
        File file = new File(pet.getPhoto());
        if (file.exists()) {
            return petsRepository.save(pet);
        } else throw new FileNotFoundException();
    }

    public Pets getPetById(Long petId) {
        logger.debug("Вызван метод getPetById");
        return petsRepository.findById(petId).get();
    }

    public Collection<Pets> getAllPet() {
        logger.debug("Вызван метод getPetById");
        return petsRepository.findAll();
    }

    public Pets updatePet(Long id, Pets pet) throws FileNotFoundException {
        logger.debug("Вызван метод updatePet");
        File file = new File(pet.getPhoto());
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        if (petsRepository.existsById(id)) {
            pet.setId(id);
            petsRepository.save(pet);
            return pet;
        } else {
            return null;
        }
    }

    public void deletePet(Long petId) {
        logger.debug("Вызван метод deletePet");
        petsRepository.deleteById(petId);
    }

    public SendMessage petInfoById(Update update, Long id) {
        //try-catch - Проверка доступа к БД
        try {
            //retrieve the pets info
            Optional<Pets> pet = petsRepository.findById(id);

            String msg = "";
            if (pet.isPresent()) {
                msg = "Имя: " + pet.get().getName() + "\n"
                        + "Порода: " + pet.get().getBreed() + "\n"
                        + "Возраст: " + pet.get().getAge();
                SendPhoto schema = new SendPhoto(update.message().chat().id(), new File(pet.get().getPhoto()));
                telegramBot.execute(schema);
            } else {
                msg = "Питомец с идентификатором " + id + " не найден в базе данных.";
            }
            return new SendMessage(update.message().chat().id(), msg);
        } catch (RuntimeException e) {
            return new SendMessage(update.message().chat().id(), "Ошибка доступа к списку питомцев, попробуйте позже");
        }
    }

}
