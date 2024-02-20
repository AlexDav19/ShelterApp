package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.TelegramBotService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private TelegramBotService telegramBotService;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Process your updates here

            if (update.message().text().equals("/start")) {
                telegramBot.execute(telegramBotService.startMessage(update));
            }

            if (update.message().text().equals("/help")) {
                telegramBot.execute(telegramBotService.menuMessage(update));
            }

            if (update.message().text().equals("/callVolunteer")) {
                telegramBot.execute(telegramBotService.callVolunteer(update));
            }

            if (update.message().text().equals("/addVolunteer")) {
                telegramBot.execute(telegramBotService.addVolunteer(update));
            }

            if (update.message().text().equals("/getSchedule")) {
                telegramBot.execute(telegramBotService.getSchedule(update));
            }

            if (update.message().text().contains("/leaveContactDetails")) {
                telegramBot.execute(telegramBotService.saveCustomerDetails(update));
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
