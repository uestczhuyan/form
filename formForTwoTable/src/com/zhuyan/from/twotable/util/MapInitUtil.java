package com.zhuyan.from.twotable.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapInitUtil {

	public static Map<Integer, List<Double>> initMapOne() {
		Map<Integer, List<Double>> map = new HashMap<Integer, List<Double>>();

		map.put(0, getArrayList("1"));
		map.put(1, getArrayList("1 1"));
		map.put(2, getArrayList("2	2	2"));
		map.put(3, getArrayList("3	3	3	3"));
		map.put(4, getArrayList("5	5	5	5	5"));
		map.put(5, getArrayList("8	8	8	8	8	8"));
		map.put(6, getArrayList("20	20	20	20	20"));
		map.put(7, getArrayList("50	50	50	50"));
		map.put(8, getArrayList("135 135 135"));
		map.put(9, getArrayList("405	405"));

		return map;
	}

	public static Map<Integer, List<Double>> initMapTwo() {
		Map<Integer, List<Double>> map = new HashMap<Integer, List<Double>>();

		map.put(0, getArrayList("1"));
		map.put(1, getArrayList("1 1"));
		map.put(2, getArrayList("2	2	2"));
		map.put(3, getArrayList("3	3	3	3"));
		map.put(4, getArrayList("5	5	5	5	5"));
		map.put(5, getArrayList("12 12 12 12"));
		map.put(6, getArrayList("33 33 33"));
		map.put(7, getArrayList("100 100"));

		return map;
	}

	private static List getArrayList(String str) {
		String[] strs = str.split("\\s+");
		ArrayList<Double> arrayList0 = new ArrayList<Double>(strs.length);
		for (String s : strs) {
			arrayList0.add(Double.parseDouble(s));
		}
		return arrayList0;
	}
}
