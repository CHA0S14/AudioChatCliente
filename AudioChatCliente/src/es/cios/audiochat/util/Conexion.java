package es.cios.audiochat.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import es.cios.audiochat.entities.Canal;
import es.cios.audiochat.exceptions.ConexionException;
import es.cios.audiochat.servicios.AudioChatService;

public class Conexion extends Thread {
	private static Socket socket = null;
	private static boolean seguir = true;

	public static void conectar() throws ConexionException{
		try {
			if (socket == null) {
				socket = new Socket("localhost", 9999);	
				recibirCanales();
			}
		} catch (UnknownHostException e) {
			throw new ConexionException("Error de conexion: " + e.getMessage(),e);
		} catch (IOException e) {
			throw new ConexionException("Error de conexion: " + e.getMessage(),e);
		}
	}

	@SuppressWarnings("unchecked")
	private static void recibirCanales() {
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			AudioChatService.setCanales((ArrayList<Canal>)in.readObject());
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

	public static Socket getSocket(){
		return Conexion.socket;
	}

	public static void enviarObjeto(Object obj) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(obj);
			out.flush();
		} catch (IOException e) {
			throw new ConexionException("Error al enviar el objeto: "
					+ e.getMessage(), e);
		}
	}
}
