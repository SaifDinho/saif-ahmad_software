package com.library.service;

import com.library.model.User;
import com.library.repository.UserRepository;
import com.library.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    public void testRegisterUserSuccessfully() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPhone("1234567890");
        user.setMemberId("MEM001");

        userService.registerUser(user);

        verify(userRepository).save(any(User.class));
        assertTrue(user.isActive());
    }

    @Test
    public void testRegisterUserWithInvalidEmail() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("invalid-email");
        user.setPhone("1234567890");
        user.setMemberId("MEM001");

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
    }

    @Test
    public void testRegisterUserWithInvalidPhone() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPhone("123");
        user.setMemberId("MEM001");

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
    }

    @Test
    public void testGetUserById() throws UserNotFoundException {
        User user = new User();
        user.setUserId(1);
        user.setName("John Doe");

        when(userRepository.findById(1)).thenReturn(user);

        User result = userService.getUserById(1);

        assertEquals(user, result);
    }

    @Test
    public void testGetUserByIdNotFound() {
        when(userRepository.findById(999)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(999));
    }

    @Test
    public void testGetUserByMemberId() throws UserNotFoundException {
        User user = new User();
        user.setMemberId("MEM001");
        user.setName("John Doe");

        when(userRepository.findByMemberId("MEM001")).thenReturn(user);

        User result = userService.getUserByMemberId("MEM001");

        assertEquals(user, result);
    }

    @Test
    public void testSearchByName() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setName("John Doe");
        users.add(user);

        when(userRepository.findByName("John")).thenReturn(users);

        List<User> result = userService.searchByName("John");

        assertEquals(1, result.size());
    }

    @Test
    public void testUnregisterUser() throws UserNotFoundException {
        User user = new User();
        user.setUserId(1);
        user.setActive(true);

        when(userRepository.findById(1)).thenReturn(user);

        userService.unregisterUser(1);

        assertFalse(user.isActive());
        verify(userRepository).update(user);
    }
}
