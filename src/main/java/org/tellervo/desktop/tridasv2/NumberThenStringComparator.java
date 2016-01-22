package org.tellervo.desktop.tridasv2;

import java.util.Comparator;

public class NumberThenStringComparator implements Comparator<Object> {

	@Override
	public int compare(Object oo1, Object oo2) {
		boolean isFirstNumeric, isSecondNumeric;
		String o1 = oo1.toString(), o2 = oo2.toString();


		isFirstNumeric = o1.matches("\\d+");
		isSecondNumeric = o2.matches("\\d+");

		if (isFirstNumeric) {
			if (isSecondNumeric) {
				return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
			} else {
				return -1; // numbers always smaller than letters
			}
		} else {
			if (isSecondNumeric) {
				return 1; // numbers always smaller than letters
			} else {
				isFirstNumeric = o1.split("[^0-9]")[0].matches("\\d+");
				isSecondNumeric = o2.split("[^0-9]")[0].matches("\\d+");

				if (isFirstNumeric) {
					if (isSecondNumeric) {
						int intCompare = Integer.valueOf(o1.split("[^0-9]")[0]).compareTo(Integer.valueOf(o2.split("[^0-9]")[0]));
						if (intCompare == 0) {
							return o1.compareToIgnoreCase(o2);
						}
						return intCompare;
					} else {
						return -1; // numbers always smaller than letters
					}
				} else {
					if (isSecondNumeric) {
						return 1; // numbers always smaller than letters
					} else {
						return o1.compareToIgnoreCase(o2);
					}
				}
			}
		}
	}
}


