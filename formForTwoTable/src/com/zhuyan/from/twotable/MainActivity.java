package com.zhuyan.from.twotable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Date;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.zhuyan.from.twotable.adapter.MainViewPaggerAdapter;
import com.zhuyan.from.twotable.util.DataRunning;
import com.zhuyan.from.twotable.util.Point;
import com.zhuyan.from.twotable.util.SettingShares;

public class MainActivity extends SherlockFragmentActivity implements
		OnClickListener {

	private File contentFile;

	private SharedPreferences sharedPreferences;

	private ViewPager viewPager = null;

	private ImageView addOne;
	private ImageView addTwo;
	private Button delBtn;
	private Button recoveryBtn;

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
		delBtn = (Button) findViewById(R.id.del_btn);
		recoveryBtn = (Button) findViewById(R.id.recover_btn);

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

			// 重新开始计算 sum 清空 px py 归0
			dataRunning.initPos();

			for (int i = 0; i < value.length(); i++) {
				if (value.charAt(i) == '1') {
					dataRunning.doWrong(false, MainActivity.this);
				} else if (value.charAt(i) == '2') {
					dataRunning.doRight(false, MainActivity.this);
				}
			}

			dataRunning.notifyDataChanged();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e+"  Onresume");
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

}
