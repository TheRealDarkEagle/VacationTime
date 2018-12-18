package holidays;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;



public class HolidayCalculator {
	
	private int year;
	private String stateOf;
	private StateOf state;
	
	public HolidayCalculator(String allInfos) {
		this.year =  Integer.valueOf(allInfos.replaceAll("[^0-9]", ""));
		this.stateOf = allInfos.replaceAll("[^a-z]", "");
		if(!stateOf.contains("alle")) {
			this.state = StateOf.valueOf(stateOf.toLowerCase());
		}
	}

	/**
	 * Einstiegspunkt 
	 * @throws IOException 
	 */
	public void calendarStart() {
		//Erstelle allHoliday liste 
		ArrayList<Day> allHoliday = new ArrayList<Day>(getEasterHolidays());
		//füge in Liste alle Feiertage ein, welche durch Ostern berechnet werden 
		allHoliday.addAll(getSolidHolidays());
		//Sortiere die Liste nach datum
		allHoliday = sortAfterDate(allHoliday);
		//Selektiere Liste nach eingebenem Bundesland
		if(!stateOf.contains("alle")){
			String whichHolidays = selectState();
			allHoliday = selectHolidays(allHoliday,whichHolidays);
		}
		//Erstelle Brückentage durch Feiertage 
		ArrayList<Day>bridgeDays = getBridgeDays(allHoliday);
		//Erstelle urlaub anhand feiertage 
		ArrayList<Day>vacation = sortOutOfHolidays(allHoliday);
		vacation = setVacation(vacation);
		//Beschreibe CSV Datei
		try {
			enterInCsvFile(allHoliday,bridgeDays, vacation);
		} catch (IOException e) {
			System.out.println("Es gab ein fehler beim beschreiben ihrere Datei!");
		}
	}
	
	//Setz listeneinträge auf vorherigen Montag bzw nächsten Sonntag
	private ArrayList<Day> setVacation(ArrayList<Day> vacation) {
		for(int a=0;a<vacation.size()-1;a+=2) {
			while(vacation.get(a).getGc().get(Calendar.DAY_OF_WEEK)!=2) {
				if(vacation.get(a).getDayOfMonth()==vacation.get(a).getGc().getActualMinimum(Calendar.DAY_OF_MONTH)) {
					vacation.get(a).getGc().set(Calendar.MONTH,vacation.get(a).getMonth()-1);
					vacation.get(a).getGc().set(Calendar.DAY_OF_MONTH, vacation.get(a).getGc().getActualMaximum(Calendar.DAY_OF_MONTH));
				}
				vacation.get(a).getGc().roll(Calendar.DAY_OF_MONTH, -1);
			}
			while(vacation.get(a+1).getGc().get(Calendar.DAY_OF_WEEK)!=1) {
				if(vacation.get(a+1).getDayOfMonth()==vacation.get(a+1).getGc().getActualMaximum(Calendar.DAY_OF_MONTH)) {
					vacation.get(a+1).getGc().set(Calendar.MONTH, vacation.get(a+1).getMonth()+1);
					vacation.get(a+1).getGc().set(Calendar.DAY_OF_MONTH, 1);
				}
				vacation.get(a+1).getGc().roll(Calendar.DAY_OF_MONTH, 1);;
			}
		}
		return vacation;
	}

	//Schreibt alle Listen in eine CSV Datei 
	private void enterInCsvFile(ArrayList<Day> holiday,ArrayList<Day> bridgeDay,ArrayList<Day> vacation) throws IOException {
		int allVacDays=0;
		int sumOfVacDays = 0;
		Path pathWithFile = Paths.get("D:\\develop\\java\\Holidays", "NoWorkTime.csv");
		BufferedWriter bw = Files.newBufferedWriter(pathWithFile);
		bw.write("Feiertage: \n\n");
		writeInFile(holiday, bw);
		bw.write("Potentielle Brueckentage:\n\n");
		writeInFile(bridgeDay, bw);
		bw.write("Potentielle Urlaubsmoeglichkeiten:\n\n");
		bw.write("von:;;;;bis:;;;;;gesamt:;;Verbrauchte Urlaubstage:\n");
		for(int a=0;a<vacation.size()-1;a+=2) {
			int numberOfVacDays = getNumberOfVacDays(vacation.get(a),vacation.get(a+1));
			sumOfVacDays += numberOfVacDays;
			int usedVacDays = numberOfVacDays-getUsedVacDays(vacation.get(a),vacation.get(a+1),holiday);
			allVacDays +=usedVacDays;
			String info = (vacation.get(a).getCsvEntry()+";"+vacation.get(a+1).getCsvEntry()).replace(',', ';');
			String zeile = String.format("%s;;%d;;%d\n", info,numberOfVacDays,usedVacDays);
			bw.write(zeile);
		}
		bw.newLine();
		bw.write("Gesamte verbrauchte Urlaubstage:\n");
		String usedVac = String.format("%d\n\n", allVacDays);
		bw.write(usedVac);
		bw.write("Gesamte Urlaubstage\n");
		String completeVacation = String.format("%d", sumOfVacDays);
		bw.write(completeVacation);
		System.out.println("Es wurde eine Date erstellt!");
		bw.close();
	}

