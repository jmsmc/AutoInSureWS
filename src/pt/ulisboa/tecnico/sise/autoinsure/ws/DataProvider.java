package pt.ulisboa.tecnico.sise.autoinsure.ws;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import pt.ulisboa.tecnico.sise.autoinsure.datamodel.ClaimMessage;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.ClaimRecord;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.Customer;
import pt.ulisboa.tecnico.sise.autoinsure.datamodel.Person;

import java.io.FileNotFoundException;
import org.json.simple.parser.ParseException;

public class DataProvider {

	private static final String DATAFILE = 
			//"/home/username/Desktop/CreditInSureWSServer/data/insuredata.txt";			//linux
			//"C:\\Users\\username\\Desktop\\CreditInSureWSServer\\data\\insuredata.txt";	//windows
			"./data/insuredata.json";														// relative path
	/*
	 * The DATAFILE constant must be updated with the correct location of the
	 * file where the server keeps the JSON data persistent.
	 * 
	 * In Windows, to represent the FULL path, the directory separators use two back slashes, e.g.:
	 * 		DATAFILE = "c:\\Documents\\Test\\AutoInSureWS\\CreditInSureWSServer\\data\\insuredata.txt"
	 * Relative paths remain with forward slash /
	 */

	//set to true to run DataGenerator.main and then set to false to keep saving changes in file 
	private static final boolean MEMORYSOURCE = false;

	@SuppressWarnings("serial")
	public static ArrayList<Customer> initializeDummyData() {
		return new ArrayList<Customer>() {{
			// customer 1
			add(new Customer(
				"j",								// login username
				"j",								// login password
				1112222333,							// policy Number
				new Person(
					"Joao Silva",					// name
					1234,							// fiscal number
					"Rua das Flores 21, Lisboa",	// address
					"23-01-1960"					// date of birth
					),
				new ArrayList<ClaimRecord>() {{
					add(new ClaimRecord(
						1,
						"Claim 1 Title",
						"02-07-2018",				// submissionDate
						"01-07-2018",				// occurrenceDate
						"88-JJ-99",
						"Description of claim 1.",
						ClaimRecord.STATUS_PENDING,
                        new ArrayList<ClaimMessage>() {{
                            add(new ClaimMessage("Joao Silva", "If you need any more information, contact me.", "17-01-2016 11:15"));
                        }}));
				}},
				new ArrayList<String>(){{
					add("88-JJ-99");
					add("88-JJ-99");
					add("88-JJ-99");
				}}
			));
			
			// customer 2
			add(new Customer(
				"m",								// login username
				"m",								// login password
				202020202,							// policy Number
				new Person(
					"Maria Albertina",				// name
					211,							// fiscal number
					"Av. da Liberdade 1, Lisboa",	// address
					"16-02-1986"					// date of birth
					),
				new ArrayList<ClaimRecord>() {{
					add(new ClaimRecord(
						1,
						"Claim 1 Title",
						"20-06-2018",				// submissionDate
						"20-06-2018",				// occurrenceDate
						"88-MM-88",					// plate
						"Description of claim 2.",
						ClaimRecord.STATUS_ACCEPTED,
                        new ArrayList<ClaimMessage>() {{
                            add(new ClaimMessage("Maria Albertina", "Any news regarding this claim?", "20-06-2018 17:15"));
                            add(new ClaimMessage("AutoInSure", "Yes, the claim was accepted!", "21-06-2018 09:15"));
                        }}
					));
					add(new ClaimRecord(
						2,
						"Claim 2 Title",
						"26-06-2018",				// submissionDate
						"25-06-2018",				// occurrenceDate
						"88-MM-99",					// plate
						"Description of claim 3.",
						"denied",
		                new ArrayList<ClaimMessage>() {{
		                    add(new ClaimMessage("AutoInSure", "Your claim was denied due to ...", "25-06-2018 14:15"));
		                }}
					));
				}},
				new ArrayList<String>() {{
					add("88-MM-99");
					add("88-MM-88");
				}}
			));
		}};
	}

	private static ArrayList<Customer> _customers = null;
	
	public static ArrayList<Customer> loadCustomersFromMemory () {
		if (_customers == null) {
			_customers = initializeDummyData();
		}
		return _customers;
	}

	public static void storeCustomersInMemory(ArrayList<Customer> customers) {
		_customers = customers;
	}
	
