package com.example.utils;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class SnowflakeIdUtilTest {

    @Test
    void testUniqueId() {
        long id1 = SnowflakeIdUtil.nextId();
        long id2 = SnowflakeIdUtil.nextId();
        assertTrue(id1 > 0);
        assertTrue(id2 > 0);
        assertNotEquals(id1, id2);
    }

    @Test
    void testIncreasingIds() {
        long prev = SnowflakeIdUtil.nextId();
        for (int i = 0; i < 1000; i++) {
            long current = SnowflakeIdUtil.nextId();
            assertTrue(current > prev, "生成的ID应趋势递增");
            prev = current;
        }
    }

    @Test
    void testConcurrentUniqueness() throws InterruptedException {
        int threadCount = 16;
        int idsPerThread = 10000;
        Set<Long> allIds = Collections.newSetFromMap(new ConcurrentHashMap<>());
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < idsPerThread; j++) {
                        allIds.add(SnowflakeIdUtil.nextId());
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        assertEquals(threadCount * idsPerThread, allIds.size(), "多线程下不应生成重复ID");
    }

    @Test
    void testDatacenterIdOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> new SnowflakeIdUtil(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> new SnowflakeIdUtil(32, 0));
    }

    @Test
    void testWorkerIdOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> new SnowflakeIdUtil(0, -1));
        assertThrows(IllegalArgumentException.class, () -> new SnowflakeIdUtil(0, 32));
    }

    @Test
    void testCustomDatacenterAndWorkerId() {
        SnowflakeIdUtil generator = new SnowflakeIdUtil(1, 1);
        long id = generator.generateId();
        assertTrue(id > 0);
    }

    @Test
    void testClockDriftDetection() {
        SnowflakeIdUtil generator = new SnowflakeIdUtil(0, 0);
        // 先生成一个ID，设置lastTimestamp
        generator.generateId();
        // 通过反射修改lastTimestamp为未来时间，模拟时钟回拨
        try {
            java.lang.reflect.Field lastTimestampField = SnowflakeIdUtil.class.getDeclaredField("lastTimestamp");
            lastTimestampField.setAccessible(true);
            lastTimestampField.setLong(generator, System.currentTimeMillis() + 10000);

            RuntimeException exception = assertThrows(RuntimeException.class, generator::generateId);
            assertTrue(exception.getMessage().contains("时钟回拨"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("反射访问lastTimestamp失败: " + e.getMessage());
        }
    }

    @Test
    void testModuleDatacenterIdInGeneratedId() {
        long systemId = SnowflakeIdUtil.systemNextId();
        long ossId = SnowflakeIdUtil.ossNextId();
        long blogId = SnowflakeIdUtil.blogNextId();

        // 从ID中提取 datacenterId：右移22位（sequence 12位 + workerId 5位 + datacenterId 5位中的低5位）
        long sequenceBits = 12L;
        long workerIdBits = 5L;
        long datacenterIdShift = sequenceBits + workerIdBits;
        long datacenterIdMask = 0x1FL; // 5位掩码

        assertEquals(SnowflakeIdUtil.DATACENTER_ID_SYSTEM, (systemId >> datacenterIdShift) & datacenterIdMask);
        assertEquals(SnowflakeIdUtil.DATACENTER_ID_OSS, (ossId >> datacenterIdShift) & datacenterIdMask);
        assertEquals(SnowflakeIdUtil.DATACENTER_ID_BLOG, (blogId >> datacenterIdShift) & datacenterIdMask);
    }

    @Test
    void testModuleConstants() {
        assertEquals(0L, SnowflakeIdUtil.DATACENTER_ID_DEFAULT);
        assertEquals(1L, SnowflakeIdUtil.DATACENTER_ID_SYSTEM);
        assertEquals(2L, SnowflakeIdUtil.DATACENTER_ID_OSS);
        assertEquals(3L, SnowflakeIdUtil.DATACENTER_ID_BLOG);
    }

    @Test
    void testNextIdStillWorks() {
        long id1 = SnowflakeIdUtil.nextId();
        long id2 = SnowflakeIdUtil.nextId();
        assertTrue(id1 > 0);
        assertTrue(id2 > id1, "nextId() 仍应趋势递增");
    }
}
