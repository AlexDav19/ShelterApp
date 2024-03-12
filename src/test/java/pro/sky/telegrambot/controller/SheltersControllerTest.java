package pro.sky.telegrambot.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import pro.sky.telegrambot.entity.Shelters;
import pro.sky.telegrambot.repository.SheltersRepository;
import pro.sky.telegrambot.service.SheltersService;

import java.util.Collection;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SheltersControllerTest {


    @LocalServerPort
    private int port;
    @Autowired
    SheltersController sheltersController;
    @Autowired
    SheltersRepository sheltersRepository;
    @Autowired
    SheltersService sheltersService;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(sheltersController).isNotNull();
    }


    @Test
    public void createSheltersTest() throws Exception {
        String address = "TestAddress";
        String workingHours = "TestWorkingHours";
        String drivingDirections = "Schema.jpg";
        String phoneMain = "TestPhoneMain";
        String phoneSecurity = "TestPhoneSecurity";
        Shelters shelter = new Shelters(address, workingHours, "src/main/resources/map/" + drivingDirections, phoneMain, phoneSecurity);

        ResponseEntity<Shelters> expected = ResponseEntity.ok(shelter);
        ResponseEntity<Shelters> actual = sheltersController.createShelter(address, workingHours, drivingDirections, phoneMain, phoneSecurity);
        shelter.setId(Objects.requireNonNull(actual.getBody()).getId());
        org.junit.jupiter.api.Assertions.assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
        sheltersRepository.delete(shelter);
    }

    @Test
    public void getSheltersByIdTest() throws Exception {
        String address = "TestAddress";
        String workingHours = "TestWorkingHours";
        String drivingDirections = "src/main/resources/map/Schema.jpg";
        String phoneMain = "TestPhoneMain";
        String phoneSecurity = "TestPhoneSecurity";
        Shelters shelter = new Shelters(address, workingHours, drivingDirections, phoneMain, phoneSecurity);
        shelter.setId(1L);
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/shelters/" + shelter.getId()
                        , Shelters.class))
                .isNotNull();
    }

    @Test
    public void getAllSheltersTest() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/shelters", Collection.class))
                .isNotNull();

    }

    @Test
    public void updateSheltersTest() throws Exception {
        String address = "TestAddress";
        String workingHours = "TestWorkingHours";
        String drivingDirections = "src/main/resources/map/Schema.jpg";
        String phoneMain = "TestPhoneMain";
        String phoneSecurity = "TestPhoneSecurity";
        Shelters shelter = new Shelters(address, workingHours, drivingDirections, phoneMain, phoneSecurity);
        shelter.setId(1L);

        String url = "http://localhost:" + port + "/shelter";
        HttpEntity<Shelters> requestEntity = new HttpEntity<>(shelter);
        Assertions.assertThat(this.restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Shelters.class)).isNotNull();
    }

    @Test
    public void deleteSheltersTest() throws Exception {
        String address = "TestAddress";
        String workingHours = "TestWorkingHours";
        String drivingDirections = "src/main/resources/map/Schema.jpg";
        String phoneMain = "TestPhoneMain";
        String phoneSecurity = "TestPhoneSecurity";
        Shelters shelter = new Shelters(address, workingHours, drivingDirections, phoneMain, phoneSecurity);
        shelter.setId(1L);

        String url = "http://localhost:" + port + "/shelters/" + shelter.getId();
        HttpEntity<Shelters> requestEntity = new HttpEntity<>(shelter);
        Assertions.assertThat(this.restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Shelters.class)).isNotNull();
    }


}
