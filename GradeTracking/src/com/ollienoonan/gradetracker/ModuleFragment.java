package com.ollienoonan.gradetracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ollienoonan.android.dev.AppUtils;
import com.ollienoonan.android.dev.adapters.SpinnerItem;
import com.ollienoonan.android.dev.adapters.SpinnerItemAdapter;
import com.ollienoonan.gradetracker.adapters.AssessmentsExpandableListAdapter;
import com.ollienoonan.gradetracker.dialogs.EditAssessmentDialog;
import com.ollienoonan.gradetracker.dialogs.EditExamDialog;
import com.ollienoonan.gradetracker.sqlite.helper.GraderDatabaseHelper;
import com.ollienoonan.gradetracker.sqlite.helper.ModulesTable;
import com.ollienoonan.gradetracker.sqlite.model.Assessment;
import com.ollienoonan.gradetracker.sqlite.model.Exam;
import com.ollienoonan.gradetracker.sqlite.model.Module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link ModuleFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link ModuleFragment#newInstance} factory method
 * to create an instance of this fragment.
 *
 */
@SuppressLint("InflateParams")
public class ModuleFragment extends Fragment {// implements LoaderManager.LoaderCallbacks<Cursor> {

	//the views used in this fragment
    TextView codeTV, titleTV, overallGradeTV, examMaxTV, caMaxTV, caCurrentTV, weightErrorView;
    EditText examCurrentTV;
    ExpandableListView examListView, caListView;

	private OnFragmentInteractionListener mListener;

	//for passing module to this fragment
	public static final String ARG_MODULE = "module";

	//the module that this fragment is displaying
	private Module module;
	
	//the final exam for the module
	private Exam exam;

	
	public ModuleFragment(Module m) {
		module = m;
	}
	
	public ModuleFragment() {
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//all module data should have been passed in as argument bundle from activity to save requerying the datasource
		Log.d("FR","Creating fragment");
		
		if (savedInstanceState != null) {
	        // Restore last state for checked position.
	        long id = savedInstanceState.getLong(ModulesTable.COLUMN_ID, 0);
			module = new GraderDatabaseHelper(getActivity()).getModuleFromDatabase(id);
	    } else {
			if (getArguments() != null) {
				module = (Module) getArguments().get(ARG_MODULE);
			}
	    }

		//get the final exam (if exists)
		loadExamFromDataSource();

		setHasOptionsMenu(true);
	}


	/**
	 * Loads the exam from the datasource whose moduleCode matches the current module
	 */
	private void loadExamFromDataSource() {
		exam = new GraderDatabaseHelper(getActivity()).getExamFromDatabase(module);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	     // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.module_detailsview, container, false);
        codeTV = (TextView) rootView.findViewById(R.id.module_detailsview_codeTextView);
        titleTV = (TextView) rootView.findViewById(R.id.module_detailsview_titleTextView);
        overallGradeTV = (TextView) rootView.findViewById(R.id.module_detailsview_gradeTextView);
        
        examListView = (ExpandableListView) rootView.findViewById(R.id.module_detailsview_examExpandableListView);
        caListView = (ExpandableListView) rootView.findViewById(R.id.module_detailsview_caExpandableListView);


        //set any known values to the appropriate views
        codeTV.setText(module.getCode());
        titleTV.setText(module.getTitle());
        
        weightErrorView = (TextView) rootView.findViewById(R.id.module_detailsview_weighterrorTextView);

//        updateTotal("0.0");

    	refreshGrades();

        return rootView;
	}
	
	
	private void refreshGrades() {
		weights = reCalculateGrades();

		if(weights != 100) {
			showWeightError(true);
		} else
			showWeightError(false);
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.modulesfragment_actionbar, menu);
		
		if(exam != null) {
			menu.removeItem(R.id.action_add_exam);
		}
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		switch(id) {
			case R.id.action_add_exam:
				addFinalExam();
				return true;
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
		createNewAssessment(Assessment.CORE_PARENT_ID);		
	}
	
	private void createNewAssessment(int parentID) {
		Assessment a = new Assessment();
		a.setModuleID(module.getId());
		a.setParentID(parentID);
		a.setNotes("");
		a.setTitle("");
		
		editAssessment(a);
	}
	
	

