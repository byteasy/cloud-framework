package com.byteasy.util;

import com.byteasy.entity.LogicDeleteEntity;
import org.junit.jupiter.api.Test;

public class MainTest {


    @Test
    public void test() {
        System.out.println(LogicDeleteEntity.class.isAssignableFrom(TestEntity.class));
    }

    public static class TestEntity extends LogicDeleteEntity {

    }
}
