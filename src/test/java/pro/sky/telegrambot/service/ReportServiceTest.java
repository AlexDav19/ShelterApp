package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.entity.Adoptions;
import pro.sky.telegrambot.entity.Customers;
import pro.sky.telegrambot.entity.Report;
import pro.sky.telegrambot.repository.AdoptionsRepository;
import pro.sky.telegrambot.repository.CustomersRepository;
import pro.sky.telegrambot.repository.ReportRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {
    @Mock
    Update update;
    @Mock
    Message message;
    @Mock
    Chat chat;
    @Mock
    PhotoSize photoSize;

    @Mock
    ReportRepository reportRepository;
    @Mock
    TelegramBot telegramBot;
    @Mock
    CustomersRepository customersRepository;
    @Mock
    AdoptionsRepository adoptionsRepository;
    @InjectMocks
    ReportService reportService;

    @Test
    public void mainMessageTest_success() {
        Long chatId = 1L;
        String expectedMessage = "Для отправки отчета необходимо выбрать для отправки фотографию Вашего питомца и в подписи к фотографии указать:\n" +
                "Рацион животного.\n" +
                "Общее самочувствие и привыкание к новому месту.\n" +
                "Изменения в поведении: отказ от старых привычек, приобретение новых.";
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        String expected = new SendMessage(chatId, expectedMessage).getParameters().toString();

        //test execution
        String actual = reportService.mainMessage(update).getParameters().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void saveReportTest_success() {
        PhotoSize[] photo = new PhotoSize[]{photoSize};

        Long chatId = 1L;
        String expectedMessage = "Отчет принят";
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);
        when(update.message().caption()).thenReturn("Отчет");
        when(update.message().photo()).thenReturn(photo);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse("2014-04-08 21:00", formatter);
        Adoptions adoptions = new Adoptions(1L, 1L, dateTime);
        adoptions.setId(1L);
        Customers customers = new Customers(789L, "TestName", "+7-999-999-99-99");
        customers.setId(1L);
        when(customersRepository.findByChatId(chatId)).thenReturn(customers);
        when(adoptionsRepository.findByCustomerId(1L)).thenReturn(adoptions);
        when(adoptionsRepository.findById(1L)).thenReturn(Optional.of(adoptions));

        String expected = new SendMessage(chatId, expectedMessage).getParameters().toString();

        //test execution
        String actual = reportService.saveReport(update).getParameters().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void getReportByIdTest_success() {

        Report expected = new Report(1L, "textTest", "photoIdTest");
        expected.setProcessed(false);
        Report report = new Report(1L, "textTest", "photoIdTest");

        when(reportRepository.findById(1L)).thenReturn(Optional.of(report));

        //test execution
        Report actual = reportService.getReportById(1L);
        assertEquals(expected, actual);
    }

    @Test
    public void getNewReportTest_success() {

        Report report = new Report(1L, "textTest", "photoIdTest");
        List<Report> expected = new ArrayList<>();
        expected.add(report);

        when(reportRepository.findAll()).thenReturn(expected);

        //test execution
        Collection<Report> actual = reportService.getNewReport();
        assertEquals(expected, actual);
    }

    @Test
    public void sendMessageAboutBadReportTest_success() {

        Report expected = new Report(1L, "textTest", "photoIdTest");

        Report report = new Report(1L, "textTest", "photoIdTest");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse("2014-04-08 21:00", formatter);
        Adoptions adoptions = new Adoptions(1L, 1L, dateTime);
        adoptions.setId(1L);
        Customers customers = new Customers(789L, "TestName", "+7-999-999-99-99");
        customers.setId(1L);
        when(reportRepository.findById(1L)).thenReturn(Optional.of(report));
        when(adoptionsRepository.findById(1L)).thenReturn(Optional.of(adoptions));
        when(customersRepository.findById(1L)).thenReturn(Optional.of(customers));

        //test execution
        Report actual = reportService.sendMessageAboutBadReport(1L);
        assertEquals(expected, actual);
    }
}
