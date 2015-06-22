/**
 * CopyRight   2013 ZhuYan
 * @auther BLUE
 *
 * All right reserved
 *
 * Created on 2015-6-17 下午9:24:34
 * 
 */
package com.zhuyan.from.twotable.fragment;

import java.util.List;

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
import com.zhuyan.from.twotable.util.MapInitUtil;
import com.zhuyan.from.twotable.util.DataRunning.OnDataChange;
import com.zhuyan.from.twotable.util.Point;

/**
 * @author zy
 * 
 *         Create on 2015-6-17 下午9:24:34
 */
public abstract class TabFragment extends Fragment implements OnItemClickListener {

	private ListView listView;
	private MyAdapter adapter;
	
	private TextView notifyTextLeft;
	private TextView notifyTextRight;
	private TextView titleTextView;

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
					notifyTextRight.setText("\n 最终结果:" + dataRunning.getSum(getMapValue()));
					Double now = dataRunning.getCurrentNum(getMapValue());
					if(now == null){
						notifyTextLeft.setText("现在值是:--");
					}else{
						notifyTextLeft.setText("现在值是:"
								+ dataRunning.getBaseNotify()
								* now);
					}
					System.out.println(getMapValue()+":"+dataRunning.getNowPoint(getMapValue()));
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
		dataRunning.notifyDataChanged();
		System.out.println("fragment init:" + dataRunning.getNowPoint(getMapValue())
				+ "   n:" + dataRunning.getBaseNotify());
	}

	@Override
	public void onDestroy() {
		dataRunning.removeOnDataChange(onDataChange);
		super.onDestroy();
	}

	private void init() {
		listView = (ListView) getView().findViewById(R.id.list);
		notifyTextLeft = (TextView)  getView().findViewById(R.id.notify_left);
		notifyTextRight = (TextView)  getView().findViewById(R.id.notify_right);
		titleTextView = (TextView) getView().findViewById(R.id.fragment_title);
		
		titleTextView.setText("当前是图表:"+getMapValue());

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
				List<Double> sumList = dataRunning.getCurrentSumList(getMapValue());
//				DataRunning db = dataRunning;
//				if(getMapValue() ==2){
//					System.out.println(sumList);
//				}
				if(pos>= sumList.size() || sumList.get(pos).equals(0.0)){
					tv.setText(dataRunning.getResults().get(pos) + "      --");
				}else{
					tv.setText(dataRunning.getResults().get(pos) + "      "
							+ (sumList.get(pos) > 0 ? "+" : "")
							+ sumList.get(pos));
				}
 				
				numTv.setText((pos + 1) + "");
			}
		}
	}
	
	abstract int getMapValue();

}
