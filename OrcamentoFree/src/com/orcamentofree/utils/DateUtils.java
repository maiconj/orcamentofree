package com.orcamentofree.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public String getNewDate() {
		String data = "";

		try {
			Format df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Date today = Calendar.getInstance().getTime();
			data = df.format(today);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

}
