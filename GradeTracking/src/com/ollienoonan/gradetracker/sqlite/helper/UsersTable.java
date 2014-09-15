package com.ollienoonan.gradetracker.sqlite.helper;

import com.ollienoonan.android.dev.AppUtils;
import com.ollienoonan.gradetracker.sqlite.model.User;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author Ollie Noonan
 * @version 2014/06/19
 * 
 * Database table for Users data
 * Contains required methods for creation and update, also some useful constants
 */
public class UsersTable {

	// Database table
	public static final String TABLE = "users";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_STUDENT_NUMBER = "student_number";

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table "
	      + TABLE
	      + "("
	      	+ COLUMN_ID + " integer primary key autoincrement, "
	      	+ COLUMN_TITLE + " text not null, "
	      	+ COLUMN_STUDENT_NUMBER + " text"
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
	    		Log.w(UsersTable.class.getName(), "Upgrading database from version "
	    			       + oldVersion + " to " + newVersion
	    			       	        + ", which will destroy all old data");
	    		database.execSQL("DROP TABLE IF EXISTS " + TABLE);
	    		onCreate(database);
	    	case 2:
	    		//db.execSQL(DATABASE_CREATE_someothertable);
	    }
	}

	/**
	 * Converts the given User Object to a ContentsValue Object.
	 * The ContentValues Object contains all the details of the User except its database id.
	 * All keys set using UsersTable column constants
	 * @param user the user to be converted
	 * @return the contentvalue
	 */
	public static ContentValues convertToContentValues(User user) {
		ContentValues values = new ContentValues();

		values.put(UsersTable.COLUMN_STUDENT_NUMBER, user.getStudentNumber());
		values.put(UsersTable.COLUMN_TITLE, user.getName());
		
		return values;
	}
}