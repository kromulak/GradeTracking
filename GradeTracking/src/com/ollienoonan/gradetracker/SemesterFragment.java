package com.ollienoonan.gradetracker;

import java.util.List;

import com.ollienoonan.android.dev.AppUtils;
import com.ollienoonan.gradetracker.adapters.ModulesSummaryAdapter;
import com.ollienoonan.gradetracker.sqlite.helper.GraderDatabaseHelper;
import com.ollienoonan.gradetracker.sqlite.model.Module;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SemesterFragment extends ListFragment {
	public static final String ARG_SEMESTER = "SEMESTER";
	public static final String ARG_YEAR = "YEAR";

	private static final String LOG_ORIGIN = "Semesterfragment";
	
	private GraderDatabaseHelper datasource;
	
	private int mSemester, mYear;
	
	
	private Context mContext;

	private List<Module> modules;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mContext = getActivity().getApplicationContext();
		
		View view = inflater.inflate(R.layout.semesterview, null);		
		
		loadListFromDatabase();
		
		if(modules.size() == 0) {
			Button b = (Button) view.findViewById(R.id.semesterview_createButton);
			b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((MainActivity)getActivity()).launchActivity_newModule();
				}
				
			});
		}
	
		return view;
	}

	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Module m = (Module) l.getAdapter().getItem(position);
		Toast.makeText(mContext, m.getTitle(), Toast.LENGTH_SHORT).show();
		
		Intent i = new Intent(mContext, ModulesActivity.class);
		
		i.putExtra(ModulesActivity.ARG_SEMESTER, this.mSemester);
		i.putExtra(ModulesActivity.ARG_YEAR, this.mYear);
		i.putExtra(ModulesActivity.ARG_POSITION, position);
		
		startActivity(i);
	}

	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        int position = info.position;
	    
	    menu.setHeaderTitle(modules.get(position).getCode());
	    
	    menu.add(0, (int) modules.get(position).getId(), 0, "Edit");
	    menu.add(0, (int) modules.get(position).getId(), 0, "Delete");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        int id = (int) modules.get(info.position).getId();
		
		
	    if (item.getTitle().equals("Edit")) {
	    	Intent i = new Intent(mContext, ModuleEditorActivity.class);
			i.putExtra(ModuleEditorActivity.ARG_MODULE, id);
			i.putExtra(ModuleEditorActivity.ARG_MODE, ModuleEditorActivity.MODE_EDIT);
			
			startActivityForResult(i, REQUEST_EDIT_MODULE);
			
	   } else if (item.getTitle().equals("Delete")) {
		   deleteWithConfirmDialog(info.position);
	   }
	    return super.onContextItemSelected(item);
	}

	/**
	 * Shows confirmation dialog before deleting
	 */
	private void deleteWithConfirmDialog(final int index) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.dialog_delete_msg)
		   .setCancelable(false)
		   .setPositiveButton(R.string.dialog_confirm_txt, new DialogInterface.OnClickListener() {
		       public void onClick(DialogInterface dialog, int id) {
		            deleteData(index);
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
	 * Deletes the module from the database
	 * calls finish on this activity once deleted
	 */
	private void deleteData(int index) {
		//GraderDatabaseHelper datasource = new GraderDatabaseHelper(getActivity());
		
		Module m = modules.remove(index);

		datasource.deleteRecord(m);
		
		
		
		//datasource.closeDB();
		
		loadListFromDatabase();
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);  
		registerForContextMenu(getListView());
	}

	@Override
	public void setArguments(Bundle args) {
		mSemester = args.getInt(ARG_SEMESTER);
		mYear = args.getInt(ARG_YEAR);
	}
	
	@Override
	public void onPause() {
		if(datasource != null)
			datasource.close();
		
		super.onPause();
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		MenuItem m = menu.findItem(R.id.action_set_as_current_semester);

		boolean b = isDefaultSemester();
		
		m.setChecked(b);
		m.setEnabled(!b);
	}

	private boolean isDefaultSemester() {
		SharedPreferences sharedPref = getActivity().getSharedPreferences("GRADER_OPTIONS", Activity.MODE_PRIVATE);
		
		int defaultSem = sharedPref.getInt("preferredSemester", 1);
		int defaultYear = sharedPref.getInt("preferredYear", 1);
		
		return ( (defaultSem == mSemester) &&(defaultYear == mYear) );
	}
	
	/**
	 * Load the items from the database and puts them into the list
	 */
	private void loadListFromDatabase() {

		datasource = new GraderDatabaseHelper(mContext);

		modules = datasource.getAllModulesFromDatabase(mSemester, mYear);

		//use the SimpleCursorAdapter to show the
		// elements in a ListView
		ModulesSummaryAdapter adapter = new ModulesSummaryAdapter(mContext,
		        R.layout.semesterview_row, modules);
		
		if(AppUtils.isDebugging())
			Log.d(LOG_ORIGIN,"list = " + modules.size());
		
		
		setListAdapter(adapter);	
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == REQUEST_EDIT_MODULE) {
	        // Make sure the request was successful
	        if (resultCode == Activity.RESULT_OK) {
	        	Toast.makeText(getActivity(), "SemesterFrag: edit result", Toast.LENGTH_SHORT).show();
	        	((MainActivity)getActivity()).completeStartup(null);
	        }
	    }
	}
	
	private static final int REQUEST_EDIT_MODULE = 2;
}