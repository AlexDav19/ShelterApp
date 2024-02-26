package pro.sky.telegrambot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.Shelters;
import pro.sky.telegrambot.repository.SheltersRepository;

import java.util.Collection;

@Service
public class SheltersService {

    Logger logger = LoggerFactory.getLogger(SheltersService.class);

    private final SheltersRepository sheltersRepository;

    public SheltersService(SheltersRepository sheltersRepository) {
        this.sheltersRepository = sheltersRepository;
    }


    public Shelters createShelter(Shelters shelter) {
        logger.debug("Вызван метод createShelter");
        return sheltersRepository.save(shelter);
    }

    public Shelters getShelterById(Long shelterId) {
        logger.debug("Вызван метод getShelterById");
        return sheltersRepository.findById(shelterId).get();
    }

    public Collection<Shelters> getAllShelter() {
        logger.debug("Вызван метод getShelterById");
        return sheltersRepository.findAll();
    }

    public Shelters updateShelter(Long id, Shelters shelter) {
        logger.debug("Вызван метод updateShelter");
        if (sheltersRepository.existsById(id)) {
            shelter.setId(id);
            sheltersRepository.save(shelter);
            return shelter;
        } else {
            return null;
        }
    }

    public void deleteShelter(Long shelterId) {
        logger.debug("Вызван метод deleteShelter");
        sheltersRepository.deleteById(shelterId);
    }
}
