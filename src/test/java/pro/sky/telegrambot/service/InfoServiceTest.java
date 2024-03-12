package pro.sky.telegrambot.service;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pro.sky.telegrambot.entity.Pets;
import pro.sky.telegrambot.repository.PetsRepository;

import java.lang.reflect.Array;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class InfoServiceTest {

    @Mock
    Update update;

    @Mock
    Message message;

    @Mock
    Chat chat;

    @Mock
    PetsRepository petsRepository;

    @InjectMocks
    InfoService infoService;

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
    void getInfoForShelter() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "К сожалению, на улицах нашего города находится ещё не мало безнадзорных животных. Мы все с ними сталкивались. У многих есть желание остановиться, помочь, но не у всех есть возможность — и вот тут люди вспоминают, что у нас в городе есть приюты для животных.\n" +
                "Приют НашПриют - это муниципальный приют для бездомных собак и кошек в Южном округе г. Москвы. В нем живет почти 2500 собак и 150 кошек. Большие и маленькие, пушистые и гладкие, веселые и задумчивые - и на всех одна большая мечта - встретить своего Человека и найти Дом.";

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        SendMessage expected = new SendMessage(chatId, expectedMessage);

        //test execution
        SendMessage actual = infoService.getInfoForShelter(update);
        assertEquals(getJsonString(expected), getJsonString(actual));
    }

    @Test
    void getSafetyPrecautions() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "При первичном посещении Приюта, в целях безопасности, гостю запрещается без сопровождения ответственного рабочего по уходу за животными:\n" +
                "заходить в вольеры с животными;\n" +
                "кормить и гладить животных;\n" +
                "выпускать животных из вольеров;\n" +
                "выходить с животными за территорию приюта.";

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        SendMessage expected = new SendMessage(chatId, expectedMessage);

        //test execution
        SendMessage actual = infoService.getSafetyPrecautions(update);
        assertEquals(getJsonString(expected), getJsonString(actual));
    }

    @Test
    void getHowToAdopt_Success() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Добрый день! Здесь вы можете получить следующую информацию\n" +
                "/getListPets - Выдать список животных для усыновления.\n" +
                "/getRulesForMeetingAnimals - Выдать правила знакомства с животным до того, как забрать его из приюта.\n" +
                "/getRequiredDocuments - Выдать список документов, необходимых для того, чтобы взять животное из приюта.\n" +
                "/getInfoTransportationOfAnimals - Выдать список рекомендаций по транспортировке животного.\n" +
                "/getMakingHomeForSmallAnimal - Выдать список рекомендаций по обустройству дома для щенка.\n" +
                "/getMakingHomeForAdultAnimal - Выдать список рекомендаций по обустройству дома для взрослого животного.\n" +
                "/getInfoForAnimalWithDisabilities - Выдать список рекомендаций по обустройству дома для животного с ограниченными возможностями (зрение, передвижение).\n" +
                "/getTipsForFirstCommunication - Выдать советы кинолога по первичному общению с собакой.\n" +
                "/getTipsForFurtherCommunication - Выдать рекомендации по проверенным кинологам для дальнейшего обращения к ним.\n" +
                "/getReasonsForRefusal - Выдать список причин, почему могут отказать и не дать забрать собаку из приюта.\n" +
                "\n Чтобы записать Ваши контактные данные для связи отправте сообщение в следующем формате\n" +
                "/leaveContactDetails Имя +7-***-***-**-**\n" +
                "\n/call_volunteer - Для вызова волонтера";

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        SendMessage expected = new SendMessage(chatId, expectedMessage);

        //test execution
        SendMessage actual = infoService.getHowToAdopt(update);
        assertEquals(getJsonString(expected), getJsonString(actual));
    }

    @Test
    void getListPets_Success() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Для получения информации о питомце, выберите интересующего Вас питомца ниже:";

        Pets pet1 = new Pets("name1", "breed1", 5, "photo1.jpg");
        pet1.setId(1L);
        Pets pet2 = new Pets("name2", "breed2", 10, "photo2.jpg");
        pet2.setId(2L);
        List<Pets> expectedPets = List.of(pet1, pet2);

        String[][] expectedPetsButtons = expectedPets.stream()
                .map(e -> new String[]{"/pet_" + e.getId() + " " + e.getName()})
                .toArray(String[][]::new);

        Keyboard expectedReplyKeyboardMarkup = new ReplyKeyboardMarkup(expectedPetsButtons)
                .oneTimeKeyboard(true);

        SendMessage expectedSendMessage = new SendMessage(chatId, expectedMessage);
        expectedSendMessage.replyMarkup(expectedReplyKeyboardMarkup);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        when(petsRepository.findAll()).thenReturn(expectedPets);

        //test execution
        SendMessage actualSendMessage = infoService.getListPets(update);
        assertEquals(getJsonString(expectedSendMessage), getJsonString(actualSendMessage));
    }

    @Test
    void getRulesForMeetingAnimals() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Правило №1 – Не подходи близко к собаке, находящейся на привязи.\n" +
                "\n" +
                "Правило №2 – Не трогай и не гладь чужих собак.\n" +
                "\n" +
                "Правило №3 – Не пугайся и не кричи, если к тебе бежит собака.\n" +
                "\n" +
                "Правило №4 – Не убегай. Остановись. Собака чаще нападает на движущегося человека.\n" +
                "\n" +
                "Правило №5 – Не трогай миску с пищей.\n" +
                "\n" +
                "Правило №6 – Не дразни собаку едой.\n" +
                "\n" +
                "Правило №7 – Не отбирай у собаки еду и игрушки.\n" +
                "\n" +
                "Правило №8 – Не трогай щенков.\n" +
                "\n" +
                "Правило №9 – Не подходи к незнакомой собаке.\n" +
                "\n" +
                "Правило №10 – Не трогай спящую собаку.\n" +
                "\n" +
                "Правило №11 – Не разнимай дерущихся собак.\n" +
                "\n" +
                "Правило №12 – Не подходи к стаям бродячих собак.\n" +
                "\n" +
                "Правило №13 – Не дразни собак.\n" +
                "\n" +
                "Правило №14 – Не позволяй собаке кусать тебя за руки.\n" +
                "\n" +
                "Правило №15 – Не смотри в глаза нападающей собаке.";

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        SendMessage expected = new SendMessage(chatId, expectedMessage);

        //test execution
        SendMessage actual = infoService.getRulesForMeetingAnimals(update);
        assertEquals(getJsonString(expected), getJsonString(actual));
    }

    @Test
    void getRequiredDocuments() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "В нашей организации при передаче животного оформляется договор с будущим хозяином животного. В нем фиксируются данные обеих сторон, оговариваются пункты ответственного содержания животного. Мы также всегда ненавязчиво отслеживаем судьбу наших бывших подопечных. Всех наших питомцев мы лично провожаем до места их дальнейшего жительства. \n";

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        SendMessage expected = new SendMessage(chatId, expectedMessage);

        //test execution
        SendMessage actual = infoService.getRequiredDocuments(update);
        assertEquals(getJsonString(expected), getJsonString(actual));
    }

    @Test
    void getInfoTransportationOfAnimals() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "днище контейнеров или автомобильных кузовов должно быть прочным, цельным и ровным. На его поверхности не должно быть трещин, отверстий, гвоздей, проволоки или сколов. В противном случае животные могут травмироваться в дороге, или попасться в искусственную «ловушку»;\n" +
                "днище обычно застилается специальной впитывающей пленкой, сеном или сухостоем;\n" +
                "железнодорожный контейнер или кузов машины должен защищать животное от дождя и прямых солнечных лучей. Но одновременно с этим он должен обеспечивать свободную циркуляцию свежего воздуха внутри ограниченного пространства;\n" +
                "автомобильный кузов или контейнер должны быть выполнены из натуральных или нетоксичных материалов, например, прочного металла, досок или пластика.";

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        SendMessage expected = new SendMessage(chatId, expectedMessage);

        //test execution
        SendMessage actual = infoService.getInfoTransportationOfAnimals(update);
        assertEquals(getJsonString(expected), getJsonString(actual));
    }

    @Test
    void getMakingHomeForSmallAnimal() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Сразу стоит озаботиться тем, чтобы у животного было свое место. Вы должны заранее подумать о том, где ему будет комфортно и при этом удобно вам.\n" +
                "Щенки любопытны и любят исследовать пространство, поэтому еще до прибытия питомца важно убедиться, что ваш дом безопасен для него. ";

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        SendMessage expected = new SendMessage(chatId, expectedMessage);

        //test execution
        SendMessage actual = infoService.getMakingHomeForSmallAnimal(update);
        assertEquals(getJsonString(expected), getJsonString(actual));
    }

    @Test
    void getMakingHomeForAdultAnimal() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Сразу стоит озаботиться тем, чтобы у животного было свое место. Вы должны заранее подумать о том, где ему будет комфортно и при этом удобно вам.\n" +
                " Большой плюс взрослых животных заключается именно в том, что у них уже сформировался характер. Как правило, они более самодостаточные и спокойные, нежели котята и щенки. Взрослое животное — это скорее друг.";

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        SendMessage expected = new SendMessage(chatId, expectedMessage);

        //test execution
        SendMessage actual = infoService.getMakingHomeForAdultAnimal(update);
        assertEquals(getJsonString(expected), getJsonString(actual));
    }

    @Test
    void getInfoForAnimalWithDisabilities() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Нужно понимать ответственность шага, после которого человек будет отвечать не только за свою жизнь, но и за жизнь хвостатого. Если вы не готовы к этому, то, конечно, лучше никого не брать. ";

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        SendMessage expected = new SendMessage(chatId, expectedMessage);

        //test execution
        SendMessage actual = infoService.getInfoForAnimalWithDisabilities(update);
        assertEquals(getJsonString(expected), getJsonString(actual));
    }

    @Test
    void getTipsForFirstCommunication() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Первые несколько недель животного в новом доме – это адаптация и социализация, поэтому нужно быть готовым к тому, что животное может ходить за вами по пятам, даже в душ. ";

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        SendMessage expected = new SendMessage(chatId, expectedMessage);

        //test execution
        SendMessage actual = infoService.getTipsForFirstCommunication(update);
        assertEquals(getJsonString(expected), getJsonString(actual));
    }

    @Test
    void getTipsForFurtherCommunication() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Что необходимо купить:\n" +
                "\n" +
                "миски — от 200 рублей;\n" +
                "лежанку — от 500 рублей;\n" +
                "игрушки — от 100 рублей;\n" +
                "переноску (обязательно пластиковую) — от 1000 рублей;\n" +
                "шлейку (специалисты утверждают, что использование ошейника и поводка во время прогулки может привести к травмам шеи, позвоночника, гортани, в то время как шлейка распределяет нагрузку более равномерно и, соответственно, является более безопасной) — от 500 рублей;\n" +
                "корм. Животных можно кормить домашним кормом, главное — готовить для них отдельно, подбирать правильные продукты и знать основные принципы питания собак и кошек (например, животным категорически запрещено есть соленое, жирное, сладкое и так далее). Коммерческий корм бывает сухим и влажным и делится на следующие типы: холистик, суперпремиум, премиум и эконом. Самый сбалансированный вариант — это корма холистик. В них хорошо выдержан баланс белков и клетчатки и практически нет вкусовых добавок. Другим хорошим вариантом является линейка суперпремиум. Корма класса премиум и эконом менее полезны для животных, поскольку содержат меньшее количество питательных элементов. Самое главное — обращать внимание на состав корма.";

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        SendMessage expected = new SendMessage(chatId, expectedMessage);

        //test execution
        SendMessage actual = infoService.getTipsForFurtherCommunication(update);
        assertEquals(getJsonString(expected), getJsonString(actual));
    }

    @Test
    void getReasonsForRefusal() {
        //data preparation
        Long chatId = 1L;
        String expectedMessage = "Вам могут отказать, если у вас нет регистрации или собственного жилья. К одиноким пожилым людям и семьям с младенцами в приютах тоже относятся с осторожностью. Иногда сотрудники или волонтеры просят показать квартиру, могут захотеть пообщаться с вашей семьей и соседями.";

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message().chat().id()).thenReturn(chatId);

        SendMessage expected = new SendMessage(chatId, expectedMessage);

        //test execution
        SendMessage actual = infoService.getReasonsForRefusal(update);
        assertEquals(getJsonString(expected), getJsonString(actual));
    }
}