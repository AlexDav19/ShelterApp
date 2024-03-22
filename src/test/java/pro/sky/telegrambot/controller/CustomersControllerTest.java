package pro.sky.telegrambot.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pro.sky.telegrambot.entity.Customers;
import pro.sky.telegrambot.entity.Pets;
import pro.sky.telegrambot.repository.CustomersRepository;
import pro.sky.telegrambot.service.CustomersService;

import java.util.Collection;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomersControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    CustomersController customersController;
    @Autowired
    CustomersRepository customersRepository;

    @Autowired
    CustomersService customersService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(customersController).isNotNull();
    }

    @Test
    void createCustomerTest() throws Exception {
        final Long chatId = 1L;
        final String name = "name";
        final String phone = "+7-945-123-45-78";

        Customers expectedCustomer = new Customers(chatId,name,phone);

        //create request entity based on updated customer
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("chatId", chatId.toString());
        map.add("name", name);
        map.add("phone", phone);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

        String url = "http://localhost:" + port + "/customers";

        //send post request
        Customers postedCustomer = restTemplate.postForObject(url, requestEntity, Customers.class);

        //update expected customer
        expectedCustomer.setId(postedCustomer.getId());

        //compare
        org.junit.jupiter.api.Assertions.assertNotNull(postedCustomer);
        org.junit.jupiter.api.Assertions.assertEquals(expectedCustomer, postedCustomer);

        //delete test customer
        customersRepository.delete(postedCustomer);
    }

    @Test
    void getAllCustomersTest() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/customers", Collection.class))
                .isNotNull();
    }

    @Test
    void getCustomerByIdTest() {
        final Long chatId = 1L;
        final String name = "name";
        final String phone = "+7-945-123-45-78";
        Customers customer = new Customers(chatId,name,phone);

        ResponseEntity<Customers> saveCustomer = customersController.createCustomer(chatId,name,phone);
        customer.setId(Objects.requireNonNull(saveCustomer.getBody()).getId());

        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/pets/" + customer.getId()
                        , Pets.class))
                .isNotNull();
        customersRepository.delete(customer);
    }

    @Test
    void updateCustomerTest() throws Exception {
        final Long chatId = 1L;
        final String name = "name";
        final String phone = "+7-945-123-45-78";

        final Long chatIdUpdate = 999L;
        final String nameUpdate = "nameUpdate";
        final String phoneUpdate = "+7-945-999-99-99";

        //create new customer
        ResponseEntity<Customers> createCustomer = customersController.createCustomer(chatId,name,phone);

        //create update customer with the same id
        Customers updateCustomer = new Customers(chatIdUpdate,nameUpdate,phoneUpdate);
        updateCustomer.setId(Objects.requireNonNull(createCustomer.getBody()).getId());

        //create request entity based on updated customer
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("customerId", updateCustomer.getId().toString());
        map.add("chatId", chatIdUpdate.toString());
        map.add("name", nameUpdate);
        map.add("phone", phoneUpdate);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

        String url = "http://localhost:" + port + "/customers";

        //send put request
        Assertions.assertThat(this.restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Customers.class)).isNotNull();

        //get updated customer after put request
        ResponseEntity<Customers> actualUpdatedCustomer = customersController.getCustomerById(updateCustomer.getId());

        //compare
        Assertions.assertThat(actualUpdatedCustomer.getBody().equals(updateCustomer));

        //delete test customer
        customersController.deleteCustomer(updateCustomer.getId());
    }

    @Test
    void deleteCustomerTest() {
        final Long chatId = 1L;
        final String name = "name";
        final String phone = "+7-945-123-45-78";
        Customers customer = new Customers(chatId,name,phone);

        ResponseEntity<Customers> createCustomer = customersController.createCustomer(chatId,name,phone);
        customer.setId(Objects.requireNonNull(createCustomer.getBody()).getId());

        String url = "http://localhost:" + port + "/customers/" + customer.getId();
        HttpEntity<Customers> requestEntity = new HttpEntity<>(customer);
        Assertions.assertThat(this.restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Customers.class)).isNotNull();
    }
}