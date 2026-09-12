package com.byteasy.platform.service.impl;

import com.byteasy.platform.service.impl.PlatformUserDetails;

import java.util.HashMap;

public class PlatformUserDetailsTest {
    public static void main(String[] args) {
        // 创建两个不同实例，但ID相同
        PlatformUserDetails user1 = new PlatformUserDetails();
        user1.setId(1L);
        user1.setUsername("test");

        PlatformUserDetails user2 = new PlatformUserDetails();
        user2.setId(2L);
        user2.setUsername("test"); // 用户名不同，但ID相同

        HashMap<PlatformUserDetails, String> map = new HashMap<>();
        map.put(user1, "value1");

        // 能获取到值，说明是相同Key
        System.out.println(map.get(user2)); // 输出：value1
    }
}