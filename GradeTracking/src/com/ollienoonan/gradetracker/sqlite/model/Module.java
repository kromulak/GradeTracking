package com.ollienoonan.gradetracker.sqlite.model;

/**
 * @author Ollie Noonan
 * @version 1 2014/06/16
 *
 * Defines a Module AKA Subject/Class
 */
public class Module {
	
	/** The id of this Module */
	private long id = -1;
	
	/** The code of this Module */
	private String code;
	
	/** The title of this Module */
	private String title;
	
	/** The semester that this Module is from */
	private int semester;
	
	/** The year of study this Module is taken*/
	private int year;
	
	/** The minimum grade (as a percentage) required to pass this Module */
	private float minPassGrade;
	
	/** The grade (as a percentage) the User hopes to achieve in this Module */
	private float goalGrade;
	
	/** The percentage of the overall grade for this Module that is
	 *  marked through continuous assessment */
	private float caWeight;
	
	/** The current grade (as a percentage) of this Module */
	private float currentGrade;
	
	
	public static final int SEMESTER_ONE = 1;
	public static final int SEMESTER_TWO = 2;
	public static final int SEMESTER_ONE_AND_TWO = 3;

	
	
	/**
	 * Empty constructor
	 */
	public Module() {
		
	}
	
	
	/**
	 * Creates a Module Object with the given values
	 * 
	 * @param code the code of this Module
	 * @param title the title of this Module
	 * @param semester the semester that this Module is part of
	 * @param year the year of study this module is part of
	 * @param minPassGrade the minimum grade (as a percentage) required to pass this Module
	 * @param goalGrade the grade (as a percentage) the User hopes to achieve
	 * @param caWeight the percentage of this Modules grade that is marked by continuous assessment
	 * @param currentGrade the current grade (as a percentage) achieved by the User
	 */
	public Module(String code, String title, int semester, int year,
			float minPassGrade, float goalGrade, float cAWeight,
			float currentgrade) {
		this.code = code;
		this.title = title;
		this.semester = semester;
		this.year = year;
		this.minPassGrade = minPassGrade;
		this.goalGrade = goalGrade;
		this.caWeight = cAWeight;
		this.currentGrade = currentgrade;
	}

	
	/**
	 * Creates a Module Object with the given values
	 * 
	 * @param id the id of this Module
	 * @param code the code of this Module
	 * @param title the title of this Module
	 * @param semester the semester that this Module is part of
	 * @param year the year of study this module is part of
	 * @param minPassGrade the minimum grade (as a percentage) required to pass this Module
	 * @param goalGrade the grade (as a percentage) the User hopes to achieve
	 * @param caWeight the percentage of this Modules grade that is marked by continuous assessment
	 * @param currentGrade the current grade (as a percentage) achieved by the User
	 */
	public Module(int id, String code, String title, int semester, int year,
			float minPassGrade, float goalGrade, float cAWeight,
			float currentgrade) {
		this.id = id;
		this.code = code;
		this.title = title;
		this.semester = semester;
		this.year = year;
		this.minPassGrade = minPassGrade;
		this.goalGrade = goalGrade;
		this.caWeight = cAWeight;
		this.currentGrade = currentgrade;
	}
	
	
	/***********************************
	 *             GETTERS             *
	 ***********************************/

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}


	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}


	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @return the semester
	 */
	public int getSemester() {
		return semester;
	}


	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}


	/**
	 * @return the minPassGrade
	 */
	public float getMinPassGrade() {
		return minPassGrade;
	}


	/**
	 * @return the goalGrade
	 */
	public float getGoalGrade() {
		return goalGrade;
	}


	/**
	 * @return the caWeight
	 */
	public float getCaWeight() {
		return caWeight;
	}

	/**
	 * @return 1 - the CA weight
	 */
	public float getFinalsGrade() {
		return 100f - caWeight;
	}

	/**
	 * @return the currentGrade
	 */
	public float getCurrentGrade() {
		return currentGrade;
	}


	/***********************************
	 *             SETTERS             *
	 ***********************************/
	
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}


	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * @param semester the semester to set
	 */
	public void setSemester(int semester) {
		this.semester = semester;
	}


	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}


	/**
	 * @param minPassGrade the minPassGrade (as a percentage) to set
	 */
	public void setMinPassGrade(float minPassGrade) {
		this.minPassGrade = minPassGrade;
	}


	/**
	 * @param goalGrade the goalGrade (as a percentage) to set
	 */
	public void setGoalGrade(float goalGrade) {
		this.goalGrade = goalGrade;
	}


	/**
	 * @param caWeight the caWeight (as a percentage) to set
	 */
	public void setCaWeight(float caWeight) {
		this.caWeight = caWeight;
	}


	/**
	 * Sets the current grade for this Module to the given value.
	 * 
	 * @param currentGrade the current grade (as a percentage) to set
	 */
	public void setCurrentGrade(float currentGrade) {
		this.currentGrade = currentGrade;
	}
}
