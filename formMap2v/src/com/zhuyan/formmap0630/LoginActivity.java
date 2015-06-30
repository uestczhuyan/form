package com.zhuyan.formmap0630;

import java.util.Date;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.util.mail.MailSenderInfo;
import com.util.mail.SimpleMailSender;
import com.zhuyan.formmap0630.util.SettingShares;

public class LoginActivity extends SherlockActivity {

	private String code = null;
	private SharedPreferences sharedPreferences;

	private EditText editText;
	private Button btn;

	public static void redirectToLogin(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		sharedPreferences = getSharedPreferences(SettingShares.NAME, 0);

		sendCode();

		init();
	}

	private void init() {
		editText = (EditText) findViewById(R.id.login_tv);
		btn = (Button) findViewById(R.id.login_btn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String content = editText.getText().toString();
				if (code != null && code.length() > 0) {
					if (code.equals(content)) {
						SettingShares.storeTime(
								SettingShares.FORMAT.format(new Date()),
								sharedPreferences);
						SettingShares.clearCode(sharedPreferences);

						Intent intent = new Intent(LoginActivity.this,
								MainActivity.class);
						startActivity(intent);
						LoginActivity.this.finish();
					}
				}
			}
		});
	}

	private void sendCode() {
		code = SettingShares.getCode(sharedPreferences);
		if (code == null || code.length() <= 0) {
			Random random = new Random(System.currentTimeMillis());
			code = "";
			for (int i = 0; i <= 5; i++) {
				code = random.nextInt(20) + code;
			}
			new MyTask().execute(code);
		}
	}

	private class MyTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... arg0) {
			MailSenderInfo mailInfo = new MailSenderInfo();
			mailInfo.setMailServerHost("smtp.163.com");
			mailInfo.setMailServerPort("25");
			mailInfo.setValidate(true);
			mailInfo.setUserName("zyc2005988@163.com");
			mailInfo.setPassword("zcw998100ZCW");// 您的邮箱密码
			mailInfo.setFromAddress("zyc2005988@163.com");
			mailInfo.setToAddress("zyc2005988@163.com");
			mailInfo.setSubject("验证码");
			mailInfo.setContent(arg0[0]);
			// 这个类主要来发送邮件
			SimpleMailSender sms = new SimpleMailSender();
			boolean b = sms.sendTextMail(mailInfo);// 发送文体格式
			// sms.sendHtmlMail(mailInfo);// 发送html格式
			System.out.println(b + " send " + arg0[0]);
			return b;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				SettingShares.storeCode(code, sharedPreferences);
			} else {
				Toast.makeText(LoginActivity.this, "发生验证码失败，请关闭软件重新打开",
						Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}

	}

}
