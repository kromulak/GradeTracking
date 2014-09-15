package com.ollienoonan.gradetracker.sqlite.helper;

import com.ollienoonan.android.dev.AppUtils;
import com.ollienoonan.gradetracker.sqlite.model.Assessment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author Ollie Noonan
 * @version 1 - 2014/06/19
 * 
 * This is the details for the assessments table in the database.<br>
 * Contains the methods for calling create and update of this table.<br>
 * Any update needing to be done to the Assessments Table should be done in the onUpgrade method in this class
 * Also contains constants for column names etc.
 */
public class AssessmentsTable {

	// Database table
	public static final String TABLE = "assessments";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_MODULE_CODE = "module_code";
	public static final String COLUMN_PARENTID = "parentID";
	public static final String COLUMN_MAXPOINTS = "max_points";
	public static final String COLUMN_POINTSACHIEVED = "points_achieved";
	public static final String COLUMN_WEIGHT = "weight";
	public static final String COLUMN_NOTES = "notes";
	

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE
			+ "("
				+ COLUMN_ID + " integer primary key autoincrement, "
				+ COLUMN_TITLE + " text not null, "
				+ COLUMN_MODULE_CODE + " text not null,"
				+ COLUMN_PARENTID + " text not null,"
				+ COLUMN_MAXPOINTS + " real,"
				+ COLUMN_POINTSACHIEVED + " real,"
				+ COLUMN_WEIGHT + " real not null,"
				+ COLUMN_NOTES + " text not null"
			+ ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		if(AppUtils.isDebugging())
			Log.d("Database", "Table " + TABLE + " created");
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
	/*REMEMBER changes here may require changes in the oncreate method for new users too!!*/
      	/*When you have successive app upgrades, several of which have database upgrades,
      	 * you want to be sure to check the oldVersion
      	 * This way when a user upgrades from version 1 to version 3, they get both updates.
      	 * When a user upgrades from version 2 to 3, they just get the revision 3 update...
      	 * After all, you can't count on 100% of your user base to upgrade each time you release an update.
      	 * Sometimes they skip an update or 12
      	 *
      	 * from: http://stackoverflow.com/questions/8133597/android-upgrading-db-version-and-adding-new-table
      	 */
		switch(oldVersion) {
    		case 1:
    			Log.w(AssessmentsTable.class.getName(), "Upgrading database from version "
    			       + oldVersion + " to " + newVersion
    			       	        + ", which will destroy all old data");
    			database.execSQL("DROP TABLE IF EXISTS " + TABLE);
    			onCreate(database);
    		case 2:
    			//db.execSQL(DATABASE_CREATE_someothertable);
		}
	}

	/**
	 * Converts the given Assessment Object to a ContentsValue Object.
	 * The ContentValues Object contains all the details of the Assessment except its database id.
	 * All keys set using AssessmentsTable column constants
	 * @param assessment the Assessment to be converted
	 * @return the contentvalue
	 */
	public static ContentValues convertToContentValues(Assessment assessment) {
		ContentValues values = new ContentValues();

		values.put(AssessmentsTable.COLUMN_TITLE, assessment.getTitle());
		values.put(AssessmentsTable.COLUMN_MODULE_CODE, assessment.getModuleID());
		values.put(AssessmentsTable.COLUMN_PARENTID, assessment.getParentID());
		values.put(AssessmentsTable.COLUMN_WEIGHT, assessment.getWeight());
		values.put(AssessmentsTable.COLUMN_MAXPOINTS, assessment.getMaxPoints());
		values.put(AssessmentsTable.COLUMN_POINTSACHIEVED, assessment.getPointsAchieved());
		values.put(AssessmentsTable.COLUMN_NOTES, assessment.getNotes());

		return values;
	}

	/**
	 * Converts the given Cursor Object to a Assessment Object.
	 * Column titles of the Cursor should match with the COLUMN_ constants for AssessmentsTable
	 * 
	 * @param c The Cursor Object to be converted
	 * @return A Assessment Object
	 */
	public static Assessment convertToAssessment(Cursor c) {
		Assessment assessment = new Assessment();
		
		assessment.setId(c.getInt(c.getColumnIndex(AssessmentsTable.COLUMN_ID)));
		assessment.setTitle(c.getString(c.getColumnIndex(AssessmentsTable.COLUMN_TITLE)));
		assessment.setModuleID(c.getLong(c.getColumnIndex(AssessmentsTable.COLUMN_MODULE_CODE)));
		assessment.setParentID(c.getInt(c.getColumnIndex(AssessmentsTable.COLUMN_PARENTID)));
		assessment.setMaxPoints(c.getFloat(c.getColumnIndex(AssessmentsTable.COLUMN_MAXPOINTS)));
		assessment.setPointsAchieved(c.getFloat(c.getColumnIndex(AssessmentsTable.COLUMN_POINTSACHIEVED)));
		assessment.setWeight(c.getFloat(c.getColumnIndex(AssessmentsTable.COLUMN_WEIGHT)));
		assessment.setNotes(c.getString(c.getColumnIndex(AssessmentsTable.COLUMN_NOTES)));
		
		return assessment;
	}
}