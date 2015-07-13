/**
 * CopyRight   2013 ZhuYan
 * @auther BLUE
 *
 * All right reserved
 *
 * Created on 2015-6-17 ÏÂÎç10:19:23
 * 
 */
package com.zhuyan.formmap0713.fragment;

import java.util.List;

import com.zhuyan.formmap0713.R;
import com.zhuyan.formmap0713.util.DataRunning;
import com.zhuyan.formmap0713.util.DataRunning.OnDataChange;
import com.zhuyan.formmap0713.util.Point;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author zy
 * 
 *         Create on 2015-6-17 ÏÂÎç10:19:23
 */
public class MapFragment extends Fragment {

	private DataRunning dataRunning;

	private ListView listView;
	private MyAdapter adapter;
	private OnDataChange onDataChange;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_map, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		dataRunning = DataRunning.getInstance();
		listView = (ListView) getView().findViewById(R.id.map_list);
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		onDataChange = new OnDataChange() {

			@Override
			public void onChange() {
				if (adapter != null) {
					adapter.notifyDataSetChanged();
//					listView.setSelection(dataRunning.getNowPoint().getY());
				}
			}
		};
		dataRunning.addOnDataChange(onDataChange);
	}

	@Override
	public void onDestroy() {
		dataRunning.removeOnDataChange(onDataChange);
		super.onDestroy();
	}

	@Override
	public void onResume() {
		dataRunning = DataRunning.getInstance();
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
//			return dataRunning.map.size();
			return 0;
		}

		@Override
		public Object getItem(int position) {
//			return dataRunning.map.get(position);
			return null;
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
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.map_item, null);
				viewHolder = new ViewHolder(convertView);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.setValue(position);
			return convertView;
		}

		private class ViewHolder {
			private final TextView[] texts = new TextView[7];

			public ViewHolder(View view) {
				texts[0] = (TextView) view.findViewById(R.id.map_item_0);
				texts[1] = (TextView) view.findViewById(R.id.map_item_1);
				texts[2] = (TextView) view.findViewById(R.id.map_item_2);
				texts[3] = (TextView) view.findViewById(R.id.map_item_3);
				texts[4] = (TextView) view.findViewById(R.id.map_item_4);
				texts[5] = (TextView) view.findViewById(R.id.map_item_5);
				texts[6] = (TextView) view.findViewById(R.id.map_item_6);
			}

			public void setValue(int pos) {
//				List<Double> list = dataRunning.map.get(pos);
//				for (int i = 0; i < texts.length; i++) {
//					if(i==0){
//						texts[i].setText("ÐòºÅ"+(pos+1)+" :  "+list.get(i).toString());
//					}else if (i < list.size()) {
//						texts[i].setVisibility(View.VISIBLE);
//						texts[i].setText(list.get(i).toString());
//					} else {
//						texts[i].setVisibility(View.INVISIBLE);
//					}
//					texts[i].setTextColor(Color.BLACK);
//				}
				Point point;
//				= dataRunning.getNowPoint();
				Point lastPoint;
//				= dataRunning.getLastPoint();
//				if (lastPoint != null && lastPoint.getY() == pos) {
//					texts[lastPoint.getX()].setTextColor(Color.BLUE);
//				}
//				if (point.getY() == pos) {
//					texts[point.getX()].setTextColor(Color.RED);
//				}
			}
		}
	}

}
