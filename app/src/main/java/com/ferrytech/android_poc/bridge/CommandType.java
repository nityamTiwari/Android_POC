package com.ferrytech.android_poc.bridge;

public enum CommandType {

    PING,
    ECHO,
    DEVICE_INFO,
    UNKNOWN;

    public static CommandType fromString(String command){
        if(command == null){
            return UNKNOWN;
        }

        switch(command.trim().toLowerCase()){
            case "ping":
                return PING;

            case "echo":
                return ECHO;

            case "device_info":
                return DEVICE_INFO;

            default:
                return UNKNOWN;
        }
    }

    public static void testCommands(){
        System.out.println(fromString("ping"));
        System.out.println(fromString("ping "));
        System.out.println(fromString("echo"));
        System.out.println(fromString("device_info"));
        System.out.println(fromString("invalid_command"));
        System.out.println(fromString(null));    }
}
