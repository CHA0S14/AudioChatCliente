package es.cios.audiochat.util;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import es.cios.audiochat.exceptions.ReproductorException;

@SuppressWarnings("serial")
public class Reproductor extends java.applet.Applet {

	/**
	 * inicia la reproducción
	 * @param file fichero a reproducir
	 */
	public static void startPlay(File file) throws ReproductorException{
		try {
			AudioInputStream in = AudioSystem.getAudioInputStream(file);
			//se crea el formato de audio
			AudioFormat baseFormat = in.getFormat();
			//se llama al método para reproducir
			rawplay(baseFormat, in);
			
			in.close();
		} catch (IOException e) {
			throw new ReproductorException("Error de reproduccion: " + e.getMessage(), e);
		} catch (LineUnavailableException e) {
			throw new ReproductorException("Error de reproduccion: " + e.getMessage(), e);
		} catch (UnsupportedAudioFileException e) {
			throw new ReproductorException("Error de reproduccion: " + e.getMessage(), e);
		}
	}

	/**
	 * reproduce el archivo
	 * @param targetFormat formato que tiene el archivo
	 * @param din el input stream que contiene el audio
	 * @throws IOException error si el fichero no existe
	 * @throws LineUnavailableException error de línea invalida
	 */
	private static void rawplay(AudioFormat targetFormat, AudioInputStream din)
			throws IOException, LineUnavailableException {
		byte[] data = new byte[4096];
		SourceDataLine line = getLine(targetFormat);
		if (line != null) {
			// Empieza a leer el archivo
			line.start();
			int nBytesRead = 0;
			while (nBytesRead != -1) {				
				line.write(data, 0, nBytesRead);
				nBytesRead = din.read(data, 0, data.length);
			}
			//para todo
			line.drain();
			line.stop();
			line.close();
			din.close();
		}
	}

	/**
	 * obtiene las "lineas" del fichero de audio y reproduce
	 * @param audioFormat formato del audio
	 * @return SourceDataLine
	 * @throws LineUnavailableException
	 */
	private static SourceDataLine getLine(AudioFormat audioFormat)
			throws LineUnavailableException {
		SourceDataLine res = null;
		//deformatea el audio para obtener el sonido y reproduce
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);		
		return res;
	}
}