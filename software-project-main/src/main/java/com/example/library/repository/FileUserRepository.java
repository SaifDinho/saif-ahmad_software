package com.example.library.repository;

import com.example.library.domain.User;

import java.util.*;

public class FileUserRepository implements UserRepository {

    private final Map<Integer, User> storage = new HashMap<>();
    private int nextId = 1;

    @Override
    public User save(User user) {
        if (user.getUserId() == null) {
            user.setUserId(nextId++);
        }
        storage.put(user.getUserId(), cloneUser(user));
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getUserId() == null || !storage.containsKey(user.getUserId())) {
            return save(user);
        }
        storage.put(user.getUserId(), cloneUser(user));
        return user;
    }

    @Override
    public Optional<User> findById(Integer userId) {
        User user = storage.get(userId);
        return Optional.ofNullable(user == null ? null : cloneUser(user));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return storage.values().stream()
                .filter(u -> Objects.equals(u.getUsername(), username))
                .findFirst()
                .map(this::cloneUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return storage.values().stream()
                .filter(u -> Objects.equals(u.getEmail(), email))
                .findFirst()
                .map(this::cloneUser);
    }

    @Override
    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        for (User user : storage.values()) {
            result.add(cloneUser(user));
        }
        return result;
    }

    @Override
    public List<User> findByRole(String role) {
        List<User> result = new ArrayList<>();
        for (User user : storage.values()) {
            if (Objects.equals(user.getRole(), role)) {
                result.add(cloneUser(user));
            }
        }
        return result;
    }

    @Override
    public void deleteById(Integer userId) {
        storage.remove(userId);
    }

    @Override
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    private User cloneUser(User original) {
        User copy = new User();
        copy.setUserId(original.getUserId());
        copy.setUsername(original.getUsername());
        copy.setPassword(original.getPassword());
        copy.setEmail(original.getEmail());
        copy.setRole(original.getRole());
        copy.setCreatedAt(original.getCreatedAt());
        return copy;
    }
}
