package main.com.aspire.docusign;
/* CoreRecipes.java
 * 
 * Simple Class that demonstrates how to accomplish various REST API use-cases.  
 */

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
// additional imports
import java.util.List;
import java.util.Scanner;

// DocuSign imports
import com.docusign.esign.api.AuthenticationApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.Configuration;
import com.docusign.esign.model.ConsoleViewRequest;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.Envelope;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.EnvelopesInformation;
import com.docusign.esign.model.FormulaTab;
import com.docusign.esign.model.LoginAccount;
import com.docusign.esign.model.LoginInformation;
import com.docusign.esign.model.Number;
import com.docusign.esign.model.PaymentDetails;
import com.docusign.esign.model.PaymentLineItem;
import com.docusign.esign.model.RecipientViewRequest;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.ReturnUrlRequest;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;
import com.docusign.esign.model.TemplateRole;
import com.docusign.esign.model.ViewUrl;

public class CoreRecipes {

	// Note: Following values are class members for readability and easy testing
	// TODO: Enter your DocuSign credentials
	public static final String UserName = "ranganathanec95@gmail.com";
	public static final String Password = "ranga@555";

	// TODO: Enter your Integrator Key (aka API key), created through your
	// developer sandbox preferences
	public static final String IntegratorKey = "c7f4d8ad-b642-49a3-82a9-4c2c6f2baed1";

	// for production environment update to "www.docusign.net/restapi"
	public static final String BaseUrl = "https://demo.docusign.net/restapi";

