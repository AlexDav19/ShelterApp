package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.Customers;
import pro.sky.telegrambot.entity.Volunteers;
import pro.sky.telegrambot.repository.CustomersRepository;
import pro.sky.telegrambot.repository.SheltersRepository;
import pro.sky.telegrambot.repository.VolunteersRepository;

import java.io.File;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class TelegramBotService {
    @Autowired
    VolunteersRepository volunteersRepository;
    @Autowired
    CustomersRepository customersRepository;
    @Autowired
    SheltersRepository sheltersRepository;
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
     *
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
     *
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
     *
     * @param update
     * @return SendPhoto
     */
    public SendPhoto getSchedule(Update update) {
        Long shelterId = 1L;
        SendMessage message = new SendMessage(update.message().chat().id(),
                "Наш приют находится по адресу:\n" +
                        sheltersRepository.findById(shelterId).get().getAddress() +
                        "\n" + sheltersRepository.findById(shelterId).get().getWorkingHours());
        telegramBot.execute(message);
        return new SendPhoto(update.message().chat().id(), new File("src/main/resources/map/Shema.jpg"));
    }

    /**
     * Отправка контактных данных охраны для оформления пропуска на машину
     *
     * @param update
     * @return SendPhoto
     */
    public SendPhoto getSecurityContactDetails(Update update) {
        Long shelterId = 1L;
        SendMessage message = new SendMessage(update.message().chat().id(),
                            "Телефон охраны для оформления пропуска:\n" +
                                sheltersRepository.findById(shelterId).get().getPhoneSecurity() + "\n" +
                                "Адрес приюта:\n" +
                                sheltersRepository.findById(shelterId).get().getAddress() + "\n" +
                                sheltersRepository.findById(shelterId).get().getWorkingHours());
        telegramBot.execute(message);
        return new SendPhoto(update.message().chat().id(), new File("src/main/resources/map/Shema.jpg"));
    }

    /**
     * Сохранение контактных данных пользователя. Если пользователей с таким именем и телефоном уже существует, ничего не сохраняет.
     * Формат текстового сообщения "/leaveContactDetails Имя +7-9**-***-**-**".
     * Пример: "/leaveContactDetails Михаил +7-925-123-45-67".
     *
     * @param update
     * @return SendMessage
     */
    public SendMessage saveCustomerDetails(Update update) {
        Pattern pattern = Pattern.compile("(/leaveContactDetails)(.+)(\\+7-9[0-9]{2}-[0-9]{3}-[0-9]{2}-[0-9]{2})(.*)");
        Matcher matcher = pattern.matcher(update.message().text());
        if (matcher.matches()) {
            String name = matcher.group(2).trim();
            String phoneNumber = matcher.group(3).trim();
            //try-catch - check DB connection
            try {
                //check if such user is saved already
                if (customersRepository.findByNameAndPhone(name, phoneNumber).isEmpty()) {
                    customersRepository.save(new Customers(update.message().chat().id(), name, phoneNumber));
                    return new SendMessage(update.message().chat().id(),
                            "Ваши данные сохранены. Спасибо!");
                } else {
                    return new SendMessage(update.message().chat().id(),
                            "Пользователь с таким именем и телефоном уже был сохранен ранее. Спасибо!");
                }
            } catch (RuntimeException e) {
                return new SendMessage(update.message().chat().id(), "Ошибка доступа к списку пользователей, попробуйте позже");
            }
        } else {
            return new SendMessage(update.message().chat().id(),
                    "Ваше сообщение не было распознано. Пожалуйста, проверьте формат и попробуйте еще раз.");
        }
    }
}