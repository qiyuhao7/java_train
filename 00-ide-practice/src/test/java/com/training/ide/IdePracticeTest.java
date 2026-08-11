package com.training.ide;

import com.training.ide.model.User;
import com.training.ide.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 测试类：练习运行单个测试（方法名前绿色箭头）、Ctrl+Shift+F10
 */
class IdePracticeTest {

    private final UserService userService = new UserService();

    @Test
    void shouldGetUserById() {
        User user = userService.getById(1L);
        assertNotNull(user);
        assertEquals("user1", user.getUsername());
    }

    @Test
    void shouldCreateUser() {
        User user = new User();
        user.setUsername("test_user");
        user.setEmail("test@test.com");
        user.setAge(30);
        user.setVip(false);

        User created = userService.create(user);
        assertNotNull(created.getId());
        assertEquals("test_user", created.getUsername());
    }

    @Test
    void shouldListAll() {
        List<User> users = userService.listAll();
        assertTrue(users.size() >= 5);
    }
}
