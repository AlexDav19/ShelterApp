package pro.sky.telegrambot.controller;

import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.telegrambot.controller.PetsController;
import pro.sky.telegrambot.entity.Pets;
import pro.sky.telegrambot.repository.PetsRepository;
import pro.sky.telegrambot.service.PetsService;

import java.io.File;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PetsControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    PetsController petsController;
    @Autowired
    PetsRepository petsRepository;
    @Autowired
    PetsService petsService;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(petsController).isNotNull();
    }


    @Test
    public void createPetsTest() throws Exception {
        final String name = "TestName";
        final String breed = "TestBreed";
        final int age = 1;
        final String photo = "Pet1.jpg";
        Pets pet = new Pets(name, breed, age, "src/main/resources/pets/" + photo);

        ResponseEntity<Pets> expected = ResponseEntity.ok(pet);
        ResponseEntity<Pets> actual = petsController.createPets(name, breed, age, photo);
        pet.setId(Objects.requireNonNull(actual.getBody()).getId());
        org.junit.jupiter.api.Assertions.assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
        petsRepository.delete(pet);
    }

    @Test
    public void getPetByIdTest() throws Exception {
        final String name = "TestName";
        final String breed = "TestBreed";
        final int age = 1;
        final String photo = "Pets1";

        Pets pet = new Pets(name, breed, age, "src/main/resources/pets/" + photo);
        pet.setId(1L);
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/pets/" + pet.getId()
                        , Pets.class))
                .isNotNull();
    }

    @Test
    public void getAllPetTest() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/pets", Collection.class))
                .isNotNull();

    }

    @Test
    public void updateFacultyTest() throws Exception {
        final String name = "TestName";
        final String breed = "TestBreed";
        final int age = 1;
        final String photo = "Pets1";
        Pets pet = new Pets(name, breed, age, "src/main/resources/pets/" + photo);
        pet.setId(1L);

        String url = "http://localhost:" + port + "/pets232";
        HttpEntity<Pets> requestEntity = new HttpEntity<>(pet);
        Assertions.assertThat(this.restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Pets.class)).isNotNull();
    }

    @Test
    public void deletePetsTest() throws Exception {
        final String name = "TestName";
        final String breed = "TestBreed";
        final int age = 1;
        final String photo = "Pets1";
        Pets pet = new Pets(name, breed, age, "src/main/resources/pets/" + photo);
        pet.setId(1L);

        String url = "http://localhost:" + port + "/pets/" + pet.getId();
        HttpEntity<Pets> requestEntity = new HttpEntity<>(pet);
        Assertions.assertThat(this.restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Pets.class)).isNotNull();
    }


}
