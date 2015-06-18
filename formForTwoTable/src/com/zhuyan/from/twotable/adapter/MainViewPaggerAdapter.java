/**
 * CopyRight   2013 ZhuYan
 * @auther BLUE
 *
 * All right reserved
 *
 * Created on 2015-6-17 обнГ9:18:04
 * 
 */
package com.zhuyan.from.twotable.adapter;

import java.util.HashMap;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zhuyan.from.twotable.MainActivity;
import com.zhuyan.from.twotable.fragment.TabOneFragment;
import com.zhuyan.from.twotable.fragment.TabTwoFragment;

/**
 * @author zy
 * 
 *         Create on 2015-6-17 обнГ9:18:04
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
		if (fragment == null) {
			if (arg0 == 0) {
				fragment = new TabOneFragment();
			} else {
				fragment = new TabTwoFragment();
			}
			map.put(arg0, fragment);
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return 2;
	}

}
