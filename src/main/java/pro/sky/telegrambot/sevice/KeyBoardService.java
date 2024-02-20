package pro.sky.telegrambot.sevice;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public class KeyBoardService {
    public static InlineKeyboardMarkup prepareKeyboardShelter(String text) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        InlineKeyboardButton button1 = new InlineKeyboardButton("Информацмя о приюте " + text);
        InlineKeyboardButton button2 = new InlineKeyboardButton("Как взять питомца");
        InlineKeyboardButton button3 = new InlineKeyboardButton("Отправить отчет");
        InlineKeyboardButton button4 = new InlineKeyboardButton("Позвать волонетра");

        button1.callbackData("INFO" + text);
        button2.callbackData("GETPET" + text);
        button3.callbackData("SENDREPORT");
        button4.callbackData("CALL_VOLUNTEER" + text);

        markupInline.addRow(button1, button2);
        markupInline.addRow(button3, button4);
        return markupInline;
}
}
