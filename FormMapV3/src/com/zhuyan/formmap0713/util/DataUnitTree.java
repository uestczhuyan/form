/**
 * CopyRight   2013 ZhuYan
 * @auther BLUE
 *
 * All right reserved
 *
 * Created on 2015-7-14 上午12:22:27
 * 
 */
package com.zhuyan.formmap0713.util;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.provider.ContactsContract.Contacts.Data;
import android.widget.Toast;

/**
 * @author zy
 *
 * Create on 2015-7-14  上午12:22:27
 */
public class DataUnitTree extends DataUnit{

	/**
	 * @param map
	 */
	public DataUnitTree(Map<Integer, List<Double>> map) {
		super(map);
		// TODO Auto-generated constructor stub
	}

	@Override
	boolean doWrong(boolean show, Context context) {
		if(isMoveAble){
			int px = nowPoint.getX(), py = nowPoint.getY();
			List<Integer> results = DataRunning.getInstance().getResults();
			if (px == 1 && results.size() > 0
					&& results.get(results.size() - 1) == 2) {
				if (py >= 39) {
					if (show) {
						Toast.makeText(context, "无法往下移动", Toast.LENGTH_SHORT)
								.show();
					}
					changeCurrentDate(0.0, -1, -1);
					isMoveAble = false;
					return isMoveAble;
				}
				py++;
				px = 0;
			} else if (px > 0) {
				px--;
			} else {
				if (py >= 39) {
					if (show) {
						Toast.makeText(context, "无法往下移动", Toast.LENGTH_SHORT)
								.show();
					}
					changeCurrentDate(0.0, -1, -1);
					isMoveAble = false;
					return isMoveAble;
				}
				px = 0;
				py++;
			}
			changeCurrentDate((-1) * DataRunning.getInstance().getBaseNotify()
			* MapInitUtil.getValueInPox(nowPoint, map), px, py);
		}else{
			changeCurrentDate(0.0, -1, -1);
		}
		return isMoveAble;
	}

	@Override
	boolean doRight(boolean show, Context context) {
		if(isMoveAble){
			int px = nowPoint.getX(), py = nowPoint.getY();
			List<Double> list = map.get(py);

			if (list == null || list.size() <= 0) {
				if (show) {
					Toast.makeText(context, "数据出错：py=" + py, Toast.LENGTH_SHORT)
							.show();
				}
				changeCurrentDate(0.0, -1, -1);
				isMoveAble = false;
				return isMoveAble;
			}
			if (list.size() - 1 > px) {
				px++;
			} else {
				px = 0;
				py = 0;
			}

			changeCurrentDate(DataRunning.getInstance().getBaseNotify() * MapInitUtil.getValueInPox(nowPoint, map),px,py);
			isMoveAble = false;
			return isMoveAble;
		}else{
			changeCurrentDate(0.0, -1, -1);
		}
		return isMoveAble;
	}

}
