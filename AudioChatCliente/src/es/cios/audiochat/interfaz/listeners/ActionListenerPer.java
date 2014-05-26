package es.cios.audiochat.interfaz.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import es.cios.audiochat.servicios.AudioChatService;

public class ActionListenerPer implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		
		switch (action) {
		case "enviar":
			AudioChatService.escribirMensaje();
			break;

		default:
			break;
		}
	}

}
