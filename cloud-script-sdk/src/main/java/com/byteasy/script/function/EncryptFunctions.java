package com.byteasy.script.function;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.byteasy.common.component.DictionaryCache;
import com.byteasy.platform.Constant;
import com.byteasy.script.JsFunction;
import com.byteasy.script.JsFunctions;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Log4j2
public class EncryptFunctions implements JsFunctions {

    @Autowired
    private DictionaryCache dictionaryCache;

    @Override
    public Collection<JsFunction> jsFunctions() {
        List<JsFunction> list = new ArrayList<>();
        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "aesEncrypt";
            }

            @Override
            public String description() {
                return """
                            Encrypt string
                            函数原型：
                            AES.encrypt(String data, String key);
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                if (args.length == 1) {
                    String str1 = String.valueOf(args[0]);
                    String pwd = dictionaryCache.getOrCreate(StringUtils.joinWith("#", Constant.DICTIONARY_PLATFORM_SETTING, Constant.DIC_SETTING_AES_PWD), "AES密钥", Constant.DEFAULT_AES_PWD);
                    return SecureUtil.aes(DigestUtil.md5(pwd)).encryptHex(str1);
                } else if (args.length == 2) {
                    String str1 = String.valueOf(args[0]);
                    String pwd = String.valueOf(args[1]);
                    return SecureUtil.aes(DigestUtil.md5(pwd)).encryptHex(str1);
                }
                return null;
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "aesDecrypt";
            }

            @Override
            public String description() {
                return """
                        Decrypt string
                        函数原型:
                            AES.decrypt(String data, String key);
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                if (args.length == 1) {
                    String str1 = String.valueOf(args[0]);
                    String pwd = dictionaryCache.getOrCreate(StringUtils.joinWith("#", Constant.DICTIONARY_PLATFORM_SETTING, Constant.DIC_SETTING_AES_PWD), "AES密钥", Constant.DEFAULT_AES_PWD);
                    return new String(SecureUtil.aes(DigestUtil.md5(pwd)).decrypt(HexUtil.decodeHex(str1)));
                } else if (args.length == 2) {
                    String str1 = String.valueOf(args[0]);
                    String pwd = String.valueOf(args[1]);
                    return new String(SecureUtil.aes(DigestUtil.md5(pwd)).decrypt(HexUtil.decodeHex(str1)));
                }
                return null;
            }
        });
        return list;
    }
}
