package pt.ulisboa.tecnico.sise.autoinsure.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import pt.ulisboa.tecnico.sise.autoinsure.datamodel.ClaimItem;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.ClaimMessage;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.ClaimRecord;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.Customer;

public class DataManager {
	private static DataManager _instance = null;

	private final HashMap<Integer,String> _sessions;
	private int _sessionCounter;
	
	protected DataManager() {
		_sessions = new HashMap<>();
		_sessionCounter = 1;
	}

	public static DataManager getInstance() {
		if(_instance == null) {
			_instance = new DataManager();
		}
		return _instance;
	}
	
	public int login(String username, String password) {
		ArrayList<Customer> customers = DataProvider.loadCustomers();
		int sessionId = 0;
		
		for(Iterator<Customer> i = customers.iterator(); i.hasNext();) {
			Customer customer = i.next();
			if (customer.getUsername().equals(username)) {
				if (customer.getPassword().equals(password)) {
					sessionId = _sessionCounter++;
					customer.setSessionId(sessionId);
					_sessions.put(sessionId, username);
				}
				break;
			}
		}
		return sessionId;
	}
	
	public Customer listCustomerInfo(int sessionId) {
		String username = _sessions.get(sessionId);
		if (username == null) {
			return null;
		}

		ArrayList<Customer> customers = DataProvider.loadCustomers();

		for(Iterator<Customer> i = customers.iterator(); i.hasNext();) {
			Customer c = i.next();
			if (c.getUsername().equals(username)) {
				return c;
			}
		}
		return null;
	}
	
	public Iterable<String> listPlates(int sessionId) {
		String username = _sessions.get(sessionId);
		if (username == null) {
			return null;
		}

		ArrayList<Customer> customers = DataProvider.loadCustomers();

		for(Iterator<Customer> i = customers.iterator(); i.hasNext();) {
			Customer c = i.next();
			if (c.getUsername().equals(username)) {
				return c.getPlateList();
			}
		}
		return null;
	}
	
	public boolean submitNewClaim(int sessionId, String claimTitle, String occurrenceDate, String numberPlate, String claimDescription) {
		String username = _sessions.get(sessionId);
		if (username == null) {
			return false;
		}

		ArrayList<Customer> customers = DataProvider.loadCustomers();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		for(Iterator<Customer> i = customers.iterator(); i.hasNext();) {
			Customer customer = i.next();
			if (customer.getUsername().equals(username)) {
				int claimId = 0;
				    
				// find maximum claimID already existent for that customer
				for(ClaimRecord claimRecord : customer.getClaimRecordList()) {
					if (claimRecord.getId() > claimId) {
						claimId = claimRecord.getId();
					}
				}
				// increase maximum claimID found by 1
				claimId++;
                Date date = new Date();
                String submissionDate = dateFormat.format(date);
                String status = ClaimRecord.STATUS_PENDING;
                                
				customer.addClaim(new ClaimRecord(claimId, claimTitle, submissionDate, occurrenceDate, numberPlate, 
						claimDescription, status, new ArrayList<ClaimMessage>()));
				DataProvider.storeCustomers(customers);
				return true;
			}
		}
		return false;
	}
	
	public List<ClaimItem> listClaims(int sessionId) {
		String username = _sessions.get(sessionId);
		if (username == null) {
			return null;
		}

		ArrayList<Customer> customers = DataProvider.loadCustomers();
		for(Iterator<Customer> i = customers.iterator(); i.hasNext();) {
			Customer customer = i.next();
			if (customer.getUsername().equals(username)) {
				List<ClaimItem> items = new ArrayList<>();
				for(ClaimRecord claimRecord : customer.getClaimRecordList()) {
					items.add(claimRecord);		// claimRecord extends ClaimItem, so, we can return it to then use just the id and title
				}
				return items;
			}
		}
		return null;
	}
	
	public ClaimRecord readClaim(int sessionId, int claimId) {
		String username = _sessions.get(sessionId);
		if (username == null) {
			return null;
		}

		ArrayList<Customer> customers = DataProvider.loadCustomers();
		for(Iterator<Customer> i = customers.iterator(); i.hasNext();) {
			Customer customer = i.next();
			if (customer.getUsername().equals(username)) {
				for(ClaimRecord claimRecord : customer.getClaimRecordList()) {
					if (claimRecord.getId() == claimId) {
						return claimRecord;
					}
				}
				return null;
			}
		}
		return null;
	}
        
    public boolean submitNewMessage(String username, int claimId, String sender, String message) {
        ArrayList<Customer> customers = DataProvider.loadCustomers();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = new Date();
        
		for(Customer customer : customers) {
			if (customer.getUsername().equals(username)) {
				for(ClaimRecord claimRecord : customer.getClaimRecordList()) {
					if (claimRecord.getId() == claimId) {
                        ClaimMessage claimMessage = new ClaimMessage(
                                sender.equals("AutoInSure") ? sender : customer.getName(), 
                                message, 
                                dateFormat.format(date));
                        claimRecord.addClaimMessage(claimMessage);
	                    DataProvider.storeCustomers(customers);
	                    return true;
					}
				}
            }
        }
		return false;
	}
    
    public boolean setClaimStatus(String username, int claimId, String status) {
        ArrayList<Customer> customers = DataProvider.loadCustomers();
        
		for(Customer customer : customers) {
			if (customer.getUsername().equals(username)) {
				for(ClaimRecord claimRecord : customer.getClaimRecordList()) {
					if (claimRecord.getId() == claimId) {
                        boolean res = claimRecord.setStatus(status);
	                    DataProvider.storeCustomers(customers);
	                    return res;
					}
				}
            }
        }
		return false;
	}

    // used from mobile app with integer sessionID 
    // used from console with String as username to send messages from AutoInSure to the user skipping a LOGIN and user
    public boolean submitNewMessage(int sessionId, int claimId, String message) {
		String username = _sessions.get(sessionId);
		if (username == null) {
			return false;
		}
        return submitNewMessage(username, claimId, username, message);
	}
        
    public Iterable<ClaimMessage> claimMessages (int sessionId, int claimId) {
		String username = _sessions.get(sessionId);
		if (username == null) {
			return null;
		}

        for(Customer customer : DataProvider.loadCustomers()) {
			if (customer.getUsername().equals(username)) {
				for(ClaimRecord claimRecord : customer.getClaimRecordList()) {
					if (claimRecord.getId() == claimId) {
						return claimRecord.getClaimMessageList(); 
					}
				}
	        }
        }
		return null;       
	}
        
	public boolean logout(int sessionId) {
		if (_sessions.containsKey(sessionId)) {
			_sessions.remove(sessionId);
			return true;
		}		
		return false;
	}

}