	//Zählt wie viele Tage auf ein Wochenende oder Feiertag im Zeitraum liegen 
	private int getUsedVacDays(Day start, Day end,ArrayList<Day> holidays) {
		int usedVacDays=0;
		GregorianCalendar temp = new GregorianCalendar(year, start.getMonth(), start.getDayOfMonth());
		do {
			int maxDayInMonth = temp.getActualMaximum(Calendar.DAY_OF_MONTH);
			for(Day day:holidays) {
				if(temp.getTime().equals(day.getTime())&&!day.isWeekend()){
					usedVacDays++;
				}
			}
			if(temp.get(Calendar.DAY_OF_WEEK)==1 ||
			   temp.get(Calendar.DAY_OF_WEEK)==6) {
					usedVacDays++;
			}
			if(temp.get(Calendar.DAY_OF_MONTH)==maxDayInMonth) {
				temp.roll(Calendar.MONTH, 1);
				temp.set(Calendar.DAY_OF_MONTH, temp.getActualMinimum(Calendar.DAY_OF_MONTH));
			}else {
				temp.roll(Calendar.DAY_OF_MONTH, 1);
			}
		}while(!temp.after(end.getGc()));
		return usedVacDays;
	}

	private void writeInFile(ArrayList<Day> list, BufferedWriter bw) throws IOException {
		for(Day entry:list) {
			String infos = entry.getCsvEntry().replace(',', ';');
			String zeile = String.format("%s\n", infos);
			bw.write(zeile);
		}
		bw.newLine();
		bw.newLine();
	}
	
	//Zählt wie viele Tage zwischen start und ende liegen (start/ende miteingeschlossen) 
	private int getNumberOfVacDays(Day start, Day end) {
		GregorianCalendar gStart = new GregorianCalendar(year, start.getMonth(), start.getDayOfMonth());
		int numberofVacDays =0;
		do{
			System.out.println(gStart.getTime());
			if(gStart.get(Calendar.MONTH)!=end.getGc().get(Calendar.MONTH)) {
				numberofVacDays = (gStart.getActualMaximum(Calendar.DAY_OF_MONTH)-start.getGc().get(Calendar.DAY_OF_MONTH));
				gStart.roll(Calendar.MONTH, 1);
				gStart.set(Calendar.DAY_OF_MONTH,gStart.getActualMaximum(Calendar.DAY_OF_MONTH));
			}
			gStart.roll(Calendar.DAY_OF_MONTH, 1);
			numberofVacDays++;
		}while(!gStart.getTime().after(end.getTime()));
		return numberofVacDays;
	}
	
	//Prüft ob die tage nicht weiter als 2KW auseinander liegen -> wenn ja -> fügt beide tage in liste ien 
	private ArrayList<Day> OutsourceList(ArrayList<Day> vacation) {
		ArrayList<Day>vacationList = new ArrayList<Day>();
		for(int a =0; a<vacation.size()-1;a++) {
			if(vacation.get(a+1).getGc().get(Calendar.WEEK_OF_YEAR)-vacation.get(a).getGc().get(Calendar.WEEK_OF_YEAR)<2) {
				vacationList.add(new Day(new GregorianCalendar(year, vacation.get(a).getMonth(), vacation.get(a).getDayOfMonth())));
				vacationList.add(new Day(new GregorianCalendar(year, vacation.get(a+1).getMonth(), vacation.get(a+1).getDayOfMonth())));
			}
		}
		return vacationList;
	}

	//Gibt nur Tage in neue Liste welche nicht am Wochenende sind
	private ArrayList<Day> sortOutOfHolidays(ArrayList<Day> allHoliday) {
		ArrayList<Day>noWeekend = new ArrayList<Day>();
		for(Day day:allHoliday) {
			if(!day.isWeekend()) {
				noWeekend.add(day);
			}
		}
		return OutsourceList(noWeekend);
	}
	
