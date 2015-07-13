/**
 * CopyRight   2013 ZhuYan
 * @auther BLUE
 *
 * All right reserved
 *
 * Created on 2015-6-17 ÏÂÎç9:37:22
 * 
 */
package com.zhuyan.formmap0713.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;


/**
 * @author zy
 * 
 *         Create on 2015-6-17 ÏÂÎç9:37:22
 */
public  abstract class DataUnit {

	
	protected Map<Integer, List<Double>> map = MapInitUtil.initMap1();
	
	protected List<Double> sumList = new ArrayList<Double>();
	protected List<Point> points = new ArrayList<Point>();
	protected Point nowPoint = null;
	
	protected boolean isMoveAble;
	
	
	/**
	 * @return the isMoveAble
	 */
	public boolean isMoveAble() {
		return isMoveAble;
	}

	/**
	 * @param isMoveAble the isMoveAble to set
	 */
	public void setMoveAble(boolean isMoveAble) {
		this.isMoveAble = isMoveAble;
	}

	/**
	 * @return the map
	 */
	public Map<Integer, List<Double>> getMap() {
		return map;
	}

	/**
	 * @param map
	 */
	public DataUnit(Map<Integer, List<Double>> map) {
		super();
		this.map = map;
		this.isMoveAble = true;
	}

	/**
	 * @return
	 */
	public Double getSum() {
		double sum = 0;
		for (Double num : sumList) {
			sum += num;
		}
		return sum;
	}
	
	 abstract boolean doWrong(boolean show, Context context);
	 
	 abstract boolean doRight(boolean show, Context context);
	

	public boolean moveBack() {
		if (sumList.size() > 0) {
			sumList.remove(sumList.size() - 1);
		}
		if (points.size() > 0) {
			points.remove(points.size() - 1);
			nowPoint = points.get(points.size() - 1);
			
			isMoveAble = !(nowPoint.getY() == -1);
			return true;
		}
		return false;
	}

	public void clearAll() {
		sumList.clear();
		points.clear();
	}



	/**
	 * @return the sumList
	 */
	public List<Double> getSumList() {
		return sumList;
	}

	/**
	 * @param sumList
	 *            the sumList to set
	 */
	public void setSumList(List<Double> sumList) {
		this.sumList = sumList;
	}

	/**
	 * @return the points
	 */
	public List<Point> getPoints() {
		return points;
	}

	/**
	 * @param points
	 *            the points to set
	 */
	public void setPoints(List<Point> points) {
		this.points = points;
	}

	/**
	 * @return the nowPoint
	 */
	public Point getNowPoint() {
		return nowPoint;
	}

	/**
	 * @param nowPoint
	 *            the nowPoint to set
	 */
	public void setNowPoint(Point nowPoint) {
		this.nowPoint = nowPoint;
	}

	/**
	 * @return
	 */
	public Point getLastPoint() {
		if (points.size() > 2) {
			return points.get(points.size() - 2);
		}
		return null;
	}
	
	protected void changeCurrentDate(Double num,int x,int y){
		points.add(new Point(x, y));
		sumList.add(num);
	}

}
