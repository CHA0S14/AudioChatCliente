package es.cios.audiochat.exceptions;

@SuppressWarnings("serial")
public class ReproductorException extends RuntimeException{
	public ReproductorException(String mensaje) {
		super(mensaje);
	}
	
	public ReproductorException(String mensaje, Exception causa) {
		super(mensaje, causa);
	}
}
