package com.ollienoonan.gradetracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.ollienoonan.android.dev.AppUtils;
import com.ollienoonan.gradetracker.adapters.NavDrawerExpandableListAdapter;
import com.ollienoonan.gradetracker.sqlite.helper.GraderDatabaseHelper;
import com.ollienoonan.gradetracker.sqlite.model.Course;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

/**
 * 
 * @author Ollie Noonan
 * @version 1 - 2014/07/30
 */
public class MainActivity extends ActionBarActivity {

	private DrawerLayout mDrawerLayout;
	private ExpandableListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	
	private Course mCourse;

	//dont want to allow drawer until after we have established runtype
	private boolean allowDrawer = false;

	private int CURRENT_SEMESTER, CURRENT_YEAR;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mainview);

		if(AppUtils.isDebugging())
			Log.d("MainActivity", "Layout set" );

				
		boolean allOK = handleRunTypes();

		if(AppUtils.isDebugging())
			Log.d("MainActivity", "Startup modes handled");

		if(allOK) {
			loadCourseData();
			
			if (savedInstanceState != null) {
				CURRENT_SEMESTER = savedInstanceState.getInt(SemesterFragment.ARG_SEMESTER, 1);
				CURRENT_YEAR = savedInstanceState.getInt(SemesterFragment.ARG_YEAR, 1);
				Toast.makeText(this,  "Loaded from savedInstance", Toast.LENGTH_SHORT).show();
			} else {
				initGlobals();
			}
			
			completeStartup(savedInstanceState);
		}
	}

	public void completeStartup(Bundle savedInstanceState) {
//		initGlobals();

		setupActionBar();
		
		setupHomeFragment();	

		setupNavDrawer(savedInstanceState);	
		
	}
	

	private void setupHomeFragment() {
		setupHomeFragment(CURRENT_YEAR,CURRENT_SEMESTER);
	}
	
	private void setupHomeFragment(int year, int semester) {
		Fragment fragment = new SemesterFragment();
		Bundle args = new Bundle();
		args.putInt(SemesterFragment.ARG_SEMESTER, semester);
		args.putInt(SemesterFragment.ARG_YEAR, year);
		fragment.setArguments(args);

		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
	}
	

	/**
	 * Handles what to do based on the type of run situation.
	 * Could be very first time user has ever ran the app
	 * Could be first run after an update
	 * Could be none of the above and simply user just running the app  
	 * @return 
	 */
	private boolean handleRunTypes() {
		PackageInfo pInfo;
		try {
			//noinspection ConstantConditions
			pInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
			//boolean acceptedEULA = true;//TODO getSharedPreferences("GRADER_OPTIONS", MODE_PRIVATE).getBoolean("eula", false);
			long lastRunVersionCode =  getSharedPreferences("GRADER_OPTIONS", MODE_PRIVATE).getLong("lastRunVersionCode", 0);

			// TODO: Handle your first-run situations here
			if(lastRunVersionCode == pInfo.versionCode) {
				//should be most common
				//we are fine its simply just someone using the app again so dont do anything
				//if settings->ask for users at login is true might want to show users list
				//granted could cause issue where user just went back to main screen not actually launched
				if(AppUtils.isDebugging())
					Log.d("Main", "nothing to see here, just running the app");

				//Everything is complete so update the last run number to the current version
				SharedPreferences.Editor editor = getSharedPreferences("GRADER_OPTIONS", MODE_PRIVATE).edit();
				editor.putLong("lastRunVersionCode", pInfo.versionCode);
				editor.commit();

				return true;
			}
			else if(lastRunVersionCode == 0) {
				//very first run, no existing data, in the long run should be common enough
				//show what's new in version x dialog, t's & c's, create user dialog
				/////////////showDialog_WhatsNew(pInfo.packageName,0);
				//everything's complete so update the last run number to the current version

				SharedPreferences.Editor editor = getSharedPreferences("GRADER_OPTIONS", MODE_PRIVATE).edit();
				editor.putLong("lastRunVersionCode", pInfo.versionCode);
				editor.commit();

				Intent i = new Intent(this, CourseEditorActivity.class);
				startActivityForResult(i, REQUEST_CREATE_COURSE);

				if(AppUtils.isDebugging()) {
					Log.d("MainActivity", "sent to course wizard");
				}
				return false;

			} else if(lastRunVersionCode < pInfo.versionCode) {
				//its someone who upgraded
				//show what's new dialog anyways
				//showDialog_WhatsNew(pInfo.packageName,0);
				//everything's complete so update the last run number to the current version
				SharedPreferences.Editor editor = getSharedPreferences("GRADER_OPTIONS", MODE_PRIVATE).edit();
				editor.putLong("lastRunVersionCode", pInfo.versionCode);
				editor.commit();

				return true;
			} else {
				//i think this means they haven't accepted the eula yet the app was ran before
				//dont think this should exist so just gonna end here
				finish();
				return false;
			}
		} catch (PackageManager.NameNotFoundException e) {
			// TODO Something pretty serious went wrong if you got here...
			if(AppUtils.isDebugging())
				e.printStackTrace();

			return false;
		}
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		//Toast.makeText(this, "MainActivity: results", Toast.LENGTH_SHORT).show();
		
		// Check which request we're responding to
	    switch(requestCode) {
		    case REQUEST_CREATE_MODULE:
		        // Make sure the request was successful
		        if (resultCode == RESULT_OK) {
		        	completeStartup(null);
		        }
		        break;
		    case REQUEST_CREATE_COURSE:
		    	if (resultCode == RESULT_OK) {
		    		loadCourseData();
		    		initGlobals();
		    		completeStartup(null);
		    	}
		    	break;
	    }
	}

	/**
	 * Initializes the global variables used in this class.
	 * If preferred values have been set by the user then they load here.
	 */
	private void initGlobals() {
		SharedPreferences sharedPref = getSharedPreferences("GRADER_OPTIONS",MODE_PRIVATE);
		
		this.CURRENT_SEMESTER = sharedPref.getInt("preferredSemester", 1);;
		this.CURRENT_YEAR = sharedPref.getInt("preferredYear", 1);
	}

	/**
	 * 
	 */
	private void loadCourseData() {
		GraderDatabaseHelper temp = new GraderDatabaseHelper(this);
		mCourse = temp.getCourseFromDatabase(1);
		Log.d("TTT2", mCourse.getTitle());
		temp.closeDB();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainview_actionbar, menu);
		this.menu = menu;
		return super.onCreateOptionsMenu(menu);
	}	
	
	MenuItem m = null;
	Menu menu = null;

	/* (non-Javadoc)
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		m = menu.findItem(R.id.action_set_as_current_semester);
		
		boolean b = isDefaultSemester();
		
		m.setChecked(b);
		m.setEnabled(!b);
		
		return true;
	}
	
	

	private void refreshMenu() {
		m = menu.findItem(R.id.action_set_as_current_semester);
		
		boolean b = isDefaultSemester();
		
		m.setChecked(b);
		m.setEnabled(!b);
		
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if(allowDrawer) {
			if (mDrawerToggle.onOptionsItemSelected(item)) {
				return true;
			}
		}

		// Handle action buttons
		switch(item.getItemId()) {
		case R.id.action_new:
			launchActivity_newModule();
			return true;
		case R.id.action_set_as_current_semester:
			setAsCurrentSemester();
			return true;
		case R.id.action_settings:
			launchActivity_settings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void setAsCurrentSemester() {
		SharedPreferences sharedPref = getSharedPreferences("GRADER_OPTIONS", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("preferredSemester", CURRENT_SEMESTER);
		editor.putInt("preferredYear", CURRENT_YEAR);
		editor.commit();
		
		refreshMenu();
	}
	
	private boolean isDefaultSemester() {
		SharedPreferences sharedPref = getSharedPreferences("GRADER_OPTIONS",MODE_PRIVATE);
		
		int defaultSem = sharedPref.getInt("preferredSemester", 1);
		int defaultYear = sharedPref.getInt("preferredYear", 1);
		
		return ( (defaultSem == CURRENT_SEMESTER)&&(defaultYear == CURRENT_YEAR) );
	}


	public void launchActivity_newModule() {
		Intent i = new Intent(this, ModuleEditorActivity.class);
		
		i.putExtra(ModuleEditorActivity.ARG_SEMESTER, this.CURRENT_SEMESTER);
		i.putExtra(ModuleEditorActivity.ARG_YEAR, this.CURRENT_YEAR);
		i.putExtra(ModuleEditorActivity.ARG_MODE, ModuleEditorActivity.MODE_CREATE);
		
		startActivityForResult(i, REQUEST_CREATE_MODULE);
	}


	public void launchActivity_settings() {
		Intent i = new Intent(this, SettingsActivity.class);
		startActivity(i);
	}
	
	/**
	 * Sets the Action Bar for new Android versions.
	 */
	//@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
	  //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	    ActionBar ab = getSupportActionBar();
	    ab.setTitle("GradeTracking");
	    ab.setSubtitle("Semester " + CURRENT_SEMESTER + ", " + AppUtils.ordinal(CURRENT_YEAR) + " Year"); 
	  //}
	}
	

	NavDrawerExpandableListAdapter listAdapter;
	List<String> listDataHeader;
	HashMap<String, List<DrawerMenuHolder>> listDataChild;

	public void setupNavDrawer(Bundle savedInstanceState) {
		//mTitle = getSupportActionBar().getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);

		// preparing list data
		prepareListData();

		listAdapter = new NavDrawerExpandableListAdapter(this, listDataHeader, listDataChild);

		// setting list adapter
		mDrawerList.setAdapter(listAdapter);

		// Set the adapter for the list view
		//		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
		//			R.layout.drawer_list_item, mPlanetTitles));
		// Set the list's click listener
		//mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
		mDrawerList.setOnChildClickListener(new OnChildClickListener() {
			 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                        listDataHeader.get(groupPosition)).get(
                                        childPosition), Toast.LENGTH_SHORT)
                        .show();
                
                DrawerMenuHolder temp = (DrawerMenuHolder) listDataChild.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition);

                CURRENT_YEAR = temp.getYear();
                CURRENT_SEMESTER = temp.getSemester();          
                
                mDrawerLayout.closeDrawers();
                setupHomeFragment();
                
                refreshMenu();
                
                Toast.makeText(
                        getApplicationContext(),
                        CURRENT_YEAR
                                + " : "
                                + CURRENT_SEMESTER, Toast.LENGTH_LONG)
                        .show();
                
                return false;
            }
        });

		mDrawerToggle = new ActionBarDrawerToggle(this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description */
				R.string.drawer_close  /* "close drawer" description */) {


			/** Called when a drawer has settled in a completely closed state. */

			public void onDrawerClosed(View view) {
				setupActionBar();
			}


			/** Called when a drawer has settled in a completely open state. */

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle("Explore");
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mDrawerList.expandGroup(listDataHeader.size() - CURRENT_YEAR);

		allowDrawer = true;
	}

	/*
	 * Preparing the list data
	 */
	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<DrawerMenuHolder>>();

		Log.d("TTT", mCourse.getTitle());

		for(int years = mCourse.getDuration(); years > 0; years--) {
			String header = AppUtils.ordinal(years) + " Year";
			List<DrawerMenuHolder> term = new ArrayList<DrawerMenuHolder>();

			for(int semesters = mCourse.getSemesters(); semesters > 0; semesters--) {
				int c = new GraderDatabaseHelper(MainActivity.this).getAllModulesFromDatabase(semesters, years).size();
				term.add(new DrawerMenuHolder("["+c+"] Semester "+semesters, years, semesters));
			}

			listDataHeader.add(header);
			listDataChild.put(header, term);

		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    outState.putInt(SemesterFragment.ARG_SEMESTER, CURRENT_SEMESTER);
	    outState.putInt(SemesterFragment.ARG_YEAR, CURRENT_YEAR);
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		if(allowDrawer)
			mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private static final int REQUEST_CREATE_MODULE = 1;
	private static final int REQUEST_CREATE_COURSE = 2;
} 