package pro.sky.telegrambot.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.telegrambot.entity.Pets;
import pro.sky.telegrambot.repository.PetsRepository;
import pro.sky.telegrambot.service.PetsService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest
public class PetsControllerMvsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PetsRepository petsRepository;

    @SpyBean
    PetsService petsService;

    @InjectMocks
    PetsController petsController;


    @Test
    public void createPetsTest() throws Exception {
        Long id = 1L;
        final String name = "TestName";
        final String breed = "TestBreed";
        final int age = 1;
        final String photo = "Pets1";

        JSONObject petsObject = new JSONObject();
        petsObject.put("name", name);
        petsObject.put("breed", breed);
        petsObject.put("age", age);
        petsObject.put("photo", photo);
        Pets pet = new Pets(name, breed, age, photo);
        pet.setId(id);

        when(petsRepository.save(any(Pets.class))).thenReturn(pet);
        when(petsRepository.findById(any(Long.class))).thenReturn(Optional.of(pet));


//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/pets")
//                        .content(petsObject.toString())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(id))
//                .andExpect(jsonPath("$.name").value(name))
//                .andExpect(jsonPath("$.breed").value(breed))
//                .andExpect(jsonPath("$.age").value(age))
//                .andExpect(jsonPath("$.photo").value(photo));
    }


}
