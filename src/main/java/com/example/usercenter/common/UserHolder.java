package com.example.usercenter.common;

/**
 * 用户id本地线程
 * @author _LJC
 */
public class UserHolder {
    public static final ThreadLocal<Long> USER_THREAD = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        USER_THREAD.set(userId);
    }

    public static Long getUserId() {
        return USER_THREAD.get();
    }

    public static void removeUserId() {
        USER_THREAD.remove();
    }
}
