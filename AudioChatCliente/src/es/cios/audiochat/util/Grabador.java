package es.cios.audiochat.util;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import es.cios.audiochat.exceptions.GrabadorException;

public class Grabador extends Thread {
	// formato de wave
	private static final AudioFileFormat.Type AUDIO_FILE_FORMAT = AudioFileFormat.Type.WAVE;
	private static final AudioFormat AUDIO_FORMAT = new AudioFormat(8000.0F,16, 1, true, false);

	private static TargetDataLine tD;

	private static File file;

	/**
	 * Inicia el hilo de grabación
	 */
	public Grabador() {
		start();
	}

	/**
	 * metodo que graba el audio y devuelve un file con ese audio
	 * 
	 * @return file que contiene el audio grabado
	 */
	public static void grabar() throws GrabadorException{
		try {
			file = File.createTempFile("GrabacionRecibida", ".wave");
			DataLine.Info dLI = new DataLine.Info(TargetDataLine.class,
					AUDIO_FORMAT);

			tD = (TargetDataLine) AudioSystem.getLine(dLI);

			new Grabador();			
				
		} catch (LineUnavailableException e) {
			throw new GrabadorException("Error en la grabacion: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new GrabadorException("Error en la grabacion: " + e.getMessage(), e);
		} 
		
	}

	public static File endGrabar() {
		tD.close();
		return file;
	}
	
	public void run() throws GrabadorException{
		try {
			tD.open(AUDIO_FORMAT);
			tD.start();
			AudioSystem.write(new AudioInputStream(tD), AUDIO_FILE_FORMAT, file);
		} catch (Exception e) {
			throw new GrabadorException("Error en la grabacion: " + e.getMessage(), e);
		}
	}
}
