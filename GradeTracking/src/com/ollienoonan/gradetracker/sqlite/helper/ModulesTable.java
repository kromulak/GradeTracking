package com.ollienoonan.gradetracker.sqlite.helper;

import com.ollienoonan.android.dev.AppUtils;
import com.ollienoonan.gradetracker.sqlite.model.Module;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author Ollie Noonan
 * @version 1 - 2014/06/19
 * 
 * This is the details for the modules table in the database.<br>
 * Contains the methods for calling create and update of this table.<br>
 * Any update needing to be done to the Modules Table should be done in the onUpgrade method in this class.
 * Also contains constants for column names etc.
 */
public class ModulesTable {
	
	// Database table
	public static final String TABLE = "modules";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_MODULE_CODE = "code";
	public static final String COLUMN_MODULE_TITLE = "title";
	public static final String COLUMN_SEMESTER = "semester";
	public static final String COLUMN_YEAR = "year";
	public static final String COLUMN_MIN_PASS_GRADE = "min_grade_required";
	public static final String COLUMN_GOAL_PASS_GRADE = "goal_grade";
	public static final String COLUMN_CAWEIGHT = "ca_weight";
	public static final String COLUMN_CURRENT_GRADE = "current_grade";


	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE
			+ "("
				+ COLUMN_ID + " integer primary key autoincrement, "
				+ COLUMN_MODULE_CODE + " text not null, "
				+ COLUMN_MODULE_TITLE + " text not null, "
				+ COLUMN_SEMESTER + " integer not null,"
				+ COLUMN_YEAR + " integer not null,"
				+ COLUMN_MIN_PASS_GRADE + " real not null,"
				+ COLUMN_GOAL_PASS_GRADE + " real not null,"
				+ COLUMN_CAWEIGHT + " real not null,"
				+ COLUMN_CURRENT_GRADE + " real not null"
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
    			Log.w(ModulesTable.class.getName(), "Upgrading database from version "
    			       + oldVersion + " to " + newVersion
    			       	        + ", which will destroy all old data");
    			database.execSQL("DROP TABLE IF EXISTS " + TABLE);
    			onCreate(database);
    		case 2:
    		//db.execSQL(DATABASE_CREATE_someothertable);
		}
	}

	/**
	 * Converts the given Module Object to a ContentsValue Object.
	 * The ContentValues Object contains all the details of the Module except its database id.
	 * All keys set using ModulesTable.COLUMN_.. constants
	 * @param module the module to be converted
	 * @return the contentvalue
	 */
	public static ContentValues convertToContentValues(Module module) {
		ContentValues values = new ContentValues();

		values.put(ModulesTable.COLUMN_MODULE_CODE, module.getCode());
		values.put(ModulesTable.COLUMN_MODULE_TITLE, module.getTitle());
		values.put(ModulesTable.COLUMN_SEMESTER, module.getSemester());
		values.put(ModulesTable.COLUMN_YEAR, module.getYear());
		values.put(ModulesTable.COLUMN_MIN_PASS_GRADE, module.getMinPassGrade());
		values.put(ModulesTable.COLUMN_GOAL_PASS_GRADE, module.getGoalGrade());
		values.put(ModulesTable.COLUMN_CAWEIGHT, module.getCaWeight());
		values.put(ModulesTable.COLUMN_CURRENT_GRADE, module.getCurrentGrade());
		
		return values;
	}

	
	/**
	 * Converts the given Cursor Object to a Module Object.
	 * Column titles of the Cursor should match with the COLUMN_ constants for ModulesTable
	 * 
	 * @param c The Cursor Object to be converted
	 * @return A Module Object
	 */
	public static Module convertToModule(Cursor c) {
		Module module = null;
		
		try {
			module = new Module();
			
			module.setId(c.getInt(c.getColumnIndex(ModulesTable.COLUMN_ID)));
			module.setCode(c.getString(c.getColumnIndex(ModulesTable.COLUMN_MODULE_CODE)));
			module.setTitle(c.getString(c.getColumnIndex(ModulesTable.COLUMN_MODULE_TITLE)));
			module.setSemester(c.getInt(c.getColumnIndex(ModulesTable.COLUMN_SEMESTER)));
			module.setYear(c.getInt(c.getColumnIndex(ModulesTable.COLUMN_YEAR)));
			module.setMinPassGrade(c.getFloat(c.getColumnIndex(ModulesTable.COLUMN_MIN_PASS_GRADE)));
			module.setGoalGrade(c.getFloat(c.getColumnIndex(ModulesTable.COLUMN_GOAL_PASS_GRADE)));
			module.setCaWeight(c.getFloat(c.getColumnIndex(ModulesTable.COLUMN_CAWEIGHT)));
			module.setCurrentGrade(c.getFloat(c.getColumnIndex(ModulesTable.COLUMN_CURRENT_GRADE)));
		} catch(Exception e) {
			if(AppUtils.isDebugging())
				e.printStackTrace();
			
			module = null;
		}
		
		return module;
	}
}