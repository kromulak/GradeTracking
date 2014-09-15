package com.ollienoonan.gradetracker.dialogs;

import com.ollienoonan.android.dev.adapters.SpinnerItem;
import com.ollienoonan.android.dev.adapters.SpinnerItemAdapter;
import com.ollienoonan.gradetracker.ModuleEditorActivity;
import com.ollienoonan.gradetracker.R;
import com.ollienoonan.gradetracker.sqlite.model.Assessment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public abstract class EditAssessmentDialog extends DialogFragment {
	    private Assessment mAssessment;
	    private SpinnerItemAdapter<Integer> spinAdapter;

	    /**
	     * Create a new instance of MyDialogFragment, providing "num"
	     * as an argument.
	     *
	    public static EditAssessmentDialog newInstance(Assessment assessment) {
	    	EditAssessmentDialog f = new EditAssessmentDialog();

	    	f.setAssessment(assessment);
	    	
	        return f;
	    }*/

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	    }
	    
	    EditText editTextTitle, editTextWeight;
	    Spinner spin;
	    
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

	        View v = getActivity().getLayoutInflater().inflate(R.layout.assessment_setupview, null);
	        editTextTitle = (EditText) v.findViewById(R.id.editText_assessment_title);
	        String t = mAssessment.getTitle();
	        editTextTitle.setText(t);//mAssessment.getTitle());

	        editTextWeight = (EditText) v.findViewById(R.id.editText_assessment_weight);
	        editTextWeight.setText(mAssessment.getWeight()+"");
	    	
	        spin = (Spinner) v.findViewById(R.id.spinner_assessment_parent);
	        spin.setAdapter(spinAdapter);

	        int w = -1;
	        
	        for(int i = 0; i < spin.getCount(); i++) {
	        	if( ((SpinnerItem<Integer>) spin.getItemAtPosition(i)).getData() == mAssessment.getParentID() ) {
	        		w = i;
	        	}
	        }
	        
	        spin.setSelection(w);
	        
	    	String title; 
	    	
	    	if(mAssessment.getId() > 0)
	    		title = "Edit";
	    	else
	    		title = "Create";

	        builder.setView(v);
	        
	        builder.setTitle(title)
	                .setPositiveButton(R.string.dialog_confirm_txt,
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int whichButton) {
	                        	/*mAssessment.setTitle(editTextTitle.getText().toString());
	                        	
	                        	ModuleSetailsView_setupActivity.validateNumericEditTextWithinRange();
	                        	
	                        	mAssessment.setWeight(editTextWeight.getText().toString());
	                            onPositiveClick();*/
	                        }
	                    }
	                )
	                .setNegativeButton(R.string.dialog_cancel_txt,
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int whichButton) {
	                            onNegativeClick();
	                        }
	                    }
	                );
	        
	        
	        
	        AlertDialog d = builder.create();
	        d.show();
	        
	        Button theButton = d.getButton(DialogInterface.BUTTON_POSITIVE);
	        theButton.setOnClickListener(new OnClickListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onClick(View v) {
									
		        	float val = ModuleEditorActivity.validateNumericEditTextWithinRange(editTextWeight, 0, 100, "max 100", "Must be between 0 and 100", false);
		        	
		        	if(val > -1) {
		        		mAssessment.setTitle(editTextTitle.getText().toString());
		        		mAssessment.setWeight(val);
		        		mAssessment.setParentID(((SpinnerItem<Integer>)spin.getSelectedItem()).getData());
		        		
		        		onPositiveClick(mAssessment);
		        		dismiss();
		        	}
				}
	        	
	        });
	        
	        return d;
	    }
	    
	    public abstract void onPositiveClick(Assessment assessment);
	    
	    public abstract void onNegativeClick();

		public void setSpinner(SpinnerItemAdapter<Integer> asspin) {
			spinAdapter = asspin;
		}
		

	    public void setAssessment(Assessment assessment) {
	    	this.mAssessment = assessment;
	    }
	}