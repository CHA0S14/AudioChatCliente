package es.cios.audiochat.gui.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import es.cios.audiochat.servicios.AudioChatService;
import es.cios.audiochat.util.Grabador;

public class MouseListenerPer extends MouseAdapter{

	@Override
	public void mousePressed(MouseEvent e) {
		Grabador.grabar();		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		AudioChatService.setFile(Grabador.endGrabar());
		AudioChatService.enviarAudio();
	}
}
