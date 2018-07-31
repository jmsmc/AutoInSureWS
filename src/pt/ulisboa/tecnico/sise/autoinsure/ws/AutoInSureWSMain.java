package pt.ulisboa.tecnico.sise.autoinsure.ws;

import java.util.Arrays;
import java.util.Scanner;
import javax.xml.ws.Endpoint;

public class AutoInSureWSMain {
	public static void main(String[] args) {
		Logger.logStartMessage();
		Endpoint endpoint = Endpoint.publish("http://localhost:8080/", new AutoInSureWSImpl());
        
        printNewMessageHelp();
        printSetStatusHelp();
        System.out.print("> ");
        Scanner inputScanner = new Scanner(System.in);
        String line;
        boolean FLAG_EXIT = false;
        while (!FLAG_EXIT && (line = inputScanner.nextLine().trim()) != null) {
        	if(line.equals("")) {
        		printCursor();
        		continue;
        	}
        	String[] tokens = line.split(" ");
        	String username;
        	int claimId;
            switch(tokens[0].trim()) {
                case "help":
            		printNewMessageHelp();
            		printSetStatusHelp();
                    break;
                case "newMessage":
                	if(tokens.length < 4) {
                		System.out.println("Not enough arguments." + askHelpString());
                		break;
                	}
                    username = tokens[1].trim();
                    try {
                    	claimId = Integer.parseInt(tokens[2].trim());
                    }catch (NumberFormatException e){
                    	System.out.println("ERROR: claimId must be an integer." + askHelpString());
                    	break;
                    }
                    String msg = "";
                    for (String s : Arrays.copyOfRange(tokens, 3, tokens.length)) {
                        msg = msg + " " + s;
                    }
                    boolean result = DataManager.getInstance().submitNewMessage(username, claimId, "AutoInSure", msg);
                    System.out.println("newMessage => " + result);
                    break;
                case "setStatus":
                	if(tokens.length < 4) {
                		System.out.println("Not enough arguments." + askHelpString());
                		break;
                	}
                    username = tokens[1].trim();
                    try {
                    	claimId = Integer.parseInt(tokens[2].trim());
                    }catch (NumberFormatException e){
                    	System.out.println("ERROR: claimId must be an integer." + askHelpString());
                    	break;
                    }
                    String status = tokens[3].trim().toLowerCase();
                    if(DataManager.getInstance().setClaimStatus(username, claimId, status)) {
                    	System.out.println("setStatus => true");
                    }else {
                    	System.out.println("Invalid status." + askHelpString());
                    }
                    break;
                case "quit":
                	FLAG_EXIT = true;
                	break;
                default:
                	System.out.println("Invalid command." + askHelpString());
            }
            printCursor();
        }
        inputScanner.close();
        System.out.println("Terminating server");
        endpoint.stop();
        System.exit(0);
	}
	
	private static void printNewMessageHelp() {
		System.out.println("You can send new claim messages using this command line");
	    System.out.println("Sintax: newMessage <int:username> <int:claimId> <String:message>");
	    System.out.println("Example:");
	    System.out.println("> newMessage j 1 We are currently handling your claim.");
    }
	
	private static void printSetStatusHelp() {
		System.out.println("You can set claim statuses using this command line");
	    System.out.println("Sintax: setStatus <int:username> <int:claimId> <String:status>");
	    System.out.println("Example:");
	    System.out.println("> setStatus j 1 accepted");
    }
	
	private static void printCursor(String prompt) {
		System.out.print(prompt);
	}
	private static void printCursor() {
		printCursor("> ");
	}
	private static String askHelpString() {
		return " Type \"help\" for more info.";
	}
}