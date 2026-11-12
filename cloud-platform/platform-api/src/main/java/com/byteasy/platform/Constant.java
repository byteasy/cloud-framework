package com.byteasy.platform;

import java.io.File;

public interface Constant {
    String SERVICE_NAME = "platform-service";

    String CONTEXT_PATH = "platformSvr";

    String SAPI = "/" + CONTEXT_PATH + com.byteasy.Constant.SAPI;

    String UPLOAD_DIR_PUBLIC = "/public";
    String UPLOAD_DIR_PRIVATE = "/private";

    String DICTIONARY_PLATFORM_SETTING = "platform_settings";
    String DIC_SETTING_FAVICON = "favicon";
    /**
     * 亮色主题Logo 字体黑色
     */
    String DIC_SETTING_LOGO_IMG = "logoImg";
    /**
     * 暗色主题Logo，字体白色
     */
    String DIC_SETTING_LOGO_IMG_WHITE = "logoImgWhite";
    /**
     * 登录背景图片
     */
    String DIC_SETTING_BACKGROUND_IMG = "backgroundImg";
    /**
     * 系统的标题
     */
    String DIC_SETTING_TITLE = "title";
    /**
     * 全局搜索的跳转路由
     */
    String DIC_SEARCH_ROUTE = "fullSearchRoute";
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

    /**
     * 上传文件夹路径前缀
     */
    String PUBLIC_UPLOAD_DIR_PREFIX = File.separator + "upload";
}
