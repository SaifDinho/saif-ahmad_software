package com.library.service;

import com.library.model.User;
import com.library.repository.UserRepository;
import com.library.repository.UserRepositoryImpl;
import com.library.exception.UserNotFoundException;
import com.library.util.ValidationUtil;
import java.time.LocalDateTime;
import java.util.List;

public class UserService {
    private UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepositoryImpl();
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(User user) throws IllegalArgumentException {
        if (user == null || !ValidationUtil.isNotEmpty(user.getName())) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email address");
        }
        if (!ValidationUtil.isValidPhone(user.getPhone())) {
            throw new IllegalArgumentException("Phone must be 10 digits");
        }
        if (!ValidationUtil.isNotEmpty(user.getMemberId())) {
            throw new IllegalArgumentException("Member ID cannot be empty");
        }

        user.setRegistrationDate(LocalDateTime.now());
        user.setActive(true);
        userRepository.save(user);
    }

    public void updateUser(User user) throws IllegalArgumentException {
        if (user == null || user.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user");
        }
        userRepository.update(user);
    }

    public User getUserById(int userId) throws UserNotFoundException {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        return user;
    }

    public User getUserByMemberId(String memberId) throws UserNotFoundException {
        User user = userRepository.findByMemberId(memberId);
        if (user == null) {
            throw new UserNotFoundException("User not found with Member ID: " + memberId);
        }
        return user;
    }

    public List<User> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return userRepository.findAll();
        }
        return userRepository.findByName(name);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getActiveUsers() {
        return userRepository.findActive();
    }

    public void unregisterUser(int userId) throws UserNotFoundException, IllegalStateException {
        User user = getUserById(userId);
        // In a real system, check if user has outstanding fines or active loans
        user.setActive(false);
        updateUser(user);
    }
}
