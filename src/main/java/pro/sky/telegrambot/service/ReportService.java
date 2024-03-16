package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.Report;
import pro.sky.telegrambot.repository.AdoptionsRepository;
import pro.sky.telegrambot.repository.CustomersRepository;
import pro.sky.telegrambot.repository.ReportRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@Transactional
public class ReportService {

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    ReportRepository reportRepository;
    @Autowired
    CustomersRepository customersRepository;
    @Autowired
    AdoptionsRepository adoptionsRepository;

    public SendMessage mainMessage(Update update) {
        String text = "Для отправки отчета необходимо выбрать для отправки фотографию Вашего питомца и в подписи к фотографии указать:\n" +
                "Рацион животного.\n" +
                "Общее самочувствие и привыкание к новому месту.\n" +
                "Изменения в поведении: отказ от старых привычек, приобретение новых.";
        return new SendMessage(update.message().chat().id(), text);
    }


    public SendMessage saveReport(Update update) {
        Long adoption_id = adoptionsRepository.findByCustomerId(customersRepository.findByChatId(update.message().chat().id()).getId()).getId();
        String text = update.message().caption();
        PhotoSize photoData = Arrays.stream(update.message().photo()).findFirst().get();
        String photoId = photoData.fileId();
        Report report = new Report(adoption_id, text, photoId);
        reportRepository.save(report);
        adoptionsRepository.findById(adoption_id).get().setLastReport(LocalDateTime.now());
        return new SendMessage(update.message().chat().id(), "Отчет принят");
    }
}
