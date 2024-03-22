package pro.sky.telegrambot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.entity.Customers;
import pro.sky.telegrambot.entity.Shelters;
import pro.sky.telegrambot.entity.Volunteers;
import pro.sky.telegrambot.repository.CustomersRepository;
import pro.sky.telegrambot.repository.SheltersRepository;
import pro.sky.telegrambot.repository.VolunteersRepository;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramBotServiceTest {

    @Mock
    Update update;

    @Mock
    Message message;

    @Mock
    Chat chat;

    @Mock
    User user;

    @Mock
    SheltersRepository sheltersRepository;

    @Mock
    VolunteersRepository volunteersRepository;

    @Mock
    CustomersRepository customersRepository;

    @Mock
    TelegramBot telegramBot;

    @InjectMocks
    TelegramBotService telegramBotService;

    public static String getJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Test
    void startMessage() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Здравствуйте! Вас приветствует бот EasyPetShelter.\n" +
                "Я помогу Вам получить всю необходимую информацию о приютах, о том, как взять собачку к себе, как о ней заботиться и как давать отчеты.\n" +
                "Также я могу позвать волонтера, если это будет необходимо.\n" +
                "Очень рад нашему знакомству!";

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        SendMessage expected = new SendMessage(chatId, expectedMessage);

        //test execution
        SendMessage actual = telegramBotService.startMessage(update);
        assertEquals(getJsonString(expected), getJsonString(actual));
    }

    @Test
    void shelterInfo() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Для получения информации о приюте, выберите интересующий Вас приют ниже:";

        Shelters shelter1 = new Shelters("address1","working hours1","driving directions1","phone main1","phone security1");
        shelter1.setId(1L);
        Shelters shelter2 = new Shelters("address2","working hours2","driving directions2","phone main2","phone security2");
        shelter2.setId(2L);

        List<Shelters> expectedShelters = List.of(shelter1,shelter2);

        String[][] expectedShelterButtons = expectedShelters.stream()
                .map(e -> new String[]{"/si_" + e.getId() + " " + e.getAddress()})
                .toArray(String[][]::new);
        Keyboard expectedReplyKeyboardMarkup = new ReplyKeyboardMarkup(expectedShelterButtons)
                .oneTimeKeyboard(true);

        SendMessage expectedSendMessage = new SendMessage(chatId, expectedMessage);
        expectedSendMessage.replyMarkup(expectedReplyKeyboardMarkup);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        when(sheltersRepository.findAll()).thenReturn(expectedShelters);

        //test execution
        SendMessage actualSendMessage = telegramBotService.shelterInfo(update);
        assertEquals(getJsonString(expectedSendMessage), getJsonString(actualSendMessage));
    }

    @Test
    void shelterInfoById() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Адрес: address1\n" +
                "Часы работы: working hours1\n" +
                "Телефон: phone main1\n" +
                "Телефон охраны: phone security1";

        Optional<Shelters> shelter = Optional.of(new Shelters("address1","working hours1","driving directions1","phone main1","phone security1"));
        shelter.get().setId(1L);

        SendMessage expectedSendMessage = new SendMessage(chatId, expectedMessage);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        when(sheltersRepository.findById(anyLong())).thenReturn(shelter);

        //test execution
        SendMessage actualSendMessage = telegramBotService.shelterInfoById(update,1L);
        assertEquals(getJsonString(expectedSendMessage), getJsonString(actualSendMessage));
    }

    @Test
    void callVolunteer_withUsername() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Мы позвали волонтера в помощь, скоро он свяжется с вами.";

        SendMessage expectedSendMessage = new SendMessage(chatId, expectedMessage);

        List<Volunteers> expectedVolunteers = List.of(new Volunteers(1L,"name","username"));

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        when(message.from()).thenReturn(user);
        when(update.message().from().username()).thenReturn("username");

        when(volunteersRepository.findAll()).thenReturn(expectedVolunteers);

        //test execution
        SendMessage actualSendMessage = telegramBotService.callVolunteer(update);
        assertEquals(getJsonString(expectedSendMessage), getJsonString(actualSendMessage));
    }

    @Test
    void callVolunteer_withoutUsername() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Мы подобрали для вас волонтера, который вам поможет: username";

        SendMessage expectedSendMessage = new SendMessage(chatId, expectedMessage);

        List<Volunteers> expectedVolunteers = List.of(new Volunteers(1L,"name","username"));

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        when(message.from()).thenReturn(user);
        when(update.message().from().username()).thenReturn(null);

        when(volunteersRepository.findAll()).thenReturn(expectedVolunteers);

        //test execution
        SendMessage actualSendMessage = telegramBotService.callVolunteer(update);
        assertEquals(getJsonString(expectedSendMessage), getJsonString(actualSendMessage));
    }

    @Test
    void addVolunteer_withUsername() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Вы добавлены в волонтеры";

        SendMessage expectedSendMessage = new SendMessage(chatId, expectedMessage);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        when(message.from()).thenReturn(user);
        when(update.message().from().username()).thenReturn("username");
        when(update.message().from().firstName()).thenReturn("name");

        //test execution
        SendMessage actualSendMessage = telegramBotService.addVolunteer(update);
        assertEquals(getJsonString(expectedSendMessage), getJsonString(actualSendMessage));
    }

    @Test
    void addVolunteer_withoutUsername() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Чтобы быть волонтером, необходимо добавить userName в Ваш профиль или изменить его на другой.";

        SendMessage expectedSendMessage = new SendMessage(chatId, expectedMessage);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        when(message.from()).thenReturn(user);
        when(update.message().from().username()).thenReturn(null);

        //test execution
        SendMessage actualSendMessage = telegramBotService.addVolunteer(update);
        assertEquals(getJsonString(expectedSendMessage), getJsonString(actualSendMessage));
    }

    @Test
    void saveCustomerDetails_Success() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Ваши данные сохранены. Спасибо!";

        SendMessage expectedSendMessage = new SendMessage(chatId, expectedMessage);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);
        when(update.message().text()).thenReturn("/leaveContactDetails +7-945-123-45-67 username");

        when(customersRepository.findByNameAndPhone(anyString(),anyString())).thenReturn(List.of());

        //test execution
        SendMessage actualSendMessage = telegramBotService.saveCustomerDetails(update);
        assertEquals(getJsonString(expectedSendMessage), getJsonString(actualSendMessage));
    }

    @Test
    void saveCustomerDetails_Duplicate() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Пользователь с таким именем и телефоном уже был сохранен ранее. Спасибо!";

        SendMessage expectedSendMessage = new SendMessage(chatId, expectedMessage);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);
        when(update.message().text()).thenReturn("/leaveContactDetails +7-945-123-45-67 username");

        when(customersRepository.findByNameAndPhone(anyString(),anyString())).thenReturn(List.of(new Customers(1L,"name","phone")));

        //test execution
        SendMessage actualSendMessage = telegramBotService.saveCustomerDetails(update);
        assertEquals(getJsonString(expectedSendMessage), getJsonString(actualSendMessage));
    }

    @Test
    void saveCustomerDetails_incorrectFormat() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Ваше сообщение не было распознано. Пожалуйста, проверьте формат и попробуйте еще раз.";

        SendMessage expectedSendMessage = new SendMessage(chatId, expectedMessage);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);
        when(update.message().text()).thenReturn("/leaveContactDetails +79451234567 username");

        //test execution
        SendMessage actualSendMessage = telegramBotService.saveCustomerDetails(update);
        assertEquals(getJsonString(expectedSendMessage), getJsonString(actualSendMessage));
    }

    @Test
    void shelterSafetyRules() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Будьте осторожны на территории приюта. Следуйте указаниям волонтеров. Не заходите в служебные помещения. Не трогайте собак без разрешения, это может быть опасно.";

        SendMessage expectedSendMessage = new SendMessage(chatId, expectedMessage);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        //test execution
        SendMessage actualSendMessage = telegramBotService.shelterSafetyRules(update);
        assertEquals(getJsonString(expectedSendMessage), getJsonString(actualSendMessage));
    }
}