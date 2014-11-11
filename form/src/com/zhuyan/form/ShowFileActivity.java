/**
 * CopyRight   2013 ZhuYan
 * @auther BLUE
 *
 * All right reserved
 *
 * Created on 2014-11-9 ÏÂÎç9:52:20
 * 
 */
package com.zhuyan.form;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

/**
 * @author zy
 *
 * Create on 2014-11-9  ÏÂÎç9:52:20
 */
public class ShowFileActivity extends SherlockActivity{

	public static final void redirectToActivity(Context context){
		Intent intent = new Intent(context,ShowFileActivity.class);
		context.startActivity(intent);
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_show_file);
		
		TextView content = (TextView) findViewById(R.id.file_content);
		
		SharedPreferences sharedPreferences = getSharedPreferences(SettingShares.NAME, 0);
		File contentFile = new File(SettingShares.ROOT+"/"+SettingShares.getFileName(sharedPreferences)+"_count");
		
//		System.out.println("resume "+contentFile.getName());
		
		BufferedReader dr = null;
		StringBuilder sb = new StringBuilder();
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
				sb.append(key);
				sb.append("\n");
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
		
		content.setText(sb.toString());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == android.R.id.home){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
