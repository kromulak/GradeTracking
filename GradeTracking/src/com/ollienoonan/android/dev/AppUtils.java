package com.ollienoonan.android.dev;

import java.text.NumberFormat;

import android.database.Cursor;
import android.util.Log;

/**
 * General utilities class for android applications.
 * Nothing here should be specific to one particular app but should be useful to all apps
 * 
 * //TODO Eventually entire dev package to be converted to a library
 * 
 * @author Ollie Noonan
 * @version 1 - 2014/07/02
 */
public class AppUtils {

	private static boolean debugMode = false;
	
	public static boolean isDebugging() {
		// TODO Auto-generated method stub
		return debugMode;
	}
	

	/**
	 * Converts the given float to a string rounded to the given amount of decimal places.
	 * Whole numbers will include zeros after decimal place
	 * 
	 * @param f the float to be converted
	 * @param decimalPlaces the amount of digits after the decimal place to include
	 * @return the formatted string
	 */
	public static String formatFloatToStr(float f, int decimalPlaces) {
		
		NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setMinimumFractionDigits(decimalPlaces);
		formatter.setMaximumFractionDigits(decimalPlaces);
		String output = formatter.format(f);
		
		return output;
	}
	
	
	/**
	 * Converts the given int to a String of the strings ordinal
	 * eg, ordinal(2) returns "2nd"
	 * 
	 * @param i the int to be converted
	 * @return the ordinal value of the int
	 */
	public static String ordinal(int i) {
	    String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
	    switch (i % 100) {
	    case 11:
	    case 12:
	    case 13:
	        return i + "th";
	    default:
	        return i + sufixes[i % 10];

	    }
	}

	/**
	 * Prints the given cursor to the debug log
	 * 
	 * @param tag
	 * @param c
	 */
	public static void debugPrintCursor(String tag, Cursor c) {
		if(c != null) {
			if(c.moveToFirst()) {
				while(c.moveToNext()) {
					String s = "< ";
					for(int i = 0; i < c.getColumnCount(); i++) {
						s += "["+c.getColumnName(i) + " = " + c.getString(i)+"]";
					}
					Log.d(tag,s);
				}
			}
		}
	}
}
