package com.library.service;

import com.library.repository.AdminRepository;
import com.library.repository.AdminRepositoryImpl;
import com.library.exception.AuthenticationException;

public class AuthenticationService {
    private AdminRepository adminRepository;

    public AuthenticationService() {
        this.adminRepository = new AdminRepositoryImpl();
    }

    public AuthenticationService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public boolean authenticate(String username, String password) throws AuthenticationException {
        if (username == null || username.trim().isEmpty()) {
            throw new AuthenticationException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationException("Password cannot be empty");
        }

        var admin = adminRepository.findByUsername(username);
        if (admin == null) {
            throw new AuthenticationException("Invalid username or password");
        }

        if (!admin.getPasswordHash().equals(password)) {
            throw new AuthenticationException("Invalid username or password");
        }

        return true;
    }
}
