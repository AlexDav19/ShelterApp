package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.stereotype.Service;

import static com.pengrad.telegrambot.model.request.ParseMode.HTML;

@Service
public class TelegramBotService {
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

    public SendMessage callVolunteer(Update update) {
        String msg = "test";
        SendMessage message = new SendMessage(update.message().chat().id(), msg);
        return message;
    }
}