	//Legt Brückentage anhand von holiday liste fest 
	private ArrayList<Day> getBridgeDays(ArrayList<Day> allHoliday) {
		ArrayList<Day> tempBridgeDays = new ArrayList<Day>();
		for(Day day:allHoliday) {
			if(day.getDay()==2||day.getDay()==5){
				tempBridgeDays.add(new Day(false, true, false, new GregorianCalendar(year, day.getMonth(), day.getDayOfMonth()+1), ""));
			}else if(day.getDay()==3||day.getDay()==6) {
				tempBridgeDays.add(new Day(false, true, false, new GregorianCalendar(year, day.getMonth(), day.getDayOfMonth()-1), ""));
			}
		}
		return tempBridgeDays;
	}

	//Erstellt anhand der Feiertagsliste vom Bundesland die Selektierte Feiertagsliste 
	private ArrayList<Day> selectHolidays(ArrayList<Day> allHoliday,String acceptedHolidays) {
		ArrayList<String> splitHolidayString = splitString(acceptedHolidays);
		ArrayList<Day> tempList = new ArrayList<Day>();
		for(String nameOfHoliday: splitHolidayString) {
			for(Day date: allHoliday) {
				if(nameOfHoliday.equals(date.getName())){
					tempList.add(date);
				}
			}
		}
		return tempList;
	}

	//Trennt die einzlenen Feiertagsnamen aus dem String des Bundeslandes 
	private ArrayList<String> splitString(String acceptedHolidays) {
		ArrayList<String> holdHolidays = new ArrayList<String>(); 
		int start = 0;
		int end = 0;
		for(int a = 0; a< acceptedHolidays.length();a++) {
			char testChar = acceptedHolidays.charAt(a);
			if(testChar == ',') {
				end = a;
				holdHolidays.add(acceptedHolidays.substring(start, end));
				start = a+2;
			}
		}
		holdHolidays.add(acceptedHolidays.substring(start,acceptedHolidays.length()));
		return holdHolidays;
	}

	//Ruft die diesbezügliche Enum Variante des Feiertages auf. Bekommt die Namen der Feiertages des Bundeslandes als String zurük
	private String selectState() {
		String whichHoliday = "";
		switch (state) {
		case badenwürttemberg:
			state = StateOf.badenwürttemberg;
			whichHoliday = state.getHoliday();
			break;
		case bayern:
			state = StateOf.bayern;
			whichHoliday = state.getHoliday();
			break;
		case berlin:
			state = StateOf.berlin;
			whichHoliday = state.getHoliday();
			break;
		case brandenburg:
			state = StateOf.brandenburg;
			whichHoliday = state.getHoliday();
			break;
		case bremen:
			state = StateOf.bremen;
			whichHoliday = state.getHoliday();
			break;
		case hamburg:
			state = StateOf.hamburg;
			whichHoliday = state.getHoliday();
			break;
		case hessen:
			state = StateOf.hessen;
			whichHoliday = state.getHoliday();
			break;
		case mecklenburgvorpommern:
			state = StateOf.mecklenburgvorpommern;
			whichHoliday = state.getHoliday();
			break;
		case niedersachsen:
			state = StateOf.niedersachsen;
			whichHoliday = state.getHoliday();
			break;
		case nordrheinwestfalen:
			state = StateOf.nordrheinwestfalen;
			whichHoliday = state.getHoliday();
			break;
		case rheinlandpfalz:
			state = StateOf.rheinlandpfalz;
			whichHoliday = state.getHoliday();
			break;
		case saarland:
			state = StateOf.saarland;
			whichHoliday = state.getHoliday();
			break;
		case sachsen:
			state = StateOf.sachsen;
			whichHoliday = state.getHoliday();
			break;
		case sachsenanhalt:
			state = StateOf.sachsenanhalt;
			whichHoliday = state.getHoliday();
			break;
		case schleswigholstein:
			state = StateOf.schleswigholstein;
			whichHoliday = state.getHoliday();
			break;
		case thüringen:
			state = StateOf.thüringen;
			whichHoliday = state.getHoliday();
			break;
		default:
			System.out.println("Etwas ist schief gelaufen... bitte neustart!");
			break;
		}
		return whichHoliday;
	}

