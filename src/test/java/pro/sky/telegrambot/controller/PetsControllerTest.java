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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.telegrambot.controller.PetsController;
import pro.sky.telegrambot.entity.Pets;
import pro.sky.telegrambot.repository.PetsRepository;
import pro.sky.telegrambot.service.PetsService;

import java.util.Optional;

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
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(petsController).isNotNull();
    }


    @Test
    public void createPetsTest() throws Exception {
        Long id = 2L;
        final String name = "TestName";
        final String breed = "TestBreed";
        final int age = 1;
        final String photo = "Pets1";

        Pets pet = new Pets(name, breed, age, "src/main/resources/pets/" + photo);
        pet.setId(id);

        ResponseEntity<Pets> expected = ResponseEntity.ok(pet);
        ResponseEntity<Pets> actual = petsController.createPets(name,breed,age,photo);
        org.junit.jupiter.api.Assertions.assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
    }


}