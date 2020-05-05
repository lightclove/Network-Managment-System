package ceeport.netManage.util;

public class BsoriLogger {
	
	private org.apache.log4j.Logger logger;
	
    private BsoriLogger(Class clazz) {
        this.logger = org.apache.log4j.Logger.getLogger(clazz);
    }

    public static BsoriLogger getLogger(Class clazz) {
    	return new BsoriLogger(clazz);
    }
    
    /**
     * Logs an error message.
     *
     * @param o the message to be written to the log.
     */
    public void error(Object o) {
        logger.error(o != null ? o.toString() : null);
    }

    /**
     * Logs a Throwable.
     *
     * @param    t   the Throwable to be written to the log.
     */
    public void error(Throwable t) {
        logger.error("", t);
    }

    /**
     * Logs an error message including stack trace from a Throwable.
     *
     * @param   o   the message to be written to the log.
     * @param   t   the Throwable to be written to the log.
     */
    public void error(Object o, Throwable t) {
        logger.error(o != null ? o.toString() : null, t);
    }

    /**
     * Logs an informational message.
     *
     * @param   o   the message to be written to the log.
     */
    public void info(Object o) {
    	if(logger.isInfoEnabled()) {
    		logger.info(o != null ? o.toString() : null);	
    	}
    }
    
    public void debug(Object o) {
    	if(logger.isDebugEnabled()) {
    		logger.debug(o != null ? o.toString() : null);	
    	}
    }

    public void trace(Object o) {
    	if(logger.isTraceEnabled()) {
    		logger.trace(o);	
    	}
    }
    
    /**
     * Logs a Throwable.
     *
     * @param   t   the Throwable to be written to the log.
     */
    public void info(Throwable t) {
    	if(logger.isInfoEnabled()) {
    		logger.info("", t);
    	}
    }

    /**
     * Logs an informational message including stack trace from a Throwable.
     *
     * @param   o   the message to be written to the log.
     * @param   e   the exception to be written to the log.
     */
    public void info(Object o, Throwable t) {
    	if(logger.isInfoEnabled()) {
    		logger.info(o != null ? o.toString() : null, t);	
    	}
    }
    
    /**
     * Logs a warning message.
     *
     * @param o the message to be written to the log.
     */
    public void warn(Object o) {
        logger.warn(o != null ? o.toString() : null);
    }

    /**
     * Logs a Throwable.
     *
     * @param   t   the Throwable to be written to the log.
     */
    public void warn(Throwable t) {
        logger.warn("", t);
    }

    /**
     * Logs a warning message including stack trace from a Throwable.
     *
     * @param   o   the message to be written to the log.
     * @param   t   the exception to be written to the log.
     */
    public void warn(Object o, Throwable t) {
        logger.warn(o != null ? o.toString() : null, t);
    }

    /**
     * Returns true if debug level logging is enabled for this component.
     *
     * @return true if debug level logging is enabled for this component.
     */
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }
    
    /**
     * Returns true if info level logging is enabled for this component.
     *
     * @return true if info level logging is enabled for this component.
     */
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }
}
