package com.idan.calendar;

/**
 * This class represents the days of a week.
 * 
 * @author Idan Perry
 * @version 24.05.2020
 */

public enum DaysOfWeek {
	SUNDAY("Sunday"),
	MONDAY("Monday"),
	TUESDAY("Tuesday"),
	WEDNESDAY("Wednesday"),
	THURSDAY("Thursday"),
	FRIDAY("Friday"),
	SATURDAY("Saturday");
	
	public static final int DAYS_IN_WEEK = 7;
	private String day;
	
	/**
	 * Constructs a day with the specified title-case letters.
	 * 
	 * @param day
	 */
	DaysOfWeek(String day) {
		this.day = day;
	}
	
	@Override
	public String toString() {
		return day;
	}
}
