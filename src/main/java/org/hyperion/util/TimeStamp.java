package org.hyperion.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Creates a simple time stamp.
 * @author Stephen Andrews
 */
public class TimeStamp {

	/**
	 * The format of the time stamp.
	 */
	private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	/**
	 * Get the current time of an action.
	 * @return The time.
	 */
	public static String add() {
		Date date = new Date();
		return "[" + dateFormat.format(date) + "] ";
	}
}
