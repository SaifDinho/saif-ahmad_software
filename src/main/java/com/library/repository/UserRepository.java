package com.library.repository;

import com.library.model.User;
import java.util.List;

public interface UserRepository {
    void save(User user);
    void update(User user);
    void delete(int userId);
    User findById(int userId);
    User findByMemberId(String memberId);
    List<User> findByName(String name);
    List<User> findAll();
    List<User> findActive();
}
