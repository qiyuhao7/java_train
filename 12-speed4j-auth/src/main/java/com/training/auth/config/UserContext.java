package com.training.auth.config;

/**
 * 用户上下文（ThreadLocal）
 * 在认证过滤器中设置，在业务代码中获取当前用户
 */
public class UserContext {

    private static final ThreadLocal<UserInfo> HOLDER = new ThreadLocal<>();

    public static void set(UserInfo user) {
        HOLDER.set(user);
    }

    public static UserInfo get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }

    public static Long getCurrentUserId() {
        UserInfo user = get();
        if (user == null) {
            throw new RuntimeException("未登录");
        }
        return user.getUserId();
    }

    public static String getCurrentUserName() {
        UserInfo user = get();
        return user != null ? user.getName() : "anonymous";
    }
}
