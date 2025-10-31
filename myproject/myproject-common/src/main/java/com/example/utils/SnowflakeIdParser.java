package com.example.utils;

public class SnowflakeIdParser {
    private final long nodeIdBits = 10;
    private final long sequenceBits = 12;

    private final long nodeIdShift = sequenceBits;
    private final long timestampShift = nodeIdBits + sequenceBits;

    public void parse(long id) {
        long timestamp = (id >> timestampShift);
        long nodeId = (id >> sequenceBits) & ~(-1L << nodeIdBits);
        long sequence = id & ~(-1L << sequenceBits);

        System.out.printf("ID: %d%n", id);
        System.out.printf("时间戳: %d (%s)%n", timestamp, new java.util.Date(timestamp));
        System.out.printf("节点ID: %d%n", nodeId);
        System.out.printf("序列号: %d%n", sequence);
    }

    public static void main(String[] args) {
        SnowflakeIdParser parser = new SnowflakeIdParser();
        parser.parse(7346011712413241351L);
    }
}
