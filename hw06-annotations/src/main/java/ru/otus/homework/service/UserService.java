package ru.otus.homework.service;

import java.util.Map;
import java.util.TreeMap;
import ru.otus.homework.model.User;

public class UserService {
    private final Map<Integer, User> userMap = new TreeMap<>();

    public User create(Integer id, String name) {
        User user = new User(id, name);
        userMap.put(id, user);

        return user;
    }

    public User get(Integer id) {
        return userMap.get(id);
    }
}
