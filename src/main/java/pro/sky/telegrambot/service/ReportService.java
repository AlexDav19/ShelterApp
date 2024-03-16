package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.Report;
import pro.sky.telegrambot.repository.AdoptionsRepository;
import pro.sky.telegrambot.repository.CustomersRepository;
import pro.sky.telegrambot.repository.ReportRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportService {

    Logger logger = LoggerFactory.getLogger(ReportService.class);

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


    /**
     * Сохраняет отчет в базе..
     *
     * @return SendMessage
     */
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

    /**
     * Выдает отчет по id и помечает его отсмотренным.
     *
     * @return SendMessage
     */
    public Report getReportById(Long reportId) {
        logger.debug("Вызван метод getReportById");
        reportRepository.findById(reportId).get().setProcessed(false);
        return reportRepository.findById(reportId).get();
    }

    /**
     * Выдает все не отсмотренные отчеты.
     *
     * @return SendMessage
     */
    public Collection<Report> getNewReport() {
        logger.debug("Вызван метод getNewReport");
        return reportRepository.findAll().stream().filter(Report::isProcessed).collect(Collectors.toList());
    }

    /**
     * Отправить сообщение о ненадлежащем сданном отчете.
     *
     * @return SendMessage
     */
    public Report sendMessageAboutBadReport(Long reportId) {
        logger.debug("Вызван метод sendMessageAboutBadReport");
        Long chatId = customersRepository.findById(adoptionsRepository.findById(reportRepository.findById(reportId).get().getAdoption_id()).get().getCustomerId()).get().getChatId();
        String text = "Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. Пожалуйста, подойди ответственнее к этому занятию. В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного";
        telegramBot.execute(new SendMessage(chatId, text));
        return reportRepository.findById(reportId).get();
    }



}
