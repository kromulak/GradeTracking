package com.ollienoonan.gradetracker.sqlite.helper;

import com.ollienoonan.android.dev.AppUtils;
import com.ollienoonan.gradetracker.sqlite.model.Course;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author Ollie Noonan
 * @version 1 2014/06/19
 * 
 * Database Table for Course data
 * Contains required methods for creation and update, also some useful constants
 */
public class CoursesTable {
	
	
	// Database table
	public static final String TABLE = "courses";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_COURSE_CODE = "code";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DURATION = "notes";
	public static final String COLUMN_SEMESTERS_PER_TERM = "complete";

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE
			+ "("
	      		+ COLUMN_ID + " integer primary key autoincrement, "
	      		+ COLUMN_COURSE_CODE + " text not null, "
	      		+ COLUMN_TITLE + " text not null, "
	      		+ COLUMN_DURATION + " integer not null,"
	      		+ COLUMN_SEMESTERS_PER_TERM + " integer not null"
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
	    		Log.w(CoursesTable.class.getName(), "Upgrading database from version "
	    			       + oldVersion + " to " + newVersion
	    			       	        + ", which will destroy all old data");
	    		database.execSQL("DROP TABLE IF EXISTS " + TABLE);
	    		onCreate(database);
	    	case 2:
	    		//db.execSQL(DATABASE_CREATE_someothertable);
	    }
	}

	/**
	 * Converts the given Course Object to a ContentsValue Object.
	 * The ContentValues Object contains all the details of the Course except its database id.
	 * All keys set using CoursesTable column constants
	 * @param course the Course to be converted
	 * @return the contentvalue
	 */
	public static ContentValues convertToContentValues(Course course) {
		ContentValues values = new ContentValues();

		values.put(CoursesTable.COLUMN_COURSE_CODE, course.getCode());
		values.put(CoursesTable.COLUMN_TITLE, course.getTitle());
		values.put(CoursesTable.COLUMN_DURATION, course.getDuration());
		values.put(CoursesTable.COLUMN_SEMESTERS_PER_TERM, course.getSemesters());

		return values;
	}

	public static Course convertToCourse(Cursor c) {
		Course course = null;
		
		try {
			course = new Course();
			
			course.setId(c.getInt(c.getColumnIndex(CoursesTable.COLUMN_ID)));
			course.setCode(c.getString(c.getColumnIndex(CoursesTable.COLUMN_COURSE_CODE)));
			course.setDuration(c.getInt(c.getColumnIndex(CoursesTable.COLUMN_DURATION)));
			course.setSemesters(c.getInt(c.getColumnIndex(CoursesTable.COLUMN_SEMESTERS_PER_TERM)));
			course.setTitle(c.getString(c.getColumnIndex(CoursesTable.COLUMN_TITLE)));
			
		} catch(Exception e) {
			if(AppUtils.isDebugging())
				e.printStackTrace();
			
			course = null;
		}
		
		return course;
	}
}