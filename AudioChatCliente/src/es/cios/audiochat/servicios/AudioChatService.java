package es.cios.audiochat.servicios;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import es.cios.audiochat.entities.Canal;
import es.cios.audiochat.entities.CanalMod;
import es.cios.audiochat.entities.Cliente;
import es.cios.audiochat.entities.Finalizar;
import es.cios.audiochat.entities.MensajeAudio;
import es.cios.audiochat.entities.Nombre;
import es.cios.audiochat.exceptions.ServiceException;
import es.cios.audiochat.gui.AudioDialog;
import es.cios.audiochat.gui.MainFrame;
import es.cios.audiochat.hilos.RecibirMensaje;
import es.cios.audiochat.util.Conexion;
 /**
  * 
  * @author Chaos
  *
  */
public class AudioChatService {
	private static List<Canal> canales = new ArrayList<Canal>();
	private static MainFrame frame;
	private static RecibirMensaje recibir;
	private static String name;
	private static File file;

	/**
	 * inicia el programa
	 */
	public static void inciar() {
		frame = new MainFrame();
		Conexion.conectar();
		recibir = new RecibirMensaje(Conexion.getSocket());
		recibir.start();
		cambiarNombre();
		frame.setVisible(true);
	}

	/**
	 * se encarga de mostrar un dialogo pidiendo el nombre y lo envia al servidor
	 */
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

	/**
	 * @return the name
	 */
	public static String getName() {
		return name;
	}

	/**
	 * @return the file
	 */
	public static File getFile() {
		return file;
	}

	/**
	 * @return the canales
	 */
	public static List<Canal> getCanales() {
		return canales;
	}

	/**
	 * @param file the file to set
	 */
	public static void setFile(File file) {
		AudioChatService.file = file;
	}

	/**
	 * se encarga de recojer el texto y enviarlo al servidor
	 */
	public static void escribirMensaje() {
		String text = frame.getMensaje();
		if (!text.equals("")) {
			String texto = name + ": " + text + "\n";
			Conexion.enviarObjeto(texto);
		}
	}

	/**
	 * recoje el fichero de audio y lo envia
	 * @throws ServiceException
	 */
	public static void enviarAudio() throws ServiceException {
		try {
			File f = getFile();
			//se tansforma en byte[] para enviar su contenido
			byte[] content = Files.readAllBytes(f.toPath());
			MensajeAudio mensaje = new MensajeAudio(getName(), content);

			Conexion.enviarObjeto(mensaje);
		} catch (IOException e) {
			throw new ServiceException("Error al enviar el audio: "
					+ e.getMessage(), e);
		}

	}

	/**
	 * @param canales the canales to set
	 */
	public static void setCanales(List<Canal> canales) {
		AudioChatService.canales = canales;
	}

	/**
	 * recibe el objeto que envia el servidor y dependiendo de que sea realiza una accion
	 * @param obj obejto recibido
	 */
	@SuppressWarnings("unchecked")
	public static void recibirObjeto(Object obj) {		
			if (obj instanceof ArrayList) {
				setCanales((ArrayList<Canal>) obj);
				frame.actualizarJTree();
			} else if (obj instanceof String) {
				frame.escribir((String) obj);
			} else if (obj instanceof MensajeAudio) {
				reproducirAudio(obj);
			}
	}

	/**
	 * crea un dialog con el boton para reproducir el audio recibido
	 * @param obj audio
	 */
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

	/**
	 * finaliza el programa
	 */
	@SuppressWarnings("deprecation")
	public static void finalizar() {
		Conexion.enviarObjeto(new Finalizar());
		recibir.stop();
		recibir.parar();
		Conexion.finalizar();
	}

	/**
	 * cambia el nombre del canal seleccionado
	 */
	public static void cambiarNombreCanal() {
		int canal = -1;
		int subCanal = -1;
		String position = frame.getSelectedNodePosition();
		position = position.substring(1);
		String[] numeros = position.split(":");
		canal = Integer.parseInt(numeros[0]);
		if (numeros.length > 1){
			int numClientes = canales.get(canal).getClientes().size();
			subCanal = Integer.parseInt(numeros[1]) -  numClientes;
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

	/**
	 * crea un subcanal en el canal seleccionado
	 */
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

	/**
	 * mueve al cliente al canal seleccionado
	 */
	public static void moverCanal() {
		int canal = -1;
		int subCanal = -1;
		String position = frame.getSelectedNodePosition();
		try{
			position = position.substring(1);
			String[] numeros = position.split(":");
			canal = Integer.parseInt(numeros[0]);
			if (numeros.length > 1){
				int numClientes = canales.get(canal).getClientes().size();
				subCanal = Integer.parseInt(numeros[1]) - numClientes;
			}
			
			Cliente cliente = new Cliente();
			cliente.setCanal(canal);
			cliente.setSubCanal(subCanal);
			Conexion.enviarObjeto(cliente);
		}catch(StringIndexOutOfBoundsException e){
			
		}
	}
}
