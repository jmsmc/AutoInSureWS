package pt.ulisboa.tecnico.sise.autoinsure.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(
	name = "InsureWS",
	targetNamespace = "http://pt.ulisboa.tecnico.sise.autoinsure.ws/"
)

public interface AutoInSureWS {
	
	@WebMethod(operationName = "hello")
	public String hello(String name);
	
	@WebMethod(operationName = "login")
	public String login(String username, String password);
	
	@WebMethod(operationName = "getCustomerInfo")
	public String getCustomerInfo(String sessionId);

	@WebMethod(operationName = "getClaimInfo")
	public String getClaimInfo(String sessionId, String claimId);
	
	@WebMethod(operationName = "listPlates")
	public String listPlates(String sessionId);
	
	@WebMethod(operationName = "listClaims")
	public String listClaims(String sessionId);
	
    @WebMethod(operationName = "listClaimMessages")
	public String listClaimMessages(String sessionId, String claimId);

	@WebMethod(operationName = "submitNewClaim")
	public String submitNewClaim(String sessionId, String claimTitle, String occurrenceDate, String plate, String claimDescription);
	
    @WebMethod(operationName = "submitNewMessage")
	public String submitNewMessage(String sessionId, String claimId, String message);

	@WebMethod(operationName = "logout")
	public String logout(String sessionId);
}
