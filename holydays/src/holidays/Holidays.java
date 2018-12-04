package holidays;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;


/**
 * @TODO:
 * Namen der Feiertage mit ausgeben 
 * ausgabe der feiertage anhand bundesländer
 * zeitraum des urlaubs durch brückentage
 * aussehen(feiertage): 
 * Feiertag		tag 	monat	jahr	wochentag 	
 * aussehen (brückentage):
 * Feiertag 	von		bis 	start wochentag 	ende wochentag 	freie Tage
 * @author danzk
 *
 */
public class Holidays {
	
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
		//Herausfinden der Feiertage anhand allen Feiertagen 
		ArrayList<GregorianCalendar>bridgingDays = bridgingDay(allHoliDays);
		//Überarbeitung der Brückentagsliste anhand schon vorhanden einträgen (ausgehend von feiertagsliste)
		bridgingDays = deleteDoubleDays(bridgingDays,allHoliDays);
		//ausgabe der feiertage und potentiellen brückentagen des eingegebenen Jahres
		outputHoliDays(bridgingDays,allHoliDays,year);
		writeIntoFile(bridgingDays,allHoliDays);
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
		int[] month = 	{0,0,4,7,7,10,10,11,11,12,12};
		int[] day = 	{1,6,1,8,10,03,31,01,21,25,26};
		for(int i=0;i<month.length;i++) {
			solidHoliDay.add(new GregorianCalendar(year,month[i],day[i]));
		}
		return solidHoliDay;
	}
	
	//Speicherung der Tage, welche maßgebend sind für Brückentage (Montag/Dienstag/Donnerstag/Freitag), in bridgeDay Liste 
	private ArrayList<GregorianCalendar> bridgingDay(ArrayList<GregorianCalendar> holiDays) {
		ArrayList<GregorianCalendar> bridgeDay = new ArrayList<GregorianCalendar>();
		//Geht die Liste holiDays durch und speichert bei montag/Freitag den kommenden/vorherigen Tag in neuer liste ab
		for(GregorianCalendar holiDay : holiDays) {
			//Sonntag = 1 -> Samstag = 7
			int holiDayDay = holiDay.get(Calendar.DAY_OF_WEEK);
			if(holiDayDay<7 && holiDayDay>1) {
				//Speicherung der Datumsangabe in bridgeDay Liste anhand von Tag des Feiertages 
				switch(holiDayDay) {
				//Montag
				case 2:
					bridgeDay.add(new GregorianCalendar(holiDay.get(Calendar.YEAR), holiDay.get(Calendar.MONTH), holiDay.get(Calendar.DAY_OF_MONTH)+1));
					break;
				//Dienstag
				case 3:
					bridgeDay.add(new GregorianCalendar(holiDay.get(Calendar.YEAR), holiDay.get(Calendar.MONTH), holiDay.get(Calendar.DAY_OF_MONTH)-1));
					break;
				//Donnerstag
				case 5:
					bridgeDay.add(new GregorianCalendar(holiDay.get(Calendar.YEAR), holiDay.get(Calendar.MONTH), holiDay.get(Calendar.DAY_OF_MONTH)+1));
					break;
				//Freitag
				case 6:
					bridgeDay.add(new GregorianCalendar(holiDay.get(Calendar.YEAR), holiDay.get(Calendar.MONTH), holiDay.get(Calendar.DAY_OF_MONTH)-1));
					break;
				}
			}
		}
	return bridgeDay;
	}
	
	//Durchsuchen der Listen nach schon vorhandenen Einträgen -> Erstellung überarbeiter bridgingDays Liste  
	private ArrayList<GregorianCalendar> deleteDoubleDays(ArrayList<GregorianCalendar> bridgingDays,ArrayList<GregorianCalendar> allHoliDays) {
		ArrayList<GregorianCalendar> reworkedBridgingDays = new ArrayList<GregorianCalendar>();
		//Nehme datumsangabe aus brückentag-Liste (monat & tag) vergleiche mit datumsangaben aus feiertagsliste (monat & tag)
		for(GregorianCalendar bridgeDay:bridgingDays) {
			boolean dayExist = false;
			int bridgeDayMonth = bridgeDay.get(Calendar.MONTH);
			int bridgeDayDay = bridgeDay.get(Calendar.DAY_OF_MONTH);
			for(GregorianCalendar holiDay:allHoliDays) {
				int holiDayMonth = holiDay.get(Calendar.MONTH);
				int holiDayDay = holiDay.get(Calendar.DAY_OF_MONTH);
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
	
	//ausgabe der Datumsangaben für alle Feiertage während der Woche 
	private void outputHoliDays(ArrayList<GregorianCalendar> bridgDays,ArrayList<GregorianCalendar> allHolidays,int year) {
		System.out.println("\nAlle Feiertage "+ year+" während der Woche:\n");
		for(GregorianCalendar holiDay : allHolidays) {
			DateFormat df = DateFormat.getDateInstance( DateFormat.FULL );
			String s = df.format( holiDay.getTime() );
			int day = holiDay.get(Calendar.DAY_OF_WEEK);
			if(day>1&&day<7) {
			System.out.println("Feiertag am: " + s);	
			}
		}
		System.out.println("\nAlle Potentiellen Brückentage "+ year+" während der Woche:\n");
		for(GregorianCalendar holiDay : bridgDays) {
			DateFormat df = DateFormat.getDateInstance( DateFormat.FULL );
			String s = df.format( holiDay.getTime() );
			System.out.println("Brückentag am: " + s);	
		}
	}
	
	//Schreibt die Datumsangaben in eine externe CSV File
	private void writeIntoFile(ArrayList<GregorianCalendar> bridgingDays, ArrayList<GregorianCalendar> allHoliDays) throws IOException {
		String[] nameOfHoliDay = {"Neujahr","Heilige Drei Koenige","Karfreitag","Ostersonntag","Ostermontag","Tag der Arbeit","Christ Himmelfahr","Pfingstsonntag",
				"PFingsmontag","Fronleichnam","Augsburger FriedensFest","Mariä Himmelfahrt","Tag der Deutschen Einheit","Reformationstag","Allerheiligen",
				"Buß- und Bettag","1.Weichnachtag","2.Weihnachtstag"};
		String verzeichnisDestop = System.getProperty("user.home");
		Path pfadMitDatei = Paths.get(verzeichnisDestop, "try.csv");
			try(BufferedWriter writePuffer = Files.newBufferedWriter(pfadMitDatei)){
				writePuffer.write("Feiertag;Tag;Monat;Jahr;Wochentag");
				writePuffer.newLine();
				for(GregorianCalendar holiDay: allHoliDays) {
					String weekDay = whichDay(holiDay);
					int day = holiDay.get(Calendar.DAY_OF_MONTH);
					String month = whichMonth(holiDay);
					int year = holiDay.get(Calendar.YEAR);
					String zeile = String.format("%s;%d;%s;%d%n", weekDay,day,month,year);
					writePuffer.write(zeile);
				}
				writePuffer.write("Brueckentage;Tag;Monat;Jahr");
				writePuffer.newLine();
				for(GregorianCalendar bridgeDay: bridgingDays) {
					String weekDay1 = whichDay(bridgeDay);
					int day = bridgeDay.get(Calendar.DAY_OF_MONTH);
					String month = whichMonth(bridgeDay);
					int year = bridgeDay.get(Calendar.YEAR);
					String zeile = String.format("%s;%d;%s;%d%n", weekDay1,day,month,year);
					writePuffer.write(zeile);
				}
			}
		}
		
	//Gibt den Wochentag als String zurück, ausgehend von day_of_week angabe
	private String whichDay(GregorianCalendar holiDay) {
		int numberOfDay = holiDay.get(Calendar.DAY_OF_WEEK);
		String wochenTag = "";
		switch (numberOfDay) {
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
	
	//Gibt den Monat als String zurück, ausgehen von month angabe 
	private String whichMonth(GregorianCalendar holiDay) {
		int numnberOfMonth = holiDay.get(Calendar.MONTH);
		String month ="";
		switch(numnberOfMonth) {
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
}