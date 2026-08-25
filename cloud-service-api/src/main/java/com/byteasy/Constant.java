package com.byteasy;

public interface Constant {

    /**
     * public api
     * 对外网通过网关直接公开即可访问，无token认证.
     */
    String PAPI = "/papi";

    /**
     * service api
     * 供前端登录用户通过UI、第三方系统、南北流量（其它微服务）调用
     */
    String SAPI = "/sapi";

    /**
     * 应用的ID
     */
    String APP_ID = "appId";

    /**
     * 应用的接入密钥
     */
    String APP_SECRET = "appSecret";

    /**
     * 用户领域
     */
    String REALM = "realm";

    /**
     * 用户信息
     */
    String USER_INFO = "x-userinfo";
    /**
     * trace id
     */
    String TRACE_ID = "X-Trace-Id";

    /**
     * 国际化语言参数名称
     */
    String PARAM_LANG = "lang";

    String VALID_MSG_KEY = "validateResult";

    String i18nForbiddenOp = "ForbiddenOp";

    /**
     * 平台基础类
     */
    String CLS_ENTITY_DEFINE = "com.byteasy.platform.entity.EntityDefine";

    String CLS_FIELD_DEFINE = "com.byteasy.platform.entity.FieldDefine";

    String CLS_FIELD_GROUP = "com.byteasy.platform.entity.FieldGroup";

    String CLS_JS_SCRIPT = "com.byteasy.platform.entity.JsScript";

    String CLS_OPEN_API = "com.byteasy.platform.entity.OpenApi";

    String CLS_SERVICE_INSTANCE = "com.byteasy.platform.entity.ServiceInstance";
}
