/**
 * CopyRight   2013 ZhuYan
 * @auther BLUE
 *
 * All right reserved
 *
 * Created on 2015-6-17 обнГ9:18:04
 * 
 */
package com.zhuyan.formmap0603.adapter;

import java.util.HashMap;
import java.util.Map;

import com.zhuyan.formmap0603.MainActivity;
import com.zhuyan.formmap0603.fragment.MapFragment;
import com.zhuyan.formmap0603.fragment.TabFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author zy
 *
 * Create on 2015-6-17  обнГ9:18:04
 */
public class MainViewPaggerAdapter extends FragmentPagerAdapter {
	
	private MainActivity mainActivity;
	
	private Map<Integer, Fragment> map;
	
	public MainViewPaggerAdapter(FragmentManager fm, MainActivity mainActivity) {
		super(fm);
		this.mainActivity = mainActivity;
		map = new HashMap<Integer, Fragment>();
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment fragment = map.get(arg0);
		if(fragment == null){
			if(arg0 == 0){
				fragment = new TabFragment();
			}else{
				fragment  = new MapFragment();
			}
			map.put(arg0, fragment);
		}
		return fragment;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}
