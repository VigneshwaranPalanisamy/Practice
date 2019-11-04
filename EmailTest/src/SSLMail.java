import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SSLMail {
	public static void main(String[] args) {
		
		final String toEmail = "vignesh.palani@aspiresys.com"; // can be any email id 
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
		props.put("mail.smtp.port", "587"); //SMTP Port
		props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
		props.put("mail.smtp.starttls.enable", "true");
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				//return new PasswordAuthentication("vignesh.palani@aspiresys.com", "Letmein@00");
				return new PasswordAuthentication("joywithvicky@gmail.com","Vipal@3338");
			}
		};
		
		Session session = Session.getDefaultInstance(props, auth);
		System.out.println("Session created");
		try
	    {
	      MimeMessage msg = new MimeMessage(session);
	      
	      //msg.setFrom(new InternetAddress("vignesh.palani@aspiresys.com"));

	      msg.setSubject("Test Mail");

	      msg.setText("This is the messagge");

	      msg.setSentDate(new Date());

	      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
	      
    	  Transport.send(msg);  

	      System.out.println("EMail Sent Successfully!!");
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
}
}
