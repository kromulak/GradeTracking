package com.ollienoonan.gradetracker.sqlite.model;

/**
 * @author Ollie Noonan
 * @version 1 2014/06/16
 * 
 * Defines a User of the app
 * Needed in order to allow multiple users of the same device all use the app
 * 
 */
public class User {

	/** The id of this User */
	private int id;
	
	/** The name of this User */
	private String name;
	
	/** The student number of this User */
	private String studentNumber;
	
	
	/**
	 * Empty constructor
	 */
	public User() {
		
	}
	
	/**
	 * Creates a new User Object with the given student number and name
	 * @param studentNumber the student number of this User
	 * @param name the name for this user
	 */
	public User(String studentNumber, String name) {
		this.studentNumber = studentNumber;
		this.name = name;
	}
	
	/**
	 * Creates a new User Object with the given name and id
	 * @param id
	 * @param studentNumber the student number of this User
	 * @param name the name for this User
	 */
	public User(int id, String studentNumber, String name) {
		this.id = id;
		this.studentNumber = studentNumber;
		this.name = name;
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
	 * @return the student number
	 */
	public String getStudentNumber() {
		return studentNumber;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
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
	 * Sets the student number of the User
	 * 
	 * @param name the name to set
	 */
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	
	/**
	 * Sets the name of the User
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
