package filefinder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FileFinder extends TimerTask{
	
	private final static Logger logger = Logger.getLogger(FileFinder.class.getName());
	
	private static Handler logHandler;
	
	private String path;

	private String fileName;
	
	// variable holds the value of no of repetitions to be made for file search task
	private int repetitionCount = 3;
	
	// constructor which receives file path and file name for search operation
	FileFinder(String path, String fileName) {
		this.path = path;
		this.fileName = fileName;
	}
	
	// initialize the logger handlers with configurations
	static void init() {
		try {
			logHandler = new FileHandler("C:\\Users\\vignesh.palani\\Desktop\\mylogs.log", false);
			logHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(logHandler);
			logger.setLevel(Level.CONFIG);
		} catch (SecurityException e) {
			// adding console type handler if log file is not found.
			logHandler = new ConsoleHandler();
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// method to check if given filename is exists in the given path
	boolean isExists() {
		File fileSystem = new File(path,fileName);
		return fileSystem.exists();
	}

	public static void main(String[] args) {
		init();
		FileFinder fileFinder = new FileFinder("F:\\fileFinderTest","newFile3.txt");
		logger.info("Inside Main method");
		Timer timer = new Timer();
		//param 1 - instance of class which implements the run method of TimerTask - FileFinder in this case.
		//param 2 - delay in milliseconds before the task to be executed - No delay in this case.
		//param 3 - waiting time in milliseconds for each repetition of task - 3 minutes (180000 ms) in this case.
		timer.schedule(fileFinder,0,180000);
		
		logger.info("-------------Started running scheduled task-------------");
	}
	
	
	public void run() {
		if(repetitionCount > 0) {
			logger.info("********  Running scheduled task for round-"+ (4- repetitionCount));
			if(isExists()) {
				logger.info("\\\\\\\\\\\\\\  File Found At The Specified Location. Running Copy Actions.  //////////////");
				try {
					File fileSystem = new File(path,fileName);
					String sourceFileName = fileSystem.getName();
					String destinationFolderName = "F:\\FileMoveTest";
					File targetLocation = new File(destinationFolderName);
					// check file / folder exists with given name
					if(targetLocation.exists()) {
						// Verify the specified location is a folder or not
						if(targetLocation.isDirectory()) {
							// creating a new file instance with given destination path and found file name
							targetLocation = new File("F:\\FileMoveTest",sourceFileName);
							// verify any file already exists with same name
							if(!targetLocation.exists()) {
								// copy file from source path to destination folder path.
								Files.copy(fileSystem.toPath(),targetLocation.toPath());
								logger.info("-------------End Of File Search & File Moved To The Specified Location-------------");
							} else {
								logger.info("A file with given name already exists..");
							}
						} else {
							logger.info("No Directory found at the specified location..");
						}
					} else {
						logger.info("Specified Folder Not Exists..");
					}
				} catch (IOException e) {
					logger.log(Level.ALL,e.getMessage());
					e.printStackTrace();
				}
				System.exit(0);
			} else {
				repetitionCount--;
				logger.info("File Not Found At The Specified Location...");
			}
		} else {
			logger.info("-------------End Of File Search & File Not Found At The Specified Location-------------");
			System.exit(0);
		}
		
	}
}


