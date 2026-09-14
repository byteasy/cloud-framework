package com.byteasy.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class RequestMappingResolverTest {

    @Test
    @DisplayName("1. 基础标准场景：中间与末尾带有路径变量")
    void testStandardPathWithVariables() {
        String path = "/api/test/{id}/123/{code}";
        String[] expected = {"/api/test/", "/123/"};
        assertArrayEquals(expected, RequestMappingResolver.splitByPathVariable(path));
    }

    @Test
    @DisplayName("2. 末尾带有纯静态路径")
    void testPathWithStaticTail() {
        String path = "/api/test/{id}/123/{code}/detail";
        String[] expected = {"/api/test/", "/123/", "/detail"};
        assertArrayEquals(expected, RequestMappingResolver.splitByPathVariable(path));
    }

    @Test
    @DisplayName("3. 开头就是路径变量")
    void testPathStartingWithVariable() {
        String path = "{tenantId}/api/users/{id}";
        String[] expected = {"/api/users/"};
        assertArrayEquals(expected, RequestMappingResolver.splitByPathVariable(path));
    }

    @Test
    @DisplayName("4. 连续的两个路径变量")
    void testContinuousVariables() {
        String path = "/api/{version}/{module}/get";
        String[] expected = {"/api/", "/", "/get"};
        assertArrayEquals(expected, RequestMappingResolver.splitByPathVariable(path));
    }

    @Test
    @DisplayName("5. 不含任何变量的普通纯静态路径")
    void testPureStaticPath() {
        String path = "/api/v1/system/user/list";
        String[] expected = {"/api/v1/system/user/list"};
        assertArrayEquals(expected, RequestMappingResolver.splitByPathVariable(path));
    }

    @Test
    @DisplayName("6. 只有单个路径变量")
    void testOnlyOneVariable() {
        String path = "{id}";
        String[] expected = {};
        assertArrayEquals(expected, RequestMappingResolver.splitByPathVariable(path));
    }

    @Test
    @DisplayName("7. 根路径或单个斜杠包裹的变量")
    void testRootVariable() {
        String path = "/{id}";
        String[] expected = {"/"};
        assertArrayEquals(expected, RequestMappingResolver.splitByPathVariable(path));

        String path2 = "/{id}/";
        String[] expected2 = {"/", "/"};
        assertArrayEquals(expected2, RequestMappingResolver.splitByPathVariable(path2));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("8. 边界条件防御：null、空字符串、全空格")
    void testNullAndEmpty(String input) {
        String[] expected = {};
        assertArrayEquals(expected, RequestMappingResolver.splitByPathVariable(input));
    }

    @Test
    @DisplayName("9. 容错测试：未闭合或畸形的括号")
    void testMalformedBraces() {
        // 只有开括号没有闭括号
        String path = "/api/test/{id/user";
        String[] expected = {"/api/test/{id/user"};
        assertArrayEquals(expected, RequestMappingResolver.splitByPathVariable(path));
    }
}