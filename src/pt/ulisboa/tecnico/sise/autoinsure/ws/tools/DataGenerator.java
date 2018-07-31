package pt.ulisboa.tecnico.sise.autoinsure.ws.tools;

import java.util.ArrayList;

import pt.ulisboa.tecnico.sise.autoinsure.datamodel.Customer;
import pt.ulisboa.tecnico.sise.autoinsure.ws.DataProvider;

//  used just for testing the automatic creation of a data-file from the dummyData
public class DataGenerator {
	
	public static void main(String[] args) {
		DataProvider.storeCustomersInFile(DataProvider.initializeDummyData());
		ArrayList<Customer> customers = DataProvider.loadCustomersFromFile();
		DataProvider.storeCustomersInFile(customers);
		System.out.println("DummyData saved to file");
	}
}
