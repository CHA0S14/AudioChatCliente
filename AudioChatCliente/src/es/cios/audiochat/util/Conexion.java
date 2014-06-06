package es.cios.audiochat.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import es.cios.audiochat.exceptions.ConexionException;
import es.cios.audiochat.servicios.AudioChatService;

public class Conexion {
	private static Socket socket = null;
	private static ObjectInputStream in = null;
	private static ObjectOutputStream out = null;

	/**
	 * contructor de la clase
	 * @throws ConexionException
	 */
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

	

	/**
	 * @return the in
	 */
	public static ObjectInputStream getIn() {
		if(in==null){
			try {
				in = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				throw new ConexionException("Error al crear el object in: "
						+ e.getMessage(), e);
			}
		}
		return in;
	}

	/**
	 * @return the out
	 */
	public static ObjectOutputStream getOut() {
		if(out==null){
			try {
				out = new ObjectOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				throw new ConexionException("Error al crear el object out: "
						+ e.getMessage(), e);
			}
		}
		return out;
	}

	/**
	 * metodo que recibe los canales por primera vez y sirve para sincronizar la conexion
	 */
	private static void recibirCanales() {
		try {			
			AudioChatService.recibirObjeto(getIn().readObject());
		} catch (IOException e) {
			throw new ConexionException("Error al recibir el objeto: "
					+ e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new ConexionException("Error al recibir el objeto: "
					+ e.getMessage(), e);
		}
	}

	/**
	 * finaliza la conexion
	 */
	public static void finalizar() {
		try {
			socket.close();
		} catch (IOException e) {
			throw new ConexionException("Error al cerrar la conexion: "
					+ e.getMessage(), e);
		}
	}

	/**
	 * @return the socket
	 */
	public static Socket getSocket(){
		return Conexion.socket;
	}

	/**
	 * envia el objeto recibido al servidor
	 * @param obj objeto a enviar
	 */
	public static void enviarObjeto(Object obj) {
		try {	
			getOut().reset();
			getOut().writeObject(obj);
			getOut().flush();
		} catch (IOException e) {
			throw new ConexionException("Error al enviar el objeto: "
					+ e.getMessage(), e);
		}
	}
}
