package com.ollienoonan.gradetracker.adapters;

import java.util.List;

import com.ollienoonan.android.dev.AppUtils;
import com.ollienoonan.gradetracker.R;
import com.ollienoonan.gradetracker.sqlite.model.Module;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Ollie Noonan
 * @version 1 - 2014/07/02
 *
 * Adapter that maps a list of modules to a listview
 */
public class ModulesSummaryAdapter extends BaseAdapter {

    Context context;
    int resource;
    List<Module> modules;
    
    private static LayoutInflater inflater = null;

    /**
     * Constructor
     * 
     * @param context The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when instantiating views.
     * @param modules The objects to represent in the ListView.
     */
    public ModulesSummaryAdapter(Context context, int resource, List<Module> modules) {

        this.context = context;
        this.resource = resource;
        this.modules = modules;
        
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        Log.d("adapter","starts here");
    }

    
    @Override
    public int getCount() {
    	//TODO:
        Log.d("adapter","gets to getcount " + modules.size());
        return modules.size();
    }

    
    @Override
    public Object getItem(int position) {
    	//TODO:
        Log.d("adapter","gets to getitem");
        return modules.get(position);
    }

    
    @Override
    public long getItemId(int position) {

        return modules.get(position).getId();
    }

    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(resource, null);
        
        final Module currentModule = modules.get(position);
        
        TextView code = (TextView) vi.findViewById(R.id.semesterview_row_textview_moduleCode);
        code.setText(currentModule.getCode());
        
        TextView title = (TextView) vi.findViewById(R.id.semesterview_row_textview_moduleTitle);
        title.setText(currentModule.getTitle());
        
        Button gradeButton = (Button) vi.findViewById(R.id.semesterview_row_button_moduleGrade);
        gradeButton.setText(AppUtils.formatFloatToStr(currentModule.getCurrentGrade(),2) + "%");
        
        
/*        gradeButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(v.getContext(), AppUtils.formatFloatToStr(currentModule.getCurrentGrade(),2)+"%", Toast.LENGTH_SHORT).show();
			}
        	
        });
  */      
        vi.setId((int) currentModule.getId());
        
        //TODO:
        Log.d("adapter","gets here");
        return vi;
    }
}