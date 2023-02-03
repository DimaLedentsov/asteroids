package net.logwrapper;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * class with static methods for printing
 */

public class Log4jLogger  implements net.logwrapper.Logger{
    private final org.apache.logging.log4j.Logger logger;
    public Log4jLogger(String name) {
        
        this.logger = org.apache.logging.log4j.LogManager.getRootLogger();

    }
    public void trace(String message) {
        logger.trace( message);
    }
    public void info(String message) {
        logger.info(message);
    }
    public void warn(String message) {
        logger.warn(message);
    }
    public void error(String message) {
        logger.error(message);
    }
}
