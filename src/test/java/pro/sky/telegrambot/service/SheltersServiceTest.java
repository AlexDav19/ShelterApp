package pro.sky.telegrambot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.entity.Shelters;
import pro.sky.telegrambot.repository.SheltersRepository;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SheltersServiceTest {

    @Mock
    SheltersRepository sheltersRepository;

    @InjectMocks
    SheltersService sheltersService;

    @Test
    public void createSheltersTest_success() throws FileNotFoundException {
        Shelters expected = new Shelters("TestAddress", "TestWorkingHours", "src/main/resources/map/Schema.jpg","TestPhoneMain" ,"TestPhoneSecurity");
        when(sheltersRepository.save(expected)).thenReturn(expected);
        Shelters actual = sheltersService.createShelter(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void createSheltersTest_exception() throws FileNotFoundException {
        Shelters shelters = new Shelters("TestAddress", "TestWorkingHours", "SchemaException","TestPhoneMain" ,"TestPhoneSecurity");
        Assertions.assertThrows(FileNotFoundException.class, () -> sheltersService.createShelter(shelters));
    }

    @Test
    public void getSheltersTest_success() {
        Shelters expected = new Shelters("TestAddress", "TestWorkingHours", "src/main/resources/map/Schema.jpg","TestPhoneMain" ,"TestPhoneSecurity");
        when(sheltersRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        Shelters actual = sheltersService.getShelterById(expected.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllSheltersTest_success() {
        Collection<Shelters> expected = new ArrayList<>();
        Shelters shelter1 = new Shelters("TestAddress1", "TestWorkingHours1", "src/main/resources/map/Schema.jpg","TestPhoneMain1" ,"TestPhoneSecurity1");
        Shelters shelter2 = new Shelters("TestAddress2", "TestWorkingHours2", "src/main/resources/map/Schema3.jpg","TestPhoneMain2" ,"TestPhoneSecurity2");
        expected.add(shelter1);
        expected.add(shelter2);

        when(sheltersRepository.findAll()).thenReturn((List<Shelters>) expected);
        Collection<Shelters> actual = sheltersService.getAllShelter();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void updateSheltersTest_success() throws FileNotFoundException {
        Shelters expected = new Shelters("TestAddress", "TestWorkingHours", "src/main/resources/map/Schema.jpg","TestPhoneMain" ,"TestPhoneSecurity");
        when(sheltersRepository.save(expected)).thenReturn(expected);
        when(sheltersRepository.existsById(expected.getId())).thenReturn(true);
        Shelters actual = sheltersService.updateShelter(expected.getId(), expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void updateSheltersTest_NotFoundShelter() throws FileNotFoundException {
        Shelters shelter = new Shelters("TestAddress", "TestWorkingHours", "src/main/resources/map/Schema.jpg","TestPhoneMain" ,"TestPhoneSecurity");
        Assertions.assertNull(sheltersService.updateShelter(shelter.getId(), shelter));
    }
    @Test
    public void updateSheltersTest_exception() throws FileNotFoundException {
        Shelters shelter = new Shelters("TestAddress", "TestWorkingHours", "SchemaException","TestPhoneMain" ,"TestPhoneSecurity");
        Assertions.assertThrows(FileNotFoundException.class, () -> sheltersService.updateShelter(shelter.getId(), shelter));
    }

    @Test
    public void deleteSheltersTest_success() {
        Assertions.assertDoesNotThrow(() -> sheltersService.deleteShelter(-1L));
    }
}
