package com.ollienoonan.android.dev.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Adapter for SpinnerItems for use in android spinners
 * 
 * @author Ollie Noonan
 * @version 1 - 2014/08/30
 * @param <T> The Object type to be stored
 */
public abstract class SpinnerItemAdapter<T>  extends ArrayAdapter<SpinnerItem<T>>{

	    //the context this SpinnerItemAdapter is from
	    protected Context context;

	    //the data for this adapter
	    private List<SpinnerItem<T>> values;

	    /**
	     * Creates a new SpinnerItemAdapter
	     * 
	     * @param context the activity etc that owns this object
	     * @param textViewResourceId the id of the row resource to use
	     * @param values the data for this adapter
	     */
	    public SpinnerItemAdapter(Context context, int textViewResourceId,
	    		List<SpinnerItem<T>> values) {
	        super(context, textViewResourceId, values);
	        this.context = context;
	        this.values = values;
	    }

	    /**
	     * @return the size of the data in the adapter
	     */
	    public int getCount(){
	       return values.size();
	    }


	    /**
	     * @return The SpinnerItem at the given position
	     * @throws IndexOutOfBoundsException if position < 0 || position >= getCount()
	     */
	    public SpinnerItem<T> getItem(int position) throws IndexOutOfBoundsException {
	       return values.get(position);
	    }

	    /**
	     * 
	     */
	    public long getItemId(int position){
	       return position;
	    }
	    

	    /**
	     * 
	     */
	    @Override
	    public abstract View getView(int position, View convertView, ViewGroup parent);
	}