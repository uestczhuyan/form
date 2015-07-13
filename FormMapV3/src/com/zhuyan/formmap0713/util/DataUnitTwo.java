/**
 * CopyRight   2013 ZhuYan
 * @auther BLUE
 *
 * All right reserved
 *
 * Created on 2015-7-14 ионГ12:22:27
 * 
 */
package com.zhuyan.formmap0713.util;

import java.util.List;
import java.util.Map;

import android.content.Context;

/**
 * @author zy
 *
 * Create on 2015-7-14  ионГ12:22:27
 */
public class DataUnitTwo extends DataUnit{

	/**
	 * @param map
	 */
	public DataUnitTwo(Map<Integer, List<Double>> map) {
		super(map);
		
	}

	@Override
	boolean doWrong(boolean show, Context context) {
		if(isMoveAble){
			int y = nowPoint.getY(),x = nowPoint.getX();
			x--;
			List<Double> valList = map.get(y);
			if(x*-1 >= valList.size()){
				y++;
				x=0;
				if(y<map.size()){
					changeCurrentDate(valList.get(0)*-1*DataRunning.getInstance().getBaseNotify(), x, y);
				}else{
					isMoveAble = false;
					changeCurrentDate(0.0, -1, -1);
				}
			}else{
				changeCurrentDate(valList.get(0)*-1*DataRunning.getInstance().getBaseNotify(), x, y);
			}
		}else{
			changeCurrentDate(0.0, -1, -1);
		}
		return isMoveAble;
	}

	@Override
	boolean doRight(boolean show, Context context) {
		if(isMoveAble){
			int y = nowPoint.getY(),x = nowPoint.getX();
			x++;
			List<Double> valList = map.get(y);
			if(x >= valList.size()){
				y=0;
				x=0;
				changeCurrentDate( valList.get(0)*DataRunning.getInstance().getBaseNotify(), x, y);
			}else{
				changeCurrentDate(valList.get(0)*DataRunning.getInstance().getBaseNotify(), x, y);
			}
		}else{
			changeCurrentDate(0.0, -1, -1);
		}
		return isMoveAble;
	}

}
