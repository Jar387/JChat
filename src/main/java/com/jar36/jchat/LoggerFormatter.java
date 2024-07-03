package com.jar36.jchat;

import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerFormatter extends Formatter {
    @Override
    public String format(LogRecord logRecord) {
        return String.format("%tT %s %s\n", new Date(), logRecord.getLevel() ,logRecord.getMessage());
    }
    public static Logger installFormatter(Logger logger){
        logger.setUseParentHandlers(false);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new LoggerFormatter());
        logger.addHandler(consoleHandler);
        return logger;
    }
}
