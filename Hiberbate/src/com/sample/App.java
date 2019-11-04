package com.sample;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.sample.entity.Users;

public class App {

	public static void main(String[] args) {
		
		SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Users.class).buildSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			//insertUser(session);
			getUser(session);
			session.getTransaction().commit();
		} catch(Exception e) {
			System.out.println(e.getCause());
		}
		finally {
			session.close();
			sessionFactory.close();
		}
	}

	private static void insertUser(Session session) {
		Users user = new Users("Vicky","VickyP", "vickyE", "VickyFN","VickyLN");
		session.save(user);
	}
	
	private static void getUser(Session session) {
		Users user = new Users();
		user = session.get(Users.class,1);
		System.out.println(user.toString());
	}

}
