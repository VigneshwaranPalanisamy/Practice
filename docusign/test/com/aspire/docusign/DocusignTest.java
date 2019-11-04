package test.com.aspire.docusign;

import main.com.aspire.docusign.EmbeddedSigning;
import main.com.aspire.onboarding.OnboardingException;

public class DocusignTest {
	
	private static String accountId = null;
	private static EmbeddedSigning embeddedSigning = null;
	private static String envelopeId = null;
	
	static {
		System.setProperty("https.proxyHost", "eproxy.aspiresys.com");
		System.setProperty("https.proxyPort", "3128");
		if(embeddedSigning == null) {
			embeddedSigning = new EmbeddedSigning();
		}
	}
	
	private static void auth() throws OnboardingException {
		accountId = embeddedSigning.auth(Constants.BaseUrl, Constants.UserName, Constants.Password,
				Constants.IntegratorKey, Constants.DefaultHeader, Constants.ApiPassword, Constants.AccountIdGuid);
		System.out.println("Account id : ---" +accountId + "--");
	}
	
	private static void envelopCreation() throws OnboardingException {
		envelopeId = embeddedSigning.getEnvelopIdForSingleDoc(accountId, Constants.TemplateID, Constants.Role, "Clay", "vignesh.palani@aspiresys.com", 
				"101", Constants.EnvelopeStatus,Constants.FirstName, Constants.MiddleName, Constants.LastName, Constants.SIN_Number, Constants.DOB,
				Constants.Address, Constants.EmailAddress, Constants.MobileNumber, Constants.CustomerID, Constants.SavingsAccountNumber,
				Constants.ShareAccountNumber, Constants.TermDepositAccountNumber, Constants.TermLengthAndRate, Constants.CurrentDate,Constants.CASL,Constants.USResident,
				Constants.TaxCountry,Constants.PEP);
		System.out.println("Envelope id : ---" +envelopeId + "--");
	}
	
	private static void getURL() throws OnboardingException {
		String url = embeddedSigning.getSingleDocuSignURL(accountId, "Clay", "vignesh.palani@aspiresys.com", "101", Constants.ReturnUrl, Constants.Email, 
				envelopeId);
		System.out.println("URL : ---" + url + "--");
	}
	
	public static void main(String args[]) {
		try {
			auth();
			envelopCreation();
			getURL();
		} catch (OnboardingException e) {
			e.printStackTrace();
		}
	}
	
}
