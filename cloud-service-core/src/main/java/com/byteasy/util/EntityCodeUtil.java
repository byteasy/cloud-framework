package com.byteasy.util;

import java.security.SecureRandom;

/**
 * 企业级实体业务短编码生成器
 * 特性：短、带唯一保证、支持业务前缀、URL/数据库安全字符集
 */
public class EntityCodeUtil {

    // 去除了容易混淆的字符（去除了 0, O, o, 1, I, l 等），共 56 个安全字符，方便人工核对和系统展示
    private static final char[] SAFE_ALPHABET =
            "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz".toCharArray();
    private static final int BASE = SAFE_ALPHABET.length;

    // 自定义基准纪元时间（2025-01-01 00:00:00 UTC），节省时间戳位数
    private static final long CUSTOM_EPOCH = 1735689600000L;

    // 机器标识 (0~31)
    private static final long WORKER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 7L;

    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    private static long workerId = 1L; // 默认节点 1，分布式环境下可通过配置项或环境变量注入
    private static long sequence = 0L;
    private static long lastTimestamp = -1L;

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 设置当前机器节点 ID（分布式部署时使用）
     */
    public static void setWorkerId(long id) {
        if (id < 0 || id > MAX_WORKER_ID) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        workerId = id;
    }

    /**
     * 【核心推荐】生成 10~11 位短实体编码（基于时间戳 + 序列号 + Base56 压缩）
     * 保证全局 100% 唯一，单机/分布式无冲突，且整体单调递增（索引友好）
     *
     * @return 示例: "7bK9mX2pL8Q" (仅约 10~11 字符)
     */
    public static synchronized String generateCode() {
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            // 时钟回拨保护，等待或者微调
            currentTimestamp = lastTimestamp;
        }

        if (lastTimestamp == currentTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                // 当前毫秒序列号用尽，等待下一毫秒
                while ((currentTimestamp = System.currentTimeMillis()) <= lastTimestamp) {
                    // spin wait
                }
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        long id = ((currentTimestamp - CUSTOM_EPOCH) << TIMESTAMP_LEFT_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;

        return encodeToBase56(id);
    }

    /**
     * 生成带业务前缀的实体编码（推荐业务实体使用）
     *
     * @param prefix 业务前缀（例如 "MAT" 代表物料, "DEV" 代表设备, "ORD" 代表订单）
     * @return 示例: "MAT_7bK9mX2pL8Q"
     */
    public static String generateCode(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            return generateCode();
        }
        return prefix.trim().toUpperCase() + "_" + generateCode();
    }

    /**
     * 【方案 B】生成固定长度的安全高熵随机短码（如 8~10 位）
     * 基于加密级安全随机数，碰撞概率极低（适用于无序验证码、防枚举对外代号）
     *
     * @param length 指定长度（推荐 8 ~ 12 位）
     * @return 示例: "8kX2mP9A"
     */
    public static String generateRandomCode(int length) {
        if (length <= 0) {
            length = 8;
        }
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = SAFE_ALPHABET[RANDOM.nextInt(BASE)];
        }
        return new String(chars);
    }

    /**
     * 将 64 位 long 整数压缩为 Base56 短字符串
     */
    private static String encodeToBase56(long num) {
        if (num <= 0) {
            return String.valueOf(SAFE_ALPHABET[0]);
        }
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(SAFE_ALPHABET[(int) (num % BASE)]);
            num /= BASE;
        }
        return sb.reverse().toString();
    }
}
