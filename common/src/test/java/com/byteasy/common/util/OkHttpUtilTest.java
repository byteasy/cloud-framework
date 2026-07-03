package com.byteasy.common.util;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@Log4j2
public class OkHttpUtilTest {

    @Test
    public void test1() throws IOException {
        String response = OkHttpUtil.get("https://www.baidu.com");
        log.info(response);
        Assertions.assertTrue(response.contains("<!DOCTYPE html>"));
    }
}