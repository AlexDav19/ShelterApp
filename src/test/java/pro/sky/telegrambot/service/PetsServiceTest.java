package pro.sky.telegrambot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.entity.Pets;
import pro.sky.telegrambot.repository.PetsRepository;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PetsServiceTest {

    @Mock
    Update update;
    @Mock
    Message message;
    @Mock
    Chat chat;
    @Mock
    TelegramBot telegramBot;

    @Mock
    PetsRepository petsRepository;

    @InjectMocks
    PetsService petsService;

    public static String getJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
//            LOGGER.error("Error parsing log entry", e);
            return null;
        }
    }

    @Test
    public void createPetsTest_success() throws FileNotFoundException {
        Pets expected = new Pets("TestName", "TestBreed", 1, "src/main/resources/pets/Pet1.jpg");
        when(petsRepository.save(expected)).thenReturn(expected);
        Pets actual = petsService.createPets(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void createPetsTest_exception() throws FileNotFoundException {
        Pets pet = new Pets("TestName", "TestBreed", 1, "PetsException");
        Assertions.assertThrows(FileNotFoundException.class, () -> petsService.createPets(pet));
    }

    @Test
    public void getPetsTest_success() {
        Pets expected = new Pets("TestName", "TestBreed", 1, "Pets1");
        when(petsRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        Pets actual = petsService.getPetById(expected.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllPetsTest_success() {
        List<Pets> expected = new ArrayList<>();
        Pets pet1 = new Pets("TestName1", "TestBreed1", 1, "Pets1");
        Pets pet2 = new Pets("TestName2", "TestBreed2", 2, "Pets2");
        expected.add(pet1);
        expected.add(pet2);

        when(petsRepository.findAll()).thenReturn(expected);
        Collection<Pets> actual = petsService.getAllPet();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void updatePetsTest_success() throws FileNotFoundException {
        Pets expected = new Pets("TestName2", "TestBreed2", 2, "src/main/resources/pets/Pet2.jpg");
        when(petsRepository.save(expected)).thenReturn(expected);
        when(petsRepository.existsById(expected.getId())).thenReturn(true);
        Pets actual = petsService.updatePet(expected.getId(), expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void updatePetsTest_NotFoundPet() throws FileNotFoundException {
        Pets pet = new Pets("TestName2", "TestBreed2", 2, "src/main/resources/pets/Pet1.jpg");
        Assertions.assertNull(petsService.updatePet(pet.getId(), pet));
    }
    @Test
    public void updatePetsTest_exception() throws FileNotFoundException {
        Pets pet = new Pets("TestName2", "TestBreed2", 2, "PetsException");
        Assertions.assertThrows(FileNotFoundException.class, () -> petsService.updatePet(pet.getId(), pet));
    }

    @Test
    public void deletePetsTest_success() {
        Assertions.assertDoesNotThrow(() -> petsService.deletePet(1L));
    }

    @Test
    public void petInfoById_success() {
        Pets pet = new Pets("TestName2", "TestBreed2", 2, "src/main/resources/pets/Pet134.jpg");
        String msg = "Имя: " + pet.getName() + "\n" +
                "Порода: " + pet.getBreed() + "\n" +
                "Возраст: " + pet.getAge();
        Long id = 1L;

        when(petsRepository.findById(id)).thenReturn(Optional.of(pet));
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(id);
        when(update.message().chat().id()).thenReturn(id);
        SendMessage expected = new SendMessage(id, msg);

        //test execution
        SendMessage actual = petsService.petInfoById(update, id);
        assertEquals(getJsonString(expected), getJsonString(actual));
    }

    @Test
    public void petInfoById_notFind() {
        String msg = "Питомец с идентификатором 1 не найден в базе данных.";
        Long id = 1L;

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(id);
        when(update.message().chat().id()).thenReturn(id);

        SendMessage expected = new SendMessage(id, msg);

        //test execution
        SendMessage actual = petsService.petInfoById(update, id);
        assertEquals(getJsonString(expected), getJsonString(actual));
    }

    @Test
    public void petInfoById_exception() {
        Pets pet = new Pets("TestName", "TestBreed", 1, "Pets1");
        Update update = new Update();
        Assertions.assertThrows(RuntimeException.class, () -> petsService.petInfoById(update, pet.getId()));
    }
}
