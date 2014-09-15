package com.ollienoonan.gradetracker.adapters;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

/**
 * should only have one parent that is the title
 * 
 * @author Ollie Noonan 
 *
 */
public abstract class AssessmentsExpandableListAdapter<T> extends BaseExpandableListAdapter {

//	private String mHeader; // header titles
	
	// child data in format of header title, child title
	private List<T> mListGroupData;
	private List<List<T>> mListChildData;

	public AssessmentsExpandableListAdapter(//String header,
			List<T> listGroupData, List<List<T>> listChildData) {
		//this.mHeader = header;
		this.mListGroupData = listGroupData;
		this.mListChildData = listChildData;
	}

	@Override
	public T getChild(int groupPosition, int childPosititon) {
		return this.mListChildData.get(groupPosition).get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public abstract View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent);
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return this.mListChildData.get(groupPosition).size();
	}

	@Override
	public T getGroup(int groupPosition) {
		//return this.mHeader;
		return this.mListGroupData.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		if(mListGroupData == null)
			return 0;
		
		return this.mListGroupData.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public abstract View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent);


	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	public List<List<T>> getAdapterChildData() {
		return mListChildData;
	}
	
	public List<T> getAdapterGroupData() {
		return mListGroupData;
	}
	

}