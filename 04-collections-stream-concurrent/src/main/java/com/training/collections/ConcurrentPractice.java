package com.training.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并发编程实战练习
 */
public class ConcurrentPractice {

    public static void main(String[] args) throws Exception {
        threadPoolBasics();
        countDownLatchDemo();
        completableFutureDemo();
        semaphoreDemo();
    }

    /**
     * 练习1：线程池基础
     */
    static void threadPoolBasics() throws InterruptedException {
        System.out.println("=== 线程池基础 ===");

        // 手动创建线程池（阿里规范）
        var executor = new ThreadPoolExecutor(
            2,                          // 核心线程数
            4,                          // 最大线程数
            60L, TimeUnit.SECONDS,      // 空闲存活时间
            new LinkedBlockingQueue<>(10),  // 有界队列
            r -> new Thread(r, "biz-pool-" + System.nanoTime()),  // 线程命名
            new ThreadPoolExecutor.CallerRunsPolicy()  // 拒绝策略
        );

        // 提交任务
        for (int i = 1; i <= 6; i++) {
            final int taskId = i;
            executor.execute(() -> {
                System.out.printf("  任务%d 执行于 %s%n", taskId, Thread.currentThread().getName());
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("  所有任务完成\n");
    }

    /**
     * 练习2：CountDownLatch 等待多个任务
     */
    static void countDownLatchDemo() throws InterruptedException {
        System.out.println("=== CountDownLatch ===");

        int taskCount = 5;
        var latch = new CountDownLatch(taskCount);
        var executor = Executors.newFixedThreadPool(3);
        var results = new ConcurrentLinkedQueue<String>();

        long start = System.currentTimeMillis();

        for (int i = 1; i <= taskCount; i++) {
            final int id = i;
            executor.execute(() -> {
                try {
                    // 模拟不同耗时的任务
                    Thread.sleep(id * 200L);
                    results.add("任务" + id + "完成");
                } catch (InterruptedException ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();  // 等待全部完成
        long cost = System.currentTimeMillis() - start;

        System.out.println("  全部完成，耗时: " + cost + "ms");
        results.forEach(r -> System.out.println("  " + r));

        executor.shutdown();
        System.out.println();
    }

    /**
     * 练习3：CompletableFuture 异步编排
     */
    static void completableFutureDemo() throws Exception {
        System.out.println("=== CompletableFuture ===");

        var executor = Executors.newFixedThreadPool(3);

        // 模拟并发调用三个服务
        CompletableFuture<String> userFuture = CompletableFuture.supplyAsync(() -> {
            sleep(300);
            return "用户: Alice";
        }, executor);

        CompletableFuture<String> orderFuture = CompletableFuture.supplyAsync(() -> {
            sleep(500);
            return "订单: 3笔";
        }, executor);

        CompletableFuture<String> accountFuture = CompletableFuture.supplyAsync(() -> {
            sleep(200);
            return "余额: ¥9999";
        }, executor);

        // 全部完成后汇总
        CompletableFuture<Void> all = CompletableFuture.allOf(userFuture, orderFuture, accountFuture);

        CompletableFuture<String> summary = all.thenApply(v -> {
            try {
                return String.format("汇总 → %s | %s | %s",
                    userFuture.get(), orderFuture.get(), accountFuture.get());
            } catch (Exception e) {
                return "汇总失败";
            }
        });

        // 异常处理
        CompletableFuture<String> withFallback = summary.exceptionally(ex -> {
            System.out.println("  异常: " + ex.getMessage());
            return "降级结果";
        });

        System.out.println("  " + withFallback.get(5, TimeUnit.SECONDS));

        executor.shutdown();
        System.out.println();
    }

    /**
     * 练习4：Semaphore 控制并发数
     */
    static void semaphoreDemo() throws InterruptedException {
        System.out.println("=== Semaphore（限流）===");

        var semaphore = new Semaphore(3);  // 最多3个并发
        var executor = Executors.newFixedThreadPool(10);
        var latch = new CountDownLatch(8);

        for (int i = 1; i <= 8; i++) {
            final int id = i;
            executor.execute(() -> {
                try {
                    semaphore.acquire();
                    System.out.printf("  请求%d 获得许可 (可用: %d)%n", id, semaphore.availablePermits());
                    Thread.sleep(1000);  // 模拟处理
                    System.out.printf("  请求%d 完成，释放许可%n", id);
                } catch (InterruptedException ignored) {
                } finally {
                    semaphore.release();
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        System.out.println("  全部请求处理完成\n");
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
