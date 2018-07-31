package pt.ulisboa.tecnico.sise.autoinsure.ws;

import java.util.List;
import javax.jws.*;
import org.json.simple.JSONObject;

import pt.ulisboa.tecnico.sise.autoinsure.datamodel.ClaimItem;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.ClaimMessage;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.ClaimRecord;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.Customer;

import org.json.simple.JSONArray;

@WebService(portName = "AutoInsureWSPort",
			serviceName = "AutoInsureWS",
			targetNamespace = "http://pt.ulisboa.tecnico.sise.autoinsure.ws/",
			endpointInterface = "pt.ulisboa.tecnico.sise.autoinsure.ws.AutoInSureWS")
public class AutoInSureWSImpl implements AutoInSureWS {

	@Override
	public String hello(String name) {
		Logger.logRequest("hello (" + "name:"+ name + ")");
		String res = "Hello '" + name + "'!";
		Logger.logResponse(res);
		return res;
	}

	@Override
	public String login(String username, String password) {
		Logger.logRequest("login (" + "username:"+ username + ", password: " + password + ")");
		int sessionId = DataManager.getInstance().login(username, password);
		String res = "" + sessionId;
		Logger.logResponse(res);
		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getCustomerInfo(String sessionId) {
		Logger.logRequest("listCustomerInfo (" + "sessionId:"+ sessionId + ")");

		Customer customer = DataManager.getInstance().listCustomerInfo(
				Integer.parseInt(sessionId));

		String res;
		if (customer == null) {
			res = "invalid sessionId";
		} else {
			JSONObject jsonCustomerInfo = new JSONObject();
			jsonCustomerInfo.put("username", 		customer.getUsername());
			jsonCustomerInfo.put("name", 			customer.getName());
			jsonCustomerInfo.put("fiscalNumber", 	customer.getFiscalNumber());
			jsonCustomerInfo.put("address", 		customer.getAddress());
			jsonCustomerInfo.put("dateOfBirth", 	customer.getDateOfBirth());
			jsonCustomerInfo.put("policyNumber", 	customer.getPolicyNumber());
			//JSONArray jsonPlateList = new JSONArray();
			//for(String plate : customer.getPlateList()) {
			//	jsonPlateList.add(plate);
			//}
			//jsonCustomerInfo.put("plates", jsonPlateList);
			
			res = jsonCustomerInfo.toJSONString();
		}
		Logger.logResponse(res);
		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getClaimInfo(String sessionId, String claimId) {
		Logger.logRequest("getClaimInfo (" + "sessionId:"+ sessionId + ", claimId: " + claimId + ")");
		
		ClaimRecord claimRecord = DataManager.getInstance().readClaim(Integer.parseInt(sessionId),
				Integer.parseInt(claimId));
		
		String res;
		if (claimRecord == null) {
			res = "invalid sessionId or claimId";
		} else {
			JSONObject jsonClaim = new JSONObject();
			jsonClaim.put("claimId", 		claimRecord.getId());
			jsonClaim.put("claimTitle", 	claimRecord.getTitle());
			jsonClaim.put("plate",	 		claimRecord.getPlate());
			jsonClaim.put("submissionDate", claimRecord.getSubmissionDate());
			jsonClaim.put("occurrenceDate", claimRecord.getOccurrenceDate());
			jsonClaim.put("description", 	claimRecord.getDescription());
			jsonClaim.put("status", 		claimRecord.getStatus());
			res = jsonClaim.toJSONString();
		}
		Logger.logResponse(res);
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String listPlates(String sessionId) {
		Logger.logRequest("listCustomerInfo (" + "sessionId:"+ sessionId + ")");

		Iterable<String> plateList = DataManager.getInstance().listPlates(
				Integer.parseInt(sessionId));
		
		String res;
		if(plateList == null) { 
			res = "invalid sessionId";
		} else {
			JSONArray jsonPlateList = new JSONArray();
			for(String plate : plateList) {
				jsonPlateList.add(plate);
			}
			res = jsonPlateList.toJSONString();
		}
		Logger.logResponse(res);
		return res;
	}


	@SuppressWarnings("unchecked")
	@Override
	public String listClaims(String sessionId) {
		Logger.logRequest("listClaims (" + "sessionId:"+ sessionId + ")");
		
		List<ClaimItem> claimItemList = DataManager.getInstance().listClaims(Integer.parseInt(sessionId));

		String res;
		if (claimItemList == null) {
			res = "invalid sessionId";
		} else{
			JSONArray jsonClaimList = new JSONArray();
			for (int i = 0; i < claimItemList.size(); i++) {
				ClaimItem c = claimItemList.get(i);
				JSONObject jsonClaim = new JSONObject();
				jsonClaim.put("claimId", 	c.getId());
				jsonClaim.put("claimTitle", c.getTitle());
				jsonClaimList.add(jsonClaim);
			}
			res = jsonClaimList.toJSONString();
		}
		Logger.logResponse(res);
		return res;
	}
    
    @SuppressWarnings("unchecked")
    @Override
    public String listClaimMessages(String sessionId, String claimId) {
        Logger.logRequest("claimMessages (" + "sessionId:"+ sessionId + ", claimId: " + claimId +")");
        Iterable<ClaimMessage> claimMessageList = DataManager.getInstance().claimMessages(
            Integer.parseInt(sessionId), 
            Integer.parseInt(claimId)
        );
        
        String res;
        if (claimMessageList == null) {
        	res = "invalid sessionId or claimId";
    	} else{
	        JSONArray jsonClaimMessageList = new JSONArray();
	    	for (ClaimMessage claimMessage : claimMessageList) {
	    		JSONObject jsonClaimMessage = new JSONObject();
	            jsonClaimMessage.put("msg",    claimMessage.getMessage());
	            jsonClaimMessage.put("date",   claimMessage.getDate());
	            jsonClaimMessage.put("sender", claimMessage.getSender());
	            jsonClaimMessageList.add(jsonClaimMessage);    
	    	}
	        res = jsonClaimMessageList.toJSONString();
    	}
        Logger.logResponse(res);
        return res;
    }

	@Override
	public String submitNewClaim(String sessionId, String claimTitle, String occurrenceDate, String plate, String claimDescription) {
		Logger.logRequest("submitNewClaim (" + "sessionId:"+ sessionId + 
				", claimTitle: " + claimTitle + ", occurrenceDate:" + occurrenceDate + ", plate:" + plate + ", claimDescription:" + claimDescription + ")");

		boolean status = DataManager.getInstance().submitNewClaim(Integer.parseInt(sessionId), 
				claimTitle, occurrenceDate, plate, claimDescription);
		
		String res = (status)?"true":"false";
		Logger.logResponse(res);
		return res;
	}
    
    @Override
    public String submitNewMessage(String sessionId, String claimId, String message) {
        Logger.logRequest("newMessage (" + "sessionId:"+ sessionId + ", claimId: " + claimId + ", message: " + message +")");
        boolean status = DataManager.getInstance().submitNewMessage(Integer.parseInt(sessionId), Integer.parseInt(claimId), message);

        String res = status ? "true" : "false";
        Logger.logResponse(res);
        return res;
    }    

	@Override
	public String logout(String sessionId) {
		Logger.logRequest("logout (" + "sessionId:"+ sessionId + ")");

		boolean stat = DataManager.getInstance().logout(Integer.parseInt(sessionId));
		String res = "" + stat;
		Logger.logResponse(res);
		return res;
	}
}
