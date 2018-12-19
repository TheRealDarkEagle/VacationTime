package holidays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HoliDaysMain {

 
	public static void main(String[] args) {
		System.out.println("Willkommen bei dem neuen FeiertagsKalendar\n Bitte geben Sie Bundesland und Jahr ein.\n Wenn Sie KEINE Selection des Bundeslandes möchten,\n dann geben Sie bitte "
				+  (char)34+"alle"+(char)34 +" ein:");
		HolidayCalculator hc = new HolidayCalculator(input().toLowerCase());
		hc.calendarStart();
	}
	private static String input(){
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		
		String eingabe ="";
		try {
			eingabe = br.readLine();
		} catch (IOException e) {
			System.out.println("Etwas ist schief gelaufen....!");
		}
		return eingabe;
	}
}