package es.cios.audiochat.hilos;

import java.io.IOException;
import java.net.Socket;

import es.cios.audiochat.exceptions.ClienteException;
import es.cios.audiochat.servicios.AudioChatService;
import es.cios.audiochat.util.Conexion;
 /**
  * 
  * @author Chaos
  *
  */
public class RecibirMensaje extends Thread {
	private Socket socket;
	private boolean seguir=true;
	
	/**
	 * contructor
	 * @param socket
	 */
	public RecibirMensaje(Socket socket) {
		this.socket = socket;
	}

	/**
	 * para el while del hilo
	 */
	public void parar() {
		this.seguir = false;
		try {
			socket.close();
		} catch (IOException e) {
			throw new ClienteException("Error al cerrar la conexion: "
					+ e.getMessage(), e);
		}
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() throws ClienteException {
		try {
			while (seguir) {
				Object object = Conexion.getIn().readObject();
				AudioChatService.recibirObjeto(object);
			}
		} catch (IOException e) {
			throw new ClienteException("Error al crear la conexion: "
					+ e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new ClienteException("Error al crear la conexion: "
					+ e.getMessage(), e);
		}
	}

}
