package holidays;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;


/**
 * @TODO:
 * ausgabe der feiertage anhand bundesländer
 * zeitraum des urlaubs durch brückentage
 * aussehen (brückentage):
 * Feiertag 	von		bis 	start wochentag 	ende wochentag 	freie Tage
 * nicht aussortieren wenn allStates true
 * 
 * @author danzk
 *
 */
public class Holidays {
	
	boolean allStates;
	String yourState;
	
	//Konstruktor
	public Holidays(boolean allStates, String yourState) {
		this.allStates = allStates;
		this.yourState = yourState;
	}
	
	/**
	 * Einstiegspunkt 
	 * @throws IOException 
	 */
	public void calendar(int year) throws IOException {
		
		ArrayList<GregorianCalendar> allHoliDays = new ArrayList<GregorianCalendar>();
		//Liste mit Feiertagen, welche von Ostern abgeleitet werden 
		allHoliDays = holidaysFromEastern(year);
		//Hinzufügen der festen Feiertagen zur FeiertagsListe
		allHoliDays.addAll(setSolidHoliDays(year));
		//Sortierung der Liste mit ALLEN Feiertagen
		Collections.sort(allHoliDays);
		
		ArrayList<Holiday>completeHoliday = allDataForHolidays(allHoliDays);
		//Herausfinden der Feiertage anhand allen Feiertagen 
		ArrayList<GregorianCalendar>bridgingDays = bridgingDay(completeHoliday);
		//Überarbeitung der Brückentagsliste anhand schon vorhanden einträgen (ausgehend von feiertagsliste)
		bridgingDays = deleteDoubleDays(bridgingDays,completeHoliday);
		//ausgabe der feiertage und potentiellen brückentagen des eingegebenen Jahres - OBSOLET!
//		outputHoliDays(bridgingDays,completeHoliday,year);
		writeIntoFile(bridgingDays,completeHoliday);
				
	}
	
