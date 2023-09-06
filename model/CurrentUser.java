package com.mc.model;

import com.mc.helper.JDBCQuery;

/**
 * Represents the currently logged-in user.
 * @author Michael Cassidy
 */
public class CurrentUser {
    private final int userId;
    private final String username;

    /**
     * Constructs a new CurrentUser instance with the given username.
     * The user ID is automatically retrieved using the provided username.
     * @param username The username of the current user.
     */
    public CurrentUser(String username) {
        this.username = username;
        userId = JDBCQuery.getUserId(username);
    }

    /**
     * Retrieves the user ID of the current user.
     * @return The user ID of the current user.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Retrieves the username of the current user.
     * @return The username of the current user.
     */
    public String getUsername() { return username; }
}
