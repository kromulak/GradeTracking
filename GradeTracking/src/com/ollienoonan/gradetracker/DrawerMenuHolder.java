package com.ollienoonan.gradetracker;

/**
 * Holder for the data displayed in a row on the android DrawerMenu
 * @author Ollie Noonan
 * @version 1 - 2013/07/30
 */
public class DrawerMenuHolder {

	private String label;
	private int year;
	private int semester;
	
	/**
	 * @param label
	 * @param year
	 * @param semester
	 */
	public DrawerMenuHolder(String label, int year, int semester) {
		this.label = label;
		this.year = year;
		this.semester = semester;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @return the semester
	 */
	public int getSemester() {
		return semester;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	/**
	 * @param semester the semester to set
	 */
	public void setSemester(int semester) {
		this.semester = semester;
	}
	
	public String toString() {
		return this.label;
	}
	
}
