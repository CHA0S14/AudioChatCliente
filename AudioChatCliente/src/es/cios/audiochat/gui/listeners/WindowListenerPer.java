package es.cios.audiochat.gui.listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import es.cios.audiochat.servicios.AudioChatService;
/**
 * 
 * @author Chaos
 *
 */
public class WindowListenerPer extends WindowAdapter{

	/**
	 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		AudioChatService.finalizar();		
	}
}
