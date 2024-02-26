package pro.sky.telegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import com.pengrad.telegrambot.model.botcommandscope.BotCommandScopeDefault;
import com.pengrad.telegrambot.request.SetMyCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.ArrayUtils.toArray;

@Configuration
public class TelegramBotConfiguration {

    @Value("${telegram.bot.token}")
    private String token;

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        List<BotCommand> listofCommands = new ArrayList<>();

        //only lowercase letters and underscore are accepted by Telegram for the menu commands!
        listofCommands.add(new BotCommand("/start", "Приветствие"));
        listofCommands.add(new BotCommand("/help", "Как пользоваться ботом"));
        listofCommands.add(new BotCommand("/shelter_info", "Узнать информацию о приюте"));
        listofCommands.add(new BotCommand("/how_to_adopt", "Как взять животное из приюта"));
        listofCommands.add(new BotCommand("/send_report", "Прислать отчет о питомце"));
        listofCommands.add(new BotCommand("/call_volunteer", "Позвать волонтера"));

        bot.execute(new DeleteMyCommands());
        bot.execute(new SetMyCommands(listofCommands.toArray(new BotCommand[0])));
        return bot;
    }

}
