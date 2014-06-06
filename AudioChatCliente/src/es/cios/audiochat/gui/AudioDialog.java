package es.cios.audiochat.gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import es.cios.audiochat.util.Reproductor;
/**
 * 
 * @author Chaos
 *
 */
@SuppressWarnings("serial")
public class AudioDialog extends JDialog {
	File file;

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * contructor y ademas le da los elementos al modal
	 * @param owner frame al cual pertenece
	 * @param modal si deja utilizar el frame o no
	 */
	public AudioDialog(Frame owner, boolean modal) {
		super(owner, modal);
		
		setBounds(100, 100, 400, 151);
		getContentPane().setLayout(null);
		
		JLabel lblMensaje = new JLabel("Has recibido un mensaje de audio que desea hacer:");
		lblMensaje.setBounds(42, 23, 299, 14);
		getContentPane().add(lblMensaje);
		
		JButton btnReproducir = new JButton("Reproducir");
		btnReproducir.setBounds(42, 48, 104, 23);
		getContentPane().add(btnReproducir);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(247, 48, 94, 23);
		getContentPane().add(btnCancelar);
		btnCancelar.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnReproducir.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Reproductor.startPlay(getFile());
				dispose();
			}
		});
	}
}
