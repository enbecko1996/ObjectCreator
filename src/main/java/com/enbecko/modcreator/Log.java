package com.enbecko.modcreator;

/**
 * Created by Niclas on 09.03.2017.
 */
public class Log {
    public static void d(LogEnums logEnums, String txt) {
        if (GlobalSettings.shouldLog(logEnums)) {
            System.out.println("\nDEBUG: " + logEnums + ", " + Thread.currentThread().getStackTrace()[2] + "\n"+txt);
        }
    }

    public static void d(LogEnums logEnums, Object txt) {
        d(logEnums, txt.toString());
    }

    public enum LogEnums {
        CONTENTHOLDER, BLOCKSETTING, MATH, GEOMETRY, MINECRAFT, ETC;
    }
}
