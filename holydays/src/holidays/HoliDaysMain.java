package holidays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HoliDaysMain {

 
	public static void main(String[] args) throws IOException {
		boolean allStates = false;
		System.out.println("Willkommen! für welches Bundesland möchten Sie gerne die Feiertage ausgegeben haben?\nFür alle Bundesländer bitte \"alle\" eingeben:");
		String whichStateToSelect = callToWrite();
		allStates = searchAllStates(whichStateToSelect);
		System.out.println("Fast geschafft... nun bitte noch das Jahr für dessen Sie die Feiertage wissen möchten.");
		int year = insertYear();
		new Holidays(allStates, whichStateToSelect).calendar(year);
		
		System.out.println("Es wurde eine CSV Datei für Sie erstellt.\nPfad: D:\\develop\\java\\Holidays\\Urlaub.csv");
	}
	private static int insertYear() throws IOException {
		String yearAsString = callToWrite();
		Integer year = Integer.parseInt(yearAsString);
		return year;
	}
	//Setzt bei eingabe von "all" boolean auf true, alle Feiertage für jedes Bundesland werden ausgegeben 
	private static boolean searchAllStates(String stateToSelectSearch) {
		if(stateToSelectSearch.equals("alle")) {
			return true;
		}
		return false;
	}
	
	//Eingabe des Bundeslandes für die Feiertage, welche man ausgegeben haben möchte
	private static String callToWrite() throws IOException {
		InputStreamReader state = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(state);
		String stateToSearch = br.readLine();
		return stateToSearch;
	}

}
