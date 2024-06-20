package ru.otus.homework.test;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import ru.otus.homework.annotations.After;
import ru.otus.homework.annotations.Before;
import ru.otus.homework.annotations.Test;
import ru.otus.homework.model.User;
import ru.otus.homework.service.UserService;

@Slf4j
public class UserServiceTest {
    private UserService userService;
    private User testUser;
    private Integer expectedUserId;
    private String expectedUserName;

    @Before
    public void prepare() {
        userService = new UserService();
        expectedUserId = 1;
        expectedUserName = "John";
        testUser = userService.create(expectedUserId, expectedUserName);
    }

    @Test
    public void testCreate() {
        assertThat(testUser.getId()).isEqualTo(expectedUserId);
        assertThat(testUser.getName()).isEqualTo(expectedUserName);

        log.debug("checking creation new user");
    }

    @Test
    public void testGet() {
        User gettingUser = userService.get(expectedUserId);

        assertThat(gettingUser.getId()).isEqualTo(expectedUserId);
        assertThat(gettingUser.getName()).isEqualTo(expectedUserName);

        log.debug("checking get user");
    }

    @Test
    public void testGetFail() {
        User gettingUser = userService.get(expectedUserId + 1);

        assertThat(gettingUser.getId()).isEqualTo(expectedUserId);
        assertThat(gettingUser.getName()).isEqualTo(expectedUserName);

        log.debug("checking get user fail");
    }

    @Test
    public void testGetNotFound() {
        Assertions.assertThatThrownBy(() -> userService.get(expectedUserId + 1).getName())
                .isInstanceOf(NullPointerException.class);

        log.debug("checking get user NullPointerException");
    }

    @After
    public void clean() {
        userService = null;
        testUser = null;
        expectedUserId = null;
        expectedUserName = null;
    }
}
