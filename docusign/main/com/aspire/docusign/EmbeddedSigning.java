package main.com.aspire.docusign;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.docusign.esign.api.AuthenticationApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.Configuration;
import com.docusign.esign.model.Envelope;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.LoginAccount;
import com.docusign.esign.model.LoginInformation;
import com.docusign.esign.model.RecipientViewRequest;
import com.docusign.esign.model.Tabs;
import com.docusign.esign.model.TemplateRole;
import com.docusign.esign.model.Text;
import com.docusign.esign.model.ViewUrl;

import main.com.aspire.onboarding.OnboardingException;
import main.com.aspire.onboarding.OnboardingUtils;

/*****************************************************************************************************************
 * EmbeddedSigning()
 * 
 * This recipe demonstrates how to open the Embedded Signing view of a given
 * envelope (AKA the Recipient View). The Recipient View can be used to sign
 * document(s) directly through your UI without having to context-switch and
 * sign through the DocuSign Website. This is done by opening the Recipient
 * View in an iFrame for web applications or a webview for mobile apps.
 ******************************************************************************************************************/
public class EmbeddedSigning {

	public EmbeddedSigning(){
//		System.setProperty("https.proxyHost", "eproxy.aspiresys.com");
//		System.setProperty("https.proxyPort", "3128");
//		this.configSystemProxy();
	}
	
	public String auth(String baseURL, String userName, String password, String integratorKey, String defaultHeader, String apiPassword, String accountIdGuid) throws OnboardingException {
			String accountId = null;
			// initialize the api client
			ApiClient apiClient = new ApiClient();
			apiClient.setBasePath(baseURL);
			// create JSON formatted auth header
			String creds = "{\"Username\":\"" + userName + "\",\"Password\":\"" + password + "\",\"IntegratorKey\":\"" + integratorKey + "\"}";
			apiClient.addDefaultHeader(defaultHeader, creds);
			// assign api client to the Configuration object
			Configuration.setDefaultApiClient(apiClient);
			
			try {
				// login call available off the AuthenticationApi
				AuthenticationApi authApi = new AuthenticationApi();
	
				// login has some optional parameters we can set
				AuthenticationApi.LoginOptions loginOps = authApi.new LoginOptions();
				loginOps.setApiPassword(apiPassword);
				loginOps.setIncludeAccountIdGuid(accountIdGuid);
				LoginInformation loginInfo = authApi.login(loginOps);
				
				// note that a given user may be a member of multiple accounts
				List<LoginAccount> loginAccounts = loginInfo.getLoginAccounts();
				accountId = loginAccounts.get(0).getAccountId();
				
				printLoginAccountDetail(loginAccounts);
			} catch (com.docusign.esign.client.ApiException exception) {
				printExceptionErrorMsg(exception);
				throw new OnboardingException(exception.getMessage());
			}
			return accountId;
	}
	
	public String getEnvelopIdForSingleDoc(String accountId, String templateId, String templateRole, String signerName, 
			String signerEmail, String clientUserId, String envelopeStatus, String firstName, String middleName, String lastName,
			String sinNumber, String dateOfBirth, String address, String email, String mobileNumber,
			String customerID, String savingsAccountNumber, String shareAccountNumber,String termDepositAccountNumber,
			String termLengthAndRate, String currentDate, String CASL, String USResident, String taxCountry,String PEP) throws OnboardingException {
		
		// Set input values to custom fields
		Map<String,String> customInputFieldsList = createCustomFieldMap(firstName, middleName, lastName,
				sinNumber, dateOfBirth, address, email, mobileNumber, customerID, savingsAccountNumber, 
				shareAccountNumber, termDepositAccountNumber, termLengthAndRate, currentDate, CASL, USResident, taxCountry, PEP);
		
		String envelopId = createEnvelopId(accountId, templateId, templateRole, signerEmail, signerName, clientUserId, envelopeStatus, customInputFieldsList);
		
		return envelopId;
	}
	
	public String getSingleDocuSignURL(String accountId, String signerName, String signerEmail, String clientUserId, String returnUrl, 
			String email, String envelopeId) throws OnboardingException {
			
		StringBuffer envelopeIds = new StringBuffer();
		envelopeIds.append(envelopeId);
		ViewUrl recipientView = createRecipientViewAPI(signerName, signerEmail, accountId, envelopeIds, clientUserId, returnUrl, email);
		
		return recipientView.getUrl();
	}
	
