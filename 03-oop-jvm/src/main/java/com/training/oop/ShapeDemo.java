package com.training.oop;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * OOP 综合练习：图形系统
 * 演示：接口、抽象类、继承、多态
 */
public class ShapeDemo {

    public static void main(String[] args) {
        // 多态：用父类引用管理不同子类
        List<Shape> shapes = new ArrayList<>();
        shapes.add(new Circle("红色", 5.0));
        shapes.add(new Rectangle("蓝色", 3.0, 4.0));
        shapes.add(new Triangle("绿色", 6.0, 8.0));

        System.out.println("=== 绘制所有图形 ===");
        shapes.forEach(Shape::draw);  // 多态调用

        System.out.println("\n=== 按面积排序 ===");
        shapes.sort(Comparator.comparingDouble(Shape::calculateArea).reversed());
        shapes.forEach(s -> System.out.printf("  %s: 面积 = %.2f%n", s.getDescription(), s.calculateArea()));

        System.out.println("\n=== 缩放操作 ===");
        shapes.forEach(s -> {
            s.resize(2.0);
            System.out.printf("  缩放后 %s: 面积 = %.2f%n", s.getDescription(), s.calculateArea());
        });

        System.out.println("\n=== 总面积 ===");
        double totalArea = shapes.stream()
            .mapToDouble(Shape::calculateArea)
            .sum();
        System.out.printf("  总面积: %.2f%n", totalArea);
    }
}

// ===== 接口：定义能力 =====

interface Drawable {
    void draw();
}

interface Resizable {
    void resize(double factor);
}

// ===== 抽象类：共享状态 + 模板 =====

abstract class Shape implements Drawable, Resizable {
    protected String color;

    protected Shape(String color) {
        this.color = color;
    }

    public abstract double calculateArea();
    public abstract String getDescription();

    @Override
    public void draw() {
        System.out.printf("  绘制 [%s] %s，面积: %.2f%n", color, getDescription(), calculateArea());
    }

    public String getColor() {
        return color;
    }
}

// ===== 具体类 =====

class Circle extends Shape {
    private double radius;

    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public void resize(double factor) {
        this.radius *= factor;
    }

    @Override
    public String getDescription() {
        return String.format("圆形(r=%.1f)", radius);
    }
}

class Rectangle extends Shape {
    private double width;
    private double height;

    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }

    @Override
    public double calculateArea() {
        return width * height;
    }

    @Override
    public void resize(double factor) {
        this.width *= factor;
        this.height *= factor;
    }

    @Override
    public String getDescription() {
        return String.format("矩形(%.1f×%.1f)", width, height);
    }
}

class Triangle extends Shape {
    private double base;
    private double height;

    public Triangle(String color, double base, double height) {
        super(color);
        this.base = base;
        this.height = height;
    }

    @Override
    public double calculateArea() {
        return 0.5 * base * height;
    }

    @Override
    public void resize(double factor) {
        this.base *= factor;
        this.height *= factor;
    }

    @Override
    public String getDescription() {
        return String.format("三角形(底%.1f,高%.1f)", base, height);
    }
}
