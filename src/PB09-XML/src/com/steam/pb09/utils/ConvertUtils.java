package com.steam.pb09.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class ConvertUtils {

	public final static String fmtDate(String y, String m, String d) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Integer.parseInt(y), Integer.parseInt(m) - 1, Integer
				.parseInt(d));
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}

	public final static String fmtDollar(String dollar) {
		String dolVal = dollar;
		dolVal = StringUtils.replace(dolVal, "*", "");
		dolVal = StringUtils.replace(dolVal, ",", "");
		return new DecimalFormat("0.00").format(new BigDecimal(dolVal));
	}

	public final static String roc2dcDashStyle(String rocDate) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").format(roc2dc(rocDate));
		} catch (Exception e) {
			throw new IllegalStateException(rocDate, e);
		}
	}

	public final static Date roc2dc(String rocDate) {
		int date = Integer.parseInt(StringUtils.trim(rocDate.substring(rocDate
				.length() - 2, rocDate.length())));
		int mon = Integer.parseInt(StringUtils.trim(rocDate.substring(rocDate
				.length() - 4, rocDate.length() - 2)));
		int year = Integer.parseInt(StringUtils.trim(rocDate.substring(0,
				rocDate.length() - 4)));
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year + 1911, mon - 1, date);
		return cal.getTime();
	}
}
