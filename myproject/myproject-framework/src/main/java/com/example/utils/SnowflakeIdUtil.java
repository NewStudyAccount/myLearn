package com.example.utils;

/**
 * 雪花算法（Snowflake）唯一ID生成工具类
 * <p>
 * 生成64位长整型ID，结构：1位符号位 + 41位时间戳 + 5位数据中心ID + 5位机器ID + 12位序列号
 * <p>
 * datacenterId 模块映射：
 * <pre>
 *   默认(framework)  0
 *   system模块       1
 *   oss模块          2
 *   blog模块         3
 * </pre>
 * <p>
 * 使用方式：
 * <pre>
 *   // 使用默认实例
 *   long id = SnowflakeIdUtil.nextId();
 *
 *   // 使用模块专用方法
 *   long systemId = SnowflakeIdUtil.systemNextId();
 *   long ossId = SnowflakeIdUtil.ossNextId();
 *   long blogId = SnowflakeIdUtil.blogNextId();
 *
 *   // 使用自定义实例
 *   SnowflakeIdUtil generator = new SnowflakeIdUtil(1, 1);
 *   long id = generator.generateId();
 * </pre>
 * <p>
 * 参数约束：datacenterId 和 workerId 取值范围为 0-31
 */
public class SnowflakeIdUtil {

    /** 默认 datacenterId */
    public static final long DATACENTER_ID_DEFAULT = 0L;
    /** system 模块 datacenterId */
    public static final long DATACENTER_ID_SYSTEM = 1L;
    /** oss 模块 datacenterId */
    public static final long DATACENTER_ID_OSS = 2L;
    /** blog 模块 datacenterId */
    public static final long DATACENTER_ID_BLOG = 3L;

    /** 起始时间戳 (2024-01-01 00:00:00 UTC) */
    private static final long EPOCH = 1704067200000L;

    /** 各部分占用的位数 */
    private static final long DATACENTER_ID_BITS = 5L;
    private static final long WORKER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;

    /** 各部分的最大值 */
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    /** 各部分左移位数 */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

    /** 数据中心ID (0-31) */
    private final long datacenterId;

    /** 机器ID (0-31) */
    private final long workerId;

    /** 序列号 */
    private long sequence = 0L;

    /** 上次生成ID的时间戳 */
    private long lastTimestamp = -1L;

    /** 默认实例 */
    private static final SnowflakeIdUtil DEFAULT_INSTANCE = new SnowflakeIdUtil(DATACENTER_ID_DEFAULT, 0);
    /** system 模块实例 */
    private static final SnowflakeIdUtil SYSTEM_INSTANCE = new SnowflakeIdUtil(DATACENTER_ID_SYSTEM, 0);
    /** oss 模块实例 */
    private static final SnowflakeIdUtil OSS_INSTANCE = new SnowflakeIdUtil(DATACENTER_ID_OSS, 0);
    /** blog 模块实例 */
    private static final SnowflakeIdUtil BLOG_INSTANCE = new SnowflakeIdUtil(DATACENTER_ID_BLOG, 0);

    /**
     * 构造方法
     *
     * @param datacenterId 数据中心ID，取值范围 0-31
     * @param workerId     机器ID，取值范围 0-31
     * @throws IllegalArgumentException 如果 datacenterId 或 workerId 超出范围
     */
    public SnowflakeIdUtil(long datacenterId, long workerId) {
        if (datacenterId < 0 || datacenterId > MAX_DATACENTER_ID) {
            throw new IllegalArgumentException(
                    String.format("datacenterId 取值范围 0-%d，当前值: %d", MAX_DATACENTER_ID, datacenterId));
        }
        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException(
                    String.format("workerId 取值范围 0-%d，当前值: %d", MAX_WORKER_ID, workerId));
        }
        this.datacenterId = datacenterId;
        this.workerId = workerId;
    }

    /**
     * 使用默认实例生成下一个唯一ID
     *
     * @return 唯一的64位长整型ID
     */
    public static long nextId() {
        return DEFAULT_INSTANCE.generateId();
    }

    /**
     * 使用 system 模块实例生成唯一ID（datacenterId=1）
     *
     * @return 唯一的64位长整型ID
     */
    public static long systemNextId() {
        return SYSTEM_INSTANCE.generateId();
    }

    /**
     * 使用 oss 模块实例生成唯一ID（datacenterId=2）
     *
     * @return 唯一的64位长整型ID
     */
    public static long ossNextId() {
        return OSS_INSTANCE.generateId();
    }

    /**
     * 使用 blog 模块实例生成唯一ID（datacenterId=3）
     *
     * @return 唯一的64位长整型ID
     */
    public static long blogNextId() {
        return BLOG_INSTANCE.generateId();
    }

    /**
     * 生成下一个唯一ID
     *
     * @return 唯一的64位长整型ID
     * @throws RuntimeException 如果检测到时钟回拨
     */
    public synchronized long generateId() {
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("时钟回拨，拒绝生成ID。当前时间: %d，上次时间: %d",
                            currentTimestamp, lastTimestamp));
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - EPOCH) << TIMESTAMP_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 等待到下一毫秒
     */
    private long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
