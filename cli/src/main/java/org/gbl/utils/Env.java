package org.gbl.utils;

public class Env {

    private static final String ENV = System.getProperty("env", "local");

    public static boolean isTest() {
        return "test".equals(ENV);
    }
}
