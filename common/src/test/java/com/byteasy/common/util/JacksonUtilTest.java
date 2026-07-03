package com.byteasy.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.common.token.UserInfoTest;
import com.byteasy.common.util.JacksonUtilTest.MessageEvent.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class JacksonUtilTest {

    @Test
    public void test1() {
        MessageEvent event = new MessageEvent();
        event.setIp("123");
        event.setMeasureNo("456");
        event.setType(Type.ALARM);
        event.setContent("ct");
        event.setBol1(true);
        event.setBol2(false);
        event.setBy1((byte) (RandomUtil.randomInt() & 0xFF));
        event.setBy2((byte) (RandomUtil.randomInt() & 0xFF));
        event.setSht1((short) (RandomUtil.randomInt() & 0xFFFF));
        event.setSht2((short) (RandomUtil.randomInt() & 0xFFFF));
        event.setInteger1(RandomUtil.randomInt());
        event.setInteger2(RandomUtil.randomInt());
        event.setDataTime1(RandomUtil.randomLong());
        event.setDataTime2(RandomUtil.randomLong());
        event.setUserInfo(UserInfoTest.create());
        event.getList().add(UserInfoTest.create());
        event.getMap().put("test", UserInfoTest.create());

        String json = JacksonUtil.toJson(event);
        System.out.println(json);
        MessageEvent newEvent = JacksonUtil.parse(json, MessageEvent.class);
        Assertions.assertTrue(BeanUtil.isCommonFieldsEqual(newEvent, event));
        newEvent.getList().get(0).setUsername("123");
        Assertions.assertFalse(BeanUtil.isCommonFieldsEqual(newEvent, event));

        String classJson = JacksonUtil.toJson(event, true);
        System.out.println(classJson);

        Object obj = JacksonUtil.parseObj(classJson);
        Assertions.assertInstanceOf(MessageEvent.class, obj);
        Assertions.assertTrue(BeanUtil.isCommonFieldsEqual(event, obj));
        ((MessageEvent) obj).getList().get(0).setUsername("123");
        Assertions.assertFalse(BeanUtil.isCommonFieldsEqual(event, obj));
    }

    @Test
    public void test2() {
        UserInfo info = UserInfoTest.create();
        String json = JacksonUtil.toJson(new Object[]{info}, true);
        System.out.println(json);

        List<Object> newValue = JacksonUtil.parseObjList(json);
        Assertions.assertEquals(1, newValue.size());
        Assertions.assertEquals(UserInfo.class.getName(), newValue.get(0).getClass().getName());
        Assertions.assertTrue(BeanUtil.isCommonFieldsEqual(info, newValue.get(0)));

        Object[] arrs = JacksonUtil.parseObjArray(json);
        Assertions.assertEquals(1, arrs.length);
        Assertions.assertEquals(UserInfo.class.getName(), arrs[0].getClass().getName());
        Assertions.assertTrue(BeanUtil.isCommonFieldsEqual(info, arrs[0]));

    }

    @Test
    public void test3() {
        LocalDateTimeTestEntity timeTestEntity = new LocalDateTimeTestEntity();
        timeTestEntity.setDefaultTime(LocalDateTime.now());
        timeTestEntity.setSpecTime(LocalDateTime.now());
        System.out.println(JacksonUtil.toJson(timeTestEntity));
    }

    @Data
    public static class LocalDateTimeTestEntity {
        private LocalDateTime defaultTime;
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
        private LocalDateTime specTime;
    }

    @Data
    public static class MessageEvent {

        private String measureNo;
        private String ip;
        private Type type;
        private Object content;
        private Boolean bol1;
        private boolean bol2;
        private Byte by1;
        private byte by2;
        private Short sht1;
        private short sht2;
        private Integer integer1;
        private int integer2;
        private Long dataTime1;
        private long dataTime2;
        private UserInfo userInfo;
        private List<UserInfo> list = Lists.newArrayList();
        private Map<String, UserInfo> map = Maps.newHashMap();

        public static enum Type {
            REMOVE_SESSION,// websocket
            STAFF_TO_SEAT, SEAT_TO_STAFF, // 员工和坐席转换
            WEBRTC, // webRTC连接
            SHOW, // 展示
            TASK, // 任务
            HELP, // 帮助
            ALARM, // 告警
            NORMAL;// 正常
        }
    }
}