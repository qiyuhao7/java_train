package com.training.basics;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Java 11 现代特性练习
 */
public class Java11Features {

    public static void main(String[] args) throws Exception {
        stringFeatures();
        collectionFactory();
        streamEnhancements();
        optionalEnhancements();
        fileOperations();
        // httpClientDemo();  // 需要运行中的服务
        System.out.println("\n=== 全部练习完成 ===");
    }

    /**
     * 练习1：字符串新特性
     */
    static void stringFeatures() {
        System.out.println("=== 字符串特性 ===");

        // strip vs trim（strip 支持 Unicode 空白）
        var padded = "  hello\u2003world  ";
        System.out.println("strip: [" + padded.strip() + "]");
        System.out.println("stripLeading: [" + padded.stripLeading() + "]");
        System.out.println("stripTrailing: [" + padded.stripTrailing() + "]");

        // isBlank
        System.out.println("'  '.isBlank() = " + "   ".isBlank());
        System.out.println("''.isEmpty() = " + "".isEmpty());

        // repeat
        System.out.println("repeat: " + "Java ".repeat(3));

        // lines
        var multiline = "line1\nline2\nline3";
        multiline.lines().forEach(line -> System.out.println("  -> " + line));
    }

    /**
     * 练习2：集合工厂方法
     */
    static void collectionFactory() {
        System.out.println("\n=== 集合工厂 ===");

        // 不可变集合
        var fruits = List.of("苹果", "香蕉", "橘子");
        var numbers = Set.of(1, 2, 3, 4, 5);
        var config = Map.of("host", "localhost", "port", "9099");

        System.out.println("List: " + fruits);
        System.out.println("Set: " + numbers);
        System.out.println("Map: " + config);

        // Map.ofEntries
        var userScores = Map.ofEntries(
            Map.entry("Alice", 95),
            Map.entry("Bob", 87),
            Map.entry("Charlie", 92)
        );
        System.out.println("Scores: " + userScores);

        // 尝试修改（会抛异常）
        try {
            fruits.add("西瓜");
        } catch (UnsupportedOperationException e) {
            System.out.println("✅ 不可变集合，无法修改");
        }

        // 需要可变集合时
        var mutableList = new java.util.ArrayList<>(fruits);
        mutableList.add("西瓜");
        System.out.println("可变副本: " + mutableList);
    }

    /**
     * 练习3：Stream 增强
     */
    static void streamEnhancements() {
        System.out.println("\n=== Stream 增强 ===");

        var data = List.of(1, 2, 3, 4, 5, 1, 2, 3);

        // takeWhile：取满足条件的前缀
        var taken = data.stream().takeWhile(n -> n < 4).collect(Collectors.toList());
        System.out.println("takeWhile(<4): " + taken);  // [1, 2, 3]

        // dropWhile：跳过满足条件的前缀
        var dropped = data.stream().dropWhile(n -> n < 3).collect(Collectors.toList());
        System.out.println("dropWhile(<3): " + dropped);  // [3, 4, 5, 1, 2, 3]

        // ofNullable
        String nullValue = null;
        var count = Stream.ofNullable(nullValue).count();
        System.out.println("ofNullable(null).count(): " + count);  // 0

        // iterate 带终止条件
        var powers = Stream.iterate(1, n -> n <= 1024, n -> n * 2)
            .collect(Collectors.toList());
        System.out.println("2的幂: " + powers);  // [1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024]
    }

    /**
     * 练习4：Optional 增强
     */
    static void optionalEnhancements() {
        System.out.println("\n=== Optional 增强 ===");

        Optional<String> present = Optional.of("Hello");
        Optional<String> empty = Optional.empty();

        // ifPresentOrElse
        present.ifPresentOrElse(
            v -> System.out.println("有值: " + v),
            () -> System.out.println("无值")
        );
        empty.ifPresentOrElse(
            v -> System.out.println("有值: " + v),
            () -> System.out.println("无值，使用默认逻辑")
        );

        // or()：提供备选
        var result = empty.or(() -> Optional.of("默认值"));
        System.out.println("or() 结果: " + result.get());

        // stream()：转为 Stream
        var list = Stream.of(present, empty, Optional.of("World"))
            .flatMap(Optional::stream)
            .collect(Collectors.toList());
        System.out.println("flatMap Optional::stream: " + list);  // [Hello, World]
    }

    /**
     * 练习5：文件读写
     */
    static void fileOperations() throws IOException {
        System.out.println("\n=== 文件读写 ===");

        Path tempFile = Path.of("/tmp/java11-demo.txt");

        // 一行写入
        Files.writeString(tempFile, "Hello Java 11!\n第二行内容\n");
        System.out.println("写入完成: " + tempFile);

        // 一行读取
        String content = Files.readString(tempFile);
        System.out.println("读取内容:\n" + content);

        // 追加写入
        Files.writeString(tempFile, "追加的第三行\n",
            java.nio.file.StandardOpenOption.APPEND);

        // 按行读取
        Files.readAllLines(tempFile).forEach(line -> System.out.println("  行: " + line));

        // 清理
        Files.deleteIfExists(tempFile);
    }

    /**
     * 练习6：HTTP Client（需要运行中的服务）
     */
    static void httpClientDemo() throws Exception {
        System.out.println("\n=== HTTP Client ===");

        var client = HttpClient.newBuilder()
            .connectTimeout(java.time.Duration.ofSeconds(5))
            .build();

        var request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:9099/api/health"))
            .header("Accept", "application/json")
            .GET()
            .build();

        // 同步
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("状态码: " + response.statusCode());
        System.out.println("响应体: " + response.body());

        // 异步
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(body -> System.out.println("异步响应: " + body))
            .join();
    }
}
