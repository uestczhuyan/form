/**
 * CopyRight   2013 ZhuYan
 * @auther BLUE
 *
 * All right reserved
 *
 * Created on 2014-10-29 下午10:41:37
 * 
 */
package com.zhuyan.form;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

/**
 * @author zy
 *
 * Create on 2014-10-29  下午10:41:37
 */
public class SettingActivity  extends SherlockActivity{
	
	public static final void redirectToActivity(Context context){
		Intent intent = new Intent(context,SettingActivity.class);
		context.startActivity(intent);
	}
	
	private SharedPreferences sharedPreferences;
	private EditText patchEditText;
	private ListView listView;
	private List<File> files = new ArrayList<File>();
	private MyAdapter adapter;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setting);
		sharedPreferences = getSharedPreferences(SettingShares.NAME, 0);
		init();
	}

	/**
	 * 
	 */
	private void init() {
		// TODO Auto-generated method stub
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		patchEditText = (EditText) findViewById(R.id.setting_patch_edit);
		patchEditText.setText(SettingShares.getPatch(sharedPreferences)+"");
		
		freshFileList();
		
		listView = (ListView)findViewById(R.id.file_list);
		TextView footer = new TextView(this);
		footer.setText("添加文件");
		footer.setGravity(Gravity.CENTER);
		footer.setTextSize(20);
		footer.setPadding(0, 10, 0, 10);
		listView.addFooterView(footer, null, true);
		footer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("add file");
			}
		});
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
 	}
	
	
	
	/**
	 * 
	 */
	private void freshFileList() {
		// TODO Auto-generated method stub
		File file = new File(SettingShares.ROOT);
		files.clear();
		for(File content:file.listFiles()){
			if(content.isFile() && content.getName().endsWith(".txt")){
				files.add(content);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockActivity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		try {
			SettingShares.storePatch(Integer.parseInt(patchEditText.getText().toString()), sharedPreferences);
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onStop();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == android.R.id.home){
			checkData();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			checkData();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * 
	 */
	private void checkData() {
		int value = 0;
		try {
			value = Integer.parseInt(patchEditText.getText().toString());
			if(value <= 0){
				Toast.makeText(SettingActivity.this, "输入不能小于0", Toast.LENGTH_SHORT).show();
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(SettingActivity.this, "输入不正确", Toast.LENGTH_SHORT).show();
			return ;
		}
		finish();
	}
	
	private  class MyAdapter  extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return files.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return files.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if(convertView == null){
				convertView = SettingActivity.this.getLayoutInflater().inflate(R.layout.file_item, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.setContent(position);
			return convertView;
		}
		
	}
	private class ViewHolder{
		private TextView fileName;
		
		public ViewHolder(View convertView){
			fileName = (TextView) convertView.findViewById(R.id.file_name);
		}
		
		public void setContent(int pos){
			fileName.setText(files.get(pos).getName());
		}
	}
}
