package com.ollienoonan.gradetracker.sqlite.model;

/**
 * @author Ollie Noonan
 * 
 * @version 1.3 2014/07/30
 * Changed score to pointsAchieved and added maxPoints
 * 
 * @version 1.2 2014/07/24
 * Added static int NEW and set the default id to be NEW
 * 
 * @version 1.1 2014/07/20
 * Added CORE_PARENT_ID, changed doubles to floats,
 * replaced categories with parentIDs
 * 
 * @version 1 2014/06/16
 *
 * Defines an Assessment for a Module
 */
public class Assessment {
	
	/** The id of root assessment */
	public static final int CORE_PARENT_ID = -1;

	public static final int NEW = -1;

	/** The id of this Assessment */
	private int id = NEW;
	
	/** The title of this Assessment */
	private String title;
	
	/** The id of the Module that this Assessment belongs to*/
	private long moduleID;
	
	/** The id of this Assessments parent Assessment if any */
	private int parentID;
		
	/** The max points available for this assignment */	
	private float maxPoints;
	
	/** The points received for this assignment */
	private float pointsAchieved;

	/** The percentage of the overall grade that this Assessment accounts for*/
	private float weight;
	
	/** Any notes about this Assessment */
	private String notes;
	
	/**
	 * Empty Constructor
	 */
	public Assessment() {
		
	}
	
	/**
	 * Creates an Assessment Object with the given values
	 * 
	 * @param title the title for this Assignment ('Lab 5' etc.)
	 * @param moduleID the code of the Module that this Assignment is from
	 * @param parentID the parentID for this Assignment
	 * @param maxPoints the maximum points available for this Assignment
	 * @param pointsAchieved the points achieved of this Assignment
	 * @param weight the percentage of the module grade that this Assignment is worth
	 * @param notes any note about this Assignment
	 */
	public Assessment(String title, long moduleID, int parentID, float maxPoints,
			float pointsAchieved, float weight, String notes) {
		this.title = title;
		this.moduleID = moduleID;
		this.parentID = parentID;
		this.maxPoints = maxPoints;
		this.pointsAchieved = pointsAchieved;
		this.weight = weight;
		this.notes = notes;
	}


	/**
	 * Creates an Assessment Object with the given values
	 * 
	 * @param id the id of this Assessment
	 * @param title the title for this Assignment ('Lab 5' etc.)
	 * @param moduleID the code of the Module that this Assignment is from
	 * @param parentID the parentID for this Assignment
	 * @param maxPoints the maximum points available for this Assignment
	 * @param pointsAchieved the points achieved of this Assignment
	 * @param weight the percentage of the module grade that this Assignment is worth
	 * @param notes any note about this Assignment
	 */
	public Assessment(int id, String title, long moduleID, int parentID, float maxPoints,
			float pointsAchieved, float weight, String notes) {
		this.id = id;
		this.title = title;
		this.moduleID = moduleID;
		this.parentID = parentID;
		this.pointsAchieved = pointsAchieved;
		this.maxPoints = maxPoints;
		this.weight = weight;
		this.notes = notes;
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the moduleID
	 */
	public long getModuleID() {
		return moduleID;
	}

	/**
	 * @return the parentID
	 */
	public int getParentID() {
		return parentID;
	}

	/**
	 * @return the maximum points achievable
	 */
	public float getMaxPoints() {
		return maxPoints;
	}

	/**
	 * @return the points achieved
	 */
	public float getPointsAchieved() {
		return pointsAchieved;
	}
		
	/**
	 * @return the weight
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
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
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param moduleID the moduleID to set
	 */
	public void setModuleID(long moduleID) {
		this.moduleID = moduleID;
	}

	/**
	 * @param parentID the parentID to set
	 */
	public void setParentID(int parentID) {
		this.parentID = parentID;
	}

	/**
	 * @param set the maximum points achievable
	 */
	public void setMaxPoints(float maxPoints) {
		this.maxPoints = maxPoints;
	}

	/**
	 * @param set the points achieved
	 */
	public void setPointsAchieved(float pointsAchieved) {
		this.pointsAchieved = pointsAchieved;
	}
	
	/**
	 * @param weight the weight to set
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String toString() {
		return title;
	}

	public float calculateResult() {
		if(maxPoints == 0)
			return 0;
		
		return (this.pointsAchieved/this.maxPoints) * 100;
	}
}
