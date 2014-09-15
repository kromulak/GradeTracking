package com.ollienoonan.gradetracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ollienoonan.gradetracker.sqlite.helper.GraderDatabaseHelper;
import com.ollienoonan.gradetracker.sqlite.helper.ModulesTable;
import com.ollienoonan.gradetracker.sqlite.model.Module;

/**
 * Acivity for editing the details of a Module
 * @author Ollie Noonan
 * @version 1 - 2014/07/30
 */
public class ModuleEditorActivity extends ActionBarActivity{	

	//the current semester and year
	private int CURRENT_SEMESTER, CURRENT_YEAR;
	
	//The Module being edited
	private Module module;
	
	//the db_id of the module being edited
	private long moduleID;
	
	//for passing arguments to this activity through the extras bundle
	public static final String ARG_SEMESTER = "SEMESTER";
	public static final String ARG_YEAR = "YEAR";
	public static final String ARG_MODULE = "MODULE";
	public static final String ARG_MODE = "MODE";
	
	
	//editing an existing module that exists in the db
	public static final int MODE_EDIT = 1;
	//editing a new module thats not yet in the db
	public static final int MODE_CREATE = 2;
	//are we editing an existing Module or a new one
	private static int MODE = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//set the xml view for this activity
		setContentView(R.layout.module_details_setupview);
		
		//get the extras bundle
		Bundle extras = getIntent().getExtras();

		//setup the actionbar
		setupActionBar();
		//initialise values passed in through the extras bundle
		initFromPassedArgs(extras);
		//set the required state of the semester checkboxes
		setSemestersCheckBoxes();

