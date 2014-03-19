package net.coderodde.multilog;

import java.security.SecureRandom;
import java.util.Random;

/**
 * This class provides utilities for multilog.
 * 
 * @author Rodion Efremov
 */
public class Utils {

    public final static byte[] generateRandomSalt() {
        final Random r = new SecureRandom();
        byte[] salt = new byte[Config.SALT_LENGTH];
        r.nextBytes(salt);
        return salt;
    }
    
    public static void main(String... args) {
        int i = 32;
        
        do {
            char c = (char) i;
            
            System.out.printf("%3d %c\n", i, c);
            i++;
        } while (i < 128);
    }
}
