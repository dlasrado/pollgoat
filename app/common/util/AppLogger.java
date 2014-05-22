/**
 * 
 */
package common.util;
import play.Logger;

/**
 * @author gdev
 *
 */
public class AppLogger {
    
    private static final String SPACE = " ";
    
    public static void debug(String uuid, String message) {
        Logger.debug(uuid + SPACE +message);
    }
    
    public static void debug(String errorCode, String uuid, String message) {
        Logger.debug(uuid + SPACE + errorCode + SPACE +message);
    }
    
    public static void error(String uuid, String message) {
        Logger.error(uuid + SPACE + message);
    }
    
    public static void error(String uuid, String message, Throwable paramThrowable) {
        Logger.error(uuid + SPACE + message, paramThrowable);
    }
    
    public static void error(String errorCode, String uuid, String message){
        Logger.error(uuid + SPACE + errorCode + SPACE + message);
    }
    
    public static void info(String uuid, String message) {
        Logger.info(uuid + SPACE +message);
    }
    
    public static void info(String errorCode, String uuid, String message) {
        Logger.info(uuid + SPACE + errorCode + SPACE +message);
    }
    
    public static void warn(String uuid, String message) {
        Logger.warn(uuid + SPACE +message);
    }
    
    public static void warn(String errorCode, String uuid, String message){
        Logger.warn(uuid + SPACE + errorCode + SPACE + message);
    }
    
    public static void trace(String uuid, String paramString) {
        Logger.trace(uuid + SPACE +paramString);
    }
    
    public static boolean isDebugEnabled() {
        return Logger.isDebugEnabled();
    }
    
}
