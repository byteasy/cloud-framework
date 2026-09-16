package com.byteasy.common.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.event.Event;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.SAPI + "/events")
@Log4j2
public class EventReceiverController {

    @PostMapping("")
    @Openapi(title = "接收事件")
    public Result receivePublishEvent(@RequestBody String eventJson) {
        if (log.isInfoEnabled()) {
            log.info("receivePublishEvent : {}", eventJson);
        }
        Event event = (Event) JacksonUtil.parseObj(eventJson);
        SpringUtil.publishEvent(event);
        return Result.success();
    }
}
