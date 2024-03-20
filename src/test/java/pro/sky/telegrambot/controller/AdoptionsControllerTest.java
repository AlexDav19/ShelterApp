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
import pro.sky.telegrambot.entity.Adoptions;

import pro.sky.telegrambot.repository.AdoptionsRepository;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdoptionsControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    AdoptionsController adoptionsController;
    @Autowired
    AdoptionsRepository adoptionsRepository;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        Assertions.assertThat(adoptionsController).isNotNull();
    }



    @Test
    public void createAdoptionTest(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String dataTime = "2014-04-08 21:00";
        LocalDateTime trialEnd = LocalDateTime.parse(dataTime, formatter);
        Long customerId = 1L;
        Long petId = 1L;
        Adoptions adoptions = new Adoptions(customerId,petId,trialEnd);

        ResponseEntity<Adoptions> expected = ResponseEntity.ok(adoptions);
        ResponseEntity<Adoptions> actual = adoptionsController.createAdoption(customerId,petId,dataTime);
        adoptions.setId(Objects.requireNonNull(actual.getBody()).getId());
        org.junit.jupiter.api.Assertions.assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
        adoptionsRepository.delete(adoptions);
    }

    @Test
    public void getAdoptionByIdTest(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String dataTime = "2014-04-08 21:00";
        LocalDateTime trialEnd = LocalDateTime.parse(dataTime, formatter);
        Long customerId = 1L;
        Long petId = 1L;
        Adoptions adoptions = new Adoptions(customerId,petId,trialEnd);
        adoptions.setId(1L);
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/adoptions/" + adoptions.getId()
                        , Adoptions.class))
                .isNotNull();
    }

    @Test
    public void getAllAdoptionsTest(){
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/adoptions", Collection.class))
                .isNotNull();

    }

    @Test
    public void updateAdoptionTest(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String dataTime = "2014-04-08 21:00";
        LocalDateTime trialEnd = LocalDateTime.parse(dataTime, formatter);
        Long customerId = 1L;
        Long petId = 1L;
        Adoptions adoptions = new Adoptions(customerId,petId,trialEnd);
        adoptions.setId(1L);

        String url = "http://localhost:" + port + "/adoptions";
        HttpEntity<Adoptions> requestEntity = new HttpEntity<>(adoptions);
        Assertions.assertThat(this.restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Adoptions.class)).isNotNull();
    }

    @Test
    public void deleteAdoptionTest(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String dataTime = "2014-04-08 21:00";
        LocalDateTime trialEnd = LocalDateTime.parse(dataTime, formatter);
        Long customerId = 1L;
        Long petId = 1L;
        Adoptions adoptions = new Adoptions(customerId,petId,trialEnd);
        adoptions.setId(1L);

        String url = "http://localhost:" + port + "/adoptions/" + adoptions.getId();
        HttpEntity<Adoptions> requestEntity = new HttpEntity<>(adoptions);
        Assertions.assertThat(this.restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Adoptions.class)).isNotNull();
    }


    @Test
    public void trialEndPlus14DaysTest(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String dataTime = "2014-04-08 21:00";
        LocalDateTime trialEnd = LocalDateTime.parse(dataTime, formatter);
        Long customerId = 1L;
        Long petId = 1L;
        Adoptions adoptions = new Adoptions(customerId,petId,trialEnd);
        adoptions.setId(1L);

        String url = "http://localhost:" + port + "/adoptions/14Days/";
        HttpEntity<Adoptions> requestEntity = new HttpEntity<>(adoptions);
        Assertions.assertThat(this.restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Adoptions.class)).isNotNull();
    }
}
