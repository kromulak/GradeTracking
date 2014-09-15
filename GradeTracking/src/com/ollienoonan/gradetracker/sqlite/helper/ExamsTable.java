package com.ollienoonan.gradetracker.sqlite.helper;

import com.ollienoonan.android.dev.AppUtils;
import com.ollienoonan.gradetracker.sqlite.model.Exam;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author Ollie Noonan
 * @version 1 - 2014/06/19
 *
 *
 */
public class ExamsTable {
	// Database table
	public static final String TABLE = "exams";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_MODULE_CODE = "module_code";
	public static final String COLUMN_RESULT = "result";
	public static final String COLUMN_WEIGHT = "weight";
	public static final String COLUMN_MARKS_ON_PAPER = "marks_total";
	public static final String COLUMN_QUESTIONS_ON_PAPER = "questions_on_paper";
	public static final String COLUMN_TIME_ON_PAPER = "time_on_paper";
	public static final String COLUMN_NOTES = "notes";
	

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE
			+ "("
				+ COLUMN_ID + " integer primary key autoincrement, "
				+ COLUMN_MODULE_CODE + " text not null, "
				+ COLUMN_WEIGHT + " real not null,"
				+ COLUMN_RESULT + " real,"
				+ COLUMN_MARKS_ON_PAPER + " integer,"
				+ COLUMN_QUESTIONS_ON_PAPER + " integer,"
				+ COLUMN_TIME_ON_PAPER + " real,"
				+ COLUMN_NOTES + " text"
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
    			Log.w(ExamsTable.class.getName(), "Upgrading database from version "
    			       + oldVersion + " to " + newVersion
    			       	        + ", which will destroy all old data");
    			database.execSQL("DROP TABLE IF EXISTS " + TABLE);
    			onCreate(database);
    		case 2:
    			//db.execSQL(DATABASE_CREATE_someothertable);
		}
	}

	/**
	 * Converts the given Exam Object to a ContentsValue Object.
	 * The ContentValues Object contains all the details of the Exam except its database id.
	 * All keys set using ExamsTable column constants
	 * @param exam the Exam to be converted
	 * @return the contentvalue
	 */
	public static ContentValues convertToContentValues(Exam exam) {
		ContentValues values = new ContentValues();

		values.put(ExamsTable.COLUMN_MODULE_CODE, exam.getModuleCode());
		values.put(ExamsTable.COLUMN_MARKS_ON_PAPER, exam.getTotalMarksOnPaper());
		values.put(ExamsTable.COLUMN_QUESTIONS_ON_PAPER, exam.getTotalQuestionsOnPaper());
		values.put(ExamsTable.COLUMN_TIME_ON_PAPER, exam.getDuration());
		values.put(ExamsTable.COLUMN_WEIGHT, exam.getWeight());
		values.put(ExamsTable.COLUMN_RESULT, exam.getResult());
		values.put(ExamsTable.COLUMN_NOTES, exam.getNotes());

		return values;
	}

	/**
	 * Converts the given Cursor Object to a Exam Object.
	 * Column titles of the Cursor should match with the COLUMN_ constants for ExamsTable
	 * 
	 * @param c The Cursor Object to be converted
	 * @return A Exam Object
	 */
	public static Exam convertToExam(Cursor c) {
		Exam exam = null;
		
		try {
			exam = new Exam();
			
			exam.setId(c.getInt(c.getColumnIndex(ExamsTable.COLUMN_ID)));
			exam.setModuleCode(c.getString(c.getColumnIndex(ExamsTable.COLUMN_MODULE_CODE)));
			exam.setDuration(c.getInt(c.getColumnIndex(ExamsTable.COLUMN_TIME_ON_PAPER)));
			exam.setTotalMarksOnPaper(c.getInt(c.getColumnIndex(ExamsTable.COLUMN_MARKS_ON_PAPER)));
			exam.setTotalQuestionsOnPaper(c.getInt(c.getColumnIndex(ExamsTable.COLUMN_QUESTIONS_ON_PAPER)));
			exam.setWeight(c.getFloat(c.getColumnIndex(ExamsTable.COLUMN_WEIGHT)));
			exam.setResult(c.getFloat(c.getColumnIndex(ExamsTable.COLUMN_RESULT)));
			exam.setNotes(c.getString(c.getColumnIndex(ExamsTable.COLUMN_NOTES)));
			
		} catch(Exception e) {
			if(AppUtils.isDebugging())
				e.printStackTrace();
			
			exam = null;
		}
		
		return exam;
	}

}