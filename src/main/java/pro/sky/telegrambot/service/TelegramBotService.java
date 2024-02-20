package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.Volunteers;
import pro.sky.telegrambot.repository.VolunteersRepository;

import java.io.File;
import java.util.Random;


@Service
public class TelegramBotService {
    @Autowired
    VolunteersRepository volunteersRepository;
    @Autowired
    private TelegramBot telegramBot;

    public SendMessage startMessage(Update update) {
        String msg = "test";
        SendMessage message = new SendMessage(update.message().chat().id(), msg);
        return message;
    }

    public SendMessage menuMessage(Update update) {
        String msg = "test";
        SendMessage message = new SendMessage(update.message().chat().id(), msg);
        return message;
    }

    /**
     * Отправляет ссылку на пользователя волонтеру. Если у пользователя нет userName, отправляет ссылку на волонтера.
     * @param update
     * @return SendMessage
     */
    public SendMessage callVolunteer(Update update) {
        Random random = new Random();
        //try-catch - Проверка доступа к БД
        try {
            int randomID = random.nextInt(volunteersRepository.findAll().size());
            Long chatIdVolunteer = volunteersRepository.findAll().get(randomID).getChatId();
            if (update.message().from().username() != null) {
                SendMessage message = new SendMessage(chatIdVolunteer, update.message().from().username());
                telegramBot.execute(message);
            } else {
                return new SendMessage(update.message().chat().id(), String.format("Мы подобрали для вас волонтера, который вам поможет: %s", volunteersRepository.findAll().get(randomID).getUserName()));
            }
        } catch (RuntimeException e) {
            return new SendMessage(update.message().chat().id(), "Ошибка доступа к списку волонтеров, попробуйте позже");
        }
        return new SendMessage(update.message().chat().id(), "Мы позвали волонтера в помощь, скоро он свяжется с вами.");
    }

    /**
     * Добавление волонтера в базу волонтеров.
     * @param update
     * @return SendMessage
     */
    public SendMessage addVolunteer(Update update) {
        if (update.message().from().username() != null) {
            volunteersRepository.save(new Volunteers(update.message().chat().id(), update.message().from().firstName(), update.message().from().username()));
            return new SendMessage(update.message().chat().id(), "Вы добавлены в волонтеры");
        } else {
            return new SendMessage(update.message().chat().id(), "Чтобы быть волонтером, необходимо добавить userName в Ваш профиль или изменить его на другой.");
        }
    }

    /**
     * Отправка сообщения с информацией о графике и адресе.
     * @param update
     * @return SendPhoto
     */
    public SendPhoto getSchedule(Update update) {
        SendMessage message = new SendMessage(update.message().chat().id(),
                "Наш приют находится по адресу:\n" +
                        "г. Москва, Шоссе Революции д.1\n\n" +
                        "Часы работы: 09:00 - 18:00\n" +
                        "Воскресенье - выходной");
        telegramBot.execute(message);
        return new SendPhoto(update.message().chat().id(), new File("src/main/resources/map/Shema.jpg"));
    }
}