package com.tutorialspoint;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tutorialspoint.modal.DBConnection;

public class AudioDao {
	
static Connection connection = null;
	
	public AudioDao() {
		System.out.println("Inside UserDao constructor");
		try {
			connection =  DBConnection.getInstance().getConnection();
			//System.out.println(connection.getClientInfo());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		connection.close();
		super.finalize();
	}	
	
	
	public List<Audio> getAllFiles() {
		List<Audio> allFiles = new ArrayList<>();
		//allFiles.add(sendFile());
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from AudioManager");
			while(resultSet.next()){
				Audio audio = new Audio();
				audio.setId(resultSet.getInt(1));
				audio.setContent(resultSet.getString(2));
				allFiles.add(audio);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allFiles;
	}
	
	public Audio sendFile() {
		Audio audio = new Audio(1,sampleContent);
		return audio;
	}
	
	public boolean uploadFile(Audio audioFile) {
		//System.out.println("Id => "+audioFile.getId()+"\nContent -> \n:"+audioFile.getContent());
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("insert into AudioManager values("+audioFile.getId()+",CONVERT(varbinary(MAX),"+audioFile.getContent()+",1))");
			System.out.println("record inserted successfully...!!!!");
			statement.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean uploadFileInBytes(ByteAudio audioFile) {
		System.out.println("Id => "+audioFile.getId()+"\nContent -> \n:"+audioFile.getContent());
		/*Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("insert into AudioManager values("+audioFile.getId()+",CONVERT(varbinary(MAX),"+audioFile.getContent()+",1))");
			System.out.println("record inserted successfully...!!!!");
			statement.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;*/
		return true;
	}

	public Audio getFileByID(int id) {
		Audio audio = new Audio();
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from AudioManager where id="+id);
			while(resultSet.next()){
				audio.setId(resultSet.getInt(1));
				audio.setContent(resultSet.getString(2));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return audio;
	}

}