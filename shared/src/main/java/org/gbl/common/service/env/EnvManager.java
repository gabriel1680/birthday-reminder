package org.gbl.common.service.env;

public class EnvManager {

    private static final Env ENV =  Env.of(System.getProperty("env", "development"));

    public static boolean isTest() {
        return ENV.equals(Env.TEST);
    }
}
