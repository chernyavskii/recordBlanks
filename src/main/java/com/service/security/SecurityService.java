package com.service.security;

public interface SecurityService {
    String findUser();
    void autoLogin(String username, String password);
}
