package main.com.aspire.onboarding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;

public class OnboardingUtils {
	public String copyFile(String fullFileName, String destinationPath, String desNameWithoutExt) throws OnboardingException {
		String fileName = "";
		
		if(fullFileName != null && "".equals(fullFileName) == false) {
			File sourceFile = new File(fullFileName);
			if(sourceFile.exists()) {
				String extension = fullFileName.substring(fullFileName.lastIndexOf(".")+1);
				File desDir = new File(destinationPath);
				if(desDir.exists() && desDir.isDirectory()) {
					String desFileName = destinationPath + "/" + desNameWithoutExt + "." + extension;
					File desFile = new File(desFileName);
					try {
						desFile.createNewFile();
						fileName = desNameWithoutExt + "." + extension;
						this.copyFileContent(sourceFile, desFile);
					} catch (IOException e) {
						throw new OnboardingException("Error : Unable to create destination file - "+desFileName);
					}
				}else {
					throw new OnboardingException("Error : Destination dir is either not exist or not a directory - "+destinationPath);
				}
			}else {
				throw new OnboardingException("Error : Source File is not exist - "+fullFileName);
			}
		}else {
			throw new OnboardingException("Error : Source File full name is empty.");
		}
		
		return fileName;
	}

	public String copyBase64(String base64Content, String destinationPath, String desNameWithoutExt, String extension) throws OnboardingException {
		String fileName = desNameWithoutExt + "." + extension;
		String desFileName = destinationPath + "/" + fileName;
		if(destinationPath != null && "".equals(destinationPath) == false 
				&& desNameWithoutExt != null && "".equals(desNameWithoutExt) == false 
				&& extension != null && "".equals(extension) == false) {
			File desDir = new File(destinationPath);
			if(desDir.exists() && desDir.isDirectory()) {
				File desFile = new File(desFileName);
				try {
					desFile.createNewFile();
					this.convertBase64ToFile(base64Content, desFile);
				} catch (IOException e) {
					throw new OnboardingException("Error : Unable to create destination file - "+desFileName);
				}
			}else {
				throw new OnboardingException("Error : Destination dir is either not exist or not a directory - "+destinationPath);
			}
		}else {
			throw new OnboardingException("Error : Invalid file - "+desFileName);
		}
		
		
		return fileName;
	}
	
	public String copyByteArray(byte[] btDataFile, String destinationPath, String desNameWithoutExt, String extension) throws OnboardingException {
		String fileName = desNameWithoutExt + "." + extension;
		String desFileName = destinationPath + "/" + fileName;
		if(destinationPath != null && "".equals(destinationPath) == false 
				&& desNameWithoutExt != null && "".equals(desNameWithoutExt) == false 
				&& extension != null && "".equals(extension) == false) {
			File desDir = new File(destinationPath);
			if(desDir.exists() && desDir.isDirectory()) {
				File desFile = new File(desFileName);
				try {
					desFile.createNewFile();
					this.convertByteArrayFile(btDataFile, desFile);
				} catch (IOException e) {
					throw new OnboardingException("Error : Unable to create destination file - "+desFileName);
				}
			}else {
				throw new OnboardingException("Error : Destination dir is either not exist or not a directory - "+destinationPath);
			}
		}else {
			throw new OnboardingException("Error : Invalid file - "+desFileName);
		}
		
		return fileName;
	}
	
	private void copyFileContent(File source, File dest) throws IOException{
		InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}
	
	private void convertBase64ToFile(String base64String, File desFile) throws OnboardingException {
		if (base64String != null && "".equals(base64String) == false) {
			byte[] btDataFile = Base64.decodeBase64(base64String);
			this.convertByteArrayFile(btDataFile, desFile);
		}else {
			throw new OnboardingException("Error - empty base64 content.");
		}
	}
	
	private void convertByteArrayFile(byte[] btDataFile, File desFile) throws OnboardingException {
		if (btDataFile != null) {
			try {
				FileOutputStream osf = new FileOutputStream(desFile);
				osf.write(btDataFile);
				osf.flush();
				osf.close();
			} catch (Exception e) {
				throw new OnboardingException(e.getMessage());
			}
		}else {
			throw new OnboardingException("Error - empty base64 content.");
		}
	}
	
//	public static void main(String[] args) {
//		String s = new OnboardingUtils().moveFile(".\\resource\\Idology\\Completed.jpg", "E:\\Srinivasan Raghavan\\Projects\\Tejas-USMB\\trunk\\code\\POC\\3rd party integration\\ThirdPartyIntegrations\\resource\\Idology", "Test_Completed");
//		System.out.println(s);
//	}

}
