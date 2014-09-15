package com.ollienoonan.gradetracker.sqlite.helper;

import java.util.ArrayList;
import java.util.List;

import com.ollienoonan.android.dev.AppUtils;
import com.ollienoonan.gradetracker.sqlite.model.Assessment;
import com.ollienoonan.gradetracker.sqlite.model.Course;
import com.ollienoonan.gradetracker.sqlite.model.Exam;
import com.ollienoonan.gradetracker.sqlite.model.Module;
import com.ollienoonan.gradetracker.sqlite.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Ollie Noonan
 * @version 1 - 2014/06/19
 * 
 * Helper class for working with the SQLite database.
 * All tables should have their own class but their onCreate and onUpgrade methods called from here
 */
public class GraderDatabaseHelper extends SQLiteOpenHelper {

	/** The name of the database used for this app */
	private static final String DATABASE_NAME = "grader.db";
	
	/** the version of the database */
	private static final int DATABASE_VERSION = 2;//increment this number to trigger automatic database upgrades.

	/** Constructor */
	public GraderDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		UsersTable.onCreate(database);
		CoursesTable.onCreate(database);
		ModulesTable.onCreate(database);
		AssessmentsTable.onCreate(database);
		ExamsTable.onCreate(database);
	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
      	//Dont need to use switch(oldVersion) here as each table should handle its own upgrading when required
		UsersTable.onUpgrade(database, oldVersion, newVersion);
		CoursesTable.onUpgrade(database, oldVersion, newVersion);
		ModulesTable.onUpgrade(database, oldVersion, newVersion);
		AssessmentsTable.onUpgrade(database, oldVersion, newVersion);
		ExamsTable.onUpgrade(database, oldVersion, newVersion);
	}
	
	

	/**
	 * Adds the given User to the database
	 * 
	 * @param newUser The User to be added
	 * @return the id the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long insertRecord(User newUser) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    ContentValues values = UsersTable.convertToContentValues(newUser);

	    // insert row
	    long task_id = db.insert(UsersTable.TABLE, null, values);
	    
	    db.close();

	    return task_id;
	}
	
	/**
	 * Adds the given Course to the database
	 * 
	 * @param newCourse The Course to be added
	 * @return the id the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long insertRecord(Course newCourse) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    ContentValues values = CoursesTable.convertToContentValues(newCourse);

	    // insert row
	    long task_id = db.insert(CoursesTable.TABLE, null, values);

	    db.close();
	    
	    return task_id;
	}

	/**
	 * Adds the given Module to the database
	 * 
	 * @param newModule The Module to be added
	 * @return the id the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long insertRecord(Module newModule) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    ContentValues values = ModulesTable.convertToContentValues(newModule);

	    // insert row
	    long task_id = db.insert(ModulesTable.TABLE, null, values);

	    db.close();
	    if(AppUtils.isDebugging())
	    	Log.d("grader", newModule.getCode() + ", semester: " + newModule.getSemester() + " year: "+ newModule.getYear());
	   
	    db.close();
	    
	    return task_id;
	}

	/**
	 * Adds the given Assessment to the database
	 * 
	 * @param newAssessment The Assessment to be added
	 * @return the id the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long insertRecord(Assessment newAssessment) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    ContentValues values = AssessmentsTable.convertToContentValues(newAssessment);

	    // insert row
	    long task_id = db.insert(AssessmentsTable.TABLE, null, values);

	    db.close();
	    
	    return task_id;
	}

	/**
	 * Adds the given Exam to the database
	 * 
	 * @param newExam The Exam to be added
	 * @return the id the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long insertRecord(Exam newExam) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    ContentValues values = ExamsTable.convertToContentValues(newExam);

	    // insert row
	    long task_id = db.insert(ExamsTable.TABLE, null, values);

	    db.close();
	    
	    return task_id;
	}
	
	/**
	 * Returns a single Module from the database whose id matches db_id
	 * 
	 * @param db_id the id of the Module to get
	 * @return the Module from the database
	 */
	public Module getModuleFromDatabase(long db_id) {
	    SQLiteDatabase db = this.getReadableDatabase();

	    String selectQuery = "SELECT  * FROM " + ModulesTable.TABLE + " WHERE "
	            + ModulesTable.COLUMN_ID + " = ?";

	    String[] whereArgs = new String[] { String.valueOf(db_id) };
	    
	    Cursor c = db.rawQuery(selectQuery, whereArgs);

	    Module module = null;
	    
	    if (c != null) {
	        c.moveToFirst();

	        module = ModulesTable.convertToModule(c);
	        
	        c.close();
	    }
	    
	    db.close();
	    
	    
	    return module;
	}
	
	
	public Module getModuleFromDatabase(String code, int semester, int year) {
		SQLiteDatabase db = this.getReadableDatabase();

	    String selectQuery = "SELECT  * FROM " + ModulesTable.TABLE + " WHERE "
	            + ModulesTable.COLUMN_MODULE_CODE + " = ? AND ("
	    		+ ModulesTable.COLUMN_SEMESTER + " = ? OR "
	            + ModulesTable.COLUMN_SEMESTER + " = " + Module.SEMESTER_ONE_AND_TWO + ") AND "
	            + ModulesTable.COLUMN_YEAR + " = ?";

	    String[] whereArgs = new String[] { String.valueOf(code), String.valueOf(semester), String.valueOf(year) };
	    
	    Cursor c = db.rawQuery(selectQuery, whereArgs);

	    Module module = null;
	    
	    AppUtils.debugPrintCursor("GraderDBH",c);//TODO: remove when working
	    
	    if (c != null) {
	    	if(c.getCount() > 0) {
	    		c.moveToFirst();
	        	module = ModulesTable.convertToModule(c);
	    	}
	    	
	    	c.close();
		}

	    db.close();
	    
	    return module;
	}
	
	/**
	 * Returns an array of modules for the given semester of the given year.
	 * 
	 * @param semester use Module.SEMESTER...
	 * @param year the year 
	 * @return the array of modules
	 */
	public List<Module> getAllModulesFromDatabase(int semester, int year) {
		SQLiteDatabase db = this.getReadableDatabase();
		

	    String selectQuery = "SELECT  * FROM " + ModulesTable.TABLE + " WHERE "
	            + "(" + ModulesTable.COLUMN_SEMESTER + " = ? OR "
	            + ModulesTable.COLUMN_SEMESTER + " = " + Module.SEMESTER_ONE_AND_TWO + ") AND "
	            + ModulesTable.COLUMN_YEAR + " = ?";

	    String[] whereArgs = new String[] { String.valueOf(semester), String.valueOf(year) };
	    
	    Cursor c = db.rawQuery(selectQuery, whereArgs);

	    
	    String testQuery = "SELECT  * FROM " + ModulesTable.TABLE + " WHERE "
	            + "(" + ModulesTable.COLUMN_SEMESTER + " = " + whereArgs[0] + " OR "
	            + ModulesTable.COLUMN_SEMESTER + " = " + Module.SEMESTER_ONE_AND_TWO + ") AND "
	            + ModulesTable.COLUMN_YEAR + " = " +whereArgs[1] + " => " + c.getCount();

	    
	    Log.d("MMM", testQuery);
	    
	    List<Module> modules = new ArrayList<Module>();
	    
	    if (c != null) {
	    	if (c.getCount() > 0) {
		        c.moveToFirst();
		        
		        do {
		        	modules.add(ModulesTable.convertToModule(c));
		        } while(c.moveToNext());
	    	}	        
	        c.close();
	    }
	    
	    db.close();
	    
	    return modules;
	}

	
	/**
	 * Returns an array of Assessments for the given Module.
	 * 
	 * @param module the Module 
	 * @return the array of assessments
	 */
	public List<Assessment> getAllAssessmentsFromDatabase(Module module) {
		SQLiteDatabase db = this.getReadableDatabase();

	    String selectQuery = "SELECT  * FROM " + AssessmentsTable.TABLE + " WHERE "
	            + AssessmentsTable.COLUMN_MODULE_CODE + " = ?";

	    String[] whereArgs = new String[] { String.valueOf(module.getCode()) };
	    
	    Cursor c = db.rawQuery(selectQuery, whereArgs);

	    List<Assessment> assessments = new ArrayList<Assessment>();
	    
	    if (c != null) {
	        c.moveToFirst();

	        do {
	        	assessments.add(AssessmentsTable.convertToAssessment(c));
	        } while(c.moveToNext());
	        
	        c.close();
	    }
	    
	    db.close();
	    
	    return assessments;
	}
	
	
	public Exam getExamFromDatabase(Module module) {
		SQLiteDatabase db = this.getReadableDatabase();

	    String selectQuery = "SELECT  * FROM " + ExamsTable.TABLE + " WHERE "
	            + ExamsTable.COLUMN_MODULE_CODE + " = ?";
	            //+ ExamsTable.COLUMN_SEMESTER + " = ? AND "
	            //+ ExamsTable.COLUMN_YEAR + " = ?";

	    String[] whereArgs = new String[] { String.valueOf(module.getCode()) };
	    
	    Cursor c = db.rawQuery(selectQuery, whereArgs);

	    Exam exam = null;
	    	    
	    if (c != null) {
	    	if(c.getCount() > 0) {
	    		c.moveToFirst();
	        	exam = ExamsTable.convertToExam(c);
	    	}
	    	
	    	c.close();
		}

	    db.close();
	    
	    return exam;
	}

 
	
	/**
	 * Updates the User in the database
	 * 
	 * @param user the User to update
	 * @return the number of rows affected
	 */
	public long updateRecord(User user) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    ContentValues values = UsersTable.convertToContentValues(user);

	    String whereClause = UsersTable.COLUMN_ID + " = ?";
	    String[] whereArgs = new String[] { String.valueOf(user.getId()) };
	    
	    long rows_affected = db.update(UsersTable.TABLE, values, whereClause, whereArgs);

	    db.close();
	    
	    return rows_affected;
	}


	/**
	 * Updates the Course in the database
	 * 
	 * @param course the Course to update
	 * @return the number of rows affected
	 */
	public long updateRecord(Course course) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    ContentValues values = CoursesTable.convertToContentValues(course);

	    String whereClause = CoursesTable.COLUMN_ID + " = ?";
	    String[] whereArgs = new String[] { String.valueOf(course.getId()) };
	    
	    long rows_affected = db.update(CoursesTable.TABLE, values, whereClause, whereArgs);

	    db.close();
	    
	    return rows_affected;
	}

	/**
	 * Updates the Module in the database
	 * 
	 * @param module the Module to update
	 * @return the number of rows affected
	 */
	public long updateRecord(Module module) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    ContentValues values = ModulesTable.convertToContentValues(module);

	    String whereClause = ModulesTable.COLUMN_ID + " = ?";
	    String[] whereArgs = new String[] { String.valueOf(module.getId()) };
	    
	    long rows_affected = db.update(ModulesTable.TABLE, values, whereClause, whereArgs);

	    db.close();
	    
	    return rows_affected;
	}	
	
	/**
	 * Updates the Assessment in the database
	 * 
	 * @param assessment the Assessment to update
	 * @return the number of rows affected
	 */
	public long updateRecord(Assessment assessment) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    ContentValues values = AssessmentsTable.convertToContentValues(assessment);

	    String whereClause = AssessmentsTable.COLUMN_ID + " = ?";
	    String[] whereArgs = new String[] { String.valueOf(assessment.getId()) };
	    
	    long rows_affected = db.update(AssessmentsTable.TABLE, values, whereClause, whereArgs);

	    db.close();
	    
	    return rows_affected;
	}

	/**
	 * Updates the Exam in the database
	 * 
	 * @param exam the Exam to update
	 * @return the number of rows affected
	 */
	public long updateRecord(Exam exam) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    ContentValues values = ExamsTable.convertToContentValues(exam);

	    String whereClause = ExamsTable.COLUMN_ID + " = ?";
	    String[] whereArgs = new String[] { String.valueOf(exam.getId()) };
	    
	    long rows_affected = db.update(ExamsTable.TABLE, values, whereClause, whereArgs);

	    db.close();
	    
	    return rows_affected;
	}
	
	
	/**
	 * Deletes the given User from the database.
	 * 
	 * @param user the User to be deleted
	 * @return the amount of rows affected
	 */
	public long deleteRecord(User user) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    String whereClause = UsersTable.COLUMN_ID + " = ?";
	    String[] whereArgs = new String[] { String.valueOf(user.getId()) };
	    
	    long rows_affected = db.delete(UsersTable.TABLE, whereClause, whereArgs);

	    db.close();
	    
	    return rows_affected;
	}
	
	/**
	 * Deletes the given Course from the database.
	 * 
	 * @param course the Course to be deleted
	 * @return the amount of rows affected
	 */
	public long deleteRecord(Course course) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    String whereClause = CoursesTable.COLUMN_ID + " = ?";
	    String[] whereArgs = new String[] { String.valueOf(course.getId()) };
	    
	    long rows_affected = db.delete(CoursesTable.TABLE, whereClause, whereArgs);

	    db.close();
	    
	    return rows_affected;
	}
	
	/**
	 * Deletes the given Module from the database.
	 * 
	 * @param module the Module to be deleted
	 * @return the amount of rows affected
	 */
	public long deleteRecord(Module module) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    String whereClause = ModulesTable.COLUMN_ID + " = ?";
	    String[] whereArgs = new String[] { String.valueOf(module.getId()) };
	    
	    long rows_affected = db.delete(ModulesTable.TABLE, whereClause, whereArgs);

	    db.close();
	    
	    return rows_affected;
	}

	/**
	 * Deletes the given Assessment from the database.
	 * 
	 * @param assessment the Assessment to be deleted
	 * @return the amount of rows affected
	 */
	public long deleteRecord(Assessment assessment) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    if(assessment.getId() > 0) {
	    	Cursor c = db.rawQuery("UPDATE "+ AssessmentsTable.TABLE
	    		+ " SET " + AssessmentsTable.COLUMN_PARENTID
	    		+ " = -1 WHERE " + AssessmentsTable.COLUMN_PARENTID
	    		+ " = "+String.valueOf(assessment.getId()), null);
	    	
	    	Log.d("delete", "changed " + c.getCount());
	    }
	    
	    String whereClause = AssessmentsTable.COLUMN_ID + " = ?";
	    String[] whereArgs = new String[] { String.valueOf(assessment.getId()) };
	    
	    long rows_affected = db.delete(AssessmentsTable.TABLE, whereClause, whereArgs);

	    db.close();
	    
	    return rows_affected;
	}

	/**
	 * Deletes the given Exam from the database.
	 * 
	 * @param exam the Exam to be deleted
	 * @return the amount of rows affected
	 */
	public long deleteRecord(Exam exam) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    String whereClause = ExamsTable.COLUMN_ID + " = ?";
	    String[] whereArgs = new String[] { String.valueOf(exam.getId()) };
	    
	    long rows_affected = db.delete(ExamsTable.TABLE, whereClause, whereArgs);

	    db.close();
	    
	    return rows_affected;
	}
	
	
	/**
	 * Calculates the current grade of all assessments for the given Module.
	 * The result is as a percentage in the form of a float between 0 and 1
	 * 
	 * @param module the Module 
	 * @return a float between 0 and 1
	 */
	public float calculateCaGrade(Module module) {
		if(module.getCaWeight() == 0)
			return 0;
			
		SQLiteDatabase db = this.getWritableDatabase();
		
		String whereClause = "SELECT SUM("
				+ AssessmentsTable.COLUMN_POINTSACHIEVED + " / " + AssessmentsTable.COLUMN_MAXPOINTS
				+ ") as RESULT from " + AssessmentsTable.TABLE
				+ " where " + AssessmentsTable.COLUMN_MODULE_CODE + " = ?";

	    String[] whereArgs = new String[] { String.valueOf(module.getCode()) };
	
		Cursor c = db.rawQuery(whereClause, whereArgs);

		float total = 0f;
		
		if(c.moveToFirst())
		    total = c.getFloat(0);
		
		db.close();
		
		return total/module.getCaWeight();
	}
	
	
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	public Course getCourseFromDatabase(long db_id) {
	    SQLiteDatabase db = this.getReadableDatabase();

	    String selectQuery = "SELECT  * FROM " + CoursesTable.TABLE + " WHERE "
	            + CoursesTable.COLUMN_ID + " = ?";

	    String[] whereArgs = new String[] { String.valueOf(db_id) };
	    
	    Cursor c = db.rawQuery(selectQuery, whereArgs);

	    Course course = null;
	    
	    if (c != null) {
	        c.moveToFirst();

	        course = CoursesTable.convertToCourse(c);
	        
	        c.close();
	    }
	    
	    db.close();
	    
	    return course;
	}

	public List<String> getAllCategoriesForModule(Module module) {
		
		SQLiteDatabase db = this.getReadableDatabase();
		
	    String selectQuery = "SELECT " + AssessmentsTable.COLUMN_PARENTID + " FROM " + AssessmentsTable.TABLE + " WHERE "
	            + AssessmentsTable.COLUMN_MODULE_CODE + " = ?";

	    String[] whereArgs = new String[] { String.valueOf(module.getId()) };
	    
	    Cursor c = db.rawQuery(selectQuery, whereArgs);

	    List<String> categories = new ArrayList<String>();
	    
	    if (c != null) {
	        c.moveToFirst();
	        
	        do {
	        	categories.add(c.getString(c.getColumnIndex(AssessmentsTable.COLUMN_PARENTID)));
	        } while(c.moveToNext());
	        
	        c.close();
	    }
	    
	    db.close();
	    
	    return categories;
	}


	/**
	 * Returns a list of Assessments that belong to the given module and are sub assessments of the given assessment
	 * pass parentAssessment value of null for uppermost level of assessments
	 * 
	 * @param module the Module that owns the assessments
	 * @param parentAssessment the parent assessment
	 * @return A list of the assessment
	 */
	public List<Assessment> getSubAssessmentsForModule(Module module, Assessment parentAssessment) {
		SQLiteDatabase db = this.getReadableDatabase();
		
	    String selectQuery = "SELECT * FROM " + AssessmentsTable.TABLE + " WHERE "
	            + AssessmentsTable.COLUMN_MODULE_CODE + " = ? AND "
	            + AssessmentsTable.COLUMN_PARENTID + " = ? ";

	    String[] whereArgs;
	    
	    if(parentAssessment == null)
    		whereArgs = new String[] { String.valueOf(module.getId()), String.valueOf(Assessment.CORE_PARENT_ID) };
	    else
	    	whereArgs = new String[] { String.valueOf(module.getId()), String.valueOf(parentAssessment.getId()) };
	    
	    Cursor c = db.rawQuery(selectQuery, whereArgs);

	    List<Assessment> assessments = new ArrayList<Assessment>();
	    
	    if (c != null) {
	    	if (c.getCount() > 0) {
		    
		        c.moveToFirst();
		        
		        do {
		        	assessments.add(AssessmentsTable.convertToAssessment(c));
		        } while(c.moveToNext());
	    	}
	    	
	        c.close();
	    } else {
	    	assessments = new ArrayList<Assessment>();
	    }	    	
	    
	    db.close();


//	    Log.d("Gdr", "loading " + assessments.size() + " assessments");
	    
	    return assessments;
	}
}