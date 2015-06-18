/**
 * CopyRight   2013 ZhuYan
 * @auther BLUE
 *
 * All right reserved
 *
 * Created on 2015-6-17 下午9:37:22
 * 
 */
package com.zhuyan.from.twotable.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.Toast;

/**
 * @author zy
 * 
 *         Create on 2015-6-17 下午9:37:22
 */
public class DataRunning {

	private static DataRunning INSTANCE;

	private List<Integer> results = new ArrayList<Integer>();
	private List<Double> sumListOne = new ArrayList<Double>();
	private List<Double> sumListTwo = new ArrayList<Double>();
	private List<Point> pointsOne = new ArrayList<Point>();
	private List<Point> pointsTwo = new ArrayList<Point>();
	
	private boolean isOneMoveAble = true;
	private boolean isTwoMoveAble = true;

	public final static int MAX_PY = 39;
	private int baseNotify = 0;
	public static Map<Integer, List<Double>> mapOne = MapInitUtil.initMapOne();
	public static Map<Integer, List<Double>> mapTwo = MapInitUtil.initMapTwo();

	public interface OnDataChange {
		void onChange();
	}

	private List<OnDataChange> onDataChanges = null;

	private DataRunning() {

	}

	public static DataRunning getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DataRunning();
		}
		return INSTANCE;
	}

	/**
	 * @param onDataChange
	 *            the onDataChange to set
	 */
	public void addOnDataChange(OnDataChange onDataChange) {
		if (onDataChanges == null) {
			onDataChanges = new ArrayList<DataRunning.OnDataChange>();
		}
		onDataChanges.add(onDataChange);
	}

	public void removeOnDataChange(OnDataChange onDataChange) {
		if (onDataChanges != null) {
			onDataChanges.remove(onDataChange);
		}
	}

	/**
	 * @return
	 */
	public Double getSum(int type) {
		double sum = 0;
		List<Double> sumList = type == 1?sumListOne:sumListTwo;
		for (Double num : sumList) {
			sum += num;
		}
		return sum;
	}
	
	public  List<Double> getCurrentValueList(int type,int y) {
		Map<Integer, List<Double>> map = type == 1?mapOne:mapTwo;
		return map.get(y);
	}

	
	public  List<Double> getCurrentSumList(int type) {
		return  type == 1?sumListOne:sumListTwo;
	}
	
	public  List<Point> getCurrentPointList(int type) {
		return  type == 1?pointsOne:pointsTwo;
	}
	
	private int getCurrentMapSize(int type){
		return type==1?mapOne.size():mapTwo.size();
	}
	
	public void initPos(){
		pointsOne.clear();
		pointsTwo.clear();
		pointsOne.add(new Point(0,0));
		pointsTwo.add(new Point(0, 0));
	}
	
	public Double getCurrentNum(int type) {
		Map<Integer, List<Double>> map = type == 1?mapOne:mapTwo;
		Point point = getCurrentPoint(type);
		List<Double> values = map.get(point.getY());
		if(values != null && values.size() > 1){
			return values.get(0);
		}
		return null;
	}
	
	private Point getCurrentPoint(int type) {
		List<Point> list = type == 1?pointsOne:pointsTwo;
		return list.get(list.size()-1);
	}

	/**
	 * 如果左边有值 向左边移动，否则向下移动</br> 特殊情况，px = 1时候，前面一部并且是正确值，直接向下移动
	 */
	public boolean doWrong(boolean show, Context context) {
		if(isOneMoveAble || isTwoMoveAble){
			doWrong(show, context,1);
			doWrong(show, context,2);
			if(!isOneMoveAble && !isTwoMoveAble){
				return false;
			}
			results.add(1);
			notifyDataChanged();
			return true;
		}else{
			if(show){
				Toast.makeText(context, "表格1,2 都不能移动了  已经结束", Toast.LENGTH_SHORT)
					.show();
			}
			return false;
		}
		
	}
	

	/**
	 * @param show
	 * @param context
	 * @param i
	 */
	private void doWrong(boolean show, Context context, int type) {
		
		if(type== 1?isOneMoveAble:isTwoMoveAble){
			Point point = getCurrentPoint(type);
			int y = point.getY(),x = point.getX();
			x--;
			List<Double> valList = getCurrentValueList(type,y);
			if(x*-1 >= valList.size()){
				y++;
				x=0;
				if(y<getCurrentMapSize(type)){
					changeCurrentDate(type,valList.get(0)*-1*baseNotify, x, y);
					return;
				}else{
					if(type==1){
						isOneMoveAble = true;
					}else{
						isTwoMoveAble = false;
					}
				}
			}
		}
		if(!isOneMoveAble && !isTwoMoveAble){
			if(show){
				Toast.makeText(context, "表格1,2 都不能移动了  已经结束", Toast.LENGTH_SHORT)
					.show();
			}
			return;
		}
		if(show){
			Toast.makeText(context, "表格"+type+"已经不能移动了", Toast.LENGTH_SHORT)
			.show();
		}
		changeCurrentDate(type, 0.0, -1, -1);
	}
	
	private void changeCurrentDate(int type,Double num,int x,int y){
		getCurrentPointList(type).add(new Point(x, y));
		getCurrentSumList(type).add(num);
	}

	public void notifyDataChanged() {
		if (onDataChanges != null && onDataChanges.size() > 0) {
			for (OnDataChange change : onDataChanges) {
				change.onChange();
			}
		}
	}

	/**
	 * 右边移动一格，如果已经是边缘 程序还原。
	 * 
	 * @return
	 */
	public boolean doRight(boolean show, Context context) {
		if(isOneMoveAble || isTwoMoveAble){
			doRight(show, context,1);
			doRight(show, context,2);
			results.add(2);
			notifyDataChanged();
			return true;
		}else{
			if(show){
				Toast.makeText(context, "表格1,2 都不能移动了  已经结束", Toast.LENGTH_SHORT)
					.show();
			}
			return false;
		}
	}
	
