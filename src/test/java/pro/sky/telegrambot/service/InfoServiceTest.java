package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class InfoServiceTest {

    @Mock
    Update update;

    @Mock
    Message message;

    @Mock
    Chat chat;

    @InjectMocks
    InfoService infoService;

    public static String getJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
//            LOGGER.error("Error parsing log entry", e);
            return null;
        }
    }

    @Test
    void getInfoForShelter() {
    }

    @Test
    void getSafetyPrecautions() {
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
    void getListPets() {
    }

    @Test
    void getRulesForMeetingAnimals() {
    }

    @Test
    void getRequiredDocuments() {
    }

    @Test
    void getInfoTransportationOfAnimals() {
    }

    @Test
    void getMakingHomeForSmallAnimal() {
    }

    @Test
    void getMakingHomeForAdultAnimal() {
    }

    @Test
    void getInfoForAnimalWithDisabilities() {
    }

    @Test
    void getTipsForFirstCommunication() {
    }

    @Test
    void getTipsForFurtherCommunication() {
    }

    @Test
    void getReasonsForRefusal() {
    }
}