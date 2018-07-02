package engine;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DebugManager {
    
    private static boolean debug = true;
    public static final Level LEVEL = Level.FINEST;
    
    public static boolean isDebugModeOn() {
        return debug;
    }
    
    public static void setDebugMode(boolean mode) {
        debug = mode;
    }
    
    public static void setLoggerLevel(Logger logger) {
        setLoggerLevel(logger, LEVEL);
    }
    
    public static void setLoggerLevel(Logger logger, Level level) {
        logger.setLevel(level);
        
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(level);
        logger.addHandler(handler);
    }
}