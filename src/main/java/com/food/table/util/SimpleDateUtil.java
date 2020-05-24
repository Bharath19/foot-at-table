package com.food.table.util;

import java.text.SimpleDateFormat;

public class SimpleDateUtil {

	public static String dateFormat = "yyyy-MM-dd";

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

	public static String toDate(java.util.Date date) {
		return simpleDateFormat.format(date).toString();
	}
}
