package com.training.oop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 第03期：OOP 与 JVM 练习
 * 验证初始化顺序、多态、内部类、重载重写等
 */
@DisplayName("OOP 与 JVM")
class OopJvmTest {

    // ===== 初始化顺序 =====

    static StringBuilder initLog = new StringBuilder();

    static class Parent {
        static String pStatic = log("父类静态变量");
        static { log("父类静态代码块"); }
        String pInstance = log("父类实例变量");
        { log("父类构造块"); }
        Parent() { log("父类构造函数"); }
    }

    static class Child extends Parent {
        static String cStatic = log("子类静态变量");
        static { log("子类静态代码块"); }
        String cInstance = log("子类实例变量");
        { log("子类构造块"); }
        Child() { log("子类构造函数"); }
    }

    static String log(String s) {
        initLog.append(s).append(" → ");
        return s;
    }

    @Test
    @DisplayName("类初始化顺序：静态→实例→构造，父先子后")
    void initializationOrder() {
        initLog.setLength(0);
        // 触发类加载（静态部分只执行一次）
        new Child();

        String order = initLog.toString();
        // 验证顺序
        assertTrue(order.indexOf("父类静态变量") < order.indexOf("子类静态变量"));
        assertTrue(order.indexOf("子类静态代码块") < order.indexOf("父类实例变量"));
        assertTrue(order.indexOf("父类构造块") < order.indexOf("父类构造函数"));
        assertTrue(order.indexOf("父类构造函数") < order.indexOf("子类实例变量"));
        assertTrue(order.indexOf("子类构造块") < order.indexOf("子类构造函数"));
    }

    @Test
    @DisplayName("静态部分只执行一次")
    void staticOnlyOnce() {
        initLog.setLength(0);
        new Child();
        int firstLength = initLog.length();

        initLog.setLength(0);
        new Child();  // 第二次 new
        String secondLog = initLog.toString();

        // 第二次不应该有静态变量/静态代码块
        assertFalse(secondLog.contains("静态变量"));
        assertFalse(secondLog.contains("静态代码块"));
        // 但实例部分每次都执行
        assertTrue(secondLog.contains("父类实例变量"));
        assertTrue(secondLog.contains("子类构造函数"));
    }

    // ===== 多态 =====

    interface Animal { String speak(); }
    static class Dog implements Animal { public String speak() { return "汪"; } }
    static class Cat implements Animal { public String speak() { return "喵"; } }

    @Test
    @DisplayName("多态：编译看左边，运行看右边")
    void polymorphism() {
        Animal animal = new Dog();
        assertEquals("汪", animal.speak());  // 运行时是 Dog

        animal = new Cat();
        assertEquals("喵", animal.speak());  // 运行时是 Cat

        // 编译看左边：Animal 没有 bark() 方法
        // animal.bark();  // ❌ 编译错误
        // 需要强转
        Dog dog = new Dog();
        Animal ref = dog;
        assertEquals("汪", ((Dog) ref).speak());
    }

    // ===== 重载 vs 重写 =====

    static class Base {
        String process(Object obj) { return "Base-Object"; }
        String process(String s) { return "Base-String"; }
    }

    static class Derived extends Base {
        @Override
        String process(Object obj) { return "Derived-Object"; }
        // 注意：没有重写 process(String)
    }

    @Test
    @DisplayName("重载是静态分派（看声明类型）")
    void overloadStaticDispatch() {
        Base b = new Base();
        Object obj = "hello";

        // 重载看参数的静态类型
        assertEquals("Base-Object", b.process(obj));    // 静态类型是 Object
        assertEquals("Base-String", b.process("hello")); // 静态类型是 String
        assertEquals("Base-String", b.process((String) obj));  // 强转改变分派
    }

    @Test
    @DisplayName("重写是动态分派（看实际类型）")
    void overrideDynamicDispatch() {
        Base b = new Derived();  // 父类引用指向子类对象

        // 重写看实际对象类型
        assertEquals("Derived-Object", b.process((Object) "x"));  // 动态分派到 Derived

        // 但 process(String) 没有被 Derived 重写，调用 Base 的
        assertEquals("Base-String", b.process("x"));
    }

    // ===== 内部类 =====

    static class Outer {
        private int x = 10;

        class Inner {
            int getX() { return x; }  // 访问外部类私有成员
        }

        static class StaticInner {
            static int y = 20;
        }
    }

    @Test
    @DisplayName("成员内部类访问外部类成员")
    void memberInnerClass() {
        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner();
        assertEquals(10, inner.getX());
    }

    @Test
    @DisplayName("静态内部类无需外部实例")
    void staticInnerClass() {
        Outer.StaticInner si = new Outer.StaticInner();
        assertEquals(20, Outer.StaticInner.y);
    }

    @Test
    @DisplayName("匿名内部类")
    void anonymousInnerClass() {
        Animal animal = new Animal() {
            @Override
            public String speak() { return "匿名"; }
        };
        assertEquals("匿名", animal.speak());
    }

    // ===== 值传递 =====

    static class User {
        String name;
        User(String name) { this.name = name; }
    }

    void modifyUser(User user) {
        user.name = "Changed";   // 修改对象属性（通过引用副本找到同一对象）
        user = new User("New");  // 只修改局部变量，不影响外面
    }

    @Test
    @DisplayName("Java 是值传递：引用类型传递引用的副本")
    void passByValue() {
        User u = new User("Alice");
        modifyUser(u);
        assertEquals("Changed", u.name);  // 对象被修改了
        // 但 u 仍然指向原来的对象（不是 "New"）
    }

    // ===== final =====

    @Test
    @DisplayName("final 变量只能赋值一次")
    void finalVariable() {
        final int x;
        x = 10;  // ✅ 第一次赋值
        // x = 20;  // ❌ 编译错误

        // final 引用：引用不可变，对象内容可变
        final StringBuilder sb = new StringBuilder("hello");
        sb.append(" world");  // ✅ 修改对象内容
        // sb = new StringBuilder();  // ❌ 不能重新赋值
        assertEquals("hello world", sb.toString());
    }

    // ===== instanceof =====

    @Test
    @DisplayName("instanceof 与 null")
    void instanceOfNull() {
        Object obj = null;
        assertFalse(obj instanceof String);  // null instanceof 任何类型都是 false

        obj = "hello";
        assertTrue(obj instanceof String);
        assertTrue(obj instanceof Object);
        assertTrue(obj instanceof CharSequence);
        assertTrue(obj instanceof Comparable);
    }
}
