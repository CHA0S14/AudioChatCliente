package es.cios.audiochat.exceptions;

@SuppressWarnings("serial")
public class GrabadorException extends RuntimeException{
	public GrabadorException(String mensaje) {
		super(mensaje);
	}
	
	public GrabadorException(String mensaje, Exception causa) {
		super(mensaje, causa);
	}
}
