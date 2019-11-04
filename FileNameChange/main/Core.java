import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.CaseUtils;

public class Core {

	static String DIR = "C:\\Users\\vignesh.palani\\Documents\\songs";
	static String DEST_DIR = "G:\\Songs\\";
	static String FORMAT = ".mp3";
	
	public static void main(String args[]) {
		try {
			applyChanges();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void applyChanges() throws IOException {
		File directory = new File(DIR);
		//File dest_directory = new File(DEST_DIR);
		if(directory.isDirectory()) {
			for(File file : directory.listFiles()) {
				StringBuilder name = new StringBuilder(file.getName());
				//System.out.println(file.getName() +" - ");
				int index = name.indexOf(".");
				//System.out.print(index+", "+name.length()+" - ");
				name = name.replace(index, name.length(),"");
				name = renameFile(name.toString());
				name = name.append(FORMAT);
				File destName = new File(DEST_DIR+name);
				//System.out.println(DEST_DIR+name);
				file.renameTo(destName);
				//System.out.println(DEST_DIR+name);
			}
		}
	}
	
	private static StringBuilder renameFile(String originalFileName) {
		originalFileName = removeUnwanted(originalFileName);
		List<String> exceptions = new ArrayList<>();
		exceptions.add("vmusiq");
		exceptions.add("masstamilan");
		exceptions.add("starmusiq");
		exceptions.add("tamildada");
		String[] words = originalFileName.split("\\s+");
		String shortName = "";
		for(int i = 0; i < words.length ; i++) {
			if(i<3) {
				if(!exceptions.contains(words[i].toLowerCase())) {
					String word = words[i];
					StringBuilder word1 = new StringBuilder();
					char[] letters = word.toLowerCase().toCharArray();
					for(int j = 0; j < letters.length; j++) {
						if(j ==0) {
							char ch1 = Character.toTitleCase(letters[j]);
							word1.append(ch1);
						} else {
							word1.append(letters[j]);
						}
					}
					shortName = shortName.concat(word1.toString());
					if(i<2) {
						shortName = shortName.concat(" ");
					}
				}
				
			}
		}
		originalFileName = shortName;
		//System.out.println(originalFileName);
		StringBuilder modifiedName = new StringBuilder(originalFileName);
		//System.out.println(modifiedName);
		return modifiedName;
	}
	
	private static String removeUnwanted(String str) {
		str = str.replace("-", " ");
		str = str.replace("_", " ");
		str = str.replace("(", " ");
		str = str.replace(")", " ");
		str = str.replace("[", " ");
		str = str.replace("]", " ");
		return str;
	}
}
