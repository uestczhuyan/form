/**
 * CopyRight   2013 ZhuYan
 * @auther BLUE
 *
 * All right reserved
 *
 * Created on 2015-6-17 ÏÂÎç9:24:34
 * 
 */
package com.zhuyan.from.twotable.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zhuyan.from.twotable.R;
import com.zhuyan.from.twotable.util.DataRunning;
import com.zhuyan.from.twotable.util.DataRunning.OnDataChange;

/**
 * @author zy
 * 
 *         Create on 2015-6-17 ÏÂÎç9:24:34
 */
public class TabFragment extends Fragment implements OnItemClickListener {

	private ListView listView;
	private MyAdapter adapter;

	private DataRunning dataRunning;
	private OnDataChange onDataChange;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		dataRunning = DataRunning.getInstance();
		return inflater.inflate(R.layout.activity_main, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		dataRunning = DataRunning.getInstance();
		super.onActivityCreated(savedInstanceState);
		init();

		onDataChange = new OnDataChange() {

			@Override
			public void onChange() {
				if (adapter != null) {
					adapter.notifyDataSetChanged();
					listView.setSelection(adapter.getCount() - 1);
				}
			}
		};
		dataRunning.addOnDataChange(onDataChange);
	}

	@Override
	public void onResume() {
		dataRunning = DataRunning.getInstance();
		super.onResume();

		adapter.notifyDataSetChanged();
		System.out.println("fragment init:" + dataRunning.getNowPoint()
				+ "   n:" + dataRunning.getBaseNotify());
	}

	@Override
	public void onDestroy() {
		dataRunning.removeOnDataChange(onDataChange);
		super.onDestroy();
	}

	private void init() {
		listView = (ListView) getView().findViewById(R.id.list);

		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataRunning.getResults().size();
		}

		@Override
		public Object getItem(int position) {
			return dataRunning.getResults().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = (View) getActivity().getLayoutInflater().inflate(
						R.layout.item, null);
				viewHolder = new ViewHolder(convertView);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.setValue(position);
			return convertView;
		}

		private class ViewHolder {
			private TextView numTv;
			private TextView tv;

			public ViewHolder(View view) {
				numTv = (TextView) view.findViewById(R.id.list_item_num);
				tv = (TextView) view.findViewById(R.id.list_item_text);
			}

			public void setValue(int pos) {
				tv.setText(dataRunning.getResults().get(pos) + "      "
						+ (dataRunning.getSumList().get(pos) > 0 ? "+" : "")
						+ dataRunning.getSumList().get(pos));
				numTv.setText((pos + 1) + "");
			}
		}
	}
}
