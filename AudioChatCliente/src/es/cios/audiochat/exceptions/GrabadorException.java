package es.cios.audiochat.exceptions;

/**
 * 
 * @author Chaos
 *
 */
@SuppressWarnings("serial")
public class GrabadorException extends RuntimeException{
	/**
	 * @param message
	 * @param cause
	 */
	public GrabadorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public GrabadorException(String message) {
		super(message);
	}
}
