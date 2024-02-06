package com.example.demo.bot;

import com.example.demo.component.UserComponent;
import com.example.demo.model.User;
import com.example.demo.util.LanguageManager;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class Bot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return " "; // Data.username;
    }

    @Override
    public String getBotToken() {
        return Data.token;
    }

    @Override
    public void onUpdateReceived(Update chat) {
        SendMessage sender = new SendMessage();
        Message message = chat.getMessage();

        // Callback handler
        if (chat.hasCallbackQuery()) {
            CallbackQuery callbackQuery = chat.getCallbackQuery();
            message = callbackQuery.getMessage();
            sender.setChatId(message.getChatId());
            callbackHandler(callbackQuery, message, sender);
            return;
        }

        sender.setChatId(message.getChatId().toString());
        // Command handler
        if (message.hasEntities()) {
            commandHandler(sender, message);
            return;
        }

        // Text message handler
        if (message.hasText()) {
            textMessageHandler(sender, message);
            return;
        }

        // Any type messages [different from the above] (sticker, files or etc)
        try {
            anyMessage(sender, message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void commandHandler(SendMessage sender, Message message) {
        String command = message.getEntities().get(0).getText();
        sender.enableNotification();
        Checking check = new Checking();
        Controller.checkingMap.put(message.getChatId(), check);

        switch (command) {
            case "/start":
                Controller.start(sender, message);
                break;
            case "/menu":
                Controller.selectDepartment(sender, message);
                sender.setText(LanguageManager.getLangMessage("main", message.getChatId()));
                break;
        }
        send(sender);
    }

    private void textMessageHandler(SendMessage sender, Message message) {
        try {
            String mess = message.getText();
            if (mess.contains("\uD83D\uDCCB")) {
                Controller.question(sender, message);
            } else if (mess.contains("\uD83D\uDCE8")) {
                invitation(sender, message);
            } else if (mess.contains("⚙")) {
                Controller.setting(sender, message);
            } else if (mess.contains("\uD83C\uDDFA\uD83C\uDDFF")) {
                Controller.uz(sender, message);
            } else if (mess.contains("\uD83C\uDDF7\uD83C\uDDFA")) {
                Controller.ru(sender, message);
            } else if (Controller.checkingMap.get(message.getChatId()).isSelectRegion) {
                Controller.openSendPage(sender, message);
            } else if (Controller.checkingMap.get(message.getChatId()).name) {
                Controller.username(sender, message);
            } else if (Controller.checkingMap.get(message.getChatId()).lastname) {
                Controller.lastname(sender, message);
            } else if (mess.contains("\uD83D\uDC68")) {
                Controller.saveGender(sender, message, "Erkak");
            } else if (mess.contains("\uD83D\uDC69")) {
                Controller.saveGender(sender, message, "Ayol");
            } else if (Controller.checkingMap.get(message.getChatId()).bornDate && mess.contains(".") && mess.length() == 10) {
                Controller.saveBornDate(sender, message);
            } else if (mess.contains("\uD83C\uDFE2")) { // 0 dan 13
                Controller.saveRegion(sender, message);
            } else if (mess.contains("⚜")) {
                Controller.saveSelectJob(sender, message);
            } else if (mess.contains("\uD83D\uDCDD")) {
                Controller.selectOtherJob(sender, message);
            } else if (Controller.checkingMap.get(message.getChatId()).job) {
                Controller.saveJob(sender, message);
            } else if (Controller.checkingMap.get(message.getChatId()).seenNumber) {
                Controller.saveSeenYear(sender, message);
            } else if (Controller.checkingMap.get(message.getChatId()).plusDefinition) {
                SendDocument sendDocument = new SendDocument();
                Controller.saveDefinition(sender, message, sendDocument);
                send(sendDocument);
            } else if (mess.contains("✅")) {
                checkPersonAdded(sender, message);
            } else if (mess.contains("\uD83D\uDCDA")) {
                Controller.saveParticipateProgram(sender, message, "Online kurslar");
            } else if (mess.contains("☕")) {
                Controller.saveParticipateProgram(sender, message, "Foydali nonushta");
            } else if (mess.contains("\uD83D\uDDD2")) {
                Controller.saveParticipateProgram(sender, message, "Seminar va treninglar");
            } else if (mess.contains("✈")) {
                Controller.saveParticipateProgram(sender, message, "Sayohatga uyg'unlashgan ta'lim");
            } else if (mess.contains("\uD83C\uDF05")) {
                Controller.saveParticipateProgram(sender, message, "Retrit");
            } else if (mess.contains("\uD83D\uDCBB")) {
                Controller.saveParticipateProgram(sender, message, "Jonli kurslar");
            } else if (mess.contains("\uD83C\uDFA5")) {
                Controller.saveParticipateProgram(sender, message, "Kinoterapiya");
            } else if (mess.contains("\uD83D\uDCE2")) {
                Controller.selectSendRegion(sender, message);
            } else if (Controller.checkingMap.get(message.getChatId()).isSend) {
                Controller.sendMsgToUsersBeforeTravel(sender, message);
            } else if (mess.contains("\uD83D\uDCE4")) {
                sendMessageToRegions(sender, message);
            } else if (mess.contains("\uD83D\uDCC2")) {
                checkUsersData(sender, message);
            } else if (mess.contains("⬅")) {
                Controller.selectDepartment(sender, message);
            } else {
                Controller.none(sender, message);
            }
        } catch (Exception e) {
            sender.setText("Foydalanish uchun /start ni bosing");
        }
        send(sender);
    }

    public void sendMessageToRegions(SendMessage sender, Message message) throws TelegramApiException {
        Chat chat = message.getChat();
        List<User> users = UserComponent.getByRegion(Controller.checkingMap.get(message.getChatId()).isSelectRegionTxt);
        for (User user : users) {
//            execute(new SendMessage(String.valueOf(user.getId()), Controller.checkingMap.get(message.getChatId()).sendTxt));
            ForwardMessage mess = new ForwardMessage();
            mess.setChatId(user.getId());
            mess.setMessageId(Controller.checkingMap.get(message.getChatId()).msgId);
            mess.setFromChatId(Controller.leaderId);
            execute(mess);
        }
        String lang = EmojiParser.parseToUnicode(":white_check_mark: " + LanguageManager.getLangMessage("sended", message.getChatId()));
        Controller.selectDepartment(sender, message);
        sender.setText(lang);
    }

    private void callbackHandler(CallbackQuery query, Message message, SendMessage sendMessage) {
        if (Channel.getChannelMember(message.getChat().getId())) {
            EditMessageText markup = new EditMessageText();
            markup.setChatId(message.getChatId().toString());
            markup.setMessageId(message.getMessageId());
            String lang = EmojiParser.parseToUnicode(":earth_asia: Tilni tanlang");
            String track = EmojiParser.parseToUnicode(":arrow_up:   ");
            markup.setText(lang);
            User user = new User();
            user.setId(message.getChatId());
            user.setUserName(message.getChat().getUserName());
            user.setFirstName(message.getChat().getFirstName());
            user.setUserIsChannel(true);
            UserComponent.save(user);
            Controller.addPeople();
            Controller.lang(sendMessage, message);
            sendMessage.setText(track);
            send(sendMessage);
            send(markup);
        }
    }

    public void checkPersonAdded(SendMessage sender, Message message) {
        Chat chat = message.getChat();
        User user = UserComponent.getUserById(message.getChatId());
        String mess = "";
        if (user.getUserAdded() >= 10) {
            mess = "Kanalimizga 10 ta odam qo'shdingiz va kitob yutib oldingiz !";
            Controller.selectDepartment(sender, message);
            java.io.File sourceFile = new java.io.File("/opt/books/ikki_eshik_orasi.pdf");
            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(message.getChatId());
            sendDocument.setCaption("Minnatdormiz !!");
            sendDocument.setDocument(new InputFile(sourceFile));
            send(sendDocument);
        } else {
            mess = "Kanalimizga " + user.getUserAdded() + " ta odam qo'shgansiz !";
        }
        sender.setText(mess);
    }

    public void checkUsersData(SendMessage sendMessage, Message message) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            UserComponent.getUsersInExcel(workbook);
            OutputStream fileOut = new FileOutputStream("/opt/books/report.xlsx");
            System.out.println("Excel File has been created successfully.");
            workbook.write(fileOut);
            workbook.close();
            java.io.File file = new java.io.File("/opt/books/report.xlsx");
            SendDocument sendDocument = new SendDocument();
            sendDocument.setDocument(new InputFile(file));
            sendDocument.setChatId(message.getChatId());
            send(sendDocument);
            String check = EmojiParser.parseToUnicode(":card_index_dividers: Foydalanuvchilar haqida ma'lumot !");
            sendMessage.setText(check);
        } catch (Exception ex) {
            System.out.println("XLSX Generated Error...");
        }

    }

    private void anyMessage(SendMessage sender, Message message) throws TelegramApiException {
        if (message.getCaption() != null && (message.hasPhoto() || message.hasVideo())){
            List<User> users = new ArrayList<>();
            if (Controller.checkingMap.get(message.getChatId()).isSelectRegionTxt.trim().equals("Hammasi")){
                users = UserComponent.getList();
            }else{
                users = UserComponent.getByRegion(Controller.checkingMap.get(message.getChatId()).isSelectRegionTxt);
            }
            boolean isVideo = false;
            if (message.hasPhoto()) {
                List<PhotoSize> photos = message.getPhoto();
                PhotoSize photo = photos.get(photos.size() - 1);
                String id = photo.getFileId();
                GetFile getFile = new GetFile(id);
                try {
                    org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
                    String url = file.getFileUrl(getBotToken());
                    URL ur = new URL(url);
                    InputStream inputStream = ur.openStream();
                    FileUtils.copyInputStreamToFile(inputStream, new java.io.File("/opt/books/and.jpg"));
                    isVideo = false;
                    // TODO: cache images?
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (message.hasVideo()) {
                Video video = message.getVideo();
                GetFile getFile = new GetFile(video.getFileId());
                try {
                    org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
                    String url = file.getFileUrl(getBotToken());
                    URL ur = new URL(url);
                    InputStream inputStream = ur.openStream();
                    FileUtils.copyInputStreamToFile(inputStream, new java.io.File("/opt/books/vid.mp4"));
                    isVideo = true;
                    // TODO: cache images?
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Controller.checkingMap.get(message.getChatId()).isSend = false;
            for (User user : users) {
                if (isVideo){
                    SendVideo sendVideo = new SendVideo();
                    sendVideo.setVideo(new InputFile(new java.io.File("/opt/books/vid.mp4")));
                    String txt = message.getCaption().replace("Username", user.getFirstName());
                    sendVideo.setCaption(txt);
                    sendVideo.setChatId(user.getId());
                    send(sendVideo);
                }else {
                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setPhoto(new InputFile(new java.io.File("/opt/books/and.jpg")));
                    String txt = message.getCaption().replace("Username", user.getFirstName());
                    sendPhoto.setCaption(txt);
                    sendPhoto.setChatId(user.getId());
                    send(sendPhoto);
                }
            }
            String lang = EmojiParser.parseToUnicode(":white_check_mark: " + LanguageManager.getLangMessage("sended", message.getChatId()));
            Controller.selectDepartment(sender, message);
            sender.setText(lang);
            send(sender);
        }
    }

    private void invitation(SendMessage sender, Message message) throws InterruptedException {

        SendVideo sendVideo = new SendVideo();
        sendVideo.setChatId(message.getChatId());
        sendVideo.setVideo(new InputFile("https://v1.pinimg.com/videos/mc/720p/2c/0a/d3/2c0ad31dcb548b9e355f3a50b206d664.mp4"));
        sendVideo.setCaption("2024-yilning eng manfaatli va qaynoq loyihasi sizni kutmoqda! \n");
        send(sendVideo);

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(message.getChatId());
        sendPhoto.setPhoto(new InputFile("https://i.pinimg.com/564x/f3/0a/be/f30abe58307690980b6c422ebc73403e.jpg"));
        sendPhoto.setCaption("2024-yilning eng manfaatli va qaynoq loyihasi sizni kutmoqda! \n" +
                "\n" +
                "Sizda - yakunlanayotgan yilni munosib sarhisob qilish va 2024-yil uchun yangi maqsadlarni 7 kun davomida eng yaxshi mutaxassislar bilan qo'yish imkoniyati bor❤️\u200D\uD83D\uDD25)\n" +
                "\n" +
                "Ushbu imkoniyatni sizga - \"Azizam\" taqdim qiladi! \n" +
                "\n" +
                "- Loyihada ishtirok etish BEPUL. \n" +
                "\n" +
                "\uD83D\uDC49\uD83C\uDFFB <a href=\"https://t.me/azizam_supportBot?start=" + message.getChatId() + "\">Havola ustiga bosing</a>  \uD83D\uDC48\uD83C\uDFFB ");

        sendPhoto.setParseMode(ParseMode.HTML);
        String check = EmojiParser.parseToUnicode(":ballot_box_with_check: " + LanguageManager.getLangMessage("checking", message.getChatId()));
        sender.setText(check);
        send(sendPhoto);
        Controller.openCheck(sender, message);
    }

    private void send(SendMessage sender) {
        try {
            execute(sender);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(ForwardMessage sender) {
        try {
            execute(sender);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(SendDocument sender) {
        try {
            execute(sender);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(EditMessageCaption sender) {
        try {
            execute(sender);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(SendPhoto sender) {
        try {
            execute(sender);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(SendVideo sender) {
        try {
            execute(sender);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(EditMessageText markup) {
        try {
            execute(markup);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
