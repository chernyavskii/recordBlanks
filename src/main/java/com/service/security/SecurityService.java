package com.service.security;

import org.springframework.security.core.Authentication;

public interface SecurityService {
    String findUser();
    void autoLogin(String username, String password);
}
