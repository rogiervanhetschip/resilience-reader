package org.resilience.resiliencereader.framework;

import java.util.AbstractList;

import org.resilience.resiliencereader.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TwoLineAdapter extends BaseAdapter {
	
	private AbstractList<TwoLineListItem> list;
	
	public TwoLineAdapter(AbstractList list)
	{
		// TODO: Check if contents of list are TwoLineListItems
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int index) {
		return list.get(index);
	}

	@Override
	public long getItemId(int index) {
		return list.get(index).getId();
	}

	@Override
	public View getView(int index, View convertView, ViewGroup root) {
		LayoutInflater inflater = (LayoutInflater)root.getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = inflater.inflate(R.layout.two_line_list_item, null);
		TwoLineListItem item = list.get(index);
		
		TextView textView1 = (TextView)view.findViewById(R.id.listItemFirstLine);
		textView1.setText(item.getFirstLine());
		
		TextView textView2 = (TextView)view.findViewById(R.id.listItemSecondLine);
		textView2.setText(item.getSecondLine());
		
		return view;
	}

}
