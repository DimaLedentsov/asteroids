
package net.logwrapper;
import java.util.Date;
import java.util.logging.*;
public class DefaultLogger  implements Logger{
    private final java.util.logging.Logger logger;
    public DefaultLogger(String name) {
        
        this.logger = java.util.logging.Logger.getLogger(name);
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        handler.setFormatter(new SimpleFormatter() {
            private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";
  
            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(format,
                        new Date(lr.getMillis()),
                        lr.getLevel().getLocalizedName(),
                        lr.getMessage()
                );
            }
        });
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);

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
