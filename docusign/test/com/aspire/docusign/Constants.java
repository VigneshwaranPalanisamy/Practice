package test.com.aspire.docusign;

public class Constants {
	
	public static final String BaseUrl = "https://demo.docusign.net/restapi";
	
	// TODO: Enter your DocuSign credentials
	
	public static final String EnvelopeStatus = "sent";
	public static final String ReturnUrl = "https://www.docusign.com";
	public static final String Email = "email";
	public static final String DefaultHeader = "X-DocuSign-Authentication";
	public static final String ApiPassword = "true";
	public static final String AccountIdGuid = "true";
	
	//johan's credentials
	//public static final String TemplateID = "53d33809-b4af-4236-b7fc-60413582c718";
	public static final String SignerName = "Johan";
	public static final String SignerEmail = "johan.ravanan@aspiresys.com";
	public static final String TemplateID = "53d33809-b4af-4236-b7fc-60413582c718";
	public static final String Role = "NEWUSER";
	public static final String IntegratorKey = "4e95c605-5b9f-4d37-8b5f-6237110fd0e2";
	public static final String UserName = "johan.ravanan@aspiresys.com";
	public static final String Password = "johan123";
	
	
	// vignesh's credentials
	/*public static final String UserName = "vignesh.palani@aspiresys.com";
	public static final String Password = "vipal@3338";
	public static final String IntegratorKey = "25a3be02-f91e-4e07-b0bd-05b693342b5a";
	public static final String TemplateID = "11ab84d2-c617-40d9-95c1-537cbdf24629";
	public static final String Role = "Customer";*/
	
	//Sample User Data to create envelope
	public static final String FirstName = "Clay";
	public static final String MiddleName = "";
	public static final String LastName = "Jenson";
	public static final String SIN_Number = "999999998";
	public static final String DOB = "01/01/1999";
	public static final String Address = "1010, EASY ST, OTTAWA, ONTARIO, K1A 0B1";
	public static final String EmailAddress = "abc@test.com";
	public static final String MobileNumber = "9080908090";
	public static final String CustomerID = "58585";
	public static final String SavingsAccountNumber = "123456789";
	public static final String TermDepositAccountNumber = "123456789";
	public static final String TermLengthAndRate = "3M (0.7000)";
	public static final String ShareAccountNumber = "123456789";
	public static final String CurrentDate = "18/02/2019";
	public static final String CASL = "Yes";
	public static final String USResident = "Yes";
	public static final String TaxCountry = "";
	public static final String PEP = "No";
	// TODO: Enter your Integrator Key (aka API key), created through your
	// developer sandbox preferences
	// for production environment update to "www.docusign.net/restapi"
	// public static final String BaseUrl = "https://demo.docusign.net/restapi";
	
	public String DocumentName = "Test.pdf";
	public String DocumentId = "123";
	public String TOTAL_NO_OF_PAGES = "1";
	public String PATH = System.getProperty("user.dir") + "/TEST.PDF";
	
}
