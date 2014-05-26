package es.cios.audiochat.hilos;

import java.io.IOException;
import java.net.Socket;

import es.cios.audiochat.exceptions.ClienteException;
import es.cios.audiochat.servicios.AudioChatService;
import es.cios.audiochat.util.Conexion;

public class RecibirMensaje extends Thread {
	private Socket socket;
	private boolean seguir=true;
	

	public RecibirMensaje(Socket socket) {
		this.socket = socket;
	}

	public void parar() {
		this.seguir = false;
	}

	@Override
	public void run() throws ClienteException {
		try {
			while (seguir) {
				Object object = Conexion.getIn().readObject();
				AudioChatService.recibirObjeto(object, socket.getInetAddress());
			}
			Conexion.getIn().close();
			socket.close();
		} catch (IOException e) {
			throw new ClienteException("Error al crear la conexion: "
					+ e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new ClienteException("Error al crear la conexion: "
					+ e.getMessage(), e);
		}
	}

}
