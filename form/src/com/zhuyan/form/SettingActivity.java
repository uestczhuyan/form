/**
 * CopyRight   2013 ZhuYan
 * @auther BLUE
 *
 * All right reserved
 *
 * Created on 2014-10-29 ����10:41:37
 * 
 */
package com.zhuyan.form;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
 * Create on 2014-10-29  ����10:41:37
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
		footer.setText("����ļ�");
		footer.setGravity(Gravity.CENTER);
		footer.setTextSize(20);
		footer.setPadding(0, 10, 0, 10);
		listView.addFooterView(footer, null, true);
		footer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("add file");
				creatFile();
			}
		});
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
 	}
	
	
	
	protected void creatFile() {
		// TODO Auto-generated method stub
		final EditText et = new EditText(SettingActivity.this); 
		AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
		builder.setTitle("�����µ��ļ�");
		builder.setView(et);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String content = et.getText().toString().trim();
				if(content == null || content.length() <= 0){
					toastShow("�ļ�������Ϊ��");
				}else{
					if(!content.endsWith(".txt")){
						content = content+".txt";
					}
					for(File file:files){
						if(file.getName().equals(content)){
							toastShow("�ļ��Ѿ�����");
							return;
						}
					}
					File file =  new File(SettingShares.ROOT+"/"+content);
					try {
						file.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					freshFileList();
					adapter.notifyDataSetChanged();
					dialog.cancel();
					dialog = null;
				}
			}
		})
		.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				dialog.dismiss();
				dialog.cancel();
				dialog = null;
			}
		});
		builder.create().show();
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
				Toast.makeText(SettingActivity.this, "���벻��С��0", Toast.LENGTH_SHORT).show();
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(SettingActivity.this, "���벻��ȷ", Toast.LENGTH_SHORT).show();
			return ;
		}
		finish();
	}
	
	private void toastShow(String context){
		Toast.makeText(SettingActivity.this, context, Toast.LENGTH_SHORT).show();
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