		if(module == null) {
			module = new Module();
			module.setYear(CURRENT_YEAR);
			module.setSemester(CURRENT_SEMESTER);
		} else {
			fillFieldsForEditing(module);
		}		
	}


	/**
	 * Marks the semesters checkboxes to their correct state based on what semester this module spans
	 */
	private void setSemestersCheckBoxes() {
		if(CURRENT_SEMESTER == Module.SEMESTER_ONE_AND_TWO) {
			((CheckBox) findViewById(R.id.module_setupview_sem1checkbox)).setChecked(true);
			((CheckBox) findViewById(R.id.module_setupview_sem2checkbox)).setChecked(true);
		} else if(CURRENT_SEMESTER == Module.SEMESTER_TWO) {
			((CheckBox) findViewById(R.id.module_setupview_sem1checkbox)).setChecked(false);
			((CheckBox) findViewById(R.id.module_setupview_sem2checkbox)).setChecked(true);
		} else if(CURRENT_SEMESTER == Module.SEMESTER_ONE) {
			((CheckBox) findViewById(R.id.module_setupview_sem1checkbox)).setChecked(true);
			((CheckBox) findViewById(R.id.module_setupview_sem2checkbox)).setChecked(false);
		}
	}


	/**
	 * Initializes values to the arguments passed in the extras bundle
	 * 
	 * @param extras
	 */
	private void initFromPassedArgs(Bundle extras) {
		
		if(extras != null) {
			MODE = extras.getInt(ARG_MODE);
			
			if(MODE == MODE_CREATE) { //we are making a new module
				//so extras should contain the semester and year
				CURRENT_SEMESTER = extras.getInt(ARG_SEMESTER);
				CURRENT_YEAR = extras.getInt(ARG_YEAR);
				
			} else if(MODE == MODE_EDIT) { //we are editing an existing module
				//so extras should contain the moduleID to edit
				moduleID = extras.getInt(ARG_MODULE); //get the id of the module to be edited

				if(moduleID > 0) { //make sure its at least 1 as database ids begin at 1
					loadRecordFromDatabase();	
				}
				
				//once we have the module and exam objects we can set the current semester and year
				CURRENT_SEMESTER = module.getSemester();
				CURRENT_YEAR = module.getYear();				
			} else {
				Toast.makeText(getApplicationContext(), "No mode set", Toast.LENGTH_SHORT).show();
				
				setResult(RESULT_CANCELED);
				finish();
			}
		}
	}


	/**
	 * loads the module based on the class variable moduleID
	 */
	private void loadRecordFromDatabase() {
		if(moduleID > 0) {
			module = new GraderDatabaseHelper(this).getModuleFromDatabase(moduleID);
		}
	}


	/**
	 * Sets up the support ActionBar
	 */
	private void setupActionBar() {
		ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	
	/**
	 * Fills all the views using the values in the given Module
	 * @param m
	 */
	private void fillFieldsForEditing(Module m) {
		((EditText) findViewById(R.id.module_setupview_codeEditText)).setText(m.getCode());
		((EditText) findViewById(R.id.module_setupview_titleEditText)).setText(m.getTitle());
		((EditText) findViewById(R.id.module_setupview_minEditText)).setText(""+m.getMinPassGrade());
		((EditText) findViewById(R.id.module_setupview_goalEditText)).setText(m.getGoalGrade()+"");
		((EditText) findViewById(R.id.module_setupview_finalEditText)).setText(m.getFinalsGrade()+"");
		((EditText) findViewById(R.id.module_setupview_caEditText)).setText(m.getCaWeight()+"");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.modulesetup_actionbar, menu);
	    
	    if(MODE == MODE_CREATE) {
	    	menu.removeItem(R.id.action_delete);
	    }
	    
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_save:
	            if(validateAllFieldsOk())
	            	saveDataToDatabase();
	            return true;
	        case R.id.action_help:
	            showHelp();
	            return true;
	        case R.id.action_delete:
	        	deleteWithConfirmDialog();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	
	/**
	 * Deletes the module from the database if in edit mode.
	 * calls finish on this activity once deleted
	 */
	private void deleteData() {
		GraderDatabaseHelper datasource = new GraderDatabaseHelper(ModuleEditorActivity.this);

		datasource.deleteRecord(module);
		
		datasource.closeDB();
		
		setResult(RESULT_OK);
		finish();
	}

	/**
	 * Shows confirmation dialog before deleting
	 */
	private void deleteWithConfirmDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.dialog_delete_msg)
		   .setCancelable(false)
		   .setPositiveButton(R.string.dialog_confirm_txt, new DialogInterface.OnClickListener() {
		       public void onClick(DialogInterface dialog, int id) {
		            ModuleEditorActivity.this.deleteData();
		       }
		   })
		   .setNegativeButton(R.string.dialog_cancel_txt, new DialogInterface.OnClickListener() {
		       public void onClick(DialogInterface dialog, int id) {
		            dialog.cancel();
		       }
		   })
		   //Set your icon here
		   .setTitle(R.string.dialog_delete_title);
		   //.setIcon(R.drawable.icon);
		AlertDialog alert = builder.create();
		alert.show();//showing the dialog
	}

	/**
	 * Show a help dialog explaining how to create a new module etc
	 */
	private void showHelp() {
		//TODO: need to create help dialog for this activity explaining how
	}

	/**
	 * saved the data to the database
	 */
	private void saveDataToDatabase() {
		//gain access to the database
		GraderDatabaseHelper datasource = new GraderDatabaseHelper(ModuleEditorActivity.this);
		
		if(MODE == MODE_CREATE) {
			module.setId(datasource.insertRecord(module));
			
			Log.d("MD", "Saved MODULE " + module.getCode() + " with id " + module.getId());
			Log.d("MD", "Saved MODULE " + module.getSemester() + "," + module.getYear());
			Log.d("MD", "Saved MODULE " + module.getCaWeight() + "," + module.getTitle());
			Log.d("MD", "Saved MODULE " + module.getFinalsGrade());
			
		
		}
		else {
			datasource.updateRecord(module);
			Log.d("MD", "updated MODULE " + module.getCode() + " with id " + module.getId());
		}
			
		//always close access to the database
		datasource.closeDB();
		
		//end the activity
		if(module.getId() < 1)
			setResult(RESULT_CANCELED);
		else {
			setResult(RESULT_OK);
		
			finish();
		}
	}

	
	/**
	 * Checks if all fields have been completed correctly
	 * @return
	 */
	private boolean validateAllFieldsOk() {
		return (validateModuleCode() &&
				validateModuleTitle() &&
				validateModuleMinPass() &&
				validateModuleGoal() &&
				validateModuleFinalvCA() &&
				validateModuleSemesters());
	}


	/**
	 * Checks if the user has entered a valid module code.<br>
	 * Prevents duplicate/empty codes
	 * 
	 * @return
	 */
	private boolean validateModuleCode() {
		EditText eT = ((EditText) findViewById(R.id.module_setupview_codeEditText));
		
		String  str = eT.getText().toString();

        if(str.equalsIgnoreCase(""))
        {
            eT.setHint("Enter Code");//it gives user to hint
            eT.setError("please enter module code");//it gives user to info message //use any one //
            
            return false;
        } else {
        	GraderDatabaseHelper datasource = new GraderDatabaseHelper(ModuleEditorActivity.this);
        	
        	Module temp = datasource.getModuleFromDatabase(str, module.getSemester(), module.getYear());
        	if( (temp == null) || (temp.getId() == module.getId()) ) {
        		module.setCode(str);
        	//} else if(temp.getId() == module.getId()) {
        		
        	} else{
                eT.setHint("Enter Code");//it gives user to hint
                eT.setError("Module code already exists for this semester");//it gives user to info message //use any one //
                
                return false;
        	}
        	
        	return true;
        }
	}
	
	/**
	 * Checks if the user has entered a valid module title.<br>
	 * Prevents empty titles
	 * 
	 * @return
	 */
	private boolean validateModuleTitle() {
		EditText eT = ((EditText) findViewById(R.id.module_setupview_titleEditText));
		
		String  str = eT.getText().toString();

        if(str.equalsIgnoreCase(""))
        {
        	eT.setHint("Enter title");//it gives user to hint
        	eT.setError("please enter module title");//it gives user to info message //use any one //
            
            return false;
        } else {
        	module.setTitle(str);
        	
        	return true;
        }
    }
	
	/**
	 * Checks if the user has entered a valid pass amount.<br>
	 * ensures value is between 0 and 100
	 * 
	 * @return
	 */
	private boolean validateModuleMinPass() {

		float f = validateNumericEditTextWithinRange(
				((EditText) findViewById(R.id.module_setupview_minEditText)),
				0,100,
				"40", "Should be between 0 and 100",
				false
				);
		
		if(f == -1)
			return false;

		module.setMinPassGrade(f);
            
		return true;
	}

	/**
	 * Checks if the user has entered a valid goal amount.<br>
	 * ensures value is between 0 and 100
	 * 
	 * @return
	 */
	private boolean validateModuleGoal() {
		float f = validateNumericEditTextWithinRange(
				((EditText) findViewById(R.id.module_setupview_goalEditText)),
				0,100,
				"40", "Should be between 0 and 100",
				true
				);
		
		if(f == -1)
			return false;
		
		

		module.setGoalGrade(f);
            
        return true;
	}
	
	/**
	 * Checks if the user has entered a valid final exam amount.<br>
	 * ensures value is between 0 and 100
	 * 
	 * @return
	 */
	private boolean validateModuleFinalvCA() {
		float f = validateNumericEditTextWithinRange(
				((EditText) findViewById(R.id.module_setupview_finalEditText)),
				0,100,
				"70", "Should be between 0 and 100",
				false
				);
		
		if(f == -1)
			return false;
		
		((EditText) findViewById(R.id.module_setupview_caEditText)).setText(""+(100-f));
		
		module.setCaWeight(100-f);
		
		return true;
	}
	
	
	/**
	 * checks that the given EditText contains a number within the given range<br>
	 * 
	 * @param editText the id of the EditText to check
	 * @param min the minimum valid value
	 * @param max the maximum valid value
	 * @param hint the hinted value to show
	 * @param errorMsg the error message when invalid range
	 * @param allowEmpty 
	 * 
	 * @return the valid value or -1
	 */
	public static float validateNumericEditTextWithinRange(EditText editText, float min, float max, String hint, String errorMsg, boolean allowEmpty) {
		
		String  str = editText.getText().toString();
		
		if(allowEmpty && str.length() == 0) {
			return 0;
		}

        try {
        	float f = Float.parseFloat(str);
        	
        	if((f < min)||(f > max))
        		throw new Exception(str + " is out of range ");
        	else {
        		return f;
        	}
        } catch(Exception e) {
    		editText.setHint(hint);//it gives user to hint
    		editText.setError(errorMsg);//it gives user to info message //use any one //
        	
    		return -1;
        }
	}
	
	/**
	 * Checks if the user has selected at least one semester
	 * 
	 * @return
	 */
	private boolean validateModuleSemesters() {
		CheckBox one = (CheckBox) findViewById(R.id.module_setupview_sem1checkbox);
		CheckBox two = (CheckBox) findViewById(R.id.module_setupview_sem2checkbox);
		
		int x = Module.SEMESTER_ONE;
		
		if( one.isChecked() && two.isChecked() ) {
			x = Module.SEMESTER_ONE_AND_TWO;
		} else if( one.isChecked() && !two.isChecked() ) {
			x = Module.SEMESTER_ONE;
		} else if( !one.isChecked() && two.isChecked() ) {
			x = Module.SEMESTER_TWO;
		} else {
			one.setError("Pick at least one semester");
			return false;
		}
			
		module.setSemester(x);
		return true;
	}

}