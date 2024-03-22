package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.aspectj.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import pro.sky.telegrambot.entity.Adoptions;
import pro.sky.telegrambot.entity.Customers;
import pro.sky.telegrambot.entity.Report;
import pro.sky.telegrambot.repository.AdoptionsRepository;
import pro.sky.telegrambot.repository.CustomersRepository;
import pro.sky.telegrambot.repository.ReportRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
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
    GetFileResponse getFileResponse;
    @Mock
    File file;


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
    public void saveReportTest_success() throws IOException {
        PhotoSize[] photo = new PhotoSize[]{photoSize};

        Long chatId = 1L;
        String expectedMessage = "Отчет принят";
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);
        when(update.message().caption()).thenReturn("Отчет");
        when(update.message().photo()).thenReturn(photo);

        when(photoSize.width()).thenReturn(1);
        when(photoSize.height()).thenReturn(1);
        when(photoSize.fileId()).thenReturn("");
        doReturn(getFileResponse).when(telegramBot).execute(isNotNull());
        doReturn(file).when(getFileResponse).file();
        doReturn("").when(file).filePath();
        doReturn("https://api.telegram.org/file/bot6884906901:AAEEzXsPXSLA8LfhnFz70F5XW5cJyZoaGkE/photos/file_0.jpg").when(telegramBot).getFullFilePath(file);

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
        FileUtil.deleteContents(new java.io.File("src/main/resources/reports/null"));
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
    public void getPhotoReportByIdTest_success() throws IOException {
        Long id = 1L;
        String filePath = "src/main/resources/reports/Test.jpg";

        java.io.File photoFile = new java.io.File(filePath);
        photoFile.createNewFile();
        Path path = Path.of(filePath);

        byte[] result = Files.readAllBytes(path);
        HttpHeaders photo = new HttpHeaders();
        photo.setContentType(MediaType.IMAGE_JPEG);
        photo.setContentLength(result.length);
        ResponseEntity<byte[]> expected = ResponseEntity.status(HttpStatus.OK).headers(photo).body(result);

        Report report = new Report(1L, "textTest", "Test.jpg");

        when(reportRepository.findById(id)).thenReturn(Optional.of(report));


        //test execution
        ResponseEntity<byte[]> actual = reportService.getPhotoReportById(id);
        assertEquals(expected, actual);
        photoFile.delete();
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
