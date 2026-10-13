package com.restaurant;

import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AdminTokenService {

    private final Set<String> tokens =
            ConcurrentHashMap.newKeySet();

    public String createToken() {
        String token = java.util.UUID.randomUUID().toString();
        tokens.add(token);
        return token;
    }

    public boolean isValid(String token) {
        return token != null && tokens.contains(token);
    }

    public void removeToken(String token) {
        if (token != null) {
            tokens.remove(token);
        }
    }
}