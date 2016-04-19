package org.greenicon.common.exception;

public class ServiceException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8829994558750752066L;

	public ServiceException() {
		super("Service Exception");
	}

	public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super("Service Exception", cause);
	}

	
}
