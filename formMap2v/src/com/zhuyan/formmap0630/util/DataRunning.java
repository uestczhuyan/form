/**
 * CopyRight   2013 ZhuYan
 * @auther BLUE
 *
 * All right reserved
 *
 * Created on 2015-6-17 下午9:37:22
 * 
 */
package com.zhuyan.formmap0630.util;

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
	private List<Double> sumList = new ArrayList<Double>();
	private List<Point> points = new ArrayList<Point>();
	private Point nowPoint = null;

	public final static int MAX_PY = 49;
	private int baseNotify = 0;
	public static Map<Integer, List<Double>> map = MapInitUtil.initMap();

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
	public Double getSum() {
		double sum = 0;
		for (Double num : sumList) {
			sum += num;
		}
		return sum;
	}

	/**
	 * 错误直接往下一行的第一个跳转，直到最后一行
	 */
	public boolean doWrong(boolean show, Context context) {
		int px = nowPoint.getX(), py = nowPoint.getY();
		if (py == MAX_PY) {
			if (py >= MAX_PY) {
				if (show) {
					Toast.makeText(context, "无法往下移动", Toast.LENGTH_SHORT)
							.show();
				}
				return false;
			}
		} else {
			py++;
			px = 0;
		}

		results.add(1);
		sumList.add((-1) * baseNotify
				* MapInitUtil.getValueInPox(nowPoint, map));

		nowPoint = new Point(px, py);
		points.add(nowPoint);
		notifyDataChanged();
		return true;
	}

	public void notifyDataChanged() {
		if (onDataChanges != null && onDataChanges.size() > 0) {
			for (OnDataChange change : onDataChanges) {
				change.onChange();
			}
		}
	}

	/**
	 * 正确x+1</br> 如果x+1 >= list.size(), 那么py回退。</br> py奇数 退7 偶数退4
	 * 
	 * @return
	 */
	public boolean doRight(boolean show, Context context) {
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

		results.add(2);
		sumList.add(baseNotify * MapInitUtil.getValueInPox(nowPoint, map));

		nowPoint = new Point(px, py);
		points.add(nowPoint);
		notifyDataChanged();
		return true;
	}

	public boolean moveBack() {
		if (results.size() > 0) {
			System.out.println(results.size() + "  " + sumList.size() + "  "
					+ points.size());
			results.remove(results.size() - 1);
			sumList.remove(sumList.size() - 1);
			points.remove(points.size() - 1);

			nowPoint = points.get(points.size() - 1);

			notifyDataChanged();
			return true;
		}
		return false;
	}

	public void clearAll() {
		results.clear();
		sumList.clear();
		points.clear();
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

}
