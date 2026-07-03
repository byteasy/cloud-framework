package com.byteasy.component;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONObject;
import com.byteasy.dto.Column;
import com.byteasy.enums.YesNo;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
@Log4j2
public class I18nUtil {
    // 静态变量存储MessageSource
    private static MessageSource staticMessageSource;

    @Autowired
    private MessageSource messageSource;

    // 初始化静态变量（PostConstruct：Bean初始化后执行）
    @PostConstruct
    public void init() {
        staticMessageSource = messageSource;
    }

    // 静态方法：任意位置调用
    public static String getMessage(String key, Object... args) {
        return staticMessageSource.getMessage(key, args, key, LocaleContextHolder.getLocale());
    }

    public static Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    public static String getLanguageValue(String i18nValue) {
        if (i18nValue == null || i18nValue.isEmpty()) {
            return null;
        }
        if (!i18nValue.startsWith("{")) {
            return i18nValue;
        }
        Locale locale = LocaleContextHolder.getLocale();
        String lang = formatLang(locale.getLanguage());
        JSONObject obj = new JSONObject(i18nValue);
        String langValue = obj.getStr(lang);
        if (langValue == null) {
            langValue = obj.getStr("zh_CN");
        }
        if (langValue == null) {
            langValue = obj.getStr("en");
        }
        if (langValue == null) {
            langValue = i18nValue;
        }
        return langValue;
    }

    public static String formatLang(String lang) {
        if (lang.contains("zh")) {
            return "zh_CN";
        }
        if (lang.contains("en")) {
            return "en";
        }
        return lang;
    }

    public static String genLanguageValue(String raw) {
        Locale locale = LocaleContextHolder.getLocale();
        String lang = formatLang(locale.getLanguage());
        JSONObject obj = new JSONObject();
        obj.put(lang, raw);
        return obj.toString();
    }

    public static <T> void encodeToI18nFormat(List entities, List<Column> columns) {
        for (Column column : columns) {
            if (YesNo.YES.equals(column.getIsI18n())) {
                for (Object entity : entities) {
                    if (entity instanceof Map) {
                        Object raw = ((Map) entity).get(column.getName());
                        ((Map) entity).put(column.getName(), genLanguageValue(String.valueOf(raw)));
                    } else {
                        Object raw = ReflectUtil.getFieldValue(entity, column.getName());
                        ReflectUtil.setFieldValue(entity, column.getName(), genLanguageValue(String.valueOf(raw)));
                    }
                }
            }
        }
    }

    public static String convertI18nFieldZhValue(String chinese) {
        JSONObject obj = new JSONObject();
        obj.set("zh_CN", chinese);
        return obj.toString();
    }
}
