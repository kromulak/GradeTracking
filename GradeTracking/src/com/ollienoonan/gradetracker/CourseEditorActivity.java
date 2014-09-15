package com.ollienoonan.gradetracker;

import com.ollienoonan.gradetracker.sqlite.helper.GraderDatabaseHelper;
import com.ollienoonan.gradetracker.sqlite.model.Course;
import com.ollienoonan.gradetracker.sqlite.model.User;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * Activity to allow user setup their course information
 * 
 * @author Ollie Noonan
 * @version 1 2014/07/13
 */
public class CourseEditorActivity extends ActionBarActivity {
	
	//TODO: user will only be useful when app allows multiple users
	//The user who is doing this course
	private User user = new User();
	//The Course begin setup
	private Course course = new Course();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//set the layout
		setContentView(R.layout.course_setupview);
		//setup the controls used in the view
		setupControls();
	}
	
	
	/**
	 * Configures the controls being used in this activity.
	 */
	private void setupControls() {
		((Button) findViewById(R.id.course_setupview_saveButton)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(allFieldsOk()) {
					saveData();
				}
			}
			
		});
	}
	
	/**
	 * Saves the Course into the database
	 */
	private void saveData() {
		GraderDatabaseHelper dataSource = new GraderDatabaseHelper(CourseEditorActivity.this);
		
		//long u = 
		dataSource.insertRecord(user);
		//TODO: When adding multiuser support will need something like course.setUderId(u) before adding to db;
		long l = dataSource.insertRecord(course);

		//set the result, if we got to here then everything is ok
		this.setResult(RESULT_OK);
		//DEBUG:
		Log.d("COURSE","Saved course " + l);
		
		//close the link to the db
		dataSource.closeDB();
		finish();
	}


	/**
	 * Checks if all fields in the view have been filled out as required
	 * 
	 * @return true if all fields filled out correctly, false otherwise
	 */
	protected boolean allFieldsOk() {

		return (validateCourseCodeEditText() &&
				validateCourseTitleEditText() &&        
				validateCourseDurationEditText() &&
				validateCourseSemestersEditText() &&
				validateUserNameEditText());
	}


	/**
	 * Checks if a valid Course Code has been entered
	 * Doesnt allow empty string
	 * @return true if ok, false otherwise
	 */
	public boolean validateCourseCodeEditText() {
		codeET = ((EditText) findViewById(R.id.course_setupview_codeEditText));
		
		String  str = codeET.getText().toString();

        if(str.equalsIgnoreCase(""))
        {
            codeET.setHint("Enter Code");//it gives user to hint
            codeET.setError("please enter code");//it gives user info message 
            
            return false;
        } else {
        	course.setCode(str);
        	
        	return true;
        }
	}
	

	/**
	 * Checks if a valid Course Title has been entered
	 * Doesnt allow empty string
	 * @return true if ok, false otherwise
	 */
	public boolean validateCourseTitleEditText() {
		titleET = ((EditText) findViewById(R.id.course_setupview_titleEditText));
		
		String  str = titleET.getText().toString();

        if(str.equalsIgnoreCase(""))
        {
            titleET.setHint("Enter Title");//it gives user to hint
            titleET.setError("please enter course title");//it gives user to info message //use any one //
            
            return false;
        } else {
        	course.setTitle(str);
        	
        	return true;
        }
	}
	
	
	/**
	 * Checks if a valid Course Length has been entered
	 * Only allows positive numbers.
	 * @return true if ok, false otherwise
	 */
	public boolean validateCourseDurationEditText() {
		durationET = ((EditText) findViewById(R.id.course_setupview_durationEditText));
		
		String  str = durationET.getText().toString();

		try {
			int d = Integer.parseInt(str);
			if(d < 1)
				throw new Exception();
				
			course.setDuration(d);
			
			return true;
			
		} catch(Exception e) {
        	durationET.setHint("Enter duration");//it gives user to hint
        	durationET.setError("please enter duration of course");//it gives user to info message //use any one //
            
            return false;
        }
	}

	/**
	 * Checks if a valid Course semesters amount has been entered
	 * Only allows positive numbers.
	 * @return true if ok, false otherwise
	 */
	public boolean validateCourseSemestersEditText() {
		semestersET = ((EditText) findViewById(R.id.course_setupview_semestersEditText));
		
		String  str = semestersET.getText().toString();

		try {
			int d = Integer.parseInt(str);
			if(d < 1)
				throw new Exception();
			
			course.setSemesters(d);
			
			return true;
			
		} catch(Exception e) {
			semestersET.setHint("Enter semesters");//it gives user to hint
        	semestersET.setError("please enter semesters per year");//it gives user to info message //use any one //
            
            return false;
        }
	}
	
	/**
	 * Checks if a valid user name has been entered
	 * Doesnt allow empty string
	 * @return true if ok, false otherwise
	 */
	public boolean validateUserNameEditText() {
		usernameET = ((EditText) findViewById(R.id.course_setupview_nameEditText));
		
		String  str = usernameET.getText().toString();

        if(str.equalsIgnoreCase(""))
        {
        	usernameET.setHint("Enter name");//it gives user to hint
        	usernameET.setError("please enter your name");//it gives user to info message //use any one //
            
            return false;
        } else {
        	user = new User();
        	user.setName(str);
        	
        	//TODO: Need to add field for optional student number
        	user.setStudentNumber("4");
        	return true;
        }
	}
	
	//the fields in this activitys view
	private EditText codeET, titleET, durationET, semestersET, usernameET;
}
