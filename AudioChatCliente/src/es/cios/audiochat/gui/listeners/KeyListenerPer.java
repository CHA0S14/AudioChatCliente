package es.cios.audiochat.gui.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import es.cios.audiochat.servicios.AudioChatService;
/**
 * 
 * @author Chaos
 *
 */
public class KeyListenerPer extends KeyAdapter {

	/**
	 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			AudioChatService.escribirMensaje();
		}
	}

}
