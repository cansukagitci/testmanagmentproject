package com.example.testmanagment.util;

import java.util.HashSet;
import java.util.Set;

public class TokenBlackList {
    private static final Set<String> blackListedTokens = new HashSet<>();

    public static void blacklistToken(String token) {
        blackListedTokens.add(token);
    }

    public static boolean isTokenBlackListed(String token) {
        return blackListedTokens.contains(token);
    }
}
