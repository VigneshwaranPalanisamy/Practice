package com.sample.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

public class AudioManager {

	static final String fileLocation = "G:\\MP3_Output";

	public static void main(String[] args) {
		saveAudioFile();

	}

	static void saveAudioFile() {

		//String byteContent = "0x433A5C55736572735C73656C766170726979612E76656C7573616D795C446F776E6C6F6164735C746573745F617564696F2E6D7033";
		//String byteContent = "0x473A5C536F6E67735C426F6F6D202E6D7033";
		//G:\MP3_Output1.enc
		try {
			/*FileInputStream fis = new FileInputStream("G:\\Songs\\Boom.mp3");
			Base64.Encoder enc1 = Base64.getEncoder();
			OutputStream os1 = enc1.wrap(new FileOutputStream(fileLocation+"1.enc"));
			int _byte;
			while ((_byte = fis.read()) != -1){
				os1.write(_byte);
			}
			os1.close();
			fis.close();
			System.out.println("Completed");*/
			FileOutputStream fos1 = new FileOutputStream("G:\\\\MP3_Output\\Boom1.mp3");
			Base64.Decoder dec1 = Base64.getDecoder();
	        InputStream is1 = dec1.wrap(new FileInputStream("G:\\MP3_Output1.enc"));
	        int _byte1;
	         while ((_byte1 = is1.read()) != -1)
	            fos1.write(_byte1);
	         is1.close();
	         fos1.close();
	         System.out.println("Completed");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//File outputFile = new File(fileLocation+"\\Audio1.mp3");
		/*FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outputFile);
			fos.write(bytes);

		finally {
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/

	}

}
