package com.ollienoonan.gradetracker.adapters;

import java.util.HashMap;
import java.util.List;

import com.ollienoonan.gradetracker.DrawerMenuHolder;
import com.ollienoonan.gradetracker.R;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class NavDrawerExpandableListAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private List<String> mListDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<DrawerMenuHolder>> mListDataChild;

	public NavDrawerExpandableListAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<DrawerMenuHolder>> listChildData) {
		this.mContext = context;
		this.mListDataHeader = listDataHeader;
		this.mListDataChild = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		DrawerMenuHolder childText = (DrawerMenuHolder) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.drawer_list_item, null);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.drawer_item);

		txtListChild.setText(childText.toString());
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.mListDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.mListDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.drawer_group_view, null);
		}
		

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.drawer_groupheader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

			    
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}