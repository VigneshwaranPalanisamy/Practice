package com.tutorialspoint;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tutorialspoint.modal.DBConnection;

public class UserDao {
	
	static Connection connection = null;
	
	public UserDao() {
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
	
	public List<User> getAllUsers(){
		List<User> userList = new ArrayList<>();
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from Users");
			while(resultSet.next()){
				
				User user = new User();
				user.setId(resultSet.getInt(1));
				user.setName(resultSet.getString(2));
				user.setProfession(resultSet.getString(3));
				userList.add(user);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}

	public User getUser(int id){
		User user = new User();
		try {
			Statement statement;
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from Users where id ="+id);
			while(resultSet.next()){
				user.setId(resultSet.getInt(1));
				user.setName(resultSet.getString(2));
				user.setProfession(resultSet.getString(3));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public int addUser(User pUser){
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("insert into Users values("+pUser.getId()+",'"+pUser.getName()+"','"+pUser.getProfession()+"')");
			System.out.println("record inserted successfully...!!!!");
			statement.close();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int updateUser(User pUser){
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("update Users set id="+pUser.getId()+", name='"+pUser.getName()+"',profession='"+pUser.getProfession()+"' where id="+pUser.getId());
			System.out.println("record updated successfully...!!!!");
			statement.close();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int deleteUser(int id){
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("delete from Users where id="+id);
			System.out.println("record deleted successfully...!!!!");
			statement.close();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
