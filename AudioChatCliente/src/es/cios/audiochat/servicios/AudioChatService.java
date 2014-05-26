package es.cios.audiochat.servicios;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import es.cios.audiochat.entities.Canal;
import es.cios.audiochat.entities.Nombre;
import es.cios.audiochat.hilos.RecibirMensaje;
import es.cios.audiochat.interfaz.MainFrame;
import es.cios.audiochat.util.Conexion;

public class AudioChatService {
	private static List<Canal> canales = new ArrayList<Canal>();
	private static MainFrame frame;
	private static RecibirMensaje recibir;

	public static void inciar() {
		Conexion.conectar();
		recibir = new RecibirMensaje(Conexion.getSocket());
		recibir.start();
		frame = new MainFrame();		
		Nombre nombre = new Nombre();
		String name = (String) JOptionPane.showInputDialog(frame,
				"Que nombre quieres");

		nombre.setNombre(name);
		enviarNombre(nombre);
		frame.setVisible(true);		
	}

	private static void enviarNombre(Nombre nombre) {
		Conexion.enviarObjeto(nombre);
	}

	public static List<Canal> getCanales() {
		return canales;
	}

	public static void escribirMensaje() {
		String texto = frame.getMensaje() + "\n";
		Conexion.enviarObjeto(texto);
	}

	public static void setCanales(ArrayList<Canal> canales) {
		AudioChatService.canales = canales;
	}

	@SuppressWarnings("unchecked")
	public static void recibirObjeto(Object obj, InetAddress inetAddress) {
		if (obj instanceof List) {
			setCanales((ArrayList<Canal>) obj);
			frame.actualizarJTree();
		}else if (obj instanceof String){
			frame.escribir((String) obj);
		}

	}

}
