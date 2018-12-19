package holidays;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Day {

	private boolean holiday = false;
	private boolean bridgeday = false;
	private boolean vacation = false;
	private GregorianCalendar date = new GregorianCalendar();
	private String holidayName = "";
	
	// anhand von date bekommst du raus: ist es wochenende? wie ist der tag name (bspw. mo, di, mi), welche KW ist es
	public Day(boolean holiday, boolean bridgeday, boolean vacation, GregorianCalendar date, String holidayName) {
		this.holiday = holiday;
		this.bridgeday = bridgeday;
		this.vacation = vacation;
		this.date = date;
		this.holidayName = holidayName;
	}
	
	public Day(GregorianCalendar date) {
		this.date = date;
	}
	
	
	public String print() {
		Locale locale = Locale.getDefault();
		int day = date.get(Calendar.DAY_OF_MONTH);
		String month = date.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
		int year = date.get(Calendar.YEAR);
		String toPrint = day+","+month+","+year;
		if(holiday) {
			toPrint.concat(","+holidayName);
		}
		return day+"."+month+"."+year;
	}
	
	//gibt einen String der nach csv richtlinien benutzt werden kann 
	public String getCsvEntry() {
		Locale local = Locale.getDefault();
		int year = date.get(Calendar.YEAR);
		String month = date.getDisplayName(Calendar.MONTH, Calendar.SHORT, local);
		int day = date.get(Calendar.DAY_OF_MONTH);
		String dayName = date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, local);
		String csvEntry = day+","+month+","+year+","+dayName;
		if(isHoliday()) {
			csvEntry = csvEntry.concat(","+holidayName);
		}
		return csvEntry;
	}
	
	/**
	 * Gibt zurück ob Tag ein Wochenende ist 
	 * @return
	 */
	public boolean isWeekend() {
		if(date.get(Calendar.DAY_OF_WEEK)==1||date.get(Calendar.DAY_OF_WEEK)==7) {
			return true;
		} 
		return false;
	}
	
	/**
	 * Gibt zurück ob Tag ein Feiertag ist 
	 * @return
	 */
	public boolean isHoliday() {
		return holiday;
	}
	
	/**
	 * Gibt zurück ob Tag ein Brückentag ist 
	 * @return
	 */
	public boolean isBridgeDay() {
		return bridgeday;
	}
	
	/**
	 * Gibt zurück ob Tag ein Urlaubstag ist 
	 * @return
	 */
	public boolean isVacation() {
		return vacation;
	}
	
	/**
	 * Gibt den Tag im Monat zurück 
	 * @return
	 */
	public int getDayOfMonth() {
		return date.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * Gibt den Monat zurück 
	 * @return
	 */
	public int getMonth() {
		return date.get(Calendar.MONTH);
	}
	
	public Date getTime() {
		return date.getTime();
	}
	
	public String getName() {
		return holidayName;
	}
	
	public int getDay() {
		return date.get(Calendar.DAY_OF_WEEK);
	}
	public GregorianCalendar getGc() {
		return date;
	}
}
