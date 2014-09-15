package com.ollienoonan.gradetracker.dialogs;

import com.ollienoonan.gradetracker.ModuleEditorActivity;
import com.ollienoonan.gradetracker.R;
import com.ollienoonan.gradetracker.sqlite.model.Exam;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public abstract class EditExamDialog extends DialogFragment {
	    private Exam mExam;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	    }
	    
	    EditText editTextTitle, editTextWeight;
	    
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

	        View v = getActivity().getLayoutInflater().inflate(R.layout.exam_setupview, null);
	        editTextTitle = (EditText) v.findViewById(R.id.editText_exam_title);
	        editTextTitle.setText(mExam.getTitle());

	        editTextWeight = (EditText) v.findViewById(R.id.editText_exam_weight);
	        editTextWeight.setText(mExam.getWeight()+"");
	    	
	    	String title; 
	    	
	    	if(mExam.getId() > 0)
	    		title = "Edit";
	    	else
	    		title = "Create";

	        builder.setView(v);
	        
	        builder.setTitle(title)
	                .setPositiveButton(R.string.dialog_confirm_txt,
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int whichButton) {
	                
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

				@Override
				public void onClick(View v) {
									
		        	float val = ModuleEditorActivity.validateNumericEditTextWithinRange(editTextWeight, 0, 100, "max 100", "Must be between 0 and 100", false);
		        	
		        	if(val > -1) {
		        		//mExam.setTitle(editTextTitle.getText().toString());
		        		mExam.setWeight(val);
		        		//mExam.setParentID(((SpinnerItem<Integer>)spin.getSelectedItem()).getData());
		        		
		        		onPositiveClick(mExam);
		        		dismiss();
		        	}
				}
	        	
	        });
	        
	        return d;
	    }
	    
	    public abstract void onPositiveClick(Exam exam);
	    
	    public abstract void onNegativeClick();


	    public void setExam(Exam exam) {
	    	this.mExam = exam;
	    }
	}