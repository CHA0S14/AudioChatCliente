package es.cios.audiochat.gui.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import es.cios.audiochat.servicios.AudioChatService;

public class KeyListenerPer extends KeyAdapter {

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			AudioChatService.escribirMensaje();
		}
	}

}
