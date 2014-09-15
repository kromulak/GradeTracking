package com.ollienoonan.gradetracker.sqlite.model;

/** 
 * Defines the course a user is enrolled to.
 * Currently only expects a user to be signed up for one course
 * Currently Courses are assumed to be structured in years with the
 *  same amount of semesters each year
 *
 * @author Ollie Noonan
 * @version 1 - 2014/06/14
 */
public class Course {

	/** The id of this Course */
	private int id;
	
	/** The code of this Course */
	private String code;
	
	/** The title of this Course */
	private String title;
	
	/** The duration in years of this Course */
	private int duration;
	
	/** The amount of semesters in each year of this Course */
	private int semesters;
	

	/**
	 * Empty constructor
	 */
	public Course() {
	}

	/**
	 * Creates a Course Object with the given code,title, duration and semesters per year
	 * 
	 * @param code the code for this Course
	 * @param title the title of this Course
	 * @param duration the length in years of this Course
	 * @param semesters the amount of semesters per year
	 */
	public Course(String code, String title, int duration, int semesters) {
		this.code = code;
		this.title = title;
		this.duration = duration;
		this.semesters = semesters;
	}

	
	/**
	 * Creates a Course Object with the given id, code,title, duration and semesters per year
	 * 
	 * @param id the id for this course
	 * @param code the code for this Course
	 * @param title the title of this Course
	 * @param duration the length in years of this Course
	 * @param semesters the amount of semesters per year
	 */
	public Course(int id, String code, String title, int duration, int semesters) {
		this.id = id;
		this.code = code;
		this.title = title;
		this.duration = duration;
		this.semesters = semesters;
	}
	
	
	/***********************************
	 *             GETTERS             *
	 ***********************************/

	
	/**
	 * @return the id
	 */
	public int getId() {
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
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @return the semesters
	 */
	public int getSemesters() {
		return semesters;
	}
	
	
	
	/***********************************
	 *             SETTERS             *
	 ***********************************/

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
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
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * @param semesters the semesters to set
	 */
	public void setSemesters(int semesters) {
		this.semesters = semesters;
	}
}
