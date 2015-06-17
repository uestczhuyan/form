/**
 * CopyRight   2013 ZhuYan
 * @auther BLUE
 *
 * All right reserved
 *
 * Created on 2015-6-17 下午9:24:34
 * 
 */
package com.zhuyan.formmap0603.fragment;

import com.zhuyan.formmap0603.MainActivity;
import com.zhuyan.formmap0603.R;
import com.zhuyan.formmap0603.util.DataRunning;
import com.zhuyan.formmap0603.util.MapInitUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author zy
 *
 * Create on 2015-6-17  下午9:24:34
 */
public class TabFragment extends Fragment implements OnClickListener,
OnItemClickListener{
	
	private ListView listView;
	private TextView notifyTextLeft;
	private TextView notifyTextRight;
	private ImageView addOne;
	private ImageView addTwo;
	private Button delBtn;
	private Button recoveryBtn;
	private MyAdapter adapter;
	
	private DataRunning dataRunning;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState){
		dataRunning = DataRunning.getInstance();
		return inflater.inflate(R.layout.activity_main,
		        container, false);
	}

	@Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
		dataRunning = DataRunning.getInstance();
	    super.onActivityCreated(savedInstanceState);
	    init();
    }
	
	@Override
	public void onResume() {
		dataRunning = DataRunning.getInstance();
		super.onResume();
		
		notifyTextRight.setText("\n 最终结果:" + dataRunning.getSum());
		notifyTextLeft.setText("现在值是:" + 
				dataRunning.getBaseNotify()*MapInitUtil.getValueInPox(dataRunning.getNowPoint(), dataRunning.map));
		adapter.notifyDataSetChanged();
		System.out.println("fragment init:"+dataRunning.getNowPoint()+"   n:"+dataRunning.getBaseNotify());
	}

	private void init() {
		listView = (ListView) getView().findViewById(R.id.list);
		addOne = (ImageView)  getView().findViewById(R.id.add_one);
		addTwo = (ImageView)  getView().findViewById(R.id.add_two);
		notifyTextLeft = (TextView)  getView().findViewById(R.id.notify_left);
		notifyTextRight = (TextView)  getView().findViewById(R.id.notify_right);
		delBtn = (Button)  getView().findViewById(R.id.del_btn);
		recoveryBtn = (Button)  getView().findViewById(R.id.recover_btn);

		addOne.setClickable(true);
		addTwo.setClickable(true);

		addOne.setOnClickListener(this);
		addTwo.setOnClickListener(this);
		delBtn.setOnClickListener(this);
		recoveryBtn.setOnClickListener(this);

		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		
	}
	
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_one:
			dataRunning.doWrong(true, getActivity());
			break;
		case R.id.add_two:
			dataRunning.doRight(true, getActivity());
			break;
		case R.id.del_btn:
			dataRunning.moveBack();
			break;
		case R.id.recover_btn:
			dataRunning.clearAll();
			break;
		default:
			break;
		}
		notifyTextRight.setText("\n 最终结果:" + dataRunning.getSum());
		notifyTextLeft.setText("现在值是:" + 
				dataRunning.getBaseNotify()*MapInitUtil.getValueInPox(dataRunning.getNowPoint(), dataRunning.map));
		adapter.notifyDataSetChanged();
		listView.setSelection(adapter.getCount() - 1);
		
		System.out.println(dataRunning.getNowPoint());
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
				convertView = (View) getActivity().getLayoutInflater()
						.inflate(R.layout.item, null);
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
				tv.setText(dataRunning.getResults().get(pos)+"      "+
							(dataRunning.getSumList().get(pos)>0?"+":"")+dataRunning.getSumList().get(pos));
				numTv.setText((pos + 1) + "");
			}
		}
	}
}
