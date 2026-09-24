package com.example.library.service;

import com.example.library.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public UserService() {
        users.add(new User(idCounter.getAndIncrement(), "admin", "admin123", "ADMIN"));
        users.add(new User(idCounter.getAndIncrement(), "john", "john123", "USER"));
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User addUser(User user) {
        user.setId(idCounter.getAndIncrement());
        users.add(user);
        return user;
    }

    public boolean deleteUser(int id) {
        return users.removeIf(u -> u.getId() == id);
    }

    public User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public User getUserById(int id) {
        for (User u : users) {
            if (u.getId() == id) return u;
        }
        return null;
    }
}
