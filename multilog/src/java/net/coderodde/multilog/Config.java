package net.coderodde.multilog;

/**
 * This class defines the constants for configuring the system.
 *
 * @author Rodion Efremov
 */
public class Config {

    /**
     * Defines the length of each salt.
     */
    public static final int SALT_LENGTH = 32;

    /**
     * Defines the lookup name for JDBC.
     */
    public static final String DATABASE_LOOKUP_NAME =
            "java:/comp/env/jdbc/mlogDB";
}
