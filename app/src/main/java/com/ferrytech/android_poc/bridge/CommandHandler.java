package com.ferrytech.android_poc.bridge;

public interface CommandHandler{
    void handleCommand(
            String command,
            String payload,
            String executeTag
    );
}
