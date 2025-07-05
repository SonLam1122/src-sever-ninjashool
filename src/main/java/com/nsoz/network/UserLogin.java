package com.nsoz.network;

import java.time.Instant;


public class UserLogin {
    private int loginAttempts;
    private Instant lastLoginAttempt;

    public UserLogin() {
        loginAttempts = 0;
        lastLoginAttempt = Instant.MIN;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void incrementLoginAttempts() {
        loginAttempts++;
    }

    public Instant getLastLoginAttempt() {
        return lastLoginAttempt;
    }

    public void setLastLoginAttempt(Instant lastLoginAttempt) {
        this.lastLoginAttempt = lastLoginAttempt;
    }

    public void reset() {
        loginAttempts = 1;
        lastLoginAttempt = Instant.now();
    }
}
