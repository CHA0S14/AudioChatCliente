package es.cios.audiochat.servicios;

import java.util.ArrayList;
import java.util.List;

import es.cios.audiochat.entities.Canal;
import es.cios.audiochat.util.Conexion;

public class AudioChatService {
	private static List<Canal> canales = new ArrayList<Canal>();
	
	public static void inciar() {
		Conexion.conectar();		
	}

	public static void escribirMensaje(String readLine) {
		// TODO Auto-generated method stub
		
	}

	public static void setCanales(ArrayList<Canal> canales) {
		AudioChatService.canales = canales;		
		System.out.println(canales.size());
		for (Canal canal : AudioChatService.canales) {
			System.out.println(canal.getClientes().size());
		}
	}
	
}
