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
import com.actionbarsherlock.internal.view.menu.ActionMenu;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.os.AsyncTask;
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
	private TextView notifyText;
	private ImageView addOne;
	private ImageView addTwo;
	private Button delBtn;
	
	private LinearLayout selectItem = null;
	private int selectIndex = 0;
	private  Map<Integer, String> arrays = new HashMap<Integer, String>();
	private MyAdapter adapter;
	private Map<String, String> keyMap = new HashMap<String, String>();
	private String[][] keyMapValues = new String[5][2];
	private SharedPreferences sharedPreferences;
	private File contentFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getSupportActionBar().setDisplayOptions(getSupportActionBar().DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle("表格");
		sharedPreferences = getSharedPreferences(SettingShares.NAME, 0);
		
//		testAddData();
		File file = new File(SettingShares.ROOT);
		if(!file.exists() || !file.isDirectory()){
			file.mkdirs();
		}
		
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
		keyMap.clear();
		if(SettingShares.getOpenMohu(sharedPreferences)){
			keyMap.put("211", "1");
			keyMap.put("122", "1");
			keyMap.put("222", "2");
			keyMap.put("111", "2");
			keyMap.put("212", "3");
			keyMap.put("121", "3");
			keyMap.put("221", "4");
			keyMap.put("112", "4");
			
			keyMapValues[1] = new String[]{"211","122"};
			keyMapValues[2] = new String[]{"222","111"};
			keyMapValues[3] = new String[]{"212","121"};
			keyMapValues[4] = new String[]{"221","112"};
		}else{
			keyMap.put("211", "1");
			keyMap.put("122", "2");
			keyMap.put("222", "3");
			keyMap.put("111", "4");
			keyMap.put("212", "5");
			keyMap.put("121", "6");
			keyMap.put("221", "7");
			keyMap.put("112", "8");
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
		notifyText = (TextView) findViewById(R.id.notify);
		
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
		for(int k=0;k<=3;k++){
			System.out.println(";="+k+"   save file");
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(contentFile));
				for(int i=0;i<arrays.size();i++){
					String s = arrays.get(i);
					if( s!= null && s.length() > 0){
						writer.write(s.toString(), 0, s.toString().length());
						writer.newLine();
						writer.flush();
					}
				}
				k=4;
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println("store file:e"+e);
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
		}
		super.onStop();
	};

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
//		sharedPreferences = getSharedPreferences(SettingShares.NAME, 0);
		
		initData();
		
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
		menu.findItem(R.id.menu_settings).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.findItem(R.id.menu_settings).setIcon(getResources().getDrawable(android.R.drawable.ic_menu_more));
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
						key = keyMap.get(key);
					}else{
						if(!(key.equals(keyMap.get(arrays.get(selectIndex-patch).substring(startPos,startPos+3))))){
							System.out.println("值不对");
							notifyText.setText("");
							return;
						}
					}
				}else{
					notifyText.setText("");
					return;
				}
			}
			if(key == null){
				notifyText.setText("");
				return;
			}
			if(SettingShares.getOpenMohu(sharedPreferences)){
				int j = 0;
				int i = 1;
				try {
					i = Integer.parseInt(key);
				} catch (Exception e) {
					// TODO: handle exception
				}
				if(valueLen%4 == 1){
					if(value.charAt(valueLen - 1)== '2'){
						j =0;
					}else{
						j = 1;
					}
					notifyText.setText("第二位不要填写"+keyMapValues[i][j].charAt(1));
				}else if(valueLen%4 == 2){
					
					if(value.charAt(valueLen - 2)== '2'){
						j =0;
					}else{
						j = 1;
					}
					if(keyMapValues[i][j].charAt(1) == value.charAt(valueLen-1)){
						notifyText.setText("第三位不要填写"+keyMapValues[i][j].charAt(2));
//					Toast.makeText(MainActivity.this, "第三位不要填写"+key.charAt(2), Toast.LENGTH_SHORT).show();
					}else{
						notifyText.setText("");
					}
				}else{
					notifyText.setText("");
//				notifyText.setText("第一位不要填写"+key.charAt(0));
//				Toast.makeText(MainActivity.this, "第一位不要填写"+key.charAt(0), Toast.LENGTH_SHORT).show();
				}
			}else{
				String keyString = "";
				for (String str : keyMap.keySet()) {
					if(keyMap.get(str).equals(key)){
						keyString = str;
					}
				}
				System.out.println("key:"+key+"   "+keyString);
				if(valueLen%4 == 1){
					if(keyString.charAt(0) == value.charAt(valueLen-1)){
						notifyText.setText("第二位不要填写"+keyString.charAt(1));
					}else{
						notifyText.setText("");
					}
				}else if(valueLen%4 == 2){
					if(keyString.charAt(0) == value.charAt(valueLen-2)
							&& keyString.charAt(1) == value.charAt(valueLen-1)){
						notifyText.setText("第三位不要填写"+keyString.charAt(2));
//					Toast.makeText(MainActivity.this, "第三位不要填写"+key.charAt(2), Toast.LENGTH_SHORT).show();
					}else{
						notifyText.setText("");
					}
				}else{
					notifyText.setText("第一位不要填写"+keyString.charAt(0));
	//				Toast.makeText(MainActivity.this, "第一位不要填写"+key.charAt(0), Toast.LENGTH_SHORT).show();
				}
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
			System.out.println("initAdapterData()  " + SettingShares.getPatch(sharedPreferences));
			sliptedList.clear();
			List<String[]> stringList = new ArrayList<String[]>();
			for(int i=0;i<arrays.size();i++){
				stringList.add(arrays.get(i).split("/"));
				sliptedList.add(new Integer[stringList.get(i).length]);
			}
			for(int i=0;i<stringList.size();i++){
				Integer[] colors = sliptedList.get(i);
				int sameCount = 1;
				int checkSize = SettingShares.getPatch(sharedPreferences);
//				System.out.println("data "+checkSize);
				if(checkSize >= 1){
					checkSize = checkSize+1;
				}else{
					checkSize = 2;
				}
				for(int j = 0;j<colors.length;j++){
					if(colors[j] == null){
						colors[j] = android.R.color.white;
					}
					
					boolean isRed = true;
					for(int k=1;k<checkSize;k++){
						if(i+k < sliptedList.size() 
								&& stringList.get(i+k).length >j
								&& (keyMap.get(stringList.get(i)[j]) != null) 
								&& (keyMap.get(stringList.get(i)[j])).equals(keyMap.get(stringList.get(i+k)[j]))){
							
						}else{
							isRed = false;
							break;
						}
					}
					if(isRed){
						for(int k=1;k<checkSize;k++){
							colors[j] = R.color.red;
							sliptedList.get(i+k)[j] = R.color.red;
						}
					}
				}
				for(int j = 1;j<colors.length;j++){
					if( (keyMap.get(stringList.get(i)[j]) != null) 
							&& (keyMap.get(stringList.get(i)[j])).equals(keyMap.get(stringList.get(i)[j-1]))){
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
