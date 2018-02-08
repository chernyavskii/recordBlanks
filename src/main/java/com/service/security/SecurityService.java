package com.service.security;

import org.springframework.security.core.Authentication;

public interface SecurityService {
/*
    String findLoggedInUsername();
*/
    void autoLogin(String username, String password);
}
