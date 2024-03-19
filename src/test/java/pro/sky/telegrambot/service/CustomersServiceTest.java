package pro.sky.telegrambot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.entity.Customers;
import pro.sky.telegrambot.repository.CustomersRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomersServiceTest {

    @Mock
    CustomersRepository customersRepository;
    @InjectMocks
    CustomersService customersService;


    @Test
    void getAllCustomersTest_success() {
        List<Customers> expected = List.of(
                new Customers(1L, "name1", "phone1"),
                new Customers(2L, "name2", "phone2")
        );
        when(customersRepository.findAll()).thenReturn(expected);
        Collection<Customers> actual = customersService.getAllCustomers();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getCustomerByIdTest_success() {
        Customers expected = new Customers(1L, "name", "phone");
        when(customersRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        Customers actual = customersService.getCustomerById(expected.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void createCustomerTest_success() {
        Customers expected = new Customers(1L, "name", "phone");
        when(customersRepository.save(expected)).thenReturn(expected);
        Customers actual = customersService.createCustomer(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateCustomerTest_success() {
        Customers expected = new Customers(1L, "name", "phone");
        expected.setId(1L);
        when(customersRepository.existsById(expected.getId())).thenReturn(true);
        when(customersRepository.save(expected)).thenReturn(expected);

        Customers actual = customersService.updateCustomer(expected.getId(), expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateCustomerTest_notFound() {
        Customers expected = new Customers(1L, "name", "phone");
        expected.setId(1L);
        when(customersRepository.existsById(expected.getId())).thenReturn(false);

        Customers actual = customersService.updateCustomer(expected.getId(), expected);
        Assertions.assertNull(actual);
    }

    @Test
    void deleteCustomer() {
        Assertions.assertDoesNotThrow(() -> customersService.deleteCustomer(1L));
    }
}