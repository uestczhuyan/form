package com.zhuyan.formmap0713.util;

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
	
	public static Map<Integer, List<Double>> initMap3() {
		Map<Integer, List<Double>> map = new HashMap<Integer, List<Double>>();
		map.put(0, getArrayList(1.0, 2d, 1.5));
		map.put(1, getArrayList(1d));
		map.put(2, getArrayList(1d, 2d));
		map.put(3, getArrayList(1d, 2d));
		map.put(4, getArrayList(1d, 2d, 1.5));
		map.put(5, getArrayList(1d, 2d, 1.5, 1d));
		map.put(6, getArrayList(1d, 2d, 1.5, 1d));
		map.put(7, getArrayList(1.5, 3.0, 2.5));
		map.put(8, getArrayList(1.5, 3.0, 2.5, 2.0));
		map.put(9, getArrayList(1.5, 3.0, 2.5, 2.0, 1.5));
		map.put(10, getArrayList(2.0, 4.0, 3.0, 2.0, 1.0));

		map.put(11, getArrayList("2.5	5	4	3"));
		map.put(12, getArrayList("2.5	5	4	3	2"));
		map.put(13, getArrayList("3	6	5	4	3"));
		map.put(14, getArrayList("3	6	5	4	3	2	"));
		map.put(15, getArrayList("3.5	7	6	5	4	"));
		map.put(16, getArrayList("3.5	7	6	5	4	3"));
		map.put(17, getArrayList("4	8	7	6	5	4"));
		map.put(18, getArrayList("4.5	9	8	7	6	5"));
		map.put(19, getArrayList("5	10	9	8	7	6"));
		map.put(20, getArrayList("5.5	11	10	9	8	7"));

		map.put(21, getArrayList("6	12	11	10	9	8"));
		map.put(22, getArrayList("6.5	13	12	11	10	9"));
		map.put(23, getArrayList("7	14	13	12	11	10"));
		map.put(24, getArrayList("7.5	15	14	13	12	11"));
		map.put(25, getArrayList("8	16	15	14	13	12"));
		map.put(26, getArrayList("8.5	17	16	15	14	13	12"));
		map.put(27, getArrayList("9	18	17	16	15	14	13"));
		map.put(28, getArrayList("9.5	19	18	17	16	15	14"));
		map.put(29, getArrayList("10	20	19	18	17	16	15"));
		map.put(30, getArrayList("10	20	18	28	26	24"));

		map.put(31, getArrayList("11	22	20	31	29	27"));
		map.put(32, getArrayList("12	24	22	34	32	29"));
		map.put(33, getArrayList("13	26	24	37	35	33"));
		map.put(34, getArrayList("14	28	26	40	38	36"));
		map.put(35, getArrayList("15	30	28	43	41	39"));
		map.put(36, getArrayList("16	32	30	46	44	42"));
		map.put(37, getArrayList("17	34	32	49	47	45"));
		map.put(38, getArrayList("18	36	34	52	50	48"));
		map.put(39, getArrayList("19	38	36	55	53	51"));
		// map.put(0, getArrayList());
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

	public static Map<Integer, List<Double>> initMap1() {
		Map<Integer, List<Double>> map = new HashMap<Integer, List<Double>>();

		for (int i = 0; i <= DataRunning.MAX_PY_1; i++) {
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
