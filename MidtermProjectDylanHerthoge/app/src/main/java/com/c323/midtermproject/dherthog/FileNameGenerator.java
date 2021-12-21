package com.c323.midtermproject.dherthog;

import java.util.Random;

/**
 * A class for generating random file names.
 */
public class FileNameGenerator {

    /**
     * Generates a random filename using the system time and a random (positive) integer
     * @return A random numeric filename.
     */
    public static String generateUniqueName() {
        long millis = System.currentTimeMillis();
        Random r = new Random();
        return millis + "_" + Math.abs(r.nextInt());
    }
}
