package com.hxjr.p2p.ad5.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class AmountUtil
{
	static DecimalFormat df = new DecimalFormat("##,####,###,##0.00");
	
	static DecimalFormat vd = new DecimalFormat("###########0");
	
	static DecimalFormat vd2 = new DecimalFormat("###########0.00");
	
	public static final DecimalFormat RATE_FORMAT = new DecimalFormat("#0.00");
	
	static final DecimalFormat RATE_FORMAT_2 = new DecimalFormat("#0.00");
	
	public static final DecimalFormat DT = new DecimalFormat("0.00");
	
	public static double round(double amount)
	{
		return (Math.round(amount * 100.0D) / 100.0D);
	}
	
	public static double round(double amount, int i)
	{
		double d = Math.pow(10.0D, i);
		return (Math.round(amount * d) / d);
	}
	
	public static String format(double amount)
	{
		amount = round(amount);
		return df.format(amount);
	}
	
	public static String format2(double amount)
	{
		amount = round(amount);
		return vd2.format(amount);
	}
	
	/**
	 * 格式化金额到千分位
	 * 
	 * @param amount
	 * @return
	 */
	public static String numKbPointFormat(double amount)
	{
		amount = round(amount);
		return df.format(amount);
	}
	
	public static String formatShow(double amount)
	{
		amount = round(amount);
		return vd.format(amount);
	}
	
	public static String formatRate(double rate)
	{
		return RATE_FORMAT.format(rate * 100.0D) + "%";
	}
	
	public static String formatRate2(double rate)
	{
		return RATE_FORMAT_2.format(rate * 100.0D);
	}
	
	public static String formatAmount(double amount)
	{
		String str = round(amount) + "";
		if (amount >= 10000.0D)
			str = (int)(amount / 10000.0D) + "??";
		else if (amount >= 1000.0D)
			str = str.substring(0, 1) + "," + str.substring(1, str.length());
		else
		{
			str = round(amount) + "";
		}
		return str;
	}
	
	public static int compare(double amount1, double amount2, int precision)
	{
		double d1 = round(amount1, precision);
		double d2 = round(amount2, precision);
		return ((d1 < d2) ? -1 : (d1 > d2) ? 1 : 0);
	}
	
	public static int compare(double amount1, double amount2)
	{
		double d1 = round(amount1);
		double d2 = round(amount2);
		return ((d1 < d2) ? -1 : (d1 > d2) ? 1 : 0);
	}
	
	public static void main(String[] args)
	{
		//		double amount = 12345.219999999999D;
		//		double rate = 12.5D;
		//		System.out.println((int)rate);
		//		System.out.println("AmountUtil.format(" + amount + ") => " + format(amount));
		//		System.out.println("AmountUtil.format2(" + amount + ") => " + format2(amount));
		//		System.out.println("AmountUtil.formatShow(" + amount + ") => " + formatShow(1234.2333000000001D));
		//		System.out.println("AmountUtil.formatAmount(" + amount + ") => " + formatAmount(amount));
		//		System.out.println("AmountUtil.formatRate2(" + rate + ") => " + formatRate2(rate) + "%");
		//		System.out.println("**************** " + getString("1.0"));
		//		System.out.println("**************** " + getString("1"));
		//		System.out.println("**************** " + getString("1.03"));
	}
	
	/**
	 * 字符串保留后两位小数, 并去掉逗号
	 * @param source
	 * @return
	 */
	public static String getString(String source)
	{
		if (source == null || "".equals(source))
		{
			source = "0.00";
		}
		source = source.replace(",", "");
		source = source.replace(" ", "");
		BigDecimal b = new BigDecimal(Double.valueOf(source));
		double temp = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(temp);
	}
}