package com.zhuyan.formmap0630.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapInitUtil {

	public static double getValueInPox(Point point,
			Map<Integer, List<Double>> map) {
		if (map != null && point != null) {
			List<Double> list = map.get(point.getY());
			if (list != null && list.size() > point.getX()) {
				return list.get(point.getX());
			}
		}
		return 0.0;
	}

	public static Map<Integer, List<Double>> initMap() {
		Map<Integer, List<Double>> map = new HashMap<Integer, List<Double>>();

		for (int i = 0; i <= DataRunning.MAX_PY; i++) {
			List<Double> list = new ArrayList<Double>();
			double first = 1;
			int k = i + 1;
			if (k > 4) {
				first = k / 4 + 1;
				if (k % 4 == 3) {
					first += 0.5;
				} else if (k % 4 == 0) {
					first -= 0.5;
				}
			} else {
				first = 1;
			}

			int size = k % 2 == 1 ? 2 : 3;
			list.add(first);
			for (int j = 1; j < size; j++) {
				first = first * 2;
				list.add(first);
			}
			map.put(i, list);
		}

		return map;
	}

	private static List getArrayList(Double... floats) {
		if (floats != null && floats.length > 0) {
			ArrayList<Double> arrayList0 = new ArrayList<Double>(floats.length);
			for (Double d : floats) {
				arrayList0.add(d);
			}
			return arrayList0;
		} else {
			return Collections.EMPTY_LIST;
		}
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
