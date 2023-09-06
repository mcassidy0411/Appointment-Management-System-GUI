package com.mc.controller;

import com.mc.model.CurrentUser;

/**
 * Provides a method signature for setting the current user in controllers that implement this interface.
 * @author Michael Cassidy
 */
public interface SharedControllerInterface {
    /**
     * Sets the current user for the implementing controller.
     * @param currentUser The CurrentUser object containing information about the logged-in user.
     */
    void setCurrentUser(CurrentUser currentUser);
}