	protected void addNewAssessment(Assessment assessment) {
		
		GraderDatabaseHelper dataSource = new GraderDatabaseHelper(getActivity());
		assessment.setId((int) dataSource.insertRecord(assessment));
		dataSource.closeDB();
		
		Toast.makeText(getActivity(), "Added new assessment: " + assessment.getTitle() + "["+assessment.getId()+"]", Toast.LENGTH_SHORT).show();
		Log.d("mF", "Added new assessment: " + assessment.getTitle() + "["+assessment.getId()+"]");
		Log.d("mF", "             module : " + assessment.getModuleID() + "["+module.getCode()+"]");
		Log.d("mF", "        points, max : " + assessment.getPointsAchieved() + ", " + assessment.getMaxPoints());

		refreshGrades();
	}

	protected void updateExistingAssessment(Assessment assessment) {
		GraderDatabaseHelper dataSource = new GraderDatabaseHelper(getActivity());
		dataSource.updateRecord(assessment);
		dataSource.closeDB();
		
		Toast.makeText(getActivity(), "Handled " + assessment.getTitle() + "["+assessment.getId()+"]", Toast.LENGTH_SHORT).show();
		Log.d("mF", "Handled " + assessment.getTitle() + "["+assessment.getId()+"]");
		
		//reCalculateGrades();
		refreshGrades();
	}
	
	protected void updateFinalExam() {
		GraderDatabaseHelper dataSource = new GraderDatabaseHelper(getActivity());
		dataSource.updateRecord(exam);
		dataSource.closeDB();
		
		refreshGrades();
	}

	private void showSettings() {
		// TODO Auto-generated method stub
		
	}
	
