package com.zhuyan.formmap0713;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.zhuyan.formmap0713.adapter.MainViewPaggerAdapter;
import com.zhuyan.formmap0713.util.DataRunning;
import com.zhuyan.formmap0713.util.DataUnit;
import com.zhuyan.formmap0713.util.MapInitUtil;
import com.zhuyan.formmap0713.util.Point;
import com.zhuyan.formmap0713.util.SettingShares;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends SherlockFragmentActivity implements
		OnClickListener,OnCheckedChangeListener {

	private File contentFile;

	private SharedPreferences sharedPreferences;

	private ViewPager viewPager = null;

	private TextView notifyTextLeft;
	private TextView notifyTextRight;
	private ImageView addOne;
	private ImageView addTwo;
	private Button delBtn;
	private Button recoveryBtn;
	
	private CheckBox[] checkBoxs = new CheckBox[3];

	private DataRunning dataRunning;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dataRunning = DataRunning.getInstance();
		sharedPreferences = getSharedPreferences(SettingShares.NAME, 0);
		// 暂时不需要邮箱验证
		// checkMail();

		setContentView(R.layout.main_view);

		getSupportActionBar().setDisplayOptions(
				getSupportActionBar().DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle("表格");

		File file = new File(SettingShares.ROOT);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}

		initView();
	}

	private void initView() {
		addOne = (ImageView) findViewById(R.id.add_one);
		addTwo = (ImageView) findViewById(R.id.add_two);
		notifyTextLeft = (TextView) findViewById(R.id.notify_left);
		notifyTextRight = (TextView) findViewById(R.id.notify_right);
		delBtn = (Button) findViewById(R.id.del_btn);
		recoveryBtn = (Button) findViewById(R.id.recover_btn);
		
		checkBoxs[0] = (CheckBox) findViewById(R.id.check1);
		checkBoxs[1] = (CheckBox) findViewById(R.id.check2);
		checkBoxs[2] = (CheckBox) findViewById(R.id.check3);
		boolean[] checks = SettingShares.getCheck(sharedPreferences);
		for(int i=0;i<3;i++){
			checkBoxs[i].setChecked(checks[i]);
		}
		for(CheckBox box:checkBoxs){
			box.setOnCheckedChangeListener(this);
		}
		addOne.setClickable(true);
		addTwo.setClickable(true);

		addOne.setOnClickListener(this);
		addTwo.setOnClickListener(this);
		delBtn.setOnClickListener(this);
		recoveryBtn.setOnClickListener(this);

		viewPager = (ViewPager) findViewById(R.id.main_pager);

		viewPager.setAdapter(new MainViewPaggerAdapter(
				getSupportFragmentManager(), this));
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		SettingShares.storeCheck(checkBoxs[0].isChecked(), checkBoxs[1].isChecked(), 
					checkBoxs[2].isChecked(), sharedPreferences);
		
		notifyTextView();
	};


	/**
	 * 
	 */
	private void notifyTextView() {
		Double sumDouble  = 0.0;
		Double valueDouble = 0.0;
		List<DataUnit> units = dataRunning.getDataUnits();
		boolean[] checks = SettingShares.getCheck(sharedPreferences);
		for(int i=0;i<3;i++){
			if(checks[i]){
				sumDouble += units.get(i).getSum();
				valueDouble += dataRunning.getBaseNotify()*MapInitUtil.getValueInPox(
						units.get(i).getNowPoint(), units.get(i).getMap());
			}
		}
		notifyTextRight.setText("\n 最终结果:" + sumDouble);
		notifyTextLeft.setText("现在值是:" + valueDouble);
		dataRunning.notifyDataChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_one:
			dataRunning.doWrong(true, MainActivity.this);
			break;
		case R.id.add_two:
			dataRunning.doRight(true, MainActivity.this);
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
		notifyTextView();
//
//		System.out.println(dataRunning.getNowPoint());
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

		dataRunning = DataRunning.getInstance();
		System.out.println("activity Onresume");
		dataRunning.setBaseNotify(SettingShares.getPatch(sharedPreferences));

		contentFile = new File(SettingShares.ROOT + "/"
				+ SettingShares.getFileName(sharedPreferences));

		if (!contentFile.exists() || !contentFile.isFile()) {
			try {
				contentFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("onResume");

		BufferedReader dr = null;
		dataRunning.clearAll();
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
			
			System.out.println(" value: "+value);

			// 重新开始计算 sum 清空 px py 归0
			dataRunning.InitStartPoint();

			for (int i = 0; i < value.length(); i++) {
				if (value.charAt(i) == '1') {
					dataRunning.doWrong(false, MainActivity.this);
				} else if (value.charAt(i) == '2') {
					dataRunning.doRight(false, MainActivity.this);
				}
			}

			notifyTextView();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
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
		super.onResume();
	}

	@Override
	protected void onStop() {
		for (int k = 0; k <= 3; k++) {
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(contentFile));
				StringBuilder sb = new StringBuilder();
				for (Integer b : dataRunning.getResults()) {
					sb.append(b);
				}
				System.out.println(" value save: "+sb.toString());
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
	}

}
