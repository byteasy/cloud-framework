package com.byteasy.script.component;

import cn.hutool.crypto.digest.DigestUtil;
import com.byteasy.component.ICacheLoader;
import com.byteasy.platform.Constant;
import com.byteasy.platform.api.JsScriptClient;
import com.byteasy.platform.entity.JsScript;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.log4j.Log4j2;
import org.graalvm.polyglot.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class JsScriptFactory implements ICacheLoader<Long, JsScript> {

    @Autowired
    private JsScriptClient jsScriptClient;

    private static Cache<String, Source> SCRIPT_CACHE = CacheBuilder.newBuilder().maximumSize(10000).expireAfterAccess(2, TimeUnit.HOURS).build();

    @Override
    @Cacheable(cacheNames = Constant.PLATFORM_REP_CACHE, key = "'JsScript:' + #key")
    public JsScript load(Long key) {
        return jsScriptClient.getById(key).getIfPresent();
    }

    @Override
    public String type() {
        return JsScript.class.getName();
    }

    public static Source getScriptSource(String jsName, String jsScript) throws IOException {
        String id = DigestUtil.md5Hex(jsScript);
        Source cachedScript = SCRIPT_CACHE.getIfPresent(id);
        if (cachedScript == null) {
            cachedScript = Source.newBuilder("js", jsScript, jsName + ".js")
                    .cached(true)
                    .build();
            setScriptSource(id, cachedScript);
        }
        return cachedScript;
    }

    public static void setScriptSource(String id, Source source) {
        SCRIPT_CACHE.put(id, source);
    }

    public static void invalidate(String id) {
        SCRIPT_CACHE.invalidate(id);
    }
}