	private void addFinalExam() {
		//TODO:DEBUGGIN
		Toast.makeText(getActivity(), "Adding Exam", Toast.LENGTH_SHORT).show();
		
		if(exam == null) {
			exam = new Exam();
			exam.setWeight(module.getFinalsGrade());
			exam.setDuration(120);
			exam.setNotes("");
			exam.setResult(0);
			exam.setTotalMarksOnPaper(75);
			exam.setTotalQuestionsOnPaper(3);
			
			exam.setModuleCode(module.getCode());
		
			this.setupExamList(exam);
			
			GraderDatabaseHelper dataSource = new GraderDatabaseHelper(getActivity());
			exam.setId((int) dataSource.insertRecord(exam));
			dataSource.closeDB();
		}
	}
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		
		super.onSaveInstanceState(outState);
		outState.putLong(ModulesTable.COLUMN_ID, module.getId());
	}


	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}




	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);

	}


	private float setupExamList(final Exam exam) {
		if(exam == null)
			return 0;
		
		List<Exam> e = new ArrayList<Exam>();
		e.add(exam);
		
		List<List<Exam>> lE = new ArrayList<List<Exam>>();
		
		lE.add(e);
		
		AssessmentsExpandableListAdapter<Exam> adapter = buildExpandableListAdapterForExam(e, lE);
		
		this.examListView.setAdapter(adapter);
		
		this.examListView.setOnItemLongClickListener(new OnItemLongClickListener() {
		    @Override
		    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		    	if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {//== ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
		    		checkEditOrDelete(exam);
                   ///// editFinalExam();
                    return true;
		        } 

		        return false;
		    }
		});

		return exam.getWeight();
	}
	
	private void checkEditOrDelete(final Assessment a) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(a.getTitle())
	    		.setItems(new String[] {"Edit","Delete","Add Child"}, new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog, int which) {
	    				//The 'which' argument contains the index position
	    				// of the selected item
	    				switch(which) {
	    				case 0: 
	    					Toast.makeText(getActivity(), "Edit", Toast.LENGTH_SHORT).show();
	    					editAssessment(a);
	    					break;
	    				case 1:
	    					Toast.makeText(getActivity(), "Delete", Toast.LENGTH_SHORT).show();
	    					deleteWithConfirmDialog(a);
	    					break;
	    				case 2:
	    					Toast.makeText(getActivity(), "Add Child", Toast.LENGTH_SHORT).show();
	    					createNewAssessment(a.getId());
	    				}
	    			}
	    		});
	    
	    builder.create().show();
	}

	
	private void checkEditOrDelete(final Exam e) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(e.getTitle())
	    		.setItems(new String[] {"Edit","Delete"}, new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog, int which) {
	    				//The 'which' argument contains the index position
	    				// of the selected item
	    				switch(which) {
	    				case 0: 
	    					Toast.makeText(getActivity(), "Edit", Toast.LENGTH_SHORT).show();
	    					editFinalExam();
	    					break;
	    				case 1:
	    					Toast.makeText(getActivity(), "Delete", Toast.LENGTH_SHORT).show();
	    					deleteWithConfirmDialog(exam);
	    					break;
	    				}
	    			}
	    		});
	    
	    builder.create().show();
	}
	
	private AssessmentsExpandableListAdapter<Exam> buildExpandableListAdapterForExam(List<Exam> e, List<List<Exam>> lE) {
		
		AssessmentsExpandableListAdapter<Exam> adapter = new AssessmentsExpandableListAdapter<Exam>(e, lE ) {

			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				
//				Exam theExam = (Exam) this.getChild(groupPosition, childPosition);

				if (convertView == null) {
					LayoutInflater infalInflater = (LayoutInflater) getActivity()
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = infalInflater.inflate(R.layout.moduleview_examchild_row, null);
				}

				setupExamTextView(convertView, R.id.gradeNeeded_first, 70f);
				setupExamTextView(convertView, R.id.gradeNeeded_secondI, 60f);
				setupExamTextView(convertView, R.id.gradeNeeded_second2, 50f);
				setupExamTextView(convertView, R.id.gradeNeeded_third, 45f);
				setupExamTextView(convertView, R.id.gradeNeeded_pass, 40f);


				return convertView;
			}
			
			private void setupExamTextView(View convertView, int id, float point) {
				TextView txt = (TextView) convertView
						.findViewById(id);

				//float r = (point-(module.getCurrentGrade() - Math.max(0, exam.getResult()) )/Math.abs(exam.getWeight()))*100;
				float r = 		 ((point- (totalCA    ) )/Math.abs(exam.getWeight()))*100;

				txt.setText(AppUtils.formatFloatToStr(r,2));
				if(r > 100)
					txt.setTextColor(Color.RED);
				else if(r < 0)
					txt.setTextColor(Color.GREEN);
				else
					txt.setTextColor(Color.BLACK);
			}

			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {

				if (convertView == null) {
					LayoutInflater infalInflater = (LayoutInflater) getActivity()
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = infalInflater.inflate(R.layout.moduleview_parent_row, null);
				}
				
				TextView tv = (TextView) convertView.findViewById(R.id.module_detailsview_assignmentTitleTextView);
				tv.setText("Final Exam");

				TextView tv2 = (TextView) convertView.findViewById(R.id.module_detailsview_maxGradeTextView);
				tv2.setText(AppUtils.formatFloatToStr(exam.getWeight(),2)+"%");
												
				Button btn1 = (Button) convertView.findViewById(R.id.module_detailsview_currentGradeButton);
				btn1.setText(AppUtils.formatFloatToStr(exam.getResult(),2)+"%");
			
				btn1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						quickEdit(exam);						
					}
					
				});	

				return convertView;
			}
		};
		
		return adapter;
	}
	
	
