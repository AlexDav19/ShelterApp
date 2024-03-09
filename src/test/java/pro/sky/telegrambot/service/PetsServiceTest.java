package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Update;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PetsServiceTest {

    @Mock
    PetsRepository petsRepository;

    @InjectMocks
    PetsService petsService;

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
        Collection<Pets> expected = new ArrayList<>();
        Pets pet1 = new Pets("TestName1", "TestBreed1", 1, "Pets1");
        Pets pet2 = new Pets("TestName2", "TestBreed2", 2, "Pets2");
        expected.add(pet1);
        expected.add(pet2);

        when(petsRepository.findAll()).thenReturn((List<Pets>) expected);
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

    //todo Нужно понять как передавать update
    @Test
    public void petInfoById_success() {
//        Pets pet = new Pets("TestName", "TestBreed", 1, "Pets1");
//        Update update = new Update();
//        String message1 = "Питомец с идентификатором 1 не найден в базе данных.";
//        Long id = 1l;
//
 //       when(update.message().chat().id()).thenReturn(1L);
//        SendMessage expected = new SendMessage(1L, message1);
//        when(petsService.petInfoById(update, 1L)).thenReturn(expected);
//        when(new SendMessage(Long.class, message1)).thenReturn(expected);
//        //Optional<Pets> pets = petsRepository.findById(pet.getId());
//        //when(pets.isPresent()).thenReturn(true);
//        //when(petsRepository.existsById(pet.getId())).thenReturn(true);
//        //when(petsRepository.findById(pet.getId())).thenReturn(pets);
//
//        SendMessage actual = petsService.petInfoById(update, 1L);
//        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void petInfoById_exception() {
        Pets pet = new Pets("TestName", "TestBreed", 1, "Pets1");
        Update update = new Update();
        Assertions.assertThrows(RuntimeException.class, () -> petsService.petInfoById(update, pet.getId()));
    }
}
