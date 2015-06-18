/**
 * CopyRight   2013 ZhuYan
 * @auther BLUE
 *
 * All right reserved
 *
 * Created on 2015-6-17 下午9:37:22
 * 
 */
package com.zhuyan.formmap0603.util;

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

	public final static int MAX_PY = 39;
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
	 * 如果左边有值 向左边移动，否则向下移动</br> 特殊情况，px = 1时候，前面一部并且是正确值，直接向下移动
	 */
	public boolean doWrong(boolean show, Context context) {
		int px = nowPoint.getX(), py = nowPoint.getY();
		if (px == 1 && results.size() > 0
				&& results.get(results.size() - 1) == 2) {
			if (py >= MAX_PY) {
				if (show) {
					Toast.makeText(context, "无法往下移动", Toast.LENGTH_SHORT)
							.show();
				}
				return false;
			}
			py++;
			px = 0;
		} else if (px > 0) {
			px--;
		} else {
			if (py >= MAX_PY) {
				if (show) {
					Toast.makeText(context, "无法往下移动", Toast.LENGTH_SHORT)
							.show();
				}
				return false;
			}
			px = 0;
			py++;
		}

		results.add(1);
		// sum = sum - baseNotify * MapInitUtil.getValueInPox(oldpy, oldpx,
		// map);
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
	 * 右边移动一格，如果已经是边缘 程序还原。
	 * 
	 * @return
	 */
	public boolean doRight(boolean show, Context context) {
		int px = nowPoint.getX(), py = nowPoint.getY();
		List<Double> list = map.get(py);

		if (list == null || list.size() <= 0) {
			if (show) {
				Toast.makeText(context, "数据出错：py=" + py, Toast.LENGTH_SHORT)
						.show();
			}
			return false;
		}
		if (list.size() - 1 > px) {
			px++;
		} else {
			px = 0;
			py = 0;
		}
		results.add(2);
		// sum = sum + baseNotify * MapInitUtil.getValueInPox(oldpy, oldpx,
		// map);
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
