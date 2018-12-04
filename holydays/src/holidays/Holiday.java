package holidays;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Holiday {

	String name;
	String stateOF;
	GregorianCalendar cal;
	
	/**
	 * Konstruktor 
	 * @param calendar2
	 * @param name
	 * @param stateOF
	 */
	public Holiday(Calendar calendar,String name,String stateOf){
		this.cal = (GregorianCalendar)calendar;
		this.name = name;
		this.stateOF = stateOf;
	}
}
