package com.byteasy.platform;

public interface Constant {
    String SERVICE_NAME = "platform-service";

    String CONTEXT_PATH = "platformSvr";

    String SAPI = "/" + CONTEXT_PATH + com.byteasy.Constant.SAPI;

    String UPLOAD_DIR_PUBLIC = "/public";
    String UPLOAD_DIR_PRIVATE = "/private";

    String DICTIONARY_PLATFORM_SETTING = "platform_settings";
    String DIC_SETTING_FAVICON = "favicon";
    String DIC_SETTING_LOGO_IMG = "logoImg";
    String DIC_SETTING_BACKGROUND_IMG = "backgroundImg";
    String DIC_SETTING_TITLE = "title";

    /**
     * 平台常驻缓存
     */
    String PLATFORM_REP_CACHE = "platformRepCache";
}
