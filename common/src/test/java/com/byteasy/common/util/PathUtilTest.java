package com.byteasy.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PathUtilTest {

    @Test
    public void test1() {
        Assertions.assertTrue(PathUtil.isMatch("/test/{id}", "/test/123"));
        Assertions.assertTrue(PathUtil.isMatch("/test/{id}", "/test/123?name=80"));
        Assertions.assertTrue(PathUtil.isMatch("/test/{id}", "/test/name"));
        Assertions.assertTrue(PathUtil.isMatch("/test/123", "/test/123"));
        Assertions.assertTrue(PathUtil.isMatch("/test/{id}/{name}", "/test/123/456"));
        Assertions.assertFalse(PathUtil.isMatch("/test/{id}/{name}", "/test/123"));
    }
}