//	Assessment tempForExam = null;

	List<Assessment> assessments = null;
	List<List<Assessment>> subLists = null;	
	
	HashMap<Assessment, List<Assessment>> testData;

	private float loadCAData() {
		
		assessments = this.loadParentAssessmentsFromDatabase();
		subLists = new ArrayList<List<Assessment>>();
		
		float ca = 0;
		
		if(assessments == null)
			return ca;
		
		for(Assessment a: assessments) {
			ca += a.getWeight();
			subLists.add(this.loadSubAssessmentsFromDatabase(a));
		}
		
		return ca;
	}
	
	
	static int h = 0;
	private void showWeightError(boolean vis) {
		if(vis)
			this.weightErrorView.setVisibility(View.VISIBLE);
		else
			this.weightErrorView.setVisibility(View.GONE);
		
		h++;
		this.weightErrorView.setText("WARNING! total weight = " + AppUtils.formatFloatToStr(weights, 2));
	}


	private float weights = -1;
	
	private float setupCAList() {
				
		Log.d("setupCA", "setting up ca list");

		float f = loadCAData();
		
		Log.d("setupCA", "core: " + assessments.size() + ", subs: " + subLists.size());


		this.caListView.setOnItemLongClickListener(new OnItemLongClickListener() {
		    @Override
		    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		    	if (ExpandableListView.getPackedPositionType(id) != ExpandableListView.PACKED_POSITION_TYPE_NULL) {//== ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    final ExpandableListAdapter adapter = ((ExpandableListView) parent).getExpandableListAdapter();
                    long packedPos = ((ExpandableListView) parent).getExpandableListPosition(position);
                    int groupPosition = ExpandableListView.getPackedPositionGroup(packedPos);
                    int childPosition = ExpandableListView.getPackedPositionChild(packedPos);
                    
                    Assessment a;
                    
                    if(childPosition == -1) //its a group
                    	a = (Assessment) adapter.getGroup(groupPosition);
                    else
                    	a = (Assessment) adapter.getChild(groupPosition, childPosition);
                    
                    checkEditOrDelete(a);

///////                    editAssessment(a);
                    return true;
		        } 

		        return false;
		    }
		});

		return f;
	}
	
	
	private void editAssessment(final Assessment a) {

		SpinnerItemAdapter<Integer> asspin = buildSpinnerForEditDialog();
		
		EditAssessmentDialog ed = new EditAssessmentDialog() {

			@Override
			public void onPositiveClick(Assessment assessment) {
				if(assessment.getId() == Assessment.NEW) {
					addNewAssessment(assessment);
				} else {
					updateExistingAssessment(assessment);
				}
			}

			@Override
			public void onNegativeClick() {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		ed.setAssessment(a);
		ed.setSpinner(asspin);
		
		ed.show(getFragmentManager(), getTag());
	}

	
	private void editFinalExam() {

		EditExamDialog ed = new EditExamDialog() {

			@Override
			public void onPositiveClick(Exam e) {
				if(e.getId() == Exam.NEW) {
					addFinalExam();
				} else {
					updateFinalExam();
				}
			}

			@Override
			public void onNegativeClick() {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		ed.setExam(exam);
		
		ed.show(getFragmentManager(), getTag());
	}

	
	/**
	 * Shows confirmation dialog before deleting
	 */
	private void deleteWithConfirmDialog(final Assessment a) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.dialog_delete_msg)
		   .setCancelable(false)
		   .setPositiveButton(R.string.dialog_confirm_txt, new DialogInterface.OnClickListener() {
		       public void onClick(DialogInterface dialog, int id) {
		            deleteAssessment(a);
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


	private void deleteAssessment(Assessment a) {
		GraderDatabaseHelper datasource = new GraderDatabaseHelper(getActivity());

		datasource.deleteRecord(a);
		
		refreshGrades();
	}
	
	
	/**
	 * Shows confirmation dialog before deleting
	 */
	private void deleteWithConfirmDialog(final Exam e) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.dialog_delete_msg)
		   .setCancelable(false)
		   .setPositiveButton(R.string.dialog_confirm_txt, new DialogInterface.OnClickListener() {
		       public void onClick(DialogInterface dialog, int id) {
		            deleteFinalExam(e);
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


	private void deleteFinalExam(Exam e) {
		GraderDatabaseHelper datasource = new GraderDatabaseHelper(getActivity());

		datasource.deleteRecord(e);
		
		exam = null;
		examListView.setVisibility(View.GONE);

		examListView.setAdapter(buildExpandableListAdapterForExam(null,null));
		
		refreshGrades();
	}	
	
	/**
	 * @return
	 */
	private SpinnerItemAdapter<Integer> buildSpinnerForEditDialog() {
		List<SpinnerItem<Integer>> spinList = new ArrayList<SpinnerItem<Integer>>();
		spinList.add(new SpinnerItem<Integer>("None", "None", Assessment.CORE_PARENT_ID));
		
		for(Assessment l: assessments) {
			spinList.add(new SpinnerItem<Integer>(l.getTitle(), l.getTitle() + ": " + l.getWeight(), l.getId()));
		}
		
		SpinnerItemAdapter<Integer> asspin = new SpinnerItemAdapter<Integer>(getActivity(), 0, spinList) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView label = new TextView(context);
		        label.setTextColor(Color.BLACK);
		        // Then you can get the current item using the values array (Users array) and the current position
		        // You can NOW reference each method you has created in your bean object (User class)
		        label.setText(getItem(position).getSmallTitle());

		        // And finally return your dynamic (or custom) view for each spinner item
		        return label;
			}

			@Override
			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				TextView label = new TextView(context);
		        label.setTextColor(Color.BLACK);
		        // Then you can get the current item using the values array (Users array) and the current position
		        // You can NOW reference each method you has created in your bean object (User class)
		        SpinnerItem<Integer> item = getItem(position);
		        
		        label.setText(item.getBigTitle());

		        // And finally return your dynamic (or custom) view for each spinner item
		        return label;
			}
		};
		return asspin;
	}

	
	private List<Assessment> loadSubAssessmentsFromDatabase(Assessment assessment) {
		GraderDatabaseHelper dataSource = new GraderDatabaseHelper(getActivity());
		
		List<Assessment> data = dataSource.getSubAssessmentsForModule(module, assessment); 
		
		dataSource.closeDB();
		
		return data;
	}
	
	private List<Assessment> loadParentAssessmentsFromDatabase() {
		return this.loadSubAssessmentsFromDatabase(null);
	}

	private AssessmentsExpandableListAdapter<Assessment> buildExpandableListAdapterForAssessment(
			List<Assessment> assessments, List<List<Assessment>> subAssessments) {
		
		AssessmentsExpandableListAdapter<Assessment> adapter = new AssessmentsExpandableListAdapter<Assessment>(assessments, subAssessments) {

			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				
//				Exam theExam = (Exam) this.getChild(groupPosition, childPosition);

				if (convertView == null) {
					LayoutInflater infalInflater = (LayoutInflater) getActivity()
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = infalInflater.inflate(R.layout.moduleview_child_row, null);
				}
				
				Assessment a = this.getChild(groupPosition, childPosition);

				TextView txtTitle = (TextView) convertView
						.findViewById(R.id.moduleview_child_titleTextView);
				txtTitle.setText(a.getTitle());

				TextView txtWeight = (TextView) convertView
						.findViewById(R.id.moduleview_child_maxGradeTextView);
				txtWeight.setText(AppUtils.formatFloatToStr(a.getWeight(),2)+"%");
				
				Button btnScore = (Button) convertView
						.findViewById(R.id.moduleview_child_currentGradeButton);
				btnScore.setText(AppUtils.formatFloatToStr(a.calculateResult(),2)+"%");
				
				editOnClick(btnScore, a);

				return convertView;
			}
			

			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {

				if (convertView == null) {
					LayoutInflater infalInflater = (LayoutInflater) getActivity()
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = infalInflater.inflate(R.layout.moduleview_parent_row, null);
				}
				
				Assessment groupA = getGroup(groupPosition);
				
				TextView tv = (TextView) convertView.findViewById(R.id.module_detailsview_assignmentTitleTextView);
				tv.setText(groupA.getTitle());
				
				TextView tv2 = (TextView) convertView.findViewById(R.id.module_detailsview_maxGradeTextView);
				tv2.setText(AppUtils.formatFloatToStr(groupA.getWeight(),2)+"%");
				
				Button btn1 = (Button) convertView.findViewById(R.id.module_detailsview_currentGradeButton);
				btn1.setText(AppUtils.formatFloatToStr(groupA.calculateResult(),2)+"%");
				
				boolean b = (this.getChildrenCount(groupPosition) == 0);
				
				btn1.setClickable(b);
				
				if(b) {
					editOnClick(btn1, groupA);
				} else {
					//parent.setonInterceptTouchEvent(MotionEvent)
				}
					
				return convertView;
			}
		};
		
		return adapter;
	}
	

	private void editOnClick(Button btn, final Assessment a) {
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				quickEdit(a);						
			}
			
		});
	}
	
//	View quickEditView = null;
	
	private void quickEdit(final Assessment a) {

		LayoutInflater infalInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View quickEditView = infalInflater.inflate(R.layout.assessment_editview, null);
		
		final EditText inputScore = (EditText) quickEditView.findViewById(R.id.qEditScore);
		final EditText inputMax = (EditText) quickEditView.findViewById(R.id.qEditMax);
		
		inputScore.setText(a.getPointsAchieved()+"");
		inputMax.setText(a.getMaxPoints()+"");
				
		new AlertDialog.Builder(getActivity())
	    .setTitle(a.getTitle())
	    .setMessage("Enter your Score:")
	    .setView(quickEditView)
	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            float fScore = Float.parseFloat(inputScore.getText().toString()); 
	            float fMax = Float.parseFloat(inputMax.getText().toString());
	            
	            a.setMaxPoints(fMax);
	            a.setPointsAchieved(fScore);
	            updateExistingAssessment(a);
	            
	            refreshGrades();            
	        }
	    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            // Do nothing.
	        }
	    }).show();
	}

	private void quickEdit(final Exam e) {

		LayoutInflater infalInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View quickEditView = infalInflater.inflate(R.layout.assessment_editview, null);
		
		final EditText inputScore = (EditText) quickEditView.findViewById(R.id.qEditScore);
		final EditText inputMax = (EditText) quickEditView.findViewById(R.id.qEditMax);
		
		inputScore.setText(e.getResult()+"");//PointsAchieved()+"");
		inputMax.setText("100");//e.getMaxPoints()+"");
				
		new AlertDialog.Builder(getActivity())
	    .setTitle(e.getTitle())
	    .setMessage("Enter your Score:")
	    .setView(quickEditView)
	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            float fScore = Float.parseFloat(inputScore.getText().toString()); 
	            //float fMax = Float.parseFloat(inputMax.getText().toString());
	            
	            e.setResult(fScore);
	            updateFinalExam();
	            
	            refreshGrades();
	        }
	    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            // Do nothing.
	        }
	    }).show();
	}
	
	
	private float reCalculateGrades() {

//		loadCAData();
		setupCAList();
		float overallRes = 0;
		float overallWeight = 0;
		
		if(assessments != null) {

			for(int i = 0; i < assessments.size(); i++) {
				Assessment upper = assessments.get(i);
				
				if(i < subLists.size()) {
					List<Assessment> subs = subLists.get(i);
					
					float subTotal = 0;
					
					for(Assessment lower: subs) {
						subTotal += lower.calculateResult()/100f*lower.getWeight();
					}
					
					if(subs.size() > 0) {
						//subTotal = subTotal/subs.size();
						upper.setPointsAchieved(subTotal);
						upper.setMaxPoints(100f);
					}
					
					overallRes += upper.calculateResult()*(upper.getWeight()/100);
					
					Log.d("mF", " overall: " + overallRes);
				}
				
				overallWeight += upper.getWeight();
			}

		}		
		
		totalCA = overallRes;
		
		if(exam != null) {
			overallRes += exam.getResult()*(exam.getWeight()/100f);
			overallWeight += exam.getWeight();
		}
		
		overallGradeTV.setText(AppUtils.formatFloatToStr(overallRes,2) + "%");


		AssessmentsExpandableListAdapter<Assessment> adapter = buildExpandableListAdapterForAssessment(assessments, subLists);
		caListView.setAdapter(adapter);

		setupExamList(exam);
		
		module.setCurrentGrade(overallRes);
		
		updateModuleInDataSource();
		
		return overallWeight;
	}
	
	private float totalCA = 0;
	
	private void updateModuleInDataSource() {
		GraderDatabaseHelper dataSource = new GraderDatabaseHelper(getActivity());
		
		dataSource.updateRecord(module);
		
		dataSource.closeDB();
	}

}