private void doRight(boolean show, Context context, int type) {
		
		if(type== 1?isOneMoveAble:isTwoMoveAble){
			Point point = getCurrentPoint(type);
			int y = point.getY(),x = point.getX();
			x++;
			List<Double> valList = getCurrentValueList(type,y);
			if(x >= valList.size()){
				y=0;
				x=0;
				changeCurrentDate(type,valList.get(0)*baseNotify, x, y);
				return;
			}
		}
		if(show){
			Toast.makeText(context, "表格"+type+"已经不能移动了", Toast.LENGTH_SHORT)
			.show();
		}
		changeCurrentDate(type, 0.0, -1, -1);
	}

	public boolean moveBack() {
		if (results.size() > 0) {
			results.remove(results.size() - 1);
			sumListOne.remove(sumListOne.size() - 1);
			sumListTwo.remove(sumListTwo.size() - 1);
			pointsOne.remove(pointsOne.size() - 1);
			pointsTwo.remove(pointsTwo.size() - 1);

			notifyDataChanged();
			return true;
		}
		return false;
	}

	public void clearAll() {
		results.clear();
		sumListOne.clear();
		sumListTwo.clear();
		pointsOne.clear();
		pointsTwo.clear();
		notifyDataChanged();
	}

	/**
	 * @return the baseNotify
	 */
	public int getBaseNotify() {
		return baseNotify;
	}

	/**
	 * @param baseNotify
	 *            the baseNotify to set
	 */
	public void setBaseNotify(int baseNotify) {
		this.baseNotify = baseNotify;
	}

	/**
	 * @return the results
	 */
	public List<Integer> getResults() {
		return results;
	}

	/**
	 * @param results
	 *            the results to set
	 */
	public void setResults(List<Integer> results) {
		this.results = results;
	}

}
