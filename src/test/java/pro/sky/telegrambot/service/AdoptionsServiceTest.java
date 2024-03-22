package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.entity.Adoptions;
import pro.sky.telegrambot.entity.Customers;
import pro.sky.telegrambot.repository.AdoptionsRepository;
import pro.sky.telegrambot.repository.CustomersRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdoptionsServiceTest {
    @Mock
    TelegramBot telegramBot;
    @Mock
    AdoptionsRepository adoptionsRepository;
    @Mock
    CustomersRepository customersRepository;
    @InjectMocks
    AdoptionsService adoptionsService;

    @Test
    public void createAdoptionsTest_success() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse("2014-04-08 21:00", formatter);
        Adoptions expected = new Adoptions(1L, 1L, dateTime);
        when(adoptionsRepository.save(expected)).thenReturn(expected);
        Adoptions actual = adoptionsService.createAdoptions(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAdoptionsByIdTest_success() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse("2014-04-08 21:00", formatter);
        Adoptions expected = new Adoptions(1L, 1L, dateTime);
        when(adoptionsRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        Adoptions actual = adoptionsService.getAdoptionsById(expected.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllAdoptionsTest_success() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse("2014-04-08 21:00", formatter);
        Adoptions adoption1 = new Adoptions(1L, 1L, dateTime);
        Adoptions adoption2 = new Adoptions(2L, 2L, dateTime);
        List<Adoptions> expected = new ArrayList<>();
        expected.add(adoption1);
        expected.add(adoption2);
        when(adoptionsRepository.findAll()).thenReturn(expected);
        Collection<Adoptions> actual = adoptionsService.getAllAdoptions();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void updateAdoptionsTest_success() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse("2014-04-08 21:00", formatter);
        Adoptions expected = new Adoptions(1L, 1L, dateTime);
        when(adoptionsRepository.save(expected)).thenReturn(expected);
        Adoptions actual = adoptionsService.updateAdoptions(1L , expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void deleteAdoptionsTest_success() {
        Assertions.assertDoesNotThrow(() -> adoptionsService.deleteAdoptions(1L));
    }

    @Test
    public void trialEndPlus14DaysTest_success() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse("2014-04-08 21:00", formatter);
        LocalDateTime newDateTime = LocalDateTime.parse("2014-04-22 21:00", formatter);
        Adoptions adoptions = new Adoptions(1L, 1L, dateTime);
        Adoptions expected = new Adoptions(1L, 1L, newDateTime);
        when(adoptionsRepository.findById(1L)).thenReturn(Optional.of(adoptions));
        Adoptions actual = adoptionsService.trialEndPlus14Days(1L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void trialEndPlus30DaysTest_success() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse("2014-04-08 21:00", formatter);
        LocalDateTime newDateTime = LocalDateTime.parse("2014-05-08 21:00", formatter);
        Adoptions adoptions = new Adoptions(1L, 1L, dateTime);
        Adoptions expected = new Adoptions(1L, 1L, newDateTime);
        when(adoptionsRepository.findById(1L)).thenReturn(Optional.of(adoptions));
        Adoptions actual = adoptionsService.trialEndPlus30Days(1L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void trialEndSuccessTest_success() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse("2014-04-08 21:00", formatter);
        Adoptions adoptions = new Adoptions(1L, 1L, dateTime);
        Adoptions expected = new Adoptions(1L, 1L, dateTime);
        expected.setTrialSuccess(true);
        when(adoptionsRepository.findById(1L)).thenReturn(Optional.of(adoptions));
        when(adoptionsRepository.save(adoptions)).thenReturn(adoptions);
        when(customersRepository.findById(adoptions.getCustomerId())).thenReturn(Optional.of(new Customers()));
        Adoptions actual = adoptionsService.trialEndSuccess(1L);
        Assertions.assertEquals(expected, actual);
    }

}
