package es.cios.audiochat.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import es.cios.audiochat.servicios.AudioChatService;
import es.cios.audiochat.util.Grabador;
/**
 * 
 * @author Chaos
 *
 */
public class ActionListenerPer implements ActionListener{

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		JButton buton;
		switch (action) {
		case "enviar":
			AudioChatService.escribirMensaje();
			break;	
		case "cambiarNombre":
			AudioChatService.cambiarNombre();
			break;
		case "cambiarNombreCanal":
			AudioChatService.cambiarNombreCanal();
			break;
		case "crearSubCanal":
			AudioChatService.crearSubCanal();
			break;
		case "grabar":
			buton = (JButton) e.getSource();
			buton.setText("Parar");
			buton.setActionCommand("parar");
			Grabador.grabar();
			break;
		case "parar":
			buton = (JButton) e.getSource();
			buton.setText("Grabar");
			buton.setActionCommand("grabar");
			AudioChatService.setFile(Grabador.endGrabar());
			AudioChatService.enviarAudio();
			break;
		default:
			break;
		}
	}

}
