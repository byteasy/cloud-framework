package com.byteasy.mq.restfull.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.common.util.JacksonUtil2;
import com.byteasy.mq.Message;
import com.byteasy.mq.restfull.ConsumerFactory;
import com.byteasy.mq.restfull.dto.PubCmd;
import com.byteasy.mq.restfull.dto.PublishResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.SAPI + "/consumers")
@ConditionalOnProperty(prefix = "byteasy.mq.consumer", name = "enabled", havingValue = "true", matchIfMissing = true)
@Log4j2
public class ConsumerController {

    @Autowired
    private ConsumerFactory consumerFactory;

    @PostMapping("/{topic}")
    @Openapi(title = "接收事件", uri = "/{svrCode}" + Constant.SAPI + "/consumers/{topic}")
    public Result<PublishResult> consumer(@PathVariable("topic") String topic, @RequestBody Message message) {
        if (log.isInfoEnabled()) {
            log.info("Restfull consumer topic: {}, message: {}", topic, JacksonUtil2.toJson(message));
        }
        PubCmd pubCmd = consumerFactory.receive(topic, message.getKey(), message.getMessage());
        PublishResult result = new PublishResult();
        result.setCmd(pubCmd);
        result.setConsumers(consumerFactory.getConsumers());
        return Result.success(result);
    }
}
