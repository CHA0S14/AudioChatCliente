package es.cios.audiochat.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import es.cios.audiochat.entities.Canal;
import es.cios.audiochat.exceptions.ConexionException;
import es.cios.audiochat.servicios.AudioChatService;

public class Conexion extends Thread {
	private static Socket socket = null;
	private static boolean seguir = true;

	public static void conectar() {
		try {
			if (socket == null) {
				socket = new Socket("localhost", 9999);
				recibirCanales();
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private static void recibirCanales() {
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			while (seguir) {
				AudioChatService.setCanales((ArrayList<Canal>)in.readObject());
			}
			in.close();
			socket.close();
		} catch (IOException e) {
			throw new ConexionException("Error al recibir el objeto: "
					+ e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new ConexionException("Error al recibir el objeto: "
					+ e.getMessage(), e);
		}
	}

	public static void finalizar() {
		try {
			seguir = false;
			socket.close();
		} catch (IOException e) {
			throw new ConexionException("Error al cerrar la conexion: "
					+ e.getMessage(), e);
		}
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			while (seguir) {
				AudioChatService.escribirMensaje(in.readLine());
			}
			in.close();
		} catch (IOException e) {
			throw new ConexionException("Error al crear la conexion: "
					+ e.getMessage(), e);
		}
	}

}
