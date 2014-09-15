package com.ollienoonan.gradetracker;

import java.util.List;
import com.ollienoonan.android.dev.AppUtils;
import com.ollienoonan.gradetracker.ModuleFragment.OnFragmentInteractionListener;
import com.ollienoonan.gradetracker.sqlite.helper.GraderDatabaseHelper;
import com.ollienoonan.gradetracker.sqlite.model.Module;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;


public class ModulesActivity extends ActionBarActivity implements OnFragmentInteractionListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
//	SectionsPagerAdapter mSectionsPagerAdapter;
	ModulePagerAdapter mModulePagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private int mYear, mSemester, mPosition;
	
	public static final String ARG_SEMESTER = "SEMESTER";
	public static final String ARG_YEAR = "YEAR";
	public static final String ARG_POSITION = "POSITION";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modules);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		//mSectionsPagerAdapter = new SectionsPagerAdapter(
//				getSupportFragmentManager());
		
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			mSemester = extras.getInt(ARG_SEMESTER);
			mYear = extras.getInt(ARG_YEAR);
			mPosition = extras.getInt(ARG_POSITION);
		}
		
		mModulePagerAdapter = new ModulePagerAdapter(getSupportFragmentManager(), mSemester, mYear);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mModulePagerAdapter);//mSectionsPagerAdapter);

		mViewPager.setCurrentItem(mPosition, true);
		
		actionBarSetup();
	}

	/**
	 * Sets the Action Bar for new Android versions.
	 */
	//@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void actionBarSetup() {
	  //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	    ActionBar ab = getSupportActionBar();
	    ab.setTitle("GradeTracking");
	    ab.setSubtitle("Semester " + mSemester + ", " + AppUtils.ordinal(mYear) + " Year"); 
	  //}
	}
	
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.modulesfragment_actionbar, menu);
		
//		if(mModulePagerAdapter.getc)
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		switch(id) {
//			case R.id.action_add_exam:
	//			includeExam();
		//		break;
			case R.id.action_add_category:
				createNewCategory();
				return true;
			case R.id.action_add_assessment:
				createNewAssessment();
				return true;
			case R.id.action_settings:
				showSettings();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}


	private void createNewCategory() {
		// TODO Auto-generated method stub
		
	}

	private void createNewAssessment() {
		// TODO Auto-generated method stub
		
	}

	private void showSettings() {
		// TODO Auto-generated method stub
		
	}

*/
	/**
	 * A {@link FragmentStatePagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class ModulePagerAdapter extends FragmentStatePagerAdapter {

		/**the cursor from the database used to populate the list of tabs to display*/
		private List<Module> tabsModuleList;

		/**the year currently being displayed*/
		private int year = 2;
		/**the semester currently being displayed*/
		private int semester = 1;

		/**
		 * Creates an adapter for a ViewPager to display tabs containing ModuleFragments.
		 * The modules displayed will be the result of a query to the database and will
		 * depend on the semester and year that have been set using  {@link setCoursePeriod(int, int)}.
		 */
		public ModulePagerAdapter(FragmentManager fm, int semester, int year) {
			super(fm);
			this.setCoursePeriod(semester, year);

			tabsModuleList = runDBQuery();
		}

		/**
		 * Sets the current semester and year for this Adapter.
		 * These values will be used when the adapter queries the database for a list of modules
		 * @param semester, int representing the semester for this adapter to list
		 * @param year, int value representing the year for this adapter to list
		 */
		public void setCoursePeriod(int semester, int year) {
			this.semester = semester;
			this.year = year;
		}


		public void reQueryData() {
			tabsModuleList = runDBQuery();
		}
		/**
		 * Queries the database to get all the modules for the current semester and year.
		 * The semester and year are set using {@link setCoursePeriod(int, int)}<br>
		 * The query itself is done using {@link ModulesContentProvider}
		 */
		private List<Module> runDBQuery() {
			GraderDatabaseHelper temp = new GraderDatabaseHelper(ModulesActivity.this);
			List<Module> s = temp.getAllModulesFromDatabase(semester, year);
			temp.closeDB();
			Log.d("MAd","loaded list ("+s.size()+")");
			return s;
		}

		/**
		 * gets the modulefragment at the given position.
		 * The modulefragment has been given the parameters from the cursor matching this module
		 */
		@Override
		public Fragment getItem(int position) {
			Module m = tabsModuleList.get(position);
			
			// getItem is called to instantiate the fragment for the given page.
			Fragment fragment = new ModuleFragment(m);
		//	Bundle args = new Bundle();
//			Module m = tabsModuleList.get(position);

			//args.put(ModuleFragment.ARG_MODULE, m);


	//		fragment.setArguments(args);
			return fragment;
		}



		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#getItemPosition(java.lang.Object)
		 */
		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
			//return super.getItemPosition(object);
		}

		@Override
		public int getCount() {
			return tabsModuleList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return tabsModuleList.get(position).getCode();//getTitle();
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#notifyDataSetChanged()
		 */
		@Override
		public void notifyDataSetChanged() {

			reQueryData();
			super.notifyDataSetChanged();
		}
	}


	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
		
	}


}