	//Sortiert die Feiertagsliste nach der Richtigen Reihenfolge der Tage
	private ArrayList<Day> sortAfterDate(ArrayList<Day> allHoliday) {
		for(int a = 0;a<allHoliday.size();a++) {
			for(int b = 0;b<allHoliday.size()-1;b++) {
				//Wenn b nach b+1 liegt -> temp = b | b = b+1 | b+1 = temp 
				if(allHoliday.get(b).getTime().after(allHoliday.get(b+1).getTime())) {
					Day temp = allHoliday.get(b);
					allHoliday.set(b, allHoliday.get(b+1));
					allHoliday.set(b+1, temp);
				}
			}
		}
		return allHoliday;
	}

	//Erstellt Liste mit allen Feiertagen, welche von Ostern abhängig sind 
	private ArrayList<Day> getEasterHolidays(){
		ArrayList<Day> allEasterHolidays = new ArrayList<Day>();
		allEasterHolidays.add(new Day(true, false, false, holiDaysCalculatedFromEaster(year, -2), "Karfreitag"));
		allEasterHolidays.add(new Day(true, false, false, holiDaysCalculatedFromEaster(year, 0), "Ostersonntag"));
		allEasterHolidays.add(new Day(true, false, false, holiDaysCalculatedFromEaster(year, 1), "Ostermontag"));
		allEasterHolidays.add(new Day(true, false, false, holiDaysCalculatedFromEaster(year, 39), "Christi Himmelfahrt"));
		allEasterHolidays.add(new Day(true, false, false, holiDaysCalculatedFromEaster(year, 49), "Pfingstsonntag"));
		allEasterHolidays.add(new Day(true, false, false, holiDaysCalculatedFromEaster(year, 50), "Pfingstmontag"));
		allEasterHolidays.add(new Day(true, false, false, holiDaysCalculatedFromEaster(year, 60), "Fronleichnahm"));
		return allEasterHolidays;
	}
	
	//Erstellt Liste mit allen festen Feiertagen 
	private ArrayList<Day> getSolidHolidays(){
		ArrayList<Day> solidHolidays = new ArrayList<Day>();
		solidHolidays.add(new Day(true, false, false, new GregorianCalendar(year, 0, 1), "Neujahr"));
		solidHolidays.add(new Day(true, false, false, new GregorianCalendar(year, 0, 6), "Heilige Drei Koenige"));
		solidHolidays.add(new Day(true, false, false, new GregorianCalendar(year, 4, 1), "Tag der Arbeit"));
		solidHolidays.add(new Day(true, false, false, new GregorianCalendar(year, 7, 8), "Augsburger Friedensfest(Nur in Augsburg!)"));
		solidHolidays.add(new Day(true, false, false, new GregorianCalendar(year, 7, 15), "Mariae Himmelfahrt"));
		solidHolidays.add(new Day(true, false, false, new GregorianCalendar(year, 9, 03), "Tag der Deutschen Einheit"));
		solidHolidays.add(new Day(true, false, false, new GregorianCalendar(year, 9, 31), "Reformationstag"));
		solidHolidays.add(new Day(true, false, false, new GregorianCalendar(year, 10, 01), "Allerheiligen"));
		solidHolidays.add(new Day(true, false, false, new GregorianCalendar(year, 10, 21), "Buss- und Bettag"));
		solidHolidays.add(new Day(true, false, false, new GregorianCalendar(year, 11, 25), "1.Weihnachtsfeiertag"));
		solidHolidays.add(new Day(true, false, false, new GregorianCalendar(year, 11, 26), "2.Weihnachtsfeiertag"));
		return solidHolidays;
	}
		
	

	/**
	 * Berechnung der von Ostern abhängigen Feiertagen (Ostern als Basis)
	 * @param year
	 * @param awayFromEaster
	 * @return
	 */
	private GregorianCalendar holiDaysCalculatedFromEaster (int year, int awayFromEaster) {
		int i = year % 19;
		int j = year / 100;
		int k = year % 100;
		int l = (19 * i + j - (j / 4) - ((j - ((j + 8) / 25) + 1) / 3) + 15) % 30;
		int m = (32 + 2 * (j % 4) + 2 * (k / 4) - l - (k % 4)) % 7;
		int n = l + m - 7 * (( i + 11 * l + 22 * m) / 451 ) + 114;
		int month = n / 31;
		int day = (n % 31) + 1;
		return new GregorianCalendar (year, month-1, day+awayFromEaster);
	}
	
	

	
	
	
}