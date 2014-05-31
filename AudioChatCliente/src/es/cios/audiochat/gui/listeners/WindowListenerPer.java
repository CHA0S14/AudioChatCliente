package es.cios.audiochat.gui.listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import es.cios.audiochat.servicios.AudioChatService;

public class WindowListenerPer extends WindowAdapter{

	@Override
	public void windowClosing(WindowEvent e) {
		AudioChatService.finalizar();		
	}

}
