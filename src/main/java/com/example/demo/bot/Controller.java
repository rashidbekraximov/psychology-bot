package com.example.demo.bot;

import com.example.demo.component.UserComponent;
import com.example.demo.model.User;
import com.example.demo.util.LanguageManager;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.codec.language.bm.Lang;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Controller {

    static long id = 0;

    static long leaderId = 925704920L;

    static Map<Long,Checking> checkingMap = new HashMap<>();

    public static void start(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        sender.setText(String.format("Salom, %s! Bizning botga xush kelibsiz!", chat.getFirstName()));
        if (message.getText().contains("/start") && message.getText().length() > 10) {
            byte size = (byte) message.getText().length();
            String num = message.getText().substring(7, size);
            if (!Channel.getChannelMember(message.getChat().getId())) {
                id = Long.parseLong(num);
            } else {
                id = 0;
            }
        }
        if (UserComponent.getById(chat.getId()) || !Channel.getChannelMember(message.getChat().getId())) {
            noneKeyboard(sender);
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            List<InlineKeyboardButton> but = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText("1-kanal");
//            https://t.me/azizaxontashxodjayeva
            inlineKeyboardButton.setUrl("https://t.me/qochqors");
            but.add(inlineKeyboardButton);
            buttons.add(but);
            buttons.add(List.of(inlineButton("\u2705 Tekshirish", "currency")));
            sender.setReplyMarkup(new InlineKeyboardMarkup(buttons));
        } else if (UserComponent.getUserById(chat.getId()) != null && UserComponent.getUserById(chat.getId()).getLang() == null) {
            lang(sender, message);
        } else {
            selectDepartment(sender, message);
        }
    }

    public static void selectDepartment(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        String text1 = ":clipboard: " + LanguageManager.getLangMessage("anketa", message.getChatId());
        String text2 = ":incoming_envelope: " + LanguageManager.getLangMessage("invitation", message.getChatId());
        String text3 = ":gear: " + LanguageManager.getLangMessage("setting", message.getChatId());
        String text4 = ":house: " + LanguageManager.getLangMessage("main", message.getChatId());
        String text5 = ":open_file_folder: " + LanguageManager.getLangMessage("inform", message.getChatId());
        String text6 = ":loudspeaker: " + LanguageManager.getLangMessage("connection", message.getChatId());
        String clip = EmojiParser.parseToUnicode(text1);
        String invite = EmojiParser.parseToUnicode(text2);
        String setting = EmojiParser.parseToUnicode(text3);
        String home = EmojiParser.parseToUnicode(text4);
        String inform = EmojiParser.parseToUnicode(text5);
        String connection = EmojiParser.parseToUnicode(text6);
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(new KeyboardRow(keyboardButtons(clip, invite)));
        if (message.getChatId() == leaderId) {
            rows.add(new KeyboardRow(keyboardButtons(connection, inform)));
        }
        rows.add(new KeyboardRow(keyboardButtons(setting)));
        sender.setText(home);
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setKeyboard(rows);
        sender.setReplyMarkup(markup);
    }

    public static void noneKeyboard(SendMessage sender) {
        ReplyKeyboardRemove mark = new ReplyKeyboardRemove();
        mark.setRemoveKeyboard(true);
        sender.setReplyMarkup(mark);
    }

    public static void question(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        checkingMap.get(message.getChatId()).name = true;
        String txt = "\uD83D\uDD8A " + LanguageManager.getLangMessage("firstname", message.getChatId()) + " ?";
        noneKeyboard(sender);
        sender.setText(txt);
    }



    public static void openCheck(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        String text1 = ":white_check_mark: " + LanguageManager.getLangMessage("check", chat.getId());
        String text2 = ":arrow_left: " + LanguageManager.getLangMessage("back", chat.getId());
        String check1 = EmojiParser.parseToUnicode(text1);
        String check2 = EmojiParser.parseToUnicode(text2);
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(new KeyboardRow(keyboardButtons(check1)));
        rows.add(new KeyboardRow(keyboardButtons(check2)));
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setKeyboard(rows);
        sender.setReplyMarkup(markup);
    }

    public static void openSend(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        String text1 = ":outbox_tray: " + LanguageManager.getLangMessage("send", chat.getId());
        String check = EmojiParser.parseToUnicode(text1);
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(new KeyboardRow(keyboardButtons(check)));
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setKeyboard(rows);
        sender.setReplyMarkup(markup);
    }

    public static void addPeople() {
        if (id != 0) {
            User user = UserComponent.getUserById(id);
            user.setUserAdded(user.getUserAdded() + 1);
            UserComponent.save(user);
        }
    }

    public static void lang(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        String ru = EmojiParser.parseToUnicode(":ru: Ruscha");
        String uzb = EmojiParser.parseToUnicode(":uz: O'zbekcha");
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(new KeyboardRow(keyboardButtons(uzb, ru)));
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setKeyboard(rows);
        sender.setReplyMarkup(markup);
    }

    public static void selectRegion(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        List<KeyboardRow> rows = new ArrayList<>();
        String icon = ":office: ";
        String region1 = EmojiParser.parseToUnicode(icon + "Toshkent shahar");
        String region2 = EmojiParser.parseToUnicode(icon + "Toshkent viloyat");
        String region3 = EmojiParser.parseToUnicode(icon + "Xorazm");
        String region4 = EmojiParser.parseToUnicode(icon + "Sirdaryo");
        String region5 = EmojiParser.parseToUnicode(icon + "Andijon");
        String region6 = EmojiParser.parseToUnicode(icon + "Farg'ona");
        String region7 = EmojiParser.parseToUnicode(icon + "Namangan");
        String region8 = EmojiParser.parseToUnicode(icon + "Surxondaryo");
        String region9 = EmojiParser.parseToUnicode(icon + "Qashqadaryo");
        String region10 = EmojiParser.parseToUnicode(icon + "Jizzax");
        String region11 = EmojiParser.parseToUnicode(icon + "Buxoro");
        String region12 = EmojiParser.parseToUnicode(icon + "Navoiy");
        String region13 = EmojiParser.parseToUnicode(icon + "Samarqand");
        String region14 = EmojiParser.parseToUnicode(icon + "Qoraqalpog'iston");
        String region15 = EmojiParser.parseToUnicode(icon + "Tojikiston");
        String region16 = EmojiParser.parseToUnicode(icon + "Turkmaniston");
        String region17 = EmojiParser.parseToUnicode(icon + "Qozog'iston");
        String region18 = EmojiParser.parseToUnicode(icon + "Qirg'iziston");
        rows.add(new KeyboardRow(keyboardButtons(region1, region2)));
        rows.add(new KeyboardRow(keyboardButtons(region3, region4)));
        rows.add(new KeyboardRow(keyboardButtons(region5, region6)));
        rows.add(new KeyboardRow(keyboardButtons(region7, region8)));
        rows.add(new KeyboardRow(keyboardButtons(region9, region10)));
        rows.add(new KeyboardRow(keyboardButtons(region11, region12)));
        rows.add(new KeyboardRow(keyboardButtons(region13, region14)));
        rows.add(new KeyboardRow(keyboardButtons(region15, region16)));
        rows.add(new KeyboardRow(keyboardButtons(region17, region18)));
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setKeyboard(rows);
        sender.setReplyMarkup(markup);
    }

    public static void selectRegionForSend(SendMessage sender, Message message) {
        List<KeyboardRow> rows = new ArrayList<>();
        String icon = ":office: ";
        String region = EmojiParser.parseToUnicode(icon + "Hammasi");
        String region1 = EmojiParser.parseToUnicode(icon + "Toshkent shahar");
        String region2 = EmojiParser.parseToUnicode(icon + "Toshkent viloyat");
        String region3 = EmojiParser.parseToUnicode(icon + "Xorazm");
        String region4 = EmojiParser.parseToUnicode(icon + "Sirdaryo");
        String region5 = EmojiParser.parseToUnicode(icon + "Andijon");
        String region6 = EmojiParser.parseToUnicode(icon + "Farg'ona");
        String region7 = EmojiParser.parseToUnicode(icon + "Namangan");
        String region8 = EmojiParser.parseToUnicode(icon + "Surxondaryo");
        String region9 = EmojiParser.parseToUnicode(icon + "Qashqadaryo");
        String region10 = EmojiParser.parseToUnicode(icon + "Jizzax");
        String region11 = EmojiParser.parseToUnicode(icon + "Buxoro");
        String region12 = EmojiParser.parseToUnicode(icon + "Navoiy");
        String region13 = EmojiParser.parseToUnicode(icon + "Samarqand");
        String region14 = EmojiParser.parseToUnicode(icon + "Qoraqalpog'iston");
        String region15 = EmojiParser.parseToUnicode(icon + "Tojikiston");
        String region16 = EmojiParser.parseToUnicode(icon + "Turkmaniston");
        String region17 = EmojiParser.parseToUnicode(icon + "Qozog'iston");
        String region18 = EmojiParser.parseToUnicode(icon + "Qirg'iziston");
        rows.add(new KeyboardRow(keyboardButtons(region)));
        rows.add(new KeyboardRow(keyboardButtons(region1, region2)));
        rows.add(new KeyboardRow(keyboardButtons(region1, region2)));
        rows.add(new KeyboardRow(keyboardButtons(region3, region4)));
        rows.add(new KeyboardRow(keyboardButtons(region5, region6)));
        rows.add(new KeyboardRow(keyboardButtons(region7, region8)));
        rows.add(new KeyboardRow(keyboardButtons(region9, region10)));
        rows.add(new KeyboardRow(keyboardButtons(region11, region12)));
        rows.add(new KeyboardRow(keyboardButtons(region13, region14)));
        rows.add(new KeyboardRow(keyboardButtons(region15, region16)));
        rows.add(new KeyboardRow(keyboardButtons(region17, region18)));
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setKeyboard(rows);
        sender.setReplyMarkup(markup);
    }


    public static void selectJob(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        List<KeyboardRow> rows = new ArrayList<>();
        String icon = ":fleur_de_lis: ";
        String region1 = EmojiParser.parseToUnicode(icon + LanguageManager.getLangMessage("bank",message.getChatId()));
        String region2 = EmojiParser.parseToUnicode(icon + LanguageManager.getLangMessage("home",message.getChatId()));
        String region3 = EmojiParser.parseToUnicode(icon + LanguageManager.getLangMessage("psix",message.getChatId()));
        String region4 = EmojiParser.parseToUnicode(icon + LanguageManager.getLangMessage("driver",message.getChatId()));
        String region5 = EmojiParser.parseToUnicode(icon + LanguageManager.getLangMessage("cheev",message.getChatId()));
        String region6 = EmojiParser.parseToUnicode(icon + LanguageManager.getLangMessage("teach",message.getChatId()));
        String region7 = EmojiParser.parseToUnicode(icon + LanguageManager.getLangMessage("manager",message.getChatId()));
        String region8 = EmojiParser.parseToUnicode(icon + LanguageManager.getLangMessage("tad",message.getChatId()));
        String region9 = EmojiParser.parseToUnicode(icon + LanguageManager.getLangMessage("programmer",message.getChatId()));
        String region10 = EmojiParser.parseToUnicode(icon + LanguageManager.getLangMessage("account",message.getChatId()));
        String region11 = EmojiParser.parseToUnicode(":memo: " + LanguageManager.getLangMessage("other",message.getChatId()));
        rows.add(new KeyboardRow(keyboardButtons(region1, region2)));
        rows.add(new KeyboardRow(keyboardButtons(region3, region4)));
        rows.add(new KeyboardRow(keyboardButtons(region5, region6)));
        rows.add(new KeyboardRow(keyboardButtons(region7, region8)));
        rows.add(new KeyboardRow(keyboardButtons(region9, region10)));
        rows.add(new KeyboardRow(keyboardButtons(region11)));
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setKeyboard(rows);
        sender.setReplyMarkup(markup);
    }

    public static void selectParticipateProgram(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        String online = EmojiParser.parseToUnicode(":books: " + LanguageManager.getLangMessage("online", message.getChatId()));
        String breakfast = EmojiParser.parseToUnicode(":coffee: " + LanguageManager.getLangMessage("use", message.getChatId()));
        String seminar = EmojiParser.parseToUnicode(":spiral_note_pad: " + LanguageManager.getLangMessage("seminar", message.getChatId()));
        String travel = EmojiParser.parseToUnicode(":airplane: " + LanguageManager.getLangMessage("travel", message.getChatId()));
        String movie = EmojiParser.parseToUnicode(":movie_camera: " + LanguageManager.getLangMessage("movie", message.getChatId()));
        String live = EmojiParser.parseToUnicode(":computer: " + LanguageManager.getLangMessage("live", message.getChatId()));
        String retrit = EmojiParser.parseToUnicode(":sunrise: " + LanguageManager.getLangMessage("retrit", message.getChatId()));
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(new KeyboardRow(keyboardButtons(online, breakfast)));
        rows.add(new KeyboardRow(keyboardButtons(seminar, movie)));
        rows.add(new KeyboardRow(keyboardButtons(live, retrit)));
        rows.add(new KeyboardRow(keyboardButtons(travel)));
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setKeyboard(rows);
        sender.setReplyMarkup(markup);
    }

    public static void saveRegion(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        User user = UserComponent.getUserById(message.getChatId());
        byte size = (byte) message.getText().length();
        String region = message.getText().substring(2, size);
        user.setRegion(region);
        UserComponent.save(user);
        selectJob(sender,message);
        String uzb = "⚜️Kasbingizni tanlang: ";
        checkingMap.get(message.getChatId()).job = true;
        sender.setText(uzb);
    }

    public static void saveSelectJob(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        User user = UserComponent.getUserById(message.getChatId());
        byte size = (byte) message.getText().length();
        String job = message.getText().substring(2, size);
        user.setJob(job);
        UserComponent.save(user);
        noneKeyboard(sender);
        checkingMap.get(message.getChatId()).job = false;
        checkingMap.get(message.getChatId()).seenNumber = true;
        String uzb = "Qachondan beri kuzatasiz: ";
        sender.setText(uzb);
    }

    public static void saveJob(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        User user = UserComponent.getUserById(message.getChatId());
        user.setJob(message.getText());
        UserComponent.save(user);
        String uzb = "Qachondan beri kuzatasiz: ";
        checkingMap.get(message.getChatId()).job = false;
        checkingMap.get(message.getChatId()).seenNumber = true;
        sender.setText(uzb);
    }

    public static void selectOtherJob(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        String uzb = EmojiParser.parseToUnicode(":memo: Kasbni kiriting:");
        sender.setText(uzb);
    }

    public static void saveSeenYear(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        User user = UserComponent.getUserById(message.getChatId());
        user.setSeenYear(message.getText());
        UserComponent.save(user);
        String uzb = ":file_folder: " + LanguageManager.getLangMessage("program", message.getChatId());
        checkingMap.get(message.getChatId()).seenNumber = false;
        selectParticipateProgram(sender, message);
        sender.setText(EmojiParser.parseToUnicode(uzb));
    }

    public static void uz(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        User user = UserComponent.getUserById(message.getChatId());
        user.setLang("uz_lat");
        UserComponent.save(user);
        selectDepartment(sender, message);
        String uzb = EmojiParser.parseToUnicode(":uz: O'zbek tili tanlandi");
        sender.setText(uzb);
    }

    public static void openGender(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        String male = EmojiParser.parseToUnicode(":man: " + LanguageManager.getLangMessage("male", message.getChatId()));
        String female = EmojiParser.parseToUnicode(":woman: " + LanguageManager.getLangMessage("female", message.getChatId()));
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(new KeyboardRow(keyboardButtons(male, female)));
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setKeyboard(rows);
        sender.setReplyMarkup(markup);
    }

    public static void username(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        User user = UserComponent.getUserById(message.getChatId());
        user.setFirstName(message.getText());
        UserComponent.save(user);
        checkingMap.get(message.getChatId()).name = false;
        checkingMap.get(message.getChatId()).lastname = true;
        String txt = "\uD83D\uDD8A " + LanguageManager.getLangMessage("lastname", message.getChatId()) + " ?";
        sender.setText(txt);
    }

    public static void lastname(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        User user = UserComponent.getUserById(message.getChatId());
        user.setLastName(message.getText());
        UserComponent.save(user);
        openGender(sender, message);
        checkingMap.get(message.getChatId()).lastname = false;
        String uzb = EmojiParser.parseToUnicode(":couple: " + LanguageManager.getLangMessage("gender",message.getChatId()));
        sender.setText(uzb);
    }

    public static void saveGender(SendMessage sender, Message message, String gender) {
        Chat chat = message.getChat();
        User user = UserComponent.getUserById(message.getChatId());
        user.setGender(gender);
        UserComponent.save(user);
        noneKeyboard(sender);
        checkingMap.get(message.getChatId()).bornDate = true;
        String txt = LanguageManager.getLangMessage("born_date", message.getChatId()) + " ➡️ 22.03.1972";
        sender.setText(txt);
    }

    public static void saveParticipateProgram(SendMessage sender, Message message, String program) {
        Chat chat = message.getChat();
        User user = UserComponent.getUserById(message.getChatId());
        user.setParticipateProgram(program);
        UserComponent.save(user);
        noneKeyboard(sender);
        checkingMap.get(message.getChatId()).plusDefinition = true;
        String txt = """
                "Azizam" Klubi rivojlanishi yo'lida sizga foydali bo'lish \s
                 maqsadida qanday mavzularni yoritishimizni istaysiz. Masalan: \s
                1. ______________\s
                2._______________\s
                3._______________""";
        sender.setText(txt);
    }


    public static void selectSendRegion(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        String txt = ":earth_asia: " + LanguageManager.getLangMessage("region", message.getChatId());
        String lang = EmojiParser.parseToUnicode(txt);
        checkingMap.get(message.getChatId()).isSelectRegion = true;
        selectRegionForSend(sender, message);
        sender.setText(lang);
    }

    public static void openSendPage(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        byte size = (byte) message.getText().length();
        String region = message.getText().substring(2, size);
        String txt = "Jo'natish uchun ma'lumotni kiriting: ";
        checkingMap.get(message.getChatId()).isSelectRegion = false;
        checkingMap.get(message.getChatId()).isSend = true;
        checkingMap.get(message.getChatId()).isSelectRegionTxt = region;
        noneKeyboard(sender);
        sender.setText(txt);
    }

    public static void sendMsgToUsersBeforeTravel(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        String txt = "";
        if (!message.getText().equals("")) {
            checkingMap.get(message.getChatId()).isSend = false;
            checkingMap.get(message.getChatId()).sendTxt = message.getText();
            checkingMap.get(message.getChatId()).msgId = message.getMessageId();
            txt = "Jo'natish tugmasini bosing !";
            openSend(sender, message);
        } else {
            txt = "Qaytadan kiriting !";
        }
        sender.setText(txt);
    }


    public static void saveDefinition(SendMessage sender, Message message, SendDocument sendDocument) {
        Chat chat = message.getChat();
        User user = UserComponent.getUserById(message.getChatId());
        user.setPlusDefinition(message.getText());
        UserComponent.save(user);
        checkingMap.get(message.getChatId()).plusDefinition = false;
        selectDepartment(sender, message);
        java.io.File sourceFile = new java.io.File("/opt/books/ikki_eshik_orasi.pdf");
        sendDocument.setChatId(message.getChatId());
        sendDocument.setDocument(new InputFile(sourceFile));
        String uzb = "Javoblaringiz uchun minnatdormiz !! \n" +
                "\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89 Sizga maxsus hadiyamiz bor qabul qilib oling !";
        sender.setText(uzb);
    }

    public static void saveBornDate(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        User user = UserComponent.getUserById(message.getChatId());
        user.setDate(message.getText());
        UserComponent.save(user);
        selectRegion(sender, message);
        checkingMap.get(message.getChatId()).bornDate = false;
        String txt = ":earth_asia:" + LanguageManager.getLangMessage("where", message.getChatId());
        String lang = EmojiParser.parseToUnicode(txt);
        sender.setText(lang);
    }

    public static void ru(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        User user = UserComponent.getUserById(message.getChatId());
        user.setLang("ru");
        UserComponent.save(user);
        selectDepartment(sender, message);
        String txt = ":ru:" + LanguageManager.getLangMessage("ru_selected", message.getChatId());
        String ru = EmojiParser.parseToUnicode(txt);
        sender.setText(ru);
    }

    public static void setting(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        lang(sender, message);
        String txt = ":earth_asia:" + LanguageManager.getLangMessage("change_lang", message.getChatId());
        String lang = EmojiParser.parseToUnicode(txt);
        sender.setText(lang);
    }

    public static void none(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        String txt = ":exclamation: " + LanguageManager.getLangMessage("enter_right_data", message.getChatId());
        String lang = EmojiParser.parseToUnicode(txt);
        sender.setText(lang);
    }

    private static InlineKeyboardButton inlineButton(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData)
                .build();
    }

    private static List<KeyboardButton> keyboardButtons(String... names) {
        List<KeyboardButton> buttons = new ArrayList<>();

        for (String name : names)
            buttons.add(new KeyboardButton(name));

        return buttons;
    }

}