
package net.logwrapper;
import java.util.logging.*;
public class DefaultLogger  implements Logger{
    private final java.util.logging.Logger logger;
    public DefaultLogger() {
        this.logger = java.util.logging.Logger.getLogger("net");
    }
    public void trace(String message) {
        this.logger.log(Level.FINE, message);
    }
    public void info(String message) {
        this.logger.log(Level.INFO, message);
    }
    public void warn(String message) {
        this.logger.log(Level.WARNING, message);
    }
    public void error(String message) {
        this.logger.log(Level.SEVERE, message);
    }
}
