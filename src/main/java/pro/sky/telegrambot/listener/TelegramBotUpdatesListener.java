package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Customer;
import pro.sky.telegrambot.sevice.CustomerService;
import pro.sky.telegrambot.sevice.KeyBoardService;

import javax.annotation.PostConstruct;
import java.util.List;



@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;
    // private User userName;
    private final CustomerService customer;
    private User telegramCustomer;
    String nameCustomer;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, CustomerService customer) {
        this.telegramBot = telegramBot;
        this.customer = customer;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.message() != null) {
                telegramCustomer = update.message().from();
                if (update.message().text() != null && update.message().text().equals("/start")) {
                    Long chatId = update.message().chat().id();
                    nameCustomer = update.message().from().firstName();
                    responseOnCommandStart(chatId);

                    if (customer.findCustomerByChatId(chatId) == null) {
                        customer.createCustomer(new Customer(
                                telegramCustomer.id(),
                                chatId,
                                telegramCustomer.lastName(),
                                telegramCustomer.firstName(),
                                telegramCustomer.username(),
                                null,
                                null,
                                null
                        ));
                    }
                } else if (update.callbackQuery() != null) {
                    Long chatId = update.callbackQuery().message().chat().id();
                    switch (update.callbackQuery().data()) {
                        case ("INFO"):
                            responseOnCommanInfo(chatId);
                            break;
                        case ("GETPET"):
                            responseOnCommandGetpet(chatId);
                            break;
                        case ("SENDREPORT"):
                            responseOnCommandSendreport(chatId);
                            break;
                        case ("CALL_VOLUNTEER"):
                            responseOnCommandCallVolunteer(chatId);
                            break;
                    }
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void responseOnCommandCallVolunteer(Long chatId) {
    }

    private void responseOnCommandSendreport(Long chatId) {

    }

    private void responseOnCommandGetpet(Long chatId) {

    }

    private void responseOnCommanInfo(Long chatId) {


    }

    private SendMessage  responseOnCommandStart(Long chatId) {
        SendMessage sendMess = new SendMessage(chatId, "Привет, " + nameCustomer
                + "Приют животных приветствует тебя\n");
        sendMess.replyMarkup(prepareKeyboardShelter());
        SendResponse response = telegramBot.execute(sendMess);
        return sendMess;
    }

    private InlineKeyboardMarkup prepareKeyboardShelter() {
        return KeyBoardService.prepareKeyboardShelter("выбери раздел");


    }
}



