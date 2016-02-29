package hhm.slate.util;

import java.text.DecimalFormat;

public class DataUtil {

	public String getM2(String f) {
		try {
			Double d = Double.parseDouble(f);
			DecimalFormat df = new DecimalFormat("#0.00");
			return df.format(d);

		} catch (Exception e) {
			return "";
		}

	}
}
