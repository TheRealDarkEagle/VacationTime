package holidays;

public enum StateOf {
	
	badenwürttemberg("Neujahr, Heilige Drei Koenige, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Fronleichnahm, Tag der Deutschen Einheit, Allerheiligen, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	bayern("Neujahr, Heilige Drei Koenige, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Fronleichnahm, Augsburger Friedensfest(Nur in Augsburg!), Mariae Himmelfahrt, Tag der Deutschen Einheit, Allerheiligen, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	berlin("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Tag der Deutschen Einheit, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	brandenburg("Neujahr, Karfreitag, Ostersonntag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstsonntag, Pfingstmontag, Tag der Deutschen Einheit, Reformationstag, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	bremen("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Tag der Deutschen Einheit, Reformationstag, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	hamburg("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Tag der Deutschen Einheit, Reformationstag, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	hessen("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Fronleichnahm, Tag der Deutschen Einheit, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	mecklenburgvorpommern("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Tag der Deutschen Einheit, Reformationstag, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	niedersachsen("Neujar, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Tag der Deutschen Einheit, Reformationstag, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	nordrheinwestfalen("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Fronleichnahm, Tag der Deutschen Einheit, Allerheiligen, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	rheinlandpfalz("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Fronleichnahm, Tag der Deutschen Einheit, Allerheiligen, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	saarland("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Fronleichnahm, Mariae Himmelfahrt, Tag der Deutschen Einheit, Allerheiligen, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	sachsen("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Tag der Deutschen Einheit, Reformationstag, Buss- und Bettag, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	sachsenanhalt("Neujahr, Heilige Drei Koenige, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Tag der Deutschen Einheit, Reformationstag, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	schleswigholstein("Neujahr, Karfreitasg, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Tag der Deutschen Einheit, Reformationstag, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag"),
	thüringen("Neujahr, Karfreitag, Ostermontag, Tag der Arbeit, Christi Himmelfahrt, Pfingstmontag, Tag der Deutschen Einheit, Reformationstag, 1.Weihnachtsfeiertag, 2.Weihnachtsfeiertag");
	
	private final String holiday;
	
	
	StateOf(String state){
		holiday = state;
	}
	
	public String getHoliday() {
		return this.holiday;
	}
}
















