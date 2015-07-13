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
import android.widget.Toast;

/**
 * @author zy
 *
 * Create on 2015-7-14  上午12:22:27
 */
public class DataUnitOne extends DataUnit{

	/**
	 * @param map
	 */
	public DataUnitOne(Map<Integer, List<Double>> map) {
		super(map);
		// TODO Auto-generated constructor stub
	}

	@Override
	boolean doWrong(boolean show, Context context) {
		if(isMoveAble){
			int px = nowPoint.getX(), py = nowPoint.getY();
			if (py == DataRunning.MAX_PY_1) {
				if (py >= DataRunning.MAX_PY_1) {
					if (show) {
						Toast.makeText(context, "无法往下移动", Toast.LENGTH_SHORT)
								.show();
					}
					changeCurrentDate(0.0, -1, -1);
					isMoveAble = false;
					return false;
				}
			} else {
				py++;
				px = 0;
			}

			sumList.add((-1) * DataRunning.getInstance().getBaseNotify()
					* MapInitUtil.getValueInPox(nowPoint, map));

			nowPoint = new Point(px, py);
			points.add(nowPoint);
		}else{
			changeCurrentDate(0.0, -1, -1);
		}
		return  isMoveAble;
	}

	@Override
	boolean doRight(boolean show, Context context) {
		if(isMoveAble){
			int px = nowPoint.getX(), py = nowPoint.getY();
			List<Double> list = map.get(py);

			px++;
			if (px >= list.size()) {
				px = 0;
				if (py % 2 == 0) {
					py = py - 4;
				} else {
					py = py - 7;
				}
				py = py < 0 ? 0 : py;
			}

			sumList.add(DataRunning.getInstance().getBaseNotify() * MapInitUtil.getValueInPox(nowPoint, map));

			nowPoint = new Point(px, py);
			points.add(nowPoint);
		}else{
			changeCurrentDate(0.0,-1, -1);
		}
		return isMoveAble;
	}

}
