package com.example.utils;

/**
 * Snowflake ID 生成器
 * 用于在分布式系统中生成全局唯一、有序的64位ID。
 */
public class SnowflakeIdGenerator {
    // 定义各部分在64位ID中的位数分配
    private final long nodeIdBits = 10L;     // 节点ID占10位
    private final long sequenceBits = 12L;   // 序列号占12位

    // 最大序列号值（即 2^sequenceBits - 1）
    private final long maxSequence = ~(-1L << sequenceBits);

    // 当前节点ID（已移位存储）
    private long nodeId;
    // 上次生成ID的时间戳（毫秒级）
    private long lastTimestamp = -1L;
    // 同一毫秒内的序列号计数器
    private long sequence = 0L;

    /**
     * 构造函数，初始化节点ID
     *
     * @param nodeId 节点ID（范围应为 0 ~ 2^nodeIdBits - 1）
     */
    public SnowflakeIdGenerator(long nodeId) {
        this.nodeId = nodeId << sequenceBits; // 将节点ID左移，预留出序列号空间
    }

    /**
     * 生成下一个唯一ID（线程安全）
     *
     * @return 唯一的64位long型ID
     */
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        // 如果时间回拨，抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("时钟回拨");
        }

        // 同一毫秒内生成多个ID
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & maxSequence; // 序列号递增
            if (sequence == 0) {
                // 如果序列号达到最大值，则等待下一毫秒
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 新的一毫秒，重置序列号
            sequence = 0;
        }

        lastTimestamp = timestamp; // 更新上次生成时间

        // 拼接最终的64位ID：
        // | 时间戳(42位) | 节点ID(10位) | 序列号(12位) |
        return (timestamp << (nodeIdBits + sequenceBits))
                | nodeId
                | sequence;
    }

    /**
     * 自旋等待直到进入下一毫秒
     *
     * @param lastTimestamp 上次生成ID的时间戳
     * @return 当前时间戳（大于lastTimestamp）
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    /**
     * 测试方法：生成10个示例ID
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1);
        for (int i = 0; i < 10; i++) {
            System.out.println(idGenerator.nextId());
        }
    }
}
