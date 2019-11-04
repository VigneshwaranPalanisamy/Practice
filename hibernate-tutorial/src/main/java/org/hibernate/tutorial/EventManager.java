package org.hibernate.tutorial;

import org.hibernate.Session;

import java.util.*;

import org.hibernate.tutorial.domain.Event;
import org.hibernate.tutorial.domain.Person;
import org.hibernate.tutorial.util.HibernateUtil;

public class EventManager {

    public static void main(String[] args) {
        EventManager mgr = new EventManager();

        if (args[0].equals("store")) {
            mgr.createAndStoreEvent("My Event", new Date());
        } else if (args[0].equals("list")) {
            List events = mgr.listEvents();
            for (int i = 0; i < events.size(); i++) {
                Event theEvent = (Event) events.get(i);
                System.out.println(
                        "Event: " + theEvent.getTitle() + " Time: " + theEvent.getDate()
                );
            }
        }  else if (args[0].equals("addpersontoevent")) {
            Long eventId = mgr.createAndStoreEvent("My BirthDay", new Date());
            Long personId = mgr.createAndStorePerson(24,"Clay", "Jenson");
            mgr.addPersonToEvent(personId, eventId);
            System.out.println("Added person " + personId + " to event " + eventId);
        } /*else if(args[0].equals("storePerson")) {
        	mgr.createAndStorePerson(24, "Clay", "Jenson");
        } else if (args[0].equals("listAll")) {
            @SuppressWarnings("rawtypes")
			List events = mgr.listPersonEvents();
            for (int i = 0; i < events.size(); i++) {
            	Person theEvent = (Person) events.get(i);
                Set<?> personEvents = theEvent.getEvents();
                Iterator value = personEvents.iterator(); 
                while(value.hasNext()) {
                	System.out.println(
                            "PersonId: " + value.next()
                    );
                }
                
            }
        }*/

        HibernateUtil.getSessionFactory().close();
    }

    private Long createAndStoreEvent(String title, Date theDate) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Event theEvent = new Event();
        theEvent.setTitle(title);
        theEvent.setDate(theDate);
        session.save(theEvent);

        session.getTransaction().commit();
        return theEvent.getId();
    }
    
    private Long createAndStorePerson(int age,String firstName, String lastName) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Person person = new Person();
        person.setAge(age);
        person.setFirstname(firstName);
        person.setLastname(lastName);
        session.save(person);
        session.getTransaction().commit();
        return person.getId();
    }
    
    private List listEvents() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List result = session.createQuery("from Event").list();
        session.getTransaction().commit();
        return result;
    }
    
    private List listPersonEvents() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction(); 
        List result = session.createQuery("from Person").list();
        session.getTransaction().commit();
        return result;
    }
    
    private void addPersonToEvent(Long personId, Long eventId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Person aPerson = (Person) session
                .createQuery("select p from Person p left join fetch p.events where p.id = :pid")
                .setParameter("pid", personId)
                .uniqueResult(); // Eager fetch the collection so we can use it detached
        Event anEvent = (Event) session.load(Event.class, eventId);

        session.getTransaction().commit();

        // End of first unit of work

        aPerson.getEvents().add(anEvent); // aPerson (and its collection) is detached

        // Begin second unit of work

        Session session2 = HibernateUtil.getSessionFactory().getCurrentSession();
        session2.beginTransaction();
        session2.update(aPerson); // Reattachment of aPerson

        session2.getTransaction().commit();
    }

}