	public byte[] getDocumentSign(String accountId, String envelopeId, String documentId) throws OnboardingException{
		byte[] document = null;
		try {
			EnvelopesApi envelopesApi = new EnvelopesApi();

			// call the getDocument() API
			document = envelopesApi.getDocument(accountId, envelopeId, documentId);
					//.getDocument(accountId, envelopeId, documentId);
		//System.out.println("\nDocument details@@@@@@@@@ :\n  " + document + "\n #######\n");
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
			throw new OnboardingException(ex.getMessage());
		}
		return document;
	}
	
	public String isDocumentSigned(String accountId, String envelopeId) throws OnboardingException {
		String documentSigned = "Y";
		
		String envCompletedDate = this.getEnvelopeInformation(accountId, envelopeId);
		if(envCompletedDate == null) {
			documentSigned = "N";
		}
		
		return documentSigned;
	}
	
	public String getEnvelopeInformation(String accountId, String envelopeId) throws OnboardingException {
		String envCompletedDate = null;
		
		try {
				// instantiate a new EnvelopesApi object
			EnvelopesApi envelopesApi = new EnvelopesApi();

			// call the getEnvelope() API
			Envelope env = envelopesApi.getEnvelope(accountId, envelopeId);
			envCompletedDate = env.getCompletedDateTime();
			System.out.println("Envelope completed date: " + envCompletedDate);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
			throw new OnboardingException(ex.getMessage());
		}
		
		return envCompletedDate;
	}
	
	public String downloadEnvDocument(String accountId, String envelopeId, String documentId, 
			String dir, String desFileName) throws OnboardingException {
		byte[] document = this.getDocumentSign(accountId, envelopeId, documentId);
		return new OnboardingUtils().copyByteArray(document, dir, desFileName, "pdf");
	}
	
	public List<Text> populateUserDataForSigning(Map<String,String> customFields){
		
		List<Text> textList = new ArrayList<>();
		for( Entry<String, String> field : customFields.entrySet()) {
			textList.add(createTextTabs(field.getKey(),field.getValue()));
		}
		return textList;
	}
	
	public Map<String,String> createCustomFieldMap(String firstName, String middleName, String lastName,
			String sinNumber, String dateOfBirth, String address, String email, String mobileNumber,
			String customerID, String savingsAccountNumber, String shareAccountNumber, String termDepositAccountNumber, 
			String termLengthAndRate, String currentDate, String CASL, String USResident, String taxCountry,String PEP) {
				
		Map<String,String> customInputMap = new HashMap<>();
		customInputMap.put("FirstName",firstName);
		customInputMap.put("Date",currentDate);
		customInputMap.put("MiddleName",middleName);
		customInputMap.put("LastName",lastName);
		customInputMap.put("SIN",sinNumber);
		customInputMap.put("DOB",dateOfBirth);
		customInputMap.put("Address",address);
		customInputMap.put("Email",email);
		customInputMap.put("MobilePhone",mobileNumber);
		customInputMap.put("CIF",customerID);
		customInputMap.put("SavingAccountNumber",savingsAccountNumber);
		customInputMap.put("ShareAccountNumber",shareAccountNumber);
		customInputMap.put("Date",currentDate);
		customInputMap.put("Casl Consent",CASL);
		customInputMap.put("US Resident",USResident);
		customInputMap.put("Tax Country",taxCountry);
		customInputMap.put("PEP",PEP);
		if(termDepositAccountNumber != null && (!termDepositAccountNumber.isEmpty())) {
			customInputMap.put("TermDepositFieldLabelText","Term Deposit Account");
			customInputMap.put("TermDepositAccountNumber",termDepositAccountNumber);
		}
		if(termLengthAndRate != null && (!termLengthAndRate.isEmpty())) {
			customInputMap.put("TermLengthAndRateLabelText","Term Length and Rate:");
			customInputMap.put("TermLengthAndRate",termLengthAndRate);
		}
		return customInputMap;

	}
	
