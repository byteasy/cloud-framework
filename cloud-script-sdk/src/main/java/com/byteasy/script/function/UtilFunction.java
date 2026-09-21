package com.byteasy.script.function;

import com.byteasy.common.component.EventPublishUtil;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.EntityChangeSource;
import com.byteasy.event.EventType;
import com.byteasy.exception.BizException;
import com.byteasy.script.JsFunction;
import com.byteasy.script.JsFunctions;
import com.byteasy.util.EntityCodeUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Log4j2
public class UtilFunction implements JsFunctions {
    @Autowired
    private EventPublishUtil eventPublishUtil;

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
                return "genCode";
            }

            @Override
            public String description() {
                return """
                        Use EntityCodeUtil to generate a union entity code.
                        函数原型:
                                 public static String generateCode(String prefix);
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                Object arg = args == null ? null : args[0];
                return EntityCodeUtil.generateCode((String) arg);
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "pubEvent";
            }

            @Override
            public String description() {
                return """
                        Use EntityCodeUtil to generate a union entity code.
                        函数原型:
                                 public static String publishEvent(String className, ChangeType changeType, EventType eventType, Object oldEntity, Object newEntity);
                                 ChangeType: CREATE, UPDATE, DELETE, BATCH_CREATE, BATCH_UPDATE, BATCH_DELETE
                                 EventType:
                                     SINGLETON: 单机模式
                                     ALL: 集群运行中所有服务实例
                                     ONE_INSTANCE_PER_SERVICE_GROUP: 每个微服务的单个实例
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                if (args == null || args.length != 5) {
                    throw new BizException("参数输入不合法");
                }
                // 判断是js脚本调用
                Object before = null;
                if (args[3] != null && args[3].getClass().getName().contains("PolyglotMap")) {
                    before = new HashMap<>((Map) args[3]);
                }
                Object after = null;
                if (args[4] != null && args[4].getClass().getName().contains("PolyglotMap")) {
                    after = new HashMap<>((Map) args[4]);
                }
                eventPublishUtil.publishEvent(new EntityChangeEvent(new EntityChangeSource(EntityChangeSource.ChangeType.valueOf((String) args[1]),
                        before,
                        after,
                        (String) args[0]),
                        EventType.valueOf((String) args[2])));
                return null;
            }
        });
        return list;
    }
}
