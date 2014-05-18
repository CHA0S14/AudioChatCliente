package es.cios.audiochat.hilos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import es.cios.audiochat.exceptions.ClienteException;
import es.cios.audiochat.servicios.AudioChatService;

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
			ObjectInputStream in = new ObjectInputStream(
					socket.getInputStream());
			while (seguir) {
				AudioChatService.recibirObjeto(in.readObject(), socket.getLocalAddress());
			}
			in.close();
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