	public Text createTextTabs(String fieldName, String fieldValue){
		Text textField = null;
		try {
			textField = new Text();
			textField.setTabLabel(fieldName);
			textField.setValue(fieldValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return textField;		
	}
	
	private String createEnvelopId(String accountId, String templateId, String templateRole, String signerEmail, String signerName,
			 String clientUserId, String envelopeStatus, Map<String,String> customFieldsList) throws OnboardingException{
		
		String envelopId = null;
		EnvelopeDefinition envDef = new EnvelopeDefinition();

		
        Tabs tabs= new Tabs();
        tabs.setTextTabs(populateUserDataForSigning(customFieldsList));
        
        // create a template role with a valid templateId and roleName and assign signer info
        TemplateRole tRole = new TemplateRole();
        tRole.setRoleName(templateRole);
        tRole.setName(signerName);
        tRole.setEmail(signerEmail);
        tRole.setClientUserId(clientUserId);
        tRole.setTabs(tabs);
      
        // create a list of template roles and add our newly created role
        List<TemplateRole> templateRolesList = new ArrayList<TemplateRole>();
        templateRolesList.add(tRole);
      
        // assign template role(s) to the envelope 
        envDef.setTemplateRoles(templateRolesList);
        
        //set templateId to the envelope  
        envDef.setTemplateId(templateId);
        
        // send the envelope by setting |status| to "sent". To save as a draft set to "created"
        //envDef.setStatus("sent");
		envDef.setStatus(envelopeStatus);
		
		try {
			EnvelopesApi envelopesApi = new EnvelopesApi();
			EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envDef);
			envelopId = envelopeSummary.getEnvelopeId();
			printEnvelopeSummary( envelopeSummary);
		} catch (com.docusign.esign.client.ApiException exception) {
			printExceptionErrorMsg(exception);
			throw new OnboardingException(exception.getMessage());
		}
		
		return envelopId;
	}
	
	private RecipientViewRequest setRecipientViewRequestURL(String signerName, String signerEmail,  String clientUserId, String returnUrl, String email) {
		
		RecipientViewRequest returnUrlCustomized = new RecipientViewRequest();
		returnUrlCustomized.setReturnUrl(returnUrl);
		returnUrlCustomized.setAuthenticationMethod(email);

		// recipient information must match embedded recipient info we
		// provided in step #2
		returnUrlCustomized.setEmail(signerEmail);
		returnUrlCustomized.setUserName(signerName);
		returnUrlCustomized.setClientUserId(clientUserId);
		return returnUrlCustomized;
	}
	
	
	private ViewUrl createRecipientViewAPI(String signerName, String signerEmail, String accountId, StringBuffer envelopId, String clientUserId, String returnUrl, String email) throws OnboardingException {
		ViewUrl recipientView = new ViewUrl();
		try {
			// instantiate a new EnvelopesApi object
			EnvelopesApi envelopesApi = new EnvelopesApi();
			RecipientViewRequest returnUrlCustomized = setRecipientViewRequestURL(signerName, signerEmail, clientUserId, returnUrl, email);	
			recipientView = envelopesApi.createRecipientView(accountId, envelopId.toString(), returnUrlCustomized);
			
			printViewUrl(recipientView);
		} catch (com.docusign.esign.client.ApiException exception) {
			printExceptionErrorMsg(exception);
			throw new OnboardingException(exception.getMessage());
		}	
		return recipientView;
	}
	
	private void printExceptionErrorMsg(Exception exception) {
		System.out.println("Exception: " + exception);
	}
	
	private void printLoginAccountDetail(List<LoginAccount> loginAccounts) {
		System.out.println("LoginInformation: " + loginAccounts);
	}
	
	private void printEnvelopeSummary(EnvelopeSummary envelopeSummary) {
		System.out.println("EnvelopeSummary: " + envelopeSummary);
	}
	
	private void printViewUrl(ViewUrl recipientView) {
		System.out.println("ViewUrl: " + recipientView);
	}
	
	@SuppressWarnings("unused")
	private void configSystemProxy() {
		System.setProperty("java.net.useSystemProxies", "true");
		try {
			List<Proxy> proxyList = ProxySelector.getDefault().select(new URI("http://www.google.com/"));

			for (Proxy proxy : proxyList) {
				InetSocketAddress addr = (InetSocketAddress) proxy.address();
				if (addr == null) {
					System.out.println("No Proxy");
				} else {
					System.setProperty("https.proxyHost", addr.getHostName());
					System.setProperty("https.proxyPort", String.valueOf(addr.getPort()));

					break;
				}
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
