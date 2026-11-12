package com.byteasy.platform.configuration;

import com.byteasy.platform.Constant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import static com.byteasy.platform.Constant.*;

@Component
@ConfigurationProperties(prefix = "platform")
@RefreshScope
@Data
public class PlatformProperties {
    /**
     * 缓存名
     */
    private String platformCacheName = Constant.PLATFORM_REP_CACHE;
    /**
     * 第三方系统单点登录允许执行的openApi白名单
     */
    private String thirdLoginPermittedFunctionCodes = "";
    /**
     * 文件根目录
     */
    private String dataRootDir;
    /**
     * 允许访问的下载文件白名单
     */
    private String themePermitFileList = DIC_SETTING_FAVICON + "," + DIC_SETTING_LOGO_IMG + "," + DIC_SETTING_BACKGROUND_IMG;
}
