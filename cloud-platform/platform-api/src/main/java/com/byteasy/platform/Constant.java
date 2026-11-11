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
     * 密钥，用于外网链接生成与加密
     */
    String DIC_SETTING_AES_PWD = "aesPwd";
    /**
     * 默认密码
     */
    String DEFAULT_AES_PWD = "byteasy#1239";

    /**
     * 平台常驻缓存
     */
    String PLATFORM_REP_CACHE = "platformRepCache";

    /**
     * OAuth2免登录cookie名称
     */
    String OAUTH2_COOKIE_THIRD_TOKEN_NAME = "BT_SSO_TICKET";

    /**
     * 实体变更通知
     */
    String TOPIC_ENTITY_CHANGE = "entity_change";
}
