package holidays;

public enum StateOf {
	
	BADENWÜRTTEMBERG("Neujahr, Heilige Drei Koenige, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Fronleichnahm, Tag der Deutschen Einheit, Allerheiligen, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	BAYERN("Neujahr, Heilige Drei Koenige, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Fronleichnahm, Augsburger Friedensfest(Nur in Augsburg!), Mariae Himmelfahrt, Tag der Deutschen Einheit, Allerheiligen, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	BERLIN("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Tag der Deutschen Einheit, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	BRANDENBURG("Neujahr, Karfreitag, Ostersonntag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstsonntag, Pfingstmontag, Tag der Deutschen Einheit, Reformationstag, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	BREMEN("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Tag der Deutschen Einheit, Reformationstag, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	HAMBURG("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Tag der Deutschen Einheit, Reformationstag, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	HESSEN("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Fronleichnahm, Tag der Deutschen Einheit, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	MECKLENBURGVORPOMMERN("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Tag der Deutschen Einheit, Reformationstag, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	NIEDERSACHSEN("Neujar, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Tag der Deutschen Einheit, Reformationstag, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	NORDRHEINWESTFALEN("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Fronleichnahm, Tag der Deutschen Einheit, Allerheiligen, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	RHEINLANDPFALZ("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Fronleichnahm, Tag der Deutschen Einheit, Allerheiligen, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	SAARLAND("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Fronleichnahm, Mariae Himmelfahrt, Tag der Deutschen Einheit, Allerheiligen, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	SACHSEN("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Tag der Deutschen Einheit, Reformationstag, Buss- und Bettag, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	SACHSENANHALT("Neujahr, Heilige Drei Koenige, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Tag der Deutschen Einheit, Reformationstag, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	SCHLESWIGHOLSTEIN("Neujahr, Karfreitasg, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Tag der Deutschen Einheit, Reformationstag, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	THÜRINGEN("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Tag der Deutschen Einheit, Reformationstag, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag");
	
	private final String holiday;
	
	
	private StateOf(String state){
		holiday = state;
	}
	
	public String getHoliday() {
		return this.holiday;
	}
}
















