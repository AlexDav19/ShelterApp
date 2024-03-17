package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.Adoptions;
import pro.sky.telegrambot.repository.AdoptionsRepository;
import pro.sky.telegrambot.repository.CustomersRepository;
import pro.sky.telegrambot.repository.ReportRepository;
import pro.sky.telegrambot.repository.VolunteersRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@EnableScheduling
@Service
public class TimeMessage {

    @Autowired
    CustomersRepository customersRepository;
    @Autowired
    AdoptionsRepository adoptionsRepository;
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    VolunteersRepository volunteersRepository;

    @Autowired
    TelegramBot telegramBot;

    /**
     * Ежедневное напоминание усыновителям об отчете.
     *
     */
    @Scheduled(cron = "00 00 10 * * *")
    private void reportReminder() {
        List<Long> adoptionsStream = adoptionsRepository.findAll().stream()
                .map(Adoptions::getId)
                .filter(a -> adoptionsRepository.findById(a).get().getLastReport().isBefore(LocalDateTime.now().minusDays(1)))
                .filter(b -> !adoptionsRepository.findById(b).get().isTrialSuccess())
                .collect(Collectors.toList());
        for (Long i = 1L; i < adoptionsStream.size() + 1; i++) {
            Long chatId = customersRepository.findById(adoptionsRepository.findById(reportRepository.findById(i).get().getAdoption_id()).get().getCustomerId()).get().getChatId();
            String text = "Пожалуйста, не забывайте про ежедневный отчет";
            telegramBot.execute(new SendMessage(chatId, text));
        }
    }

    /**
     * Уведомление волонтера о просроченном отчете усыновителя.
     *
     */
    @Scheduled(cron = "00 00 10 * * *")
    private void volunteerReportReminder() {
        List<Long> adoptionsStream = adoptionsRepository.findAll().stream()
                .map(Adoptions::getId)
                .filter(a -> adoptionsRepository.findById(a).get().getLastReport().isBefore(LocalDateTime.now().minusDays(2)))
                .filter(b -> !adoptionsRepository.findById(b).get().isTrialSuccess())
                .collect(Collectors.toList());
        for (Long i = 1L; i < adoptionsStream.size() + 1; i++) {
            Random random = new Random();
            Long volunteerId = random.nextLong(volunteersRepository.findAll().size());
            Long chatId = customersRepository.findById(adoptionsRepository.findById(reportRepository.findById(i).get().getAdoption_id()).get().getCustomerId()).get().getChatId();
            String text = String.format("Пожалуйста, свяжитесь с усыновителем id:%s, chatId:%s.Он не присылал отчет уже 2 дня.", i, chatId);
            telegramBot.execute(new SendMessage(volunteerId, text));
        }
    }

}