	//Berechnung der von Ostern abhängigen Feiertagen (Ostern als Basis)
	private Calendar holiDaysCalculatedFromEaster (int year, int awayFromEaster) {
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
	
	//Liste mit int Werten, wie weit bestimmte Feiertage von Ostern entfernt sind
	private ArrayList<Integer> loadDataForEasterHolidays() {
		ArrayList<Integer> daysForHolidaysAroundEastern = new ArrayList<Integer>();
		daysForHolidaysAroundEastern.add(-2);
		daysForHolidaysAroundEastern.add(0);
		daysForHolidaysAroundEastern.add(1);
		daysForHolidaysAroundEastern.add(39);
		daysForHolidaysAroundEastern.add(49);
		daysForHolidaysAroundEastern.add(50);
		daysForHolidaysAroundEastern.add(60);
		return daysForHolidaysAroundEastern;
	}
	
	//Erstellen einer GregorianCalendar Liste mit den Feiertagen, welche von Ostern aus berechnet werden
	private ArrayList<GregorianCalendar> holidaysFromEastern (int year) {
		ArrayList<Integer> daysOfEasterHoliDays = loadDataForEasterHolidays();
		ArrayList<GregorianCalendar> holiDaysCalculatedFromEastern = new ArrayList<GregorianCalendar>();
		for (Integer fromEaster : daysOfEasterHoliDays) {
			int dayFromEaster = fromEaster;
			GregorianCalendar cal = (GregorianCalendar) holiDaysCalculatedFromEaster(year,dayFromEaster);
			holiDaysCalculatedFromEastern.add(cal);
		}
		return holiDaysCalculatedFromEastern;
		
	}
	
	//Erstellung einer Liste mit allen Festen Feiertagen -> Datumsangaben werden durch 2 int[] eingefügt
	private ArrayList<GregorianCalendar> setSolidHoliDays(int year){
		ArrayList<GregorianCalendar> solidHoliDay = new ArrayList<GregorianCalendar>();
		int[] month = 	{0,0,4,7,7,9,9,10,10,11,11};
		int[] day = 	{1,6,1,8,15,03,31,01,20,25,26};
		for(int i=0;i<month.length;i++) {
			solidHoliDay.add(new GregorianCalendar(year,month[i],day[i]));
		}
		return solidHoliDay;
	}
	
	//Speicherung der Tage, welche maßgebend sind für Brückentage (Montag/Dienstag/Donnerstag/Freitag), in bridgeDay Liste 
	private ArrayList<GregorianCalendar> bridgingDay(ArrayList<Holiday> holiDays) {
		ArrayList<GregorianCalendar> bridgeDay = new ArrayList<GregorianCalendar>();
		//Geht die Liste holiDays durch und speichert bei montag/Freitag den kommenden/vorherigen Tag in neuer liste ab
		for(Holiday holiDay : holiDays) {
			//Sonntag = 1 -> Samstag = 7
			int holiDayDay = holiDay.cal.get(Calendar.DAY_OF_WEEK);
			if(holiDayDay<7 && holiDayDay>1) {
				//Speicherung der Datumsangabe in bridgeDay Liste anhand von Tag des Feiertages 
				switch(holiDayDay) {
				//Montag und Donnerstag
				case 2:
				case 5:
					bridgeDay.add(new GregorianCalendar(holiDay.cal.get(Calendar.YEAR), holiDay.cal.get(Calendar.MONTH), holiDay.cal.get(Calendar.DAY_OF_MONTH)+1));
					break;
				//Dienstag und Freitag
				case 3:
				case 6:
					bridgeDay.add(new GregorianCalendar(holiDay.cal.get(Calendar.YEAR), holiDay.cal.get(Calendar.MONTH), holiDay.cal.get(Calendar.DAY_OF_MONTH)-1));
					break;
				}
			}
		}
	return bridgeDay;
	}
	
	//Durchsuchen der Listen nach schon vorhandenen Einträgen -> Erstellung überarbeiter bridgingDays Liste  
	private ArrayList<GregorianCalendar> deleteDoubleDays(ArrayList<GregorianCalendar> bridgingDays,ArrayList<Holiday> allHoliDays) {
		ArrayList<GregorianCalendar> reworkedBridgingDays = new ArrayList<GregorianCalendar>();
		//Nehme datumsangabe aus brückentag-Liste (monat & tag) vergleiche mit datumsangaben aus feiertagsliste (monat & tag)
		for(GregorianCalendar bridgeDay:bridgingDays) {
			boolean dayExist = false;
			int bridgeDayMonth = bridgeDay.get(Calendar.MONTH);
			int bridgeDayDay = bridgeDay.get(Calendar.DAY_OF_MONTH);
			for(Holiday holiDay:allHoliDays) {
				int holiDayMonth = holiDay.cal.get(Calendar.MONTH);
				int holiDayDay = holiDay.cal.get(Calendar.DAY_OF_MONTH);
				//Wenn monat und Tag nicht gleich speichere in neue liste 
				if(bridgeDayMonth==holiDayMonth && bridgeDayDay==holiDayDay) {
					dayExist = true;
				}
			}
			if(!dayExist) {
				reworkedBridgingDays.add(new GregorianCalendar(bridgeDay.get(Calendar.YEAR), bridgeDayMonth, bridgeDayDay));
			}
		}
		return reworkedBridgingDays;
	}
	
	
	/**
	 * @TODO: 
	 * schreiben in datei überarbeiten : 
	 * aussehen (brückentage):
	 * Feiertag 	von		bis 	start wochentag 	ende wochentag 	freie Tage
	 * @param bridgingDays
	 * @param allHoliDays
	 * @throws IOException
	 */
	//Schreibt die Datumsangaben in eine externe CSV File
	private void writeIntoFile(ArrayList<GregorianCalendar> bridgingDays, ArrayList<Holiday> allHoliDays) throws IOException {
		//wenn nicht nach allen bundesländern gesucht wird 
		//schreibe nur die einträge des gesuchten bundeslandes in die csv datei
		if(!allStates) {
		allHoliDays = selectionOfState(allHoliDays);
		}
		Path pfadMitDatei = Paths.get("D:\\develop\\java\\Holidays\\Urlaub.csv");
			try(BufferedWriter writePuffer = Files.newBufferedWriter(pfadMitDatei)){
				writePuffer.write("gesetzliche Feiertage");
				writePuffer.newLine();
				writePuffer.write("Feiertag;Tag;Monat;Jahr;Wochentag");
				writePuffer.newLine();
				for(Holiday holiDay: allHoliDays) {
					String nameOfHoliday = holiDay.name;
					String weekDay = whichWeekDay(holiDay.cal.get(Calendar.DAY_OF_WEEK));
					int day = holiDay.cal.get(Calendar.DAY_OF_MONTH);
					String month = whichMonth(holiDay.cal.get(Calendar.MONTH));
					int year = holiDay.cal.get(Calendar.YEAR);
					String zeile = String.format("%s;%d;%s;%d;%s%n", nameOfHoliday,day,month,year,weekDay);
					writePuffer.write(zeile);
				}
				writePuffer.write("Potentielle Brueckentage");
				writePuffer.newLine();
				writePuffer.write(";Tag;Monat;Jahr;Wochentag");
				writePuffer.newLine();
				for(GregorianCalendar bridgeDay: bridgingDays) {
					String weekDay1 = whichWeekDay(bridgeDay.get(Calendar.DAY_OF_WEEK));
					int day = bridgeDay.get(Calendar.DAY_OF_MONTH);
					String month = whichMonth(bridgeDay.get(Calendar.MONTH));
					int year = bridgeDay.get(Calendar.YEAR);
					String zeile = String.format("%s;%d;%s;%d;%s%n", " ",day,month,year,weekDay1);
					writePuffer.write(zeile);
//				}
			}
		}
	}

	private ArrayList<Holiday> selectionOfState(ArrayList<Holiday> allHoliDays) {
		ArrayList<Holiday> shadowListHoliDays = new ArrayList<Holiday>();
		for(Holiday list: allHoliDays) {
			if(list.stateOF.contains(yourState))
			shadowListHoliDays.add(list);
		}
		return shadowListHoliDays;
	}

	//Gibt den Wochentag als String zurück,
	private String whichWeekDay(int numberOfWeekday) {
		String wochenTag = "";
		switch (numberOfWeekday) {
		case 1: 
			wochenTag = "Sonntag";
			break;
		case 2: 
			wochenTag = "Montag";
			break;
		case 3: 
			wochenTag = "Dienstag";
			break;
		case 4: 
			wochenTag = "Mittwoch";
			break;
		case 5:
			wochenTag = "Donnerstag";
			break;
		case 6:
			wochenTag = "Freitag";
			break;
		case 7: 
			wochenTag = "Samstag";
			break;
		}
		return wochenTag;
	}
			
	
	//Gibt den Monat als String zurück
	private String whichMonth(int holiDay) {
		String month ="";
		switch(holiDay) {
		case 0: 
			month = "Januar";
			break;
		case 1: 
			month = "Februar";
			break;
		case 2: 
			month = "Maerz";
			break;
		case 3: 
			month = "April";
			break;
		case 4: 
			month = "Mai";
			break;
		case 5: 
			month = "Juni";
			break;
		case 6: 
			month = "Juli";
			break;
		case 7: 
			month = "August";
			break;
		case 8: 
			month = "September";
			break;
		case 9: 
			month = "Oktober";
			break;
		case 10: 
			month = "November";
			break;
		case 11: 
			month = "Dezember";
			break;
		}
		return month;
	}

	private String allStatesOfGermany() {
		return "Baden-Württemberg,Bayern,Berlin,Brandenburg,Bremen,Hamburg,Hessen,Mecklenburg-Vorpommern,Niedersachsen,Nordrhein-Westfalen,Rheinland-Pfalz,Saarland"
				+ ",Sachsen,Sachsen-Anhalt,Schleswig-Holstein,Thüringen";
	}
	//Gibt eine Liste mit Holiday Elementen wieder (Diese beinhalten: GregorianCalendar, Name sowie die Bundesländer in denen sie vorkommen)
	private ArrayList<Holiday> allDataForHolidays(ArrayList<GregorianCalendar>allHolidays){
		int counter =0;
		String[] nameOfHoliDay = {"Neujahr","Heilige Drei Koenige","Karfreitag","Ostersonntag","Ostermontag","Tag der Arbeit","Christi Himmelfahrt","Pfingstsonntag",
				"PFingstmontag","Fronleichnam","Augsburger FriedensFest(Nur in Augsburg!)","Mariae Himmelfahrt","Tag der Deutschen Einheit","Reformationstag","Allerheiligen",
				"Buss- und Bettag","1.Weichnachtag","2.Weihnachtstag"};
		String[] statesForHolidaysInGermany = {allStatesOfGermany(),"Baden-Württemberg,Bayern, Sachsen-Anhalt",allStatesOfGermany(),"Brandenburg",allStatesOfGermany(),
				allStatesOfGermany(),allStatesOfGermany(),"Brandenburg",allStatesOfGermany(),"Baden-Württemberg, Bayern, Hessen, Nordrhein-Westfalen,Rheinland-Pfalz, Saarland",
				"Bayern","Bayern, Saarland",allStatesOfGermany(),"Brandenburg, Mecklenburg-Vorpomern, Sachsen, Sachsen-Anhalt, Thüringen, Bremen, Hamburg, Schleswig-Holstein, Niedersachsen",
				"Baden-Württemberg, Bayern, Nordrhein-Westfalen, Rheinland-Pfalz, Saarland", "Sachsen", allStatesOfGermany(),allStatesOfGermany()};
		//Da beide Array gleich lang sind, nehme ich einen Counter um beide an der selben Stelle anzusprechen
		
		ArrayList<Holiday> bla = new ArrayList<Holiday>();
		//gehe liste mit allen holidays durch und speichere dir das objekt mit den weiteren parametern in liste<Holiday> ab
		for(GregorianCalendar holiday : allHolidays) {
			bla.add(new Holiday(holiday, nameOfHoliDay[counter],statesForHolidaysInGermany[counter]));
			counter++;
		}
		return bla;
	}
}