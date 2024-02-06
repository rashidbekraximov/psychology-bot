package com.example.demo.util;

import com.example.demo.component.UserComponent;
import org.springframework.context.i18n.LocaleContextHolder;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {
    static private final String baseBundleName = "langs/text";

    public static String getLangMessage(String keyword,long id) {
        String lang = UserComponent.getUserById(id).getLang();
        if (lang == null){
            LocaleContextHolder.setLocale(new Locale("uz_lat"));
        }else {
            LocaleContextHolder.setLocale(new Locale(lang));
        }
        String text = ResourceBundle.getBundle(baseBundleName, LocaleContextHolder.getLocale()).getString(keyword);
        ByteBuffer encodedText = StandardCharsets.UTF_8.encode(text);
        return StandardCharsets.UTF_8.decode(encodedText).toString();
    }
}