	public static ArrayList<Customer> loadCustomersFromFile() {
		JSONParser parser = new JSONParser();
		FileReader fileReader;
		JSONArray jsonCustomers = null;
		try {
			fileReader = new FileReader(DATAFILE);
			jsonCustomers = (JSONArray) parser.parse(fileReader);
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(jsonCustomers == null) {
			return null;
		}

		try {
			ArrayList<Customer> customerList = new ArrayList<>();
			for(Object jsonPersonCustomerObj : jsonCustomers) {
				JSONObject jsonPersonCustomer = (JSONObject)jsonPersonCustomerObj;
				
				String username = (String) jsonPersonCustomer.get("username");
				String password = (String) jsonPersonCustomer.get("password");				
				int policyNumber = (int) (long) jsonPersonCustomer.get("policyNumber");
				
				JSONObject jsonPerson 	= (JSONObject) jsonPersonCustomer.get("person");
				String name 			= (String) jsonPerson.get("name");
				int fiscalNumber 		= (int) (long) jsonPerson.get("fiscalNumber");
				String address 			= (String) jsonPerson.get("address");
				String dateOfBirth 		= (String) jsonPerson.get("dateOfBirth");
				
				Person person = new Person(name, fiscalNumber, address,
						dateOfBirth);

				JSONArray jsonClaimList = (JSONArray) jsonPersonCustomer.get("claimList");
				ArrayList<ClaimRecord> claims = new ArrayList<>();
				for(Object jsonClaimRecordObj: jsonClaimList) {
					JSONObject jsonClaimRecord = (JSONObject)jsonClaimRecordObj;
					int claimId = (int) (long) jsonClaimRecord.get("claimId");
					String submissionDate = (String) jsonClaimRecord.get("submissionDate");
					String occurrenceDate = (String) jsonClaimRecord.get("occurrenceDate");
					String claimTitle = (String) jsonClaimRecord.get("claimTitle");
					String plate = (String) jsonClaimRecord.get("plate");
					String description = (String) jsonClaimRecord.get("description");
					String status = (String) jsonClaimRecord.get("status");
                    
					JSONArray jsonMessageList = (JSONArray) jsonClaimRecord.get("messageList");
                    ArrayList<ClaimMessage> messages = new ArrayList<>();
                    for(Object jsonMessageObj : jsonMessageList) {
                        JSONObject jsonMessage = (JSONObject)jsonMessageObj;
                        String msgSender = (String) jsonMessage.get("sender");
                        String msg = (String) jsonMessage.get("msg");
                        String msgDate = (String) jsonMessage.get("date");
                        messages.add(new ClaimMessage(msgSender, msg, msgDate));
                    }
					ClaimRecord record = new ClaimRecord(claimId, claimTitle, submissionDate, occurrenceDate, plate,
							description, status, messages);
					claims.add(record);
				}
				
				JSONArray jsonPlateList = (JSONArray) jsonPersonCustomer.get("plateList");
				ArrayList<String> plates = new ArrayList<>();
				for(Object jsonPlatesObj: jsonPlateList) {
					String plate = (String)jsonPlatesObj;
					plates.add(plate);
				}
				
				Customer customer = new Customer(username, password, policyNumber, person, claims, plates);
				customerList.add(customer);
			}
			return customerList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static void storeCustomersInFile(ArrayList<Customer> customerList) {
		if (customerList == null) return;
		
		JSONArray jsonCustomerList = new JSONArray();
		for(Customer customer : customerList) {			
			JSONArray jsonClaimList = new JSONArray();
			for(ClaimRecord claimRecord : customer.getClaimRecordList()) {
				JSONObject jsonClaimRecord = new JSONObject();
				jsonClaimRecord.put("claimId", claimRecord.getId());
				jsonClaimRecord.put("claimTitle", claimRecord.getTitle());
				jsonClaimRecord.put("submissionDate", claimRecord.getSubmissionDate());
				jsonClaimRecord.put("occurrenceDate", claimRecord.getOccurrenceDate());
				jsonClaimRecord.put("plate", claimRecord.getPlate());
				jsonClaimRecord.put("description", claimRecord.getDescription());
				jsonClaimRecord.put("status", claimRecord.getStatus());
                JSONArray jsonMessageList = new JSONArray();
                
                for(ClaimMessage claimMessage : claimRecord.getClaimMessageList()) {
                        JSONObject jsonMessage = new JSONObject();
                        jsonMessage.put("msg", 	  claimMessage.getMessage());
                        jsonMessage.put("date",   claimMessage.getDate());
                        jsonMessage.put("sender", claimMessage.getSender());
                        jsonMessageList.add(jsonMessage);
                }
                jsonClaimRecord.put("messageList", jsonMessageList);
				jsonClaimList.add(jsonClaimRecord);
			}
			
			JSONArray jsonPlateList = new JSONArray();
			for(String plate : customer.getPlateList()) {
				jsonPlateList.add(plate);
			}

			JSONObject jsonPerson = new JSONObject();
			jsonPerson.put("name", 		   customer.getName());
			jsonPerson.put("fiscalNumber", customer.getFiscalNumber());
			jsonPerson.put("address", 	   customer.getAddress());
			jsonPerson.put("dateOfBirth",  customer.getDateOfBirth());

			JSONObject jsonCustomer = new JSONObject();
			jsonCustomer.put("username",	 customer.getUsername());
			jsonCustomer.put("password",	 customer.getPassword());
			jsonCustomer.put("policyNumber", customer.getPolicyNumber());
			jsonCustomer.put("person",	 	 jsonPerson);
			jsonCustomer.put("claimList",	 jsonClaimList);
			jsonCustomer.put("plateList",	 jsonPlateList);
			
			jsonCustomerList.add(jsonCustomer);
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(jsonCustomerList.toJSONString());
		String prettyJsonString = gson.toJson(je);

		try {			
			FileWriter file = new FileWriter(DATAFILE);
			file.write(prettyJsonString);
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Customer> loadCustomers () {
		if (MEMORYSOURCE) {
			return loadCustomersFromMemory();
		} else {
			return loadCustomersFromFile();
		}
	}
	
	public static void storeCustomers(ArrayList<Customer> customers) {
		if (MEMORYSOURCE) {
			storeCustomersInMemory(customers);
		} else {
			storeCustomersInFile(customers);
		}
	}
}
