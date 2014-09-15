package com.ollienoonan.gradetracker.sqlite.model;


/**
 * @author Ollie Noonan
 * @version 1  2014/06/14
 *
 * Defines the final exam for each module
 */
public class Exam {

	public static final long NEW = -1;

	/** The id of this Exam */
	private long id = -1;
	
	/** The code of the Module that this is the Exam for */
	private String moduleCode;
	
	/** The percentage of the overall grade for the Module that is
	 *  marked through this exam */
	private float weight = 0;
	
	/** The maximum amount of marks that score as 100% in this Exam */
	private int totalMarksOnPaper = 0;
	
	/** The amount of questions of this Exam */
	private int totalQuestionsOnPaper = 0;
	
	/** The amount of minutes allowed to complete this Exam */
	private int duration = 0;
	
	/** Any notes made about this Exam */
	private String notes;
	
	/** the marks achieved on the exam */
	private float marksAchieved = 0;
	
	/** the result of this exam as a percentage*/
	private float result = -1;
	
	
	/**
	 * Empty constructor
	 */
	public Exam() {
		
	}
	
	/**
	 * Creates an Exam Object with the given code, weight, marks, questions and duration
	 * 
	 * @param code the module code that this is the exam for
	 * @param weight the percentage of the module grade that this exam is worth
	 * @param totalMarksOnPaper the amount of marks on the exam paper
	 * @param totalQuestionsOnPaper the amount of questions on the exam paper
	 * @param duration the length of time in minutes to complete this exam
	 * @param notes Any notes made about this Exam
	 */
	public Exam(String moduleCode, int weight, int totalMarksOnPaper,
			int totalQuestionsOnPaper, int duration, String notes) {
		this.moduleCode = moduleCode;
		this.weight = weight;
		this.totalMarksOnPaper = totalMarksOnPaper;
		this.totalQuestionsOnPaper = totalQuestionsOnPaper;
		this.duration = duration;
		this.notes = notes;
	}
	
	/**
	 * Creates an Exam Object with the given id, code, weight, marks, questions and duration
	 * 
	 * @param id the id of this Exam Object
	 * @param code the module code that this is the exam for
	 * @param weight the percentage of the module grade that this exam is worth
	 * @param totalMarksOnPaper the amount of marks on the exam paper
	 * @param totalQuestionsOnPaper the amount of questions on the exam paper
	 * @param duration the length of time in minutes to complete this exam
	 * @param notes Any notes made about this Exam
	 */
	public Exam(int id, String moduleCode, int weight, int totalMarksOnPaper,
			int totalQuestionsOnPaper, int duration, String notes) {
		this.id = id;
		this.moduleCode = moduleCode;
		this.weight = weight;
		this.totalMarksOnPaper = totalMarksOnPaper;
		this.totalQuestionsOnPaper = totalQuestionsOnPaper;
		this.duration = duration;
		this.notes = notes;
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
	 * @return the moduleCode
	 */
	public String getModuleCode() {
		return moduleCode;
	}

	/**
	 * @return the weight
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * @return the totalMarksOnPaper
	 */
	public int getTotalMarksOnPaper() {
		return totalMarksOnPaper;
	}

	/**
	 * @return the totalQuestionsOnPaper
	 */
	public int getTotalQuestionsOnPaper() {
		return totalQuestionsOnPaper;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}
	
	public float getResult() {
		if(result == -1) {
			if(totalMarksOnPaper > 0)
				return (this.marksAchieved/this.totalMarksOnPaper)*100;
			else
				return 0;
		}
		else
			return result;
	}
	
	public String getTitle() {
		return "Final Exam";
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
	 * @param moduleCode the moduleCode to set
	 */
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * @param totalMarksOnPaper the totalMarksOnPaper to set
	 */
	public void setTotalMarksOnPaper(int totalMarksOnPaper) {
		this.totalMarksOnPaper = totalMarksOnPaper;
	}

	/**
	 * @param totalQuestionsOnPaper the totalQuestionsOnPaper to set
	 */
	public void setTotalQuestionsOnPaper(int totalQuestionsOnPaper) {
		this.totalQuestionsOnPaper = totalQuestionsOnPaper;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	/**
	 * 
	 * @param marksAchieved
	 */
	public void setMarksAchieved(float marksAchieved) {
		this.marksAchieved = marksAchieved;
	}
	
	/**
	 * 
	 * @param result the result of this exam
	 */
	public void setResult(float result) {
		this.result = result;
	}
}