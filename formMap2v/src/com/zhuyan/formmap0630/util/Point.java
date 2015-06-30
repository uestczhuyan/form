/**
 * CopyRight   2013 ZhuYan
 * @auther BLUE
 *
 * All right reserved
 *
 * Created on 2015-6-17 ÏÂÎç8:49:34
 * 
 */
package com.zhuyan.formmap0630.util;


/**
 * @author zy
 * 
 *         Create on 2015-6-17 ÏÂÎç8:49:34
 */
public class Point {

	private int x;
	private int y;

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * 
	 */
	public Point() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Point [y=" + y + ", x=" + x + "]";
	}

}
