package com.training.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 第04期：集合、Stream、并发练习
 */
@DisplayName("集合与并发")
class CollectionsConcurrentTest {

    // ===== HashMap =====

    @Test
    @DisplayName("HashMap 允许 null key 和 null value")
    void hashMapNullSupport() {
        Map<String, Integer> map = new HashMap<>();
        map.put(null, 1);
        map.put("a", null);
        map.put(null, 2);  // 覆盖

        assertEquals(2, map.size());
        assertEquals(2, map.get(null));
        assertNull(map.get("a"));
        assertTrue(map.containsKey(null));
    }

    @Test
    @DisplayName("ConcurrentHashMap 不允许 null")
    void concurrentHashMapNoNull() {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        assertThrows(NullPointerException.class, () -> map.put(null, 1));
        assertThrows(NullPointerException.class, () -> map.put("a", null));
    }

    @Test
    @DisplayName("equals 和 hashCode 必须一起重写")
    void equalsHashCodeContract() {
        // 两个 equals 的对象必须有相同 hashCode
        var p1 = new Person("Alice", 25);
        var p2 = new Person("Alice", 25);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());

        // HashMap 能正确找到
        Map<Person, String> map = new HashMap<>();
        map.put(p1, "value");
        assertEquals("value", map.get(p2));  // 用 p2 能找到 p1 的值
    }

    static class Person {
        String name;
        int age;
        Person(String name, int age) { this.name = name; this.age = age; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person)) return false;
            Person p = (Person) o;
            return age == p.age && Objects.equals(name, p.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }

    // ===== 遍历删除 =====

    @Test
    @DisplayName("增强 for 中删除抛 ConcurrentModificationException")
    void forEachRemoveThrows() {
        List<String> list = new ArrayList<>(List.of("a", "b", "c"));
        assertThrows(ConcurrentModificationException.class, () -> {
            for (String s : list) {
                if (s.equals("b")) list.remove(s);
            }
        });
    }

    @Test
    @DisplayName("Iterator 删除安全")
    void iteratorRemoveSafe() {
        List<String> list = new ArrayList<>(List.of("a", "b", "c"));
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().equals("b")) it.remove();
        }
        assertEquals(List.of("a", "c"), list);
    }

    @Test
    @DisplayName("removeIf 删除（推荐）")
    void removeIfSafe() {
        List<String> list = new ArrayList<>(List.of("a", "b", "c"));
        list.removeIf(s -> s.equals("b"));
        assertEquals(List.of("a", "c"), list);
    }

    // ===== Stream =====

    @Test
    @DisplayName("Stream 分组统计")
    void streamGrouping() {
        var orders = List.of(
            new Order(1L, "PAID", 100),
            new Order(2L, "PAID", 200),
            new Order(3L, "PENDING", 50),
            new Order(4L, "CANCELLED", 300)
        );

        Map<String, Integer> countByStatus = orders.stream()
            .collect(Collectors.groupingBy(
                Order::status,
                Collectors.summingInt(Order::amount)
            ));

        assertEquals(300, countByStatus.get("PAID"));
        assertEquals(50, countByStatus.get("PENDING"));
    }

    @Test
    @DisplayName("Stream flatMap 展开")
    void streamFlatMap() {
        var orders = List.of(
            new Order(1L, "PAID", 100),
            new Order(2L, "PAID", 200)
        );
        // 模拟每个订单有多个商品
        Map<Long, List<String>> items = Map.of(
            1L, List.of("MacBook", "鼠标"),
            2L, List.of("键盘", "显示器", "鼠标")
        );

        var allProducts = orders.stream()
            .flatMap(o -> items.get(o.id()).stream())
            .distinct()
            .sorted()
            .collect(Collectors.toList());

        assertEquals(List.of("MacBook", "显示器", "键盘", "鼠标"), allProducts);
    }

    static class Order {
        private final Long id;
        private final String status;
        private final int amount;

        Order(Long id, String status, int amount) {
            this.id = id;
            this.status = status;
            this.amount = amount;
        }

        Long id() { return id; }
        String status() { return status; }
        int amount() { return amount; }
    }

    // ===== volatile 不保证原子性 =====

    @Test
    @DisplayName("volatile 不保证原子性：多线程 count++ 不安全")
    void volatileNotAtomic() throws InterruptedException {
        // 用 AtomicInteger 对比
        AtomicInteger safeCounter = new AtomicInteger(0);
        int[] unsafeCounter = {0};  // 模拟非原子操作

        int threads = 10;
        int perThread = 10000;
        var latch = new CountDownLatch(threads);
        var executor = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads; i++) {
            executor.execute(() -> {
                for (int j = 0; j < perThread; j++) {
                    safeCounter.incrementAndGet();  // 原子操作
                    unsafeCounter[0]++;             // 非原子：读-改-写
                }
                latch.countDown();
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        assertEquals(threads * perThread, safeCounter.get());  // 一定正确
        // unsafeCounter[0] 大概率小于 100000（但不一定每次都复现）
        System.out.println("AtomicInteger: " + safeCounter.get());
        System.out.println("普通 int[]: " + unsafeCounter[0]);
    }

    // ===== 线程池 =====

    @Test
    @DisplayName("线程池基本使用与关闭")
    void threadPoolBasics() throws InterruptedException {
        var executor = new ThreadPoolExecutor(
            2, 4, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10),
            r -> new Thread(r, "test-pool-" + System.nanoTime()),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        var results = new ConcurrentLinkedQueue<String>();
        var latch = new CountDownLatch(5);

        for (int i = 1; i <= 5; i++) {
            final int id = i;
            executor.execute(() -> {
                results.add("任务" + id + "@" + Thread.currentThread().getName());
                latch.countDown();
            });
        }

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertEquals(5, results.size());

        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
    }

    // ===== CountDownLatch =====

    @Test
    @DisplayName("CountDownLatch 等待多个任务完成")
    void countDownLatchDemo() throws InterruptedException {
        int taskCount = 5;
        var latch = new CountDownLatch(taskCount);
        var executor = Executors.newFixedThreadPool(3);
        var results = new ConcurrentLinkedQueue<Integer>();

        for (int i = 1; i <= taskCount; i++) {
            final int id = i;
            executor.execute(() -> {
                try { Thread.sleep(id * 50L); } catch (InterruptedException ignored) {}
                results.add(id);
                latch.countDown();
            });
        }

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertEquals(taskCount, results.size());
        executor.shutdown();
    }

    // ===== CompletableFuture =====

    @Test
    @DisplayName("CompletableFuture 并发编排")
    void completableFutureDemo() throws Exception {
        var executor = Executors.newFixedThreadPool(3);

        CompletableFuture<String> userF = CompletableFuture.supplyAsync(() -> {
            sleep(100);
            return "Alice";
        }, executor);

        CompletableFuture<String> orderF = CompletableFuture.supplyAsync(() -> {
            sleep(200);
            return "3笔订单";
        }, executor);

        CompletableFuture<String> accountF = CompletableFuture.supplyAsync(() -> {
            sleep(50);
            return "¥9999";
        }, executor);

        // 全部完成后汇总
        CompletableFuture.allOf(userF, orderF, accountF).join();

        String summary = String.format("%s | %s | %s",
            userF.get(), orderF.get(), accountF.get());

        assertEquals("Alice | 3笔订单 | ¥9999", summary);
        executor.shutdown();
    }

    @Test
    @DisplayName("CompletableFuture 异常处理")
    void completableFutureException() throws Exception {
        CompletableFuture<String> future = CompletableFuture
            .<String>supplyAsync(() -> {
                throw new RuntimeException("模拟失败");
            })
            .exceptionally(ex -> "降级结果");

        assertEquals("降级结果", future.get());
    }

    // ===== Semaphore =====

    @Test
    @DisplayName("Semaphore 控制并发数")
    void semaphoreDemo() throws InterruptedException {
        var semaphore = new Semaphore(3);
        var executor = Executors.newFixedThreadPool(10);
        var latch = new CountDownLatch(8);
        var maxConcurrent = new AtomicInteger(0);
        var current = new AtomicInteger(0);

        for (int i = 0; i < 8; i++) {
            executor.execute(() -> {
                try {
                    semaphore.acquire();
                    int c = current.incrementAndGet();
                    maxConcurrent.accumulateAndGet(c, Math::max);
                    Thread.sleep(100);
                    current.decrementAndGet();
                } catch (InterruptedException ignored) {
                } finally {
                    semaphore.release();
                    latch.countDown();
                }
            });
        }

        assertTrue(latch.await(10, TimeUnit.SECONDS));
        assertTrue(maxConcurrent.get() <= 3);  // 最多 3 个并发
        executor.shutdown();
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
