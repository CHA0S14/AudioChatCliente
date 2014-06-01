package es.cios.audiochat.servicios;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import es.cios.audiochat.entities.Canal;
import es.cios.audiochat.entities.CanalMod;
import es.cios.audiochat.entities.Finalizar;
import es.cios.audiochat.entities.MensajeAudio;
import es.cios.audiochat.entities.Nombre;
import es.cios.audiochat.exceptions.ServiceException;
import es.cios.audiochat.gui.AudioDialog;
import es.cios.audiochat.gui.MainFrame;
import es.cios.audiochat.hilos.RecibirMensaje;
import es.cios.audiochat.util.Conexion;

public class AudioChatService {
	private static List<Canal> canales = new ArrayList<Canal>();
	private static MainFrame frame;
	private static RecibirMensaje recibir;
	private static String name;
	private static File file;

	public static void inciar() {
		frame = new MainFrame();
		Conexion.conectar();
		recibir = new RecibirMensaje(Conexion.getSocket());
		recibir.start();
		cambiarNombre();
		frame.setVisible(true);
	}

	public static void cambiarNombre() {
		Nombre nombre = new Nombre();
		name = (String) JOptionPane
				.showInputDialog(frame, "Que nombre quieres");
		if (name != null && !name.equals("")) {
			frame.setTitle("Usuario: " + name);
			nombre.setNombre(name);
			Conexion.enviarObjeto(nombre);
		}
	}

	public static String getName() {
		return name;
	}

	public static File getFile() {
		return file;
	}

	public static void setFile(File file) {
		AudioChatService.file = file;
	}

	public static List<Canal> getCanales() {
		return canales;
	}

	public static void escribirMensaje() {
		String text = frame.getMensaje();
		if (!text.equals("")) {
			String texto = name + ": " + text + "\n";
			Conexion.enviarObjeto(texto);
		}
	}

	public static void enviarAudio() throws ServiceException {
		try {
			File f = getFile();
			byte[] content = Files.readAllBytes(f.toPath());
			MensajeAudio mensaje = new MensajeAudio(getName(), content);

			Conexion.enviarObjeto(mensaje);
		} catch (IOException e) {
			throw new ServiceException("Error al enviar el audio: "
					+ e.getMessage(), e);
		}

	}

	public static void setCanales(ArrayList<Canal> canales) {
		AudioChatService.canales = canales;
	}

	@SuppressWarnings("unchecked")
	public static void recibirObjeto(Object obj, InetAddress inetAddress) {
		
			if (obj instanceof ArrayList) {
				setCanales((ArrayList<Canal>) obj);
				frame.actualizarJTree();
			} else if (obj instanceof String) {
				frame.escribir((String) obj);
			} else if (obj instanceof MensajeAudio) {
				reproducirAudio(obj);
			}
	}

	private static void reproducirAudio(Object obj) {
		try {
			// recoge el archivo
			MensajeAudio mensaje = (MensajeAudio) obj;
			byte[] content = mensaje.getAudioFile();
			File file = File.createTempFile("GrabacionRecibida", ".wave");
			Files.write(file.toPath(), content);
	
			// Crea el dialog
			AudioDialog dialog = new AudioDialog(frame, false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setTitle("Emisor: " + mensaje.getUser());
			dialog.setFile(file);
			dialog.setVisible(true);
		} catch (IOException e) {
			throw new ServiceException("Error al recibir el audio: "
					+ e.getMessage(), e);
		}
	}

	@SuppressWarnings("deprecation")
	public static void finalizar() {
		Conexion.enviarObjeto(new Finalizar());
		recibir.stop();
		recibir.parar();
		Conexion.finalizar();
	}

	public static void cambiarNombreCanal() {
		int canal = -1;
		int subCanal = -1;
		String position = frame.getSelectedNodePosition();
		position = position.substring(1);
		String[] numeros = position.split(":");
		canal = Integer.parseInt(numeros[0]);
		if (numeros.length > 1){
			subCanal = Integer.parseInt(numeros[1]);
		}
		
		Canal modCanal = canales.get(canal);
		if(subCanal>-1){
			modCanal = modCanal.getSubCanal(subCanal);
		}
		
		String nombre = (String) JOptionPane
				.showInputDialog(frame, "Que nombre quieres");
		
		CanalMod canalMod = new CanalMod(canal, subCanal, nombre, true);
		Conexion.enviarObjeto(canalMod);
	}

	public static void crearSubCanal() {
		int canal = -1;
		String position = frame.getSelectedNodePosition();
		
		if (!position.equals("")){
			position = position.substring(1);
			canal = Integer.parseInt(position);
		}
		String nombre = (String) JOptionPane
				.showInputDialog(frame, "Que nombre quieres");
		
		CanalMod canalMod = new CanalMod(canal, nombre);
		Conexion.enviarObjeto(canalMod);
	}
}
