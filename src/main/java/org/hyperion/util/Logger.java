package org.hyperion.util;

/**
 * A logger implementation for debugging purposes.
 * @author Stephen Andrews
 * @since 7/7/16
 */
public final class Logger {

    /**
     * Represents the different types of logging levels.
     */
    public enum Level {

        /**
         * Every minute detail of information is printed.
         */
        VERBOSE,

        /**
         * Relays only core messages related to the server.
         */
        CORE,

        /**
         * An informational message regarding the server.
         */
        INFO,

        /**
         * A debugging message.
         */
        DEBUG,

        /**
         * Nothing is printed.
         */
        NONE;
    }

    /**
     * Defines which types of messages should be displayed in the console.
     */
    private static Level sensitivity = Level.VERBOSE;

    /**
     * Private constructor to prevent instantiation.
     */
    private Logger() {}

    /**
     * Prints a message to the console.
     * @param level The logging level.
     * @param msg The message to print.
     */
    public static void log(Level level, String msg) {
        String severity = level.toString().toUpperCase();
        String time = TimeStamp.add();

        if (level.ordinal() >= sensitivity.ordinal()) {
            System.out.println(time + "[" + severity + "] " + msg);
        }
    }
}
