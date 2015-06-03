package com.zhuyan.formmap0603;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.zhuyan.formmap0603.util.MapInitUtil;
import com.zhuyan.formmap0603.util.SettingShares;

public class MainActivity extends SherlockActivity implements OnClickListener,
		OnItemClickListener {

	private final static int MAX_PY = 39;

	private ListView listView;
	private TextView notifyTextLeft;
	private TextView notifyTextRight;
	private ImageView addOne;
	private ImageView addTwo;
	private Button delBtn;
	private Button recoveryBtn;

	private List<Integer> results = new ArrayList<Integer>();
	private MyAdapter adapter;
	private SharedPreferences sharedPreferences;
	private File contentFile;

	private int baseNotify = 0;
	private int px = 0;
	private int py = 0;
	private double sum = 0.0;

	private static Map<Integer, List<Double>> map = MapInitUtil.initMap();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sharedPreferences = getSharedPreferences(SettingShares.NAME, 0);
		// 暂时不需要邮箱验证
		// checkMail();

		setContentView(R.layout.activity_main);

		getSupportActionBar().setDisplayOptions(
				getSupportActionBar().DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle("表格");

		File file = new File(SettingShares.ROOT);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}

		init();
	}

	private void checkMail() {
		String timeStr = SettingShares.getTime(sharedPreferences);
		if (timeStr == null) {
			SettingShares.storeTime(SettingShares.FORMAT.format(new Date()),
					sharedPreferences);
			return;
		}
		Date date = SettingShares.getCalender(timeStr);
		Date nowDate = new Date();
		long t = 2 * 24 * 60 * 60 * 1000l;
		System.out.println(nowDate.getTime() - date.getTime());
		if (nowDate.getTime() - date.getTime() >= t) {
			LoginActivity.redirectToLogin(this);
			this.finish();
		}
	}

	/**
	 * 
	 */
	private void init() {
		listView = (ListView) findViewById(R.id.list);
		addOne = (ImageView) findViewById(R.id.add_one);
		addTwo = (ImageView) findViewById(R.id.add_two);
		notifyTextLeft = (TextView) findViewById(R.id.notify_left);
		notifyTextRight = (TextView) findViewById(R.id.notify_right);
		delBtn = (Button) findViewById(R.id.del_btn);
		recoveryBtn = (Button) findViewById(R.id.recover_btn);

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
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, R.id.menu_settings, 1, "设置");
		menu.findItem(R.id.menu_settings).setShowAsAction(
				MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.findItem(R.id.menu_settings).setIcon(
				getResources().getDrawable(android.R.drawable.ic_menu_more));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_settings) {
			SettingActivity.redirectToActivity(MainActivity.this);
			return true;
		} else if (item.getItemId() == R.id.menu_check_file) {
			ShowFileActivity.redirectToActivity(MainActivity.this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {

		baseNotify = SettingShares.getPatch(sharedPreferences);

		contentFile = new File(SettingShares.ROOT + "/"
				+ SettingShares.getFileName(sharedPreferences));

		if (!contentFile.exists() || !contentFile.isFile()) {
			try {
				contentFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		BufferedReader dr = null;
		results.clear();
		try {
			dr = new BufferedReader(new InputStreamReader(new FileInputStream(
					contentFile)));
			String key = null;
			String value = null;
			while (true) {
				key = dr.readLine();
				if (key == null) {
					break;
				}
				value = key;
			}

			// 重新开始计算 sum 清空 px py 归0
			sum = 0.0;
			px = 0;
			py = 0;

			for (int i = 0; i < value.length(); i++) {
				if (value.charAt(i) == '1') {
					doWrong();
				} else if (value.charAt(i) == '2') {
					doRight();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dr != null) {
				try {
					dr.close();
					dr = null;
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		// System.out.println(contentFile.getName()+"   "+arrays.size());
		adapter.notifyDataSetChanged();
		super.onResume();
	}

	@Override
	protected void onStop() {
		for (int k = 0; k <= 3; k++) {
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(contentFile));
				StringBuilder sb = new StringBuilder();
				for (Integer b : results) {
					sb.append(b);
				}
				if (sb != null && sb.length() > 0) {
					writer.write(sb.toString(), 0, sb.toString().length());
					writer.newLine();
					writer.flush();
				}
				k = 4;
			} catch (Exception e) {
				System.out.println("store file:e" + e);
				e.printStackTrace();
			} finally {
				try {
					if (writer != null) {
						writer.flush();
						writer.close();
						writer = null;
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		super.onStop();
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_one:
			doWrong();
			break;
		case R.id.add_two:
			doRight();
			break;
		case R.id.del_btn:
			if (results.size() > 0) {
				results.remove(results.size() - 1);
			}
			break;
		case R.id.recover_btn:
			results.clear();
			break;
		default:
			break;
		}
		adapter.notifyDataSetChanged();
		listView.setSelection(adapter.getCount() - 1);
	}

	/**
	 * 如果左边有值 向左边移动，否则向下移动</br> 特殊情况，px = 1时候，直接向下移动
	 */
	private boolean doWrong() {
		if (px == 1) {
			if (py >= MAX_PY) {
				Toast.makeText(MainActivity.this, "无法往下移动", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
			py++;
			px = 0;
		} else if (px > 0) {
			px--;
		} else {
			if (py >= MAX_PY) {
				Toast.makeText(MainActivity.this, "无法往下移动", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
			px = 0;
			py++;
		}

		results.add(1);
		sum = sum - baseNotify * MapInitUtil.getValueInPox(py, px, map);
		return true;
	}

	/**
	 * 右边移动一格，如果已经是边缘 程序还原。
	 * 
	 * @return
	 */
	private boolean doRight() {
		List<Double> list = map.get(py);
		if (list == null || list.size() <= 0) {
			Toast.makeText(MainActivity.this, "数据出错：py=" + py,
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (list.size() - 1 > px) {
			px++;
		} else {
			px = 0;
			py = 0;
		}
		results.add(2);
		sum = sum + baseNotify * MapInitUtil.getValueInPox(py, px, map);
		return true;
	}

	private class MyAdapter extends BaseAdapter {
		// private List<Integer[]> sliptedList = new ArrayList<Integer[]>();
		// private ArrayList<String> arrays = new ArrayList<String>();

		// public MyAdapter() {
		// initAdapterData();
		// }

		private void initAdapterData() {
			notifyTextLeft.setText("现在值是:" + notify);
			notifyTextRight.setText("\n 最终结果:" + sum);
		}

		// @Override
		// public void notifyDataSetChanged() {
		// initAdapterData();
		// super.notifyDataSetChanged();
		// }

		@Override
		public int getCount() {
			return arrays.size();
		}

		@Override
		public Object getItem(int position) {
			return arrays.get(position);
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
				convertView = (View) MainActivity.this.getLayoutInflater()
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
				tv.setText(arrays.get(pos));
				numTv.setText((pos + 1) + "");
			}
		}
	}

}
