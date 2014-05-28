package es.cios.audiochat.gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import es.cios.audiochat.util.Reproductor;

@SuppressWarnings("serial")
public class AudioDialog extends JDialog {
	File file;
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public AudioDialog(Frame owner, boolean modal) {
		super(owner, modal);
	}

	/**
	 * Create the dialog.
	 */
	public AudioDialog() {
		setBounds(100, 100, 450, 204);
		getContentPane().setLayout(null);
		
		JLabel lblMensaje = new JLabel("Has recibido un mensaje de audio que desea hacer:");
		lblMensaje.setBounds(97, 39, 254, 25);
		getContentPane().add(lblMensaje);
		
		JButton btnReproducir = new JButton("Reproducir");
		btnReproducir.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Reproductor.startPlay(getFile());
				dispose();
			}
		});
		btnReproducir.setBounds(80, 105, 89, 23);
		getContentPane().add(btnReproducir);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancelar.setBounds(279, 105, 89, 23);
		getContentPane().add(btnCancelar);
	}
}
