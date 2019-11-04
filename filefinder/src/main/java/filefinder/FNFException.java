package filefinder;

import java.io.File;
import java.io.FileNotFoundException;

public class FNFException {

	public static void main(String[] args) throws Exception {
		String path = "F:\\fileFinderTest";
		File fileSystem = new File(path);
		boolean value = false;
		String[] files = {"newFile.txt","newFile1.txt","newFile2.txt"};
		for(int i = 0 ; i< 3; i++) {
			try {
				File newFile = new File(path+"\\"+files[i]);
				System.out.println(newFile.exists());
			}catch(Exception e) {
				System.out.println(e.getMessage());
				throw new Exception();
			}
		}
	}

}
