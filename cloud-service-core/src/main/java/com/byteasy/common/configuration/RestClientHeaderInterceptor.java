package com.byteasy.common.configuration;

import cn.hutool.core.codec.Base64;
import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.Constant;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.common.token.UserInfoBuilder;
import com.byteasy.common.util.JacksonUtilOld;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestClientHeaderInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        UserInfo token = TokenUtil.getUserinfo();
        if (token != null) {
            token.setForwardBy(true);
            request.getHeaders().set(Constant.USER_INFO, Base64.encode(JacksonUtilOld.toJson(token)));
        } else {
            String appName = SpringUtil.getApplicationName();
            request.getHeaders().set(Constant.USER_INFO, UserInfoBuilder.builderByMicroService(appName, appName));
        }

        // 继续执行请求调用
        return execution.execute(request, body);
    }
}
