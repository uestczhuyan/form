package com.zhuyan.form;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SherlockActivity implements OnClickListener,OnItemClickListener{
	
	private final static int SIZE = 20;
	private final static int ITEM_COUNT = 20;
	
	private ListView listView;
	private TextView content;
	private ImageView addOne;
	private ImageView addTwo;
	private Button delBtn;
	
	private LinearLayout selectItem = null;
	private int selectIndex = 0;
	private  Map<Integer, String> arrays = new HashMap<Integer, String>();
	private MyAdapter adapter;
	private Map<String, String> keyMap = new HashMap<String, String>();
	private SharedPreferences sharedPreferences;
	private File contentFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sharedPreferences = getSharedPreferences(SettingShares.NAME, 0);
		
//		testAddData();
		
		initData();
		init();
	}

	/**
	 * 
	 */
	private void testAddData() {
		// TODO Auto-generated method stub
		arrays.put(0, "111/221/222/121");
		arrays.put(1, "111/221/212/121");
	}

	private void initData() {
		// TODO Auto-generated method stub
		keyMap.put("211", "1");
		keyMap.put("122", "2");
		keyMap.put("222", "3");
		keyMap.put("111", "4");
		keyMap.put("212", "5");
		keyMap.put("121", "6");
		keyMap.put("221", "7");
		keyMap.put("112", "8");
		
		File file = new File(SettingShares.ROOT);
		if(!file.exists() || !file.isDirectory()){
			file.mkdirs();
		}
	}

	/**
	 * 
	 */
	private void init() {
		// TODO Auto-generated method stub
		listView = (ListView) findViewById(R.id.list);
		content = (TextView) findViewById(R.id.content);
		addOne = (ImageView) findViewById(R.id.add_one);
		addTwo = (ImageView) findViewById(R.id.add_two);
		delBtn = (Button) findViewById(R.id.del_btn);
		
		addOne.setClickable(true);
		addTwo.setClickable(true);
		
		addOne.setOnClickListener(this);
		addTwo.setOnClickListener(this);
		delBtn.setOnClickListener(this);
		
		TextView footer = new TextView(this);
		footer.setText("添加");
		footer.setGravity(Gravity.CENTER);
		footer.setTextSize(20);
		footer.setPadding(0, 10, 0, 10);
		listView.addFooterView(footer, null, true);
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	
	
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockActivity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
//		System.out.println("stop "+contentFile.getName());
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(contentFile));
			for(int i=0;i<arrays.size();i++){
				String s = arrays.get(i);
				if( s!= null && s.length() > 0){
					writer.write(s.toString(), 0, s.toString().length());
					writer.newLine();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if(writer != null){
					writer.flush();
					writer.close();
					writer = null;
				}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		super.onStop();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		contentFile = new File(SettingShares.ROOT+"/"+SettingShares.getFileName(sharedPreferences));
		
//		System.out.println("resume "+contentFile.getName());
		
		if(!contentFile.exists() || !contentFile.isFile()){
			try {
//				System.out.println("resume create "+contentFile.getName());
				contentFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		BufferedReader dr = null;
		arrays.clear();
		try {
			dr = new BufferedReader(new FileReader(contentFile)); 
			String key = null;
			int i =0;
			StringBuilder builder = null;
			while (true) {
				key = dr.readLine();
				if(key == null){
					break;
				}
				if(key.length() > 4 && !key.contains("/")){
					if(builder == null){
						builder = new StringBuilder();
					}
					builder.delete(0, builder.length());
					
					for(int j=0;j<key.length();j++){
						builder.append(key.charAt(j));
						if(j%3 == 2){
							builder.append("/");
						}
					}
					key = builder.toString();
				}
				arrays.put(i, key);
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(dr != null){
				try {
					dr.close();
					dr = null;
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}
//		System.out.println(contentFile.getName()+"   "+arrays.size());
		adapter.notifyDataSetChanged();
		super.onResume();
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockActivity#onCreateOptionsMenu(com.actionbarsherlock.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, R.id.menu_settings, 1, "设置");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == R.id.menu_settings){
			SettingActivity.redirectToActivity(MainActivity.this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(arrays.size() <= 0){
			return;
		}
		String str = arrays.get(selectIndex);
		String value = "";
		switch (v.getId()) {
			case R.id.add_one:
				value = itemAdd(str,"1");
				break;
			case R.id.add_two:
				value = itemAdd(str,"2");
				break;
			case R.id.del_btn:
				value = itemRemove(str);
				if(value.length() % 4 == 2){
					System.out.println("notifu");
					arrays.put(selectIndex, value);
					adapter.notifyDataSetChanged();
				}
				break;
			default:
				break;
		}
		arrays.put(selectIndex, value);
		if(!value.equals(str)){
//			System.out.println(str.length());
			if(value.length() % 4 == 3){
				adapter.notifyDataSetChanged();
			}
		}
		content.setText(value);
		
		makeNotify();
	}

	/**
	 * @param value
	 */
	private void makeNotify() {
		String value = arrays.get(selectIndex);
		int patch = SettingShares.getPatch(sharedPreferences);
		int valueLen = value.length();
		if(selectIndex + 1 > patch
//				&& (valueLen == 0 || valueLen %4 == 3 || valueLen %4 == 1 )
				&& (valueLen < ITEM_COUNT*4 -1)){
			
			String key = null;
			int startPos = 0;
			if(valueLen%4 == 3){
				startPos = valueLen+1;
			}else if(valueLen ==0){
				startPos =valueLen;
			}else if(valueLen%4 == 2){
				startPos = valueLen - 2;
			}else{
				startPos = valueLen - 1;
			}
			for(;patch>0;patch--){
				if(arrays.get(selectIndex-patch).length() >= startPos + 3){
					if(key == null){
						key = arrays.get(selectIndex-patch).substring(startPos,startPos+3);
					}else{
						if(!key.equals(arrays.get(selectIndex-patch).substring(startPos,startPos+3))){
							System.out.println("值不对");
							return;
						}
					}
				}else{
					return;
				}
			}
			if(key == null){
				return;
			}
			System.out.println(valueLen +"   "+key);
			if(valueLen%4 == 1){
				if(key.charAt(0) == value.charAt(valueLen-1)){
					Toast.makeText(MainActivity.this, "第二位不要填写"+key.charAt(1), Toast.LENGTH_SHORT).show();
				}
			}else if(valueLen%4 == 2){
				if(key.charAt(0) == value.charAt(valueLen-2)
						&& key.charAt(1) == value.charAt(valueLen-1)){
					Toast.makeText(MainActivity.this, "第三位不要填写"+key.charAt(2), Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(MainActivity.this, "第一位不要填写"+key.charAt(0), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**
	 * @param string
	 * @param key 
	 */
	private String itemAdd(String str, String key) {
		// TODO Auto-generated method stub
		if(str == null){
			str = "";
		}
		if(str.length() >= ITEM_COUNT*4 -1){
			Toast.makeText(MainActivity.this, "长度已经超出", 1000).show();
			return str;
		}
		if(str.length() % 4 == 3){
			return str+"/"+key;
		}else {
			return str +key;
		}
	}
	
	private String itemRemove(String str) {
		// TODO Auto-generated method stub
		if(str == null){
			str = "";
		}
		if(str.length() <= 0){
			return str;
		}
		if(str.length() % 4 == 1){
			if(str.length() == 1){
				return "";
			}else{
				return str.substring(0, str.length() -2);
			}
		}else {
			return str.substring(0, str.length() -1);
		}
	}

	private class MyAdapter extends BaseAdapter{
		private List<Integer[]> sliptedList = new ArrayList<Integer[]>();
		
		public MyAdapter(){
			initAdapterData();
		}
		
		private void initAdapterData() {
			// TODO Auto-generated method stub
//			System.out.println("initAdapterData()");
			sliptedList.clear();
			List<String[]> stringList = new ArrayList<String[]>();
			for(int i=0;i<arrays.size();i++){
				stringList.add(arrays.get(i).split("/"));
				sliptedList.add(new Integer[stringList.get(i).length]);
			}
			for(int i=0;i<stringList.size();i++){
				Integer[] colors = sliptedList.get(i);
				int sameCount = 1;
				for(int j = 0;j<colors.length;j++){
					if(colors[j] == null){
						colors[j] = android.R.color.white;
					}
					if(i+1 < sliptedList.size() 
							&& stringList.get(i+1).length >j
							&& stringList.get(i)[j].equals(stringList.get(i+1)[j])){
						colors[j] = R.color.red;
						sliptedList.get(i+1)[j] = R.color.red;
					}
					if(i==6 && j == 0){
						System.out.println(stringList.get(i)[j]);
						System.out.println(stringList.get(i+1)[j]);
					}
				}
				for(int j = 1;j<colors.length;j++){
					if(stringList.get(i)[j].equals(stringList.get(i)[j-1])){
						sameCount ++;
					}else{
						sameCount=1;
					}
					if(sameCount == 3){
						colors[j] = (colors[j] == R.color.red ) ? R.color.yellow:R.color.green; 
						colors[j-1] = (colors[j-1] == R.color.red ) ? R.color.yellow:R.color.green; 
						colors[j-2] = (colors[j-2] == R.color.red ) ? R.color.yellow:R.color.green; 
					}else if(sameCount >3){
						colors[j] = (colors[j] == R.color.red ) ? R.color.yellow:R.color.green; 
					}
				}
			}
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			initAdapterData();
			super.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrays.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return arrays.get(position);
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LinearLayout layout = null;
			if(position == selectIndex && selectItem != null){
				layout = selectItem;
				selectItem.removeAllViews();
			}else{
				layout = (LinearLayout)MainActivity.this
						.getLayoutInflater().inflate(R.layout.item, null);
			}
			String str = arrays.get(position);
//			System.out.println(position+"  content:"+str+"  "+(str == null || str.length() <=2));
			if(str == null || str.length() <=2){
				TextView textView = new TextView(MainActivity.this); 
				textView.setTextSize(SIZE);
				textView.setGravity(Gravity.CENTER);
				textView.setText("点击编辑");
				layout.addView(textView);
			}else{
				String[] strs = str.split("/");
				TextView keyView = null;
				for(int i=0;i<strs.length;i++){
					String key = strs[i];
					if(key == null || key.length() <= 2){
						break;
					}
//					keyView = (TextView)MainActivity.this
//							.getLayoutInflater().inflate(R.layout.textview, null);
					keyView = new TextView(MainActivity.this);
					keyView.setText(""+keyMap.get(key));
					keyView.setPadding(10, 0, 10,0);
					keyView.setTextSize(SIZE);
					keyView.setGravity(Gravity.CENTER);
					keyView.setBackgroundColor(getResources().getColor(sliptedList.get(position)[i]));
					layout.addView(keyView);
				}
			}
			return layout;
		}
		
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		System.out.println("on item click "+position);
		if(position == arrays.size()){
			arrays.put(arrays.size(), "");
			adapter.notifyDataSetChanged();
		}else{
			if(selectItem != null){
				selectItem.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			}
			selectItem = (LinearLayout) view;
			selectIndex = position;
			selectItem.setBackgroundColor(getResources().getColor(R.color.gray));
			content.setText(arrays.get(position));
			
			makeNotify();
		}
	}
}
