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

import android.content.Context;


/**
 * @author zy
 * 
 *         Create on 2015-6-17 ÏÂÎç9:37:22
 */
public class DataRunning {

	private static DataRunning INSTANCES;


	public final static int MAX_PY_3 = 49;
	public final static int MAX_PY_2 = 20;
	public final static int MAX_PY_1 = 39;
	
	private List<DataUnit> dataUnits ;
	private List<Integer> results = new ArrayList<Integer>();
	public  int baseNotify = 0;
	private List<OnDataChange> onDataChanges = null;
	
	

	public interface OnDataChange {
		void onChange();
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
	 * @param map
	 */
	private  DataRunning() {
	}

	public static DataRunning getInstance() {
		if (INSTANCES == null) {
			INSTANCES = new DataRunning();
			List<DataUnit> list = new ArrayList<DataUnit>(3);
			list.add(new DataUnitOne(MapInitUtil.initMap1()));
			list.add(new DataUnitTwo(MapInitUtil.initMapTwo()));
			list.add(new DataUnitTree(MapInitUtil.initMap3()));
			
			INSTANCES.setDataUnits(list);
		}
		return INSTANCES;
	}
	

	/**
	 * @return the dataUnits
	 */
	public List<DataUnit> getDataUnits() {
		return dataUnits;
	}

	/**
	 * @param dataUnits the dataUnits to set
	 */
	public void setDataUnits(List<DataUnit> dataUnits) {
		this.dataUnits = dataUnits;
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

	public boolean doWrong(boolean show, Context context) {
		
		if(checkMoveAble()){
			for(DataUnit unit:dataUnits){
				unit.doWrong(show, context);
			}
			results.add(1);
			System.out.println("add 1");
			notifyDataChanged();
		}
		return checkMoveAble();
	}
	
	public boolean doRight(boolean show, Context context) {
		if(checkMoveAble()){
			for(DataUnit unit:dataUnits){
				unit.doRight(show, context);
			}
			results.add(2);
			System.out.println("add 2");
			notifyDataChanged();
		}
		return checkMoveAble();
	}

	public void notifyDataChanged() {
		if (onDataChanges != null && onDataChanges.size() > 0) {
			for (OnDataChange change : onDataChanges) {
				change.onChange();
			}
		}
	}

	

	public boolean moveBack() {
		notifyDataChanged();
		return true;
	}

	public void clearAll() {
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
	 * 
	 */
	public void InitStartPoint() {
		
		for(DataUnit unit:dataUnits){
			unit.setNowPoint(new Point(0, 0));
			unit.getPoints().add(unit.getNowPoint());
		}
	}
	
	private boolean checkMoveAble(){
		for(DataUnit unit:dataUnits){
			if(unit.isMoveAble){
				return true;
			}
		}
		return false;
	}

}