	/*****************************************************************************************************************
	 * RequestSignatureOnDocument()
	 * 
	 * This recipe demonstrates how to request a signature on a document by
	 * first making the Login API call then the Create Envelope API call.
	 ******************************************************************************************************************/
	public void RequestSignatureOnDocument() {

		// TODO: Enter signer information and path to a test file
		String signerName = "Ranga";
		String signerEmail = "ranganathan.vinayagam@aspiresys.com";
		// point to a local document for testing
		final String SignTest1File = "/TEST.PDF";

		// initialize the api client
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(BaseUrl);

		// create JSON formatted auth header
		String creds = "{\"Username\":\"" + UserName + "\",\"Password\":\"" + Password + "\",\"IntegratorKey\":\""
				+ IntegratorKey + "\"}";
		apiClient.addDefaultHeader("X-DocuSign-Authentication", creds);

		// assign api client to the Configuration object
		Configuration.setDefaultApiClient(apiClient);

		// list of user account(s)
		List<LoginAccount> loginAccounts = null;

		// ===============================================================================
		// Step 1: Login() API
		// ===============================================================================
		try {
			// login call available off the AuthenticationApi
			AuthenticationApi authApi = new AuthenticationApi();

			// login has some optional parameters we can set
			AuthenticationApi.LoginOptions loginOps = authApi.new LoginOptions();
			loginOps.setApiPassword("true");
			loginOps.setIncludeAccountIdGuid("true");
			LoginInformation loginInfo = authApi.login(loginOps);

			// note that a given user may be a member of multiple accounts
			loginAccounts = loginInfo.getLoginAccounts();

			System.out.println("LoginInformation: " + loginAccounts);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}

		// ===============================================================================
		// Step 2: Create Envelope API (AKA Signature Request)
		// ===============================================================================

		// create a byte array that will hold our document bytes
		byte[] fileBytes = null;

		try {
			String currentDir = System.getProperty("user.dir");
			// read file from a local directory
			Path path = Paths.get(currentDir + SignTest1File);
			fileBytes = Files.readAllBytes(path);
		} catch (IOException ioExcp) {
			// handle error
			System.out.println("Exception: " + ioExcp);
			return;
		}

		// create an envelope that will store the document(s), field(s), and
		// recipient(s)
		EnvelopeDefinition envDef = new EnvelopeDefinition();
		envDef.setEmailSubject("Please sign this document sent from Java SDK)");

		// add a document to the envelope
		Document doc = new Document();
		String base64Doc = org.apache.commons.codec.binary.Base64.encodeBase64String(fileBytes); //Base64.getEncoder().encodeToString(fileBytes);
		doc.setDocumentBase64(base64Doc);
		doc.setName("TestFile.pdf"); // can be different from actual file name
		doc.setDocumentId("1");

		List<Document> docs = new ArrayList<Document>();
		docs.add(doc);
		envDef.setDocuments(docs);

		// add a recipient to sign the document, identified by name and email we
		// used above
		Signer signer = new Signer();
		signer.setEmail(signerEmail);
		signer.setName(signerName);
		signer.setRecipientId("1");

		// create a signHere tab somewhere on the document for the signer to
		// sign
		// default unit of measurement is pixels, can be mms, cms, inches also
		SignHere signHere = new SignHere();
		signHere.setDocumentId("1");
		signHere.setPageNumber("1");
		signHere.setRecipientId("1");
		signHere.setXPosition("100");
		signHere.setYPosition("100");

		// can have multiple tabs, so need to add to envelope as a single
		// element list
		List<SignHere> signHereTabs = new ArrayList<SignHere>();
		signHereTabs.add(signHere);
		Tabs tabs = new Tabs();
		tabs.setSignHereTabs(signHereTabs);
		signer.setTabs(tabs);

		// add recipients (in this case a single signer) to the envelope
		envDef.setRecipients(new Recipients());
		envDef.getRecipients().setSigners(new ArrayList<Signer>());
		envDef.getRecipients().getSigners().add(signer);

		// send the envelope by setting |status| to "sent". To save as a draft
		// set to "created"
		envDef.setStatus("sent");

		try {
			// use the |accountId| we retrieved through the Login API to create
			// the Envelope
			String accountId = loginAccounts.get(0).getAccountId();

			// instantiate a new EnvelopesApi object
			EnvelopesApi envelopesApi = new EnvelopesApi();

			// call the createEnvelope() API
			EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envDef);

			System.out.println("EnvelopeSummary: " + envelopeSummary);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}
	} // end RequestSignatureOnDocument()

	/*****************************************************************************************************************
	 * RequestSignatureFromTemplate()
	 * 
	 * This recipe demonstrates how to request a signature from a template in
	 * your account. Templates are design-time objects that contain documents,
	 * tabs, routing, and recipient roles. To run this recipe you need to
	 * provide a valid templateId from your account along with a role name that
	 * the template has configured.
	 ******************************************************************************************************************/
	public void RequestSignatureFromTemplate() {

		// TODO: Enter signer information and template info from a template in
		// your account
		String signerName = "[SIGNER_NAME]";
		String signerEmail = "[SIGNER_EMAIL]";
		String templateId = "[TEMPLATE_ID]";
		String templateRoleName = "[TEMPLATE_ROLE_NAME]";

		// initialize the api client
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(BaseUrl);

		// create JSON formatted auth header
		String creds = "{\"Username\":\"" + UserName + "\",\"Password\":\"" + Password + "\",\"IntegratorKey\":\""
				+ IntegratorKey + "\"}";
		apiClient.addDefaultHeader("X-DocuSign-Authentication", creds);

		// assign api client to the Configuration object
		Configuration.setDefaultApiClient(apiClient);

		// list of user account(s)
		List<LoginAccount> loginAccounts = null;

		// ===============================================================================
		// Step 1: Login() API
		// ===============================================================================
		try {
			// login call available off the AuthenticationApi
			AuthenticationApi authApi = new AuthenticationApi();

			// login has some optional parameters we can set
			AuthenticationApi.LoginOptions loginOps = authApi.new LoginOptions();
			loginOps.setApiPassword("true");
			loginOps.setIncludeAccountIdGuid("true");
			LoginInformation loginInfo = authApi.login(loginOps);

			// note that a given user may be a member of multiple accounts
			loginAccounts = loginInfo.getLoginAccounts();

			System.out.println("LoginInformation: " + loginAccounts);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}

		// ===============================================================================
		// Step 2: Create Envelope API (AKA Signature Request) from a Template
		// ===============================================================================

		// create a new envelope object that we will manage the signature
		// request through
		EnvelopeDefinition envDef = new EnvelopeDefinition();
		envDef.setEmailSubject("Please sign this document sent from Java SDK)");

		// assign template information including ID and role(s)
		envDef.setTemplateId(templateId);

		// create a template role with a valid templateId and roleName and
		// assign signer info
		TemplateRole tRole = new TemplateRole();
		tRole.setRoleName(templateRoleName);
		tRole.setName(signerName);
		tRole.setEmail(signerEmail);

		// create a list of template roles and add our newly created role
		List<TemplateRole> templateRolesList = new ArrayList<TemplateRole>();
		templateRolesList.add(tRole);

		// assign template role(s) to the envelope
		envDef.setTemplateRoles(templateRolesList);

		// send the envelope by setting |status| to "sent". To save as a draft
		// set to "created"
		envDef.setStatus("sent");

		try {
			// use the |accountId| we retrieved through the Login API to create
			// the Envelope
			String accountId = loginAccounts.get(0).getAccountId();

			// instantiate a new EnvelopesApi object
			EnvelopesApi envelopesApi = new EnvelopesApi();

			// call the createEnvelope() API
			EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envDef);

			System.out.println("EnvelopeSummary: " + envelopeSummary);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}
	} // end RequestSignatureFromTemplate()

	/*****************************************************************************************************************
	 * GetEnvelopeInformation()
	 * 
	 * This recipe demonstrates how to retrieve real-time envelope information
	 * for an existing envelope. Note that DocuSign has certain platform rules
	 * in place which limit how frequently you can poll for status on a given
	 * envelope. As of this writing the current limit is once every 15 minutes
	 * for a given envelope.
	 ******************************************************************************************************************/
	public void GetEnvelopeInformation() {

		// TODO: Enter envelopeId of an envelope you have access to (i.e. you
		// sent the envelope or you're an account admin)
		String envelopeId = "88f3918a-b46c-49f8-8e70-9324727ce427";

		// initialize the api client
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(BaseUrl);

		// create JSON formatted auth header
		String creds = "{\"Username\":\"" + UserName + "\",\"Password\":\"" + Password + "\",\"IntegratorKey\":\""
				+ IntegratorKey + "\"}";
		apiClient.addDefaultHeader("X-DocuSign-Authentication", creds);

		// assign api client to the Configuration object
		Configuration.setDefaultApiClient(apiClient);

		// list of user account(s)
		List<LoginAccount> loginAccounts = null;

		// ===============================================================================
		// Step 1: Login() API
		// ===============================================================================
		try {
			// login call available off the AuthenticationApi
			AuthenticationApi authApi = new AuthenticationApi();

			// login has some optional parameters we can set
			AuthenticationApi.LoginOptions loginOps = authApi.new LoginOptions();
			loginOps.setApiPassword("true");
			loginOps.setIncludeAccountIdGuid("true");
			LoginInformation loginInfo = authApi.login(loginOps);

			// note that a given user may be a member of multiple accounts
			loginAccounts = loginInfo.getLoginAccounts();

			System.out.println("LoginInformation: " + loginAccounts);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}

		// ===============================================================================
		// Step 2: Get Envelope API
		// ===============================================================================
		try {
			// use the |accountId| we retrieved through the Login API to access
			// envelope info
			String accountId = loginAccounts.get(0).getAccountId();

			// instantiate a new EnvelopesApi object
			EnvelopesApi envelopesApi = new EnvelopesApi();

			// call the getEnvelope() API
			Envelope env = envelopesApi.getEnvelope(accountId, envelopeId);

			System.out.println("Envelope: " + env.getCompletedDateTime());
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}
	} // end GetEnvelopeInformation()

	/*****************************************************************************************************************
	 * listRecipients()
	 * 
	 * This recipe demonstrates how to retrieve real-time envelope recipient
	 * information for an existing envelope. The call will return information on
	 * all recipients that are part of the envelope's routing order.
	 ******************************************************************************************************************/
	public void listRecipients() {

		// TODO: Enter envelopeId of an envelope you have access to (i.e. you
		// sent the envelope or you're an account admin)
		String envelopeId = "88f3918a-b46c-49f8-8e70-9324727ce427";

		// initialize the api client
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(BaseUrl);

		// create JSON formatted auth header
		String creds = "{\"Username\":\"" + UserName + "\",\"Password\":\"" + Password + "\",\"IntegratorKey\":\""
				+ IntegratorKey + "\"}";
		apiClient.addDefaultHeader("X-DocuSign-Authentication", creds);

		// assign api client to the Configuration object
		Configuration.setDefaultApiClient(apiClient);

		// list of user account(s)
		List<LoginAccount> loginAccounts = null;

		// ===============================================================================
		// Step 1: Login() API
		// ===============================================================================
		try {
			// login call available off the AuthenticationApi
			AuthenticationApi authApi = new AuthenticationApi();

			// login has some optional parameters we can set
			AuthenticationApi.LoginOptions loginOps = authApi.new LoginOptions();
			loginOps.setApiPassword("true");
			loginOps.setIncludeAccountIdGuid("true");
			LoginInformation loginInfo = authApi.login(loginOps);

			// note that a given user may be a member of multiple accounts
			loginAccounts = loginInfo.getLoginAccounts();

			System.out.println("LoginInformation: " + loginAccounts);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}

		// ===============================================================================
		// Step 2: List Recipients() API
		// ===============================================================================
		try {
			// use the |accountId| we retrieved through the Login API
			String accountId = loginAccounts.get(0).getAccountId();

			// instantiate a new EnvelopesApi object
			EnvelopesApi envelopesApi = new EnvelopesApi();

			// call the listRecipients() API
			Recipients recips = envelopesApi.listRecipients(accountId, envelopeId);

			System.out.println("Recipients: " + recips);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}
	} // end listRecipients()

	/*****************************************************************************************************************
	 * ListEnvelopes()
	 * 
	 * This recipe demonstrates how to retrieve real-time envelope status and
	 * information for an existing envelopes in your account. The returned set
	 * of envelopes can be filtered by date, status, or other properties.
	 ******************************************************************************************************************/
	public void ListEnvelopes() {

		// list of user account(s)
		List<LoginAccount> loginAccounts = null;

		// initialize the api client
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(BaseUrl);

		// create JSON formatted auth header
		String creds = "{\"Username\":\"" + UserName + "\",\"Password\":\"" + Password + "\",\"IntegratorKey\":\""
				+ IntegratorKey + "\"}";
		apiClient.addDefaultHeader("X-DocuSign-Authentication", creds);

		// assign api client to the Configuration object
		Configuration.setDefaultApiClient(apiClient);

		// ===============================================================================
		// Step 1: Login() API
		// ===============================================================================
		try {
			// login call available off the AuthenticationApi
			AuthenticationApi authApi = new AuthenticationApi();

			// login has some optional parameters we can set
			AuthenticationApi.LoginOptions loginOps = authApi.new LoginOptions();
			loginOps.setApiPassword("true");
			loginOps.setIncludeAccountIdGuid("true");
			LoginInformation loginInfo = authApi.login(loginOps);

			// note that a given user may be a member of multiple accounts
			loginAccounts = loginInfo.getLoginAccounts();

			System.out.println("LoginInformation: " + loginAccounts);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}

		// ===============================================================================
		// Step 2: List Envelopes API
		// ===============================================================================
		try {
			// use the |accountId| we retrieved through the Login API
			String accountId = loginAccounts.get(0).getAccountId();

			// instantiate a new EnvelopesApi object
			EnvelopesApi envelopesApi = new EnvelopesApi();

			// the list status changes call requires at least a from_date
			EnvelopesApi.ListStatusChangesOptions options = envelopesApi.new ListStatusChangesOptions();

			// set from date to filter envelopes (ex: Dec 1, 2015)
			options.setFromDate("2015/12/01");

			// call the listStatusChanges() API
			EnvelopesInformation envelopes = envelopesApi.listStatusChanges(accountId, options);

			System.out.println("EnvelopesInformation: " + envelopes);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}
	} // end ListEnvelopes()

	/*****************************************************************************************************************
	 * GetEnvelopeDocuments()
	 * 
	 * This recipe demonstrates how to retrieve the documents from a given
	 * envelope. Note that if the envelope is in completed status that you have
	 * the option of downloading just the signed documents or a combined PDF
	 * that contains the envelope documents as well as the envelope's
	 * auto-generated Certificate of Completion (CoC).
	 ******************************************************************************************************************/
	public void GetEnvelopeDocuments() {

		// TODO: Enter envelopeId of an envelope you have access to (i.e. you
		// sent the envelope or
		// you're an account admin in same account). Also provide a valid
		// documentId
		String envelopeId = "88f3918a-b46c-49f8-8e70-9324727ce427";
		String documentId = "1";

		// list of user account(s)
		List<LoginAccount> loginAccounts = null;

		// initialize the api client
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(BaseUrl);

		// create JSON formatted auth header
		String creds = "{\"Username\":\"" + UserName + "\",\"Password\":\"" + Password + "\",\"IntegratorKey\":\""
				+ IntegratorKey + "\"}";
		apiClient.addDefaultHeader("X-DocuSign-Authentication", creds);

		// assign api client to the Configuration object
		Configuration.setDefaultApiClient(apiClient);

		// ===============================================================================
		// Step 1: Login() API
		// ===============================================================================
		try {
			// login call available off the AuthenticationApi
			AuthenticationApi authApi = new AuthenticationApi();

			// login has some optional parameters we can set
			AuthenticationApi.LoginOptions loginOps = authApi.new LoginOptions();
			loginOps.setApiPassword("true");
			loginOps.setIncludeAccountIdGuid("true");
			LoginInformation loginInfo = authApi.login(loginOps);

			// note that a given user may be a member of multiple accounts
			loginAccounts = loginInfo.getLoginAccounts();

			System.out.println("LoginInformation: " + loginAccounts);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}

		// ===============================================================================
		// Step 2: Get Document API
		// ===============================================================================
		try {
			// use the |accountId| we retrieved through the Login API
			String accountId = loginAccounts.get(0).getAccountId();

			// instantiate a new EnvelopesApi object
			EnvelopesApi envelopesApi = new EnvelopesApi();

			// call the getDocument() API
			byte[] document = envelopesApi.getDocument(accountId, envelopeId, documentId);
			FileOutputStream fileOuputStream;
			try {
				fileOuputStream = new FileOutputStream("D:/TestDownload.pdf");
				fileOuputStream.write(document);				
					
			System.out.println(
					"Document " + documentId + " from envelope " + envelopeId + " has been downloaded to D:/TestDownload.pdf" );
			fileOuputStream.close();	
			}
			
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}
	} // end GetEnvelopeDocuments()

	/*****************************************************************************************************************
	 * EmbeddedSending()
	 * 
	 * This recipe demonstrates how to open the Embedded Sending view of a given
	 * envelope (AKA the Sender View). While in the sender view the user can
	 * edit the envelope by adding/deleting documents, tabs, and/or recipients
	 * before sending the envelope (signature request) out.
	 ******************************************************************************************************************/
	public void EmbeddedSending() {

		// TODO: Enter signer info and path to a test file
		String signerName = "Ranganathan";
		String signerEmail = "ranganathan.vinayagam@aspiresys.com";

		// point to a local document for testing
		final String SignTest1File = "/TEST.PDF";

		// we will generate this from the second API call we make
		StringBuffer envelopeId = new StringBuffer();

		// list of user account(s)
		List<LoginAccount> loginAccounts = null;

		// initialize the api client
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(BaseUrl);

		// create JSON formatted auth header
		String creds = "{\"Username\":\"" + UserName + "\",\"Password\":\"" + Password + "\",\"IntegratorKey\":\""
				+ IntegratorKey + "\"}";
		apiClient.addDefaultHeader("X-DocuSign-Authentication", creds);

		// assign api client to the Configuration object
		Configuration.setDefaultApiClient(apiClient);

		// ===============================================================================
		// Step 1: Login() API
		// ===============================================================================
		try {
			// login call available off the AuthenticationApi
			AuthenticationApi authApi = new AuthenticationApi();

			// login has some optional parameters we can set
			AuthenticationApi.LoginOptions loginOps = authApi.new LoginOptions();
			loginOps.setApiPassword("true");
			loginOps.setIncludeAccountIdGuid("true");
			LoginInformation loginInfo = authApi.login(loginOps);

			// note that a given user may be a member of multiple accounts
			loginAccounts = loginInfo.getLoginAccounts();

			System.out.println("LoginInformation: " + loginAccounts);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}

		// ===============================================================================
		// Step 2: Create Envelope API (AKA Signature Request)
		// ===============================================================================

		// create a byte array that will hold our document bytes
		byte[] fileBytes = null;

		try {
			String currentDir = System.getProperty("user.dir");

			// read file from a local directory
			Path path = Paths.get(currentDir + SignTest1File);
			fileBytes = Files.readAllBytes(path);
		} catch (IOException ioExcp) {
			// TODO: handle error
			System.out.println("Exception: " + ioExcp);
			return;
		}

		// create an envelope that will store the document(s), field(s), and
		// recipient(s)
		EnvelopeDefinition envDef = new EnvelopeDefinition();
		envDef.setEmailSubject("Please sign this document sent from Java SDK)");

		// add a document to the envelope
		Document doc = new Document();
		String base64Doc = org.apache.commons.codec.binary.Base64.encodeBase64String(fileBytes); // Base64.getEncoder().encodeToString(fileBytes);
		doc.setDocumentBase64(base64Doc);
		doc.setName("TestFile.pdf"); // can be different from actual file name
		doc.setDocumentId("1");

		List<Document> docs = new ArrayList<Document>();
		docs.add(doc);
		envDef.setDocuments(docs);

		// add a recipient to sign the document, identified by name and email we
		// used above
		Signer signer = new Signer();
		signer.setEmail(signerEmail);
		signer.setName(signerName);
		signer.setRecipientId("1");

		// create a |signHere| tab somewhere on the document for the signer to
		// sign
		// default unit of measurement is pixels, can be mms, cms, inches also
		SignHere signHere = new SignHere();
		signHere.setDocumentId("1");
		signHere.setPageNumber("1");
		signHere.setRecipientId("1");
		signHere.setXPosition("100");
		signHere.setYPosition("100");

		// can have multiple tabs, so need to add to envelope as a single
		// element list
		List<SignHere> signHereTabs = new ArrayList<SignHere>();
		signHereTabs.add(signHere);
		Tabs tabs = new Tabs();
		tabs.setSignHereTabs(signHereTabs);
		signer.setTabs(tabs);

		// add recipients (in this case a single signer) to the envelope
		envDef.setRecipients(new Recipients());
		envDef.getRecipients().setSigners(new ArrayList<Signer>());
		envDef.getRecipients().getSigners().add(signer);

		// set envelope's |status| to "created" so we can open the embedded
		// sending view next
		envDef.setStatus("created");

		try {
			// use the |accountId| we retrieved through the Login API to create
			// the Envelope
			String accountId = loginAccounts.get(0).getAccountId();

			// instantiate a new EnvelopesApi object
			EnvelopesApi envelopesApi = new EnvelopesApi();

			// call the createEnvelope() API
			EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envDef);
			envelopeId.append(envelopeSummary.getEnvelopeId());

			System.out.println("EnvelopeSummary: " + envelopeSummary);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}

		// ===============================================================================
		// Step 3: Create SenderView API
		// ===============================================================================
		try {
			// use the |accountId| we retrieved through the Login API
			String accountId = loginAccounts.get(0).getAccountId();

			// instantiate a new EnvelopesApi object
			EnvelopesApi envelopesApi = new EnvelopesApi();

			// set the url where you want the sender to go once they are done
			// editing/sending the envelope
			ReturnUrlRequest returnUrl = new ReturnUrlRequest();
			returnUrl.setReturnUrl("https://www.docusign.com/devcenter");

			// call the createEnvelope() API
			ViewUrl senderView = envelopesApi.createSenderView(accountId, envelopeId.toString(), returnUrl);

			System.out.println("ViewUrl: " + senderView);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}
	} // end EmbeddedSending()

	/*****************************************************************************************************************
	 * EmbeddedSigning()
	 * 
	 * This recipe demonstrates how to open the Embedded Signing view of a given
	 * envelope (AKA the Recipient View). The Recipient View can be used to sign
	 * document(s) directly through your UI without having to context-switch and
	 * sign through the DocuSign Website. This is done by opening the Recipient
	 * View in an iFrame for web applications or a webview for mobile apps.
	 ******************************************************************************************************************/
	public void EmbeddedSigning() {

		// TODO: Enter signer info and path to a test file
		String signerName = "Ranganathan";
		String signerEmail = "ranganathan.vinayagam@aspiresys.com";
		// point to a local document for testing
		final String SignTest1File = "/TEST.PDF";

		// we will generate this from the second API call we make
		StringBuffer envelopeId = new StringBuffer();

		// list of user account(s)
		List<LoginAccount> loginAccounts = null;

		// initialize the api client
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(BaseUrl);

		// create JSON formatted auth header
		String creds = "{\"Username\":\"" + UserName + "\",\"Password\":\"" + Password + "\",\"IntegratorKey\":\""
				+ IntegratorKey + "\"}";
		apiClient.addDefaultHeader("X-DocuSign-Authentication", creds);

		// assign api client to the Configuration object
		Configuration.setDefaultApiClient(apiClient);

		// ===============================================================================
		// Step 1: Login() API
		// ===============================================================================
		try {
			// login call available off the AuthenticationApi
			AuthenticationApi authApi = new AuthenticationApi();

			// login has some optional parameters we can set
			AuthenticationApi.LoginOptions loginOps = authApi.new LoginOptions();
			loginOps.setApiPassword("true");
			loginOps.setIncludeAccountIdGuid("true");
			LoginInformation loginInfo = authApi.login(loginOps);

			// note that a given user may be a member of multiple accounts
			loginAccounts = loginInfo.getLoginAccounts();

			System.out.println("LoginInformation: " + loginAccounts);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}

		// ===============================================================================
		// Step 2: Create Envelope API (AKA Signature Request)
		// ===============================================================================

		// create a byte array that will hold our document bytes
		byte[] fileBytes = null;

		try {
			String currentDir = System.getProperty("user.dir");

			// read file from a local directory
			Path path = Paths.get(currentDir + SignTest1File);
			fileBytes = Files.readAllBytes(path);
		} catch (IOException ioExcp) {
			// TODO: handle error
			System.out.println("Exception: " + ioExcp);
			return;
		}

		// create an envelope that will store the document(s), field(s), and
		// recipient(s)
		EnvelopeDefinition envDef = new EnvelopeDefinition();
		envDef.setEmailSubject("Please sign this document sent from Java SDK)");

		// add a document to the envelope
		Document doc = new Document();
		String base64Doc = org.apache.commons.codec.binary.Base64.encodeBase64String(fileBytes); // Base64.getEncoder().encodeToString(fileBytes);
		doc.setDocumentBase64(base64Doc);
		doc.setName("TestFile.pdf"); // can be different from actual file name
		doc.setDocumentId("1");

		List<Document> docs = new ArrayList<Document>();
		docs.add(doc);
		envDef.setDocuments(docs);

		// add a recipient to sign the document, identified by name and email we
		// used above
		Signer signer = new Signer();
		signer.setEmail(signerEmail);
		signer.setName(signerName);
		signer.setRecipientId("1");

		// Must set |clientUserId| for embedded recipients and provide the same
		// value when requesting
		// the recipient view URL in the next step
		signer.setClientUserId("1001");

		// create a |signHere| tab somewhere on the document for the signer to
		// sign
		// default unit of measurement is pixels, can be mms, cms, inches also
		SignHere signHere = new SignHere();
		signHere.setDocumentId("1");
		signHere.setPageNumber("1");
		signHere.setRecipientId("1");
		signHere.setXPosition("100");
		signHere.setYPosition("100");

		// can have multiple tabs, so need to add to envelope as a single
		// element list
		List<SignHere> signHereTabs = new ArrayList<SignHere>();
		signHereTabs.add(signHere);
		Tabs tabs = new Tabs();
		tabs.setSignHereTabs(signHereTabs);
		signer.setTabs(tabs);

		// add recipients (in this case a single signer) to the envelope
		envDef.setRecipients(new Recipients());
		envDef.getRecipients().setSigners(new ArrayList<Signer>());
		envDef.getRecipients().getSigners().add(signer);

		// send the envelope by setting |status| to "sent". To save as a draft
		// set to "created"
		envDef.setStatus("sent");

		try {
			// use the |accountId| we retrieved through the Login API to create
			// the Envelope
			String accountId = loginAccounts.get(0).getAccountId();

			// instantiate a new EnvelopesApi object
			EnvelopesApi envelopesApi = new EnvelopesApi();

			// call the createEnvelope() API
			EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envDef);
			envelopeId.append(envelopeSummary.getEnvelopeId());

			System.out.println("EnvelopeSummary: " + envelopeSummary);

		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}

		// ===============================================================================
		// Step 3: Create RecipientView API
		// ===============================================================================
		try {
			// use the |accountId| we retrieved through the Login API
			String accountId = loginAccounts.get(0).getAccountId();

			// instantiate a new EnvelopesApi object
			EnvelopesApi envelopesApi = new EnvelopesApi();

			// set the url where you want the recipient to go once they are done
			// signing
			RecipientViewRequest returnUrl = new RecipientViewRequest();
			returnUrl.setReturnUrl("https://www.docusign.com/devcenter");
			returnUrl.setAuthenticationMethod("email");

			// recipient information must match embedded recipient info we
			// provided in step #2
			returnUrl.setEmail(signerEmail);
			returnUrl.setUserName(signerName);
			returnUrl.setRecipientId("1");
			returnUrl.setClientUserId("1001");

			// call the CreateRecipientView API
			ViewUrl recipientView = envelopesApi.createRecipientView(accountId, envelopeId.toString(), returnUrl);

			System.out.println("ViewUrl: " + recipientView);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}
	} // end EmbeddedSigning()

	/*****************************************************************************************************************
	 * EmbeddedConsole()
	 * 
	 * This recipe demonstrates how to open the DocuSign Console in an embedded
	 * view. DocuSign recommends you use an iFrame for web applications and a
	 * webview for mobile apps.
	 ******************************************************************************************************************/
	public void EmbeddedConsole() {

		// list of user account(s)
		List<LoginAccount> loginAccounts = null;

		// initialize the api client
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(BaseUrl);

		// create JSON formatted auth header
		String creds = "{\"Username\":\"" + UserName + "\",\"Password\":\"" + Password + "\",\"IntegratorKey\":\""
				+ IntegratorKey + "\"}";
		apiClient.addDefaultHeader("X-DocuSign-Authentication", creds);

		// assign api client to the Configuration object
		Configuration.setDefaultApiClient(apiClient);

		// ===============================================================================
		// Step 1: Login() API
		// ===============================================================================
		try {
			// login call available off the AuthenticationApi
			AuthenticationApi authApi = new AuthenticationApi();

			// login has some optional parameters we can set
			AuthenticationApi.LoginOptions loginOps = authApi.new LoginOptions();
			loginOps.setApiPassword("true");
			loginOps.setIncludeAccountIdGuid("true");
			LoginInformation loginInfo = authApi.login(loginOps);

			// note that a given user may be a member of multiple accounts
			loginAccounts = loginInfo.getLoginAccounts();

			System.out.println("LoginInformation: " + loginAccounts);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}

		// ===============================================================================
		// Step 2: Create ConsoleView API
		// ===============================================================================
		try {
			// use the |accountId| we retrieved through the Login API
			String accountId = loginAccounts.get(0).getAccountId();

			// instantiate a new envelopesApi object
			EnvelopesApi envelopesApi = new EnvelopesApi();

			// set the url where you want the user to go once they logout of the
			// Console
			ConsoleViewRequest returnUrl = new ConsoleViewRequest();
			returnUrl.setReturnUrl("https://www.docusign.com/devcenter");

			// call the createConsoleView() API
			ViewUrl consoleView = envelopesApi.createConsoleView(accountId, returnUrl);

			System.out.println("ConsoleView: " + consoleView);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}
	} // end EmbeddedConsole()

	/*****************************************************************************************************************
	 * RequestPaymentOnDocument()
	 * 
	 * This recipe demonstrates how to request a payment on a document by first
	 * making the Login API call then the Create Envelope API call.
	 ******************************************************************************************************************/
	public void RequestPaymentOnDocument() {

		// TODO: Enter signer information and path to a test file
		String signerName = "[SIGNER_NAME]";
		String signerEmail = "[SIGNER_EMAIL]";
		String paymentGatewayId = "[PAYMENT_GATEWAY_ID]";
		// point to a local document for testing
		final String SignTest1File = "[PATH/TO/DOCUMENT/TEST.PDF]";

		// initialize the api client
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(BaseUrl);

		// create JSON formatted auth header
		String creds = "{\"Username\":\"" + UserName + "\",\"Password\":\"" + Password + "\",\"IntegratorKey\":\""
				+ IntegratorKey + "\"}";
		apiClient.addDefaultHeader("X-DocuSign-Authentication", creds);

		// assign api client to the Configuration object
		Configuration.setDefaultApiClient(apiClient);

		// list of user account(s)
		List<LoginAccount> loginAccounts = null;

		// ===============================================================================
		// Step 1: Login() API
		// ===============================================================================
		try {
			// login call available off the AuthenticationApi
			AuthenticationApi authApi = new AuthenticationApi();

			// login has some optional parameters we can set
			AuthenticationApi.LoginOptions loginOps = authApi.new LoginOptions();
			loginOps.setApiPassword("true");
			loginOps.setIncludeAccountIdGuid("true");
			LoginInformation loginInfo = authApi.login(loginOps);

			// note that a given user may be a member of multiple accounts
			loginAccounts = loginInfo.getLoginAccounts();

			System.out.println("LoginInformation: " + loginAccounts);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}

		// ===============================================================================
		// Step 2: Create Envelope API (AKA Payment Request)
		// ===============================================================================

		// create a byte array that will hold our document bytes
		byte[] fileBytes = null;

		try {
			String currentDir = System.getProperty("user.dir");
			// read file from a local directory
			Path path = Paths.get(currentDir + SignTest1File);
			fileBytes = Files.readAllBytes(path);
		} catch (IOException ioExcp) {
			// handle error
			System.out.println("Exception: " + ioExcp);
			return;
		}

		// create an envelope that will store the document(s), field(s), and
		// recipient(s)
		EnvelopeDefinition envDef = new EnvelopeDefinition();
		envDef.setEmailSubject("Please pay on this document sent from Java SDK)");

		// add a document to the envelope
		Document doc = new Document();
		String base64Doc = org.apache.commons.codec.binary.Base64.encodeBase64String(fileBytes); // Base64.getEncoder().encodeToString(fileBytes);
		doc.setDocumentBase64(base64Doc);
		doc.setName("TestFile.pdf"); // can be different from actual file name
		doc.setDocumentId("1");

		List<Document> docs = new ArrayList<Document>();
		docs.add(doc);
		envDef.setDocuments(docs);

		// add a recipient to sign the document, identified by name and email we
		// used above
		Signer signer = new Signer();
		signer.setEmail(signerEmail);
		signer.setName(signerName);
		signer.setRecipientId("1");

		Number num = new Number();
		num.setDocumentId("1");
		num.setPageNumber("1");
		num.setRecipientId("1");
		num.setXPosition("100");
		num.setYPosition("100");
		num.setTabLabel("tabvalue1");
		num.setValue("10.00");
		num.setLocked("true");

		FormulaTab formula = new FormulaTab();
		formula.setDocumentId("1");
		formula.setPageNumber("1");
		formula.setRecipientId("1");
		formula.setXPosition("1");
		formula.setYPosition("1");
		formula.setTabLabel("tabpayment1");
		formula.setRequired("true");

		formula.setFormula("[tabvalue1] * 100");
		formula.setRoundDecimalPlaces("2");

		PaymentLineItem lineItem = new PaymentLineItem();
		lineItem.setName("Name1");
		lineItem.setDescription("description1");
		lineItem.setItemCode("ITEM1");
		lineItem.setAmountReference("tabvalue1");

		PaymentDetails paymentDetails = new PaymentDetails();
		paymentDetails.setCurrencyCode("USD");
		paymentDetails.setGatewayAccountId(paymentGatewayId);

		List<PaymentLineItem> lineItems = new ArrayList<PaymentLineItem>();
		lineItems.add(lineItem);
		paymentDetails.setLineItems(lineItems);

		formula.setPaymentDetails(paymentDetails);

		// can have multiple tabs, so need to add to envelope as a single
		// element list
		List<Number> numberTabs = new ArrayList<Number>();
		List<FormulaTab> formulaTabs = new ArrayList<FormulaTab>();
		numberTabs.add(num);
		formulaTabs.add(formula);
		Tabs tabs = new Tabs();
		tabs.setNumberTabs(numberTabs);
		tabs.setFormulaTabs(formulaTabs);
		signer.setTabs(tabs);

		// add recipients (in this case a single signer) to the envelope
		envDef.setRecipients(new Recipients());
		envDef.getRecipients().setSigners(new ArrayList<Signer>());
		envDef.getRecipients().getSigners().add(signer);

		// send the envelope by setting |status| to "sent". To save as a draft
		// set to "created"
		envDef.setStatus("sent");

		try {
			// use the |accountId| we retrieved through the Login API to create
			// the Envelope
			String accountId = loginAccounts.get(0).getAccountId();

			// instantiate a new EnvelopesApi object
			EnvelopesApi envelopesApi = new EnvelopesApi();

			// call the createEnvelope() API
			EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envDef);

			System.out.println("EnvelopeSummary: " + envelopeSummary);
		} catch (com.docusign.esign.client.ApiException ex) {
			System.out.println("Exception: " + ex);
		}
	} // end RequestSignatureOnDocument()

	// *****************************************************************
	// *****************************************************************
	// main() -
	// *****************************************************************
	// *****************************************************************
	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		System.setProperty("https.proxyHost", "eproxy.aspiresys.com");
		System.setProperty("https.proxyPort", "3128");

		CoreRecipes recipes = new CoreRecipes();

		// Test #1
		System.out.println("Running test #1...\n");
		System.out.println("-----------Select Options----------\n");
		System.out.println("1.Embedded signing\n2.Email signing\n3.Download Documents");
		String input = scanner.nextLine();
		switch (input) {
		case "1":
			recipes.EmbeddedSigning();
			break;
		case "2":
			recipes.RequestSignatureOnDocument();
			break;
		case "3":
			recipes.GetEnvelopeDocuments();
			break;

		}
		// recipes.RequestSignatureOnDocument();
		// recipes.GetEnvelopeInformation();
		// recipes.GetEnvelopeDocuments();
		System.out.println("\nTest #1 Complete.\n-----------------");

		// Test #2
		// System.out.println("Running test #2...\n");
		// recipes.RequestSignatureFromTemplate();
		// System.out.println("\nTest #2 Complete.\n-----------------");

		// Test #3
		// System.out.println("Running test #3...\n");
		// recipes.GetEnvelopeInformation();
		// System.out.println("\nTest #3 Complete.\n-----------------");

		// Test #4
		// System.out.println("Running test #4...\n");
		// recipes.listRecipients();
		// System.out.println("\nTest #4 Complete.\n-----------------");

		// Test #5
		// System.out.println("Running test #5...\n");
		// recipes.ListEnvelopes();
		// System.out.println("\nTest #5 Complete.\n-----------------");

		// Test #6
		// System.out.println("Running test #6...\n");
		// recipes.GetEnvelopeDocuments();
		// System.out.println("\nTest #6 Complete.\n-----------------");

		// Test #7
		// System.out.println("Running test #7...\n");
		// recipes.EmbeddedSending();
		// System.out.println("\nTest #7 Complete.\n-----------------");

		// Test #8
		// System.out.println("Running test #8...\n");
		// recipes.EmbeddedSigning();
		// System.out.println("\nTest #8 Complete.\n-----------------");

		// Test #9
		// System.out.println("Running test #9...\n");
		// recipes.EmbeddedConsole();
		// System.out.println("\nTest #9 Complete.\n-----------------");

		// Test #10
		// System.out.println("Running test #10...\n");
		// recipes.RequestPaymentOnDocument();
		// System.out.println("\nTest #9 Complete.\n-----------------");
		scanner.close();
	} // end main()
} // end class