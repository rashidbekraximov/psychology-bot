package com.example.demo.bot;


import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMemberCount;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Channel {

    private static Bot bot;

    public Channel(Bot bot){
        Channel.bot = bot;
    }

    public static String getChannelInviteLink() throws TelegramApiException {
        return Channel.bot.execute(new GetChat("@qochqors")).getInviteLink();
    }

    public static boolean getChannelMember(Long id) {
        try {
            ChatMember chat = Channel.bot.execute(new GetChatMember("@qochqors", id));
//            ForwardMessage forwardMessage = new ForwardMessage();
//            forwardMessage.se
            if (chat.getStatus().equals("left")){
                return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static int getChannelMemberCount() throws TelegramApiException {
        return Channel.bot.execute(new GetChatMemberCount("@qochqors"));
    }

}
