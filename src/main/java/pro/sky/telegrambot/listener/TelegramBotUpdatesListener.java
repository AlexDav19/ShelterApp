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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            if (update.message().text().equals("/call_volunteer")) {
                telegramBot.execute(telegramBotService.callVolunteer(update));
            }

            if (update.message().text().equals("/addVolunteer")) {
                telegramBot.execute(telegramBotService.addVolunteer(update));
            }

            if (update.message().text().equals("/getSchedule")) {
                telegramBot.execute(telegramBotService.getSchedule(update));
            }

            if (update.message().text().equals("/getSecurityContactDetails")) {
                telegramBot.execute(telegramBotService.getSecurityContactDetails(update));
            }

            if (update.message().text().contains("/leaveContactDetails")) {
                telegramBot.execute(telegramBotService.saveCustomerDetails(update));
            }

            //generate list of shelters
            if (update.message().text().contains("/shelter_info")) {
                telegramBot.execute(telegramBotService.shelterInfo(update));
            }

            //shelter info check
            //check if message starts with /si_ and followed by a number
            Pattern pattern = Pattern.compile("/si_([0-9]{1,5})\\s(.+)");
            Matcher matcher = pattern.matcher(update.message().text());
            if (matcher.matches()) {
                Long id = Long.valueOf(matcher.group(1));
                telegramBot.execute(telegramBotService.shelterInfoById(update,id));
            }

            if (update.message().text().equals("/shelterSafetyRules")) {
                telegramBot.execute(telegramBotService.shelterSafetyRules(update));
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
