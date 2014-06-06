package es.cios.audiochat.exceptions;

/**
 * 
 * @author Chaos
 *
 */
@SuppressWarnings("serial")
public class ReproductorException extends RuntimeException{
	/**
	 * @param message
	 * @param cause
	 */
	public ReproductorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ReproductorException(String message) {
		super(message);
	}
}
