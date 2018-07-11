package com.hxjr.p2p.ad5.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.bean.RegexInfo;
import com.hxjr.p2p.ad5.service.RegisterInfoService;

/**
 * 格式转换
 * 
 * @author jiaohongyun
 *
 */
public class FormatUtil
{
	
	/**
	* 格式化金钱
	* 
	* @param money
	* @return
	*/
	public static String formatMoney(Double money)
	{
		String result = "";
		DecimalFormat df = new DecimalFormat("0.00");
		if (DMConstant.DigitalConstant.TENTHOUSAND <= money && money < DMConstant.DigitalConstant.HUNDREDMILLION)
		{
			
			result = df.format(money / DMConstant.DigitalConstant.TENTHOUSAND) + "万元";
		}
		//		else if (DMConstant.DigitalConstant.MILLION <= money && money < DMConstant.DigitalConstant.HUNDREDMILLION)
		//		{
		//			result = df.format(money / DMConstant.DigitalConstant.MILLION) + "百万";
		//		}
		else if (money >= DMConstant.DigitalConstant.HUNDREDMILLION)
		{
			result = df.format(money / DMConstant.DigitalConstant.HUNDREDMILLION) + "亿";
		}
		else
		{
			result = df.format(money) + "元";
		}
		return result;
	}

	/**
	 * 格式化金钱
	 *
	 * @param money
	 * @return
	 */
	public static String formatMoney2(Double money)
	{
		String result = "";
		DecimalFormat df = new DecimalFormat("0.00");
		if (DMConstant.DigitalConstant.TENTHOUSAND <= money && money < DMConstant.DigitalConstant.HUNDREDMILLION)
		{

			result = (int)(money / DMConstant.DigitalConstant.TENTHOUSAND) + "";
		}
		//		else if (DMConstant.DigitalConstant.MILLION <= money && money < DMConstant.DigitalConstant.HUNDREDMILLION)
		//		{
		//			result = df.format(money / DMConstant.DigitalConstant.MILLION) + "百万";
		//		}
		else if (money >= DMConstant.DigitalConstant.HUNDREDMILLION)
		{
			result = df.format(money / DMConstant.DigitalConstant.HUNDREDMILLION) + "亿";
		}
		else
		{
			result = money + "";
		}
		return result;
	}

	/**
	 * 转换还款方式
	 * 
	 * @param paymentType
	 * @return
	 */
	public static String formatPaymentType(String paymentType)
	{
		String result = "";
		if (paymentType.equals("DEBX"))
		{
			result = "等额本息";
		}
		else if (paymentType.equals("MYFX"))
		{
			result = "每月付息,到期还本";
		}
		else if (paymentType.equals("YCFQ"))
		{
			result = "本息到期一次付清";
		}
		else if (paymentType.equals("DEBJ"))
		{
			result = "等额本金";
		}
		return result;
	}
	
	/**
	 * 投资进度
	 * 
	 * @param amount
	 * @param remainAmount
	 * @return
	 */
	public static double getBidProgress(String amount, String remainAmount)
	{
		double result = 0;
		double a = Double.valueOf(amount);
		double b = Double.valueOf(remainAmount);
		double c = (a - b) * 100d / a;
		if (c <= 0)
		{
			result = 0;
		}
		else if (c < 1)
		{
			result = (int)Math.ceil(c);
		}
		else
		{
			BigDecimal bd=new BigDecimal(c);
			BigDecimal bd1=bd.setScale(2, BigDecimal.ROUND_DOWN);
			result = bd1.doubleValue();
		}
		return result;
	}
	
	public static String getQiyeOrGeRen(int type)
	{
		String result = "";
		switch (type)
		{
			case 1:
				result = "企";
				break;
			case 2:
				result = "个";
				break;
			default:
				break;
		}
		return result;
	}
	
	/**
	 * 剩余时间
	 * 
	 * @param end
	 * @return
	 */
	public static long getRemainTime(String end)
	{
		long result = 0L;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		try
		{
			Date date = sdf.parse(end);
			Date now = new Date();
			result = date.getTime() - now.getTime() + DMApplication.getInstance().diffTime;
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 转换标的状态
	 * 
	 * @param status
	 * @return
	 */
	public static String convertBidStatus(String status)
	{
		String result = "";
		// 标的状态
		if (status.equals("SQZ"))
		{
			result = "申请中";
		}
		else if (status.equals("DSH"))
		{
			result = "待审核";
		}
		else if (status.equals("DFB"))
		{
			result = "待发布";
		}
		else if (status.equals("YFB"))
		{
			result = "预发布";
		}
		else if (status.equals("TBZ"))
		{
			result = "投标中";
		}
		else if (status.equals("DFK"))
		{
			result = "待放款";
		}
		else if (status.equals("HKZ"))
		{
			result = "还款中";
		}
		else if (status.equals("YJQ"))
		{
			result = "已结清";
		}
		else if (status.equals("YLB"))
		{
			result = "已流标";
		}
		else if (status.equals("YDF"))
		{
			result = "已垫付";
		}
		else if (status.equals("YZF"))
		{
			result = "已作废";
		}
		return result;
	}
	
	/**
	 * 转换成中国货币格式
	 * @param d
	 * @return
	 */
	public static String getCurrency(double d)
	{
		//使用本地默认格式输出货币值
		return getCurrency(d, Locale.CHINA);
	}
	
	/**
	 * 转换成某地区货币格式
	 * @param d
	 * @param locale
	 * @return
	 */
	public static String getCurrency(double d, Locale locale)
	{
		//使用本地默认格式输出货币值
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
		String result = currencyFormat.format(d);
		if (result.startsWith("-￥"))
		{
			//如果是负值java 默认格式 为-￥1000
			result = result.replace("-￥", "￥-");
		}
		return result;
	}
	
	/**
	 * 转换成中国百分数格式
	 * @param d
	 * @param locale
	 * @return
	 */
	public static String getPercent(double d)
	{
		return getPercent(d, Locale.CHINA);
	}
	
	/**
	 * 转换成某地百分数格式
	 * @param d
	 * @param locale
	 * @return
	 */
	public static String getPercent(double d, Locale locale)
	{
		//使用本地默认格式输出货币值
		NumberFormat currencyFormat = NumberFormat.getPercentInstance(locale);
		//保留4位小数
		currencyFormat.setMaximumFractionDigits(4);
		return currencyFormat.format(d);
	}
	
	/**
	 * 转换成形如23.12%的百分数格式
	 * @param d
	 * @param locale
	 * @return
	 */
	public static String getDMPercent(double d)
	{
		double temp = d * 100;
		return String.format("%.2f", temp) + "%";
	}
	
	/**
	* 获取两位小数点的double
	*/
	public static double get2Double(double a)
	{
		DecimalFormat df = new DecimalFormat("0.00");
		return Double.valueOf(df.format(a).toString());
	}
	
	/**
	* 获取两位小数点的String
	*/
	public static String get2String(double a)
	{
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(a);
	}
	
	/**
	 * 四舍五入（保留两位小数）
	 */
	public static double getRound(double a)
	{
		BigDecimal b = new BigDecimal(a);
		return b.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
	
	/**
	 * 检查用户名
	 */
	public static boolean validateUserName(String userName)
	{
		return matches("^[A-Za-z][A-Za-z0-9_]{5,17}$", userName);
	}
	
	/**
	 * 检查登录密码（6-18位任意字符）
	 * 登录密码支持数字、字母（区分大小写）、符号，必须至少包含当中的任意两种
	 */
	public static boolean validateLoginPwd(String pwd)
	{
		//		return matches("^[\\w\\W]{6,16}$", pwd);
		return matches("(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^.{6,18}$", pwd);
	}
	
	/**
	 * 检查交易密码（8-16位任意字符）
	 * 交易密码由8-16位的字母+数字组成
	 */
	public static boolean validateDealPwd(String pwd)
	{
		return matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$", pwd);
	}
	
	/**
	 * 检验数字输入框大于零且不能为"-"或"+"
	 */
	public static boolean validateEditParams(String editStr)
	{
		return matches("^(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$", editStr);
	}
	
	/**
	 * 检测真实姓名格式
	 */
	public static boolean validateRealName(String name)
	{
		return matches("^[\\u4e00-\\u9fa5]*$", name);
	}
	
	/**
	 * 检测邮箱验证格式
	 */
	public static boolean validateEmail(String email)
	{
		String EMAIL_MATCH_RULE =
			"^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		
		return matches(EMAIL_MATCH_RULE, email);
	}
	
	/**
	 * 验证手机号码
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles)
	{
//		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
		Pattern p = Pattern.compile("[1][3-9]\\d{9}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	public static boolean validateMoney(String money)
	{
		return matches("^([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(.[0-9]{1,2})?$", money);
	}
	
	public static boolean checkIdCard(String idCard)
	{
		//		if (matches("/^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$/",
		//			idCard))
		//		{
		/*
		 * 身份证15位编码规则：dddddd yymmdd xx p
		 * dddddd：6位地区编码
		 * yymmdd: 出生年(两位年)月日，如：910215
		 * xx: 顺序编码，系统产生，无法确定
		 * p: 性别，奇数为男，偶数为女
		 *
		 * 身份证18位编码规则：dddddd yyyymmdd xxx y
		 * dddddd：6位地区编码
		 * yyyymmdd: 出生年(四位年)月日，如：19910215
		 * xxx：顺序编码，系统产生，无法确定，奇数为男，偶数为女
		 * y: 校验码，该位数值可通过前17位计算获得
		 *
		 * 前17位号码加权因子为 Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ]
		 * 验证位 Y = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ]
		 * 如果验证码恰好是10，为了保证身份证是十八位，那么第十八位将用X来代替
		 * 校验位计算公式：Y_P = mod( ∑(Ai×Wi),11 )
		 * i为身份证号码1...17 位; Y_P为校验码Y所在校验码数组位置
		 */
		
		//将前17位加权因子保存在数组里
		int[] idCardWi = new int[] {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
		//这是除以11后，可能产生的11位余数、验证码，也保存成数组
		int[] idCardY = new int[] {1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2};
		int idCardWiSum = 0; //用来保存前17位各自乖以加权因子后的总和
		for (int i = 0; i < 17; i++)
		{
			idCardWiSum += Integer.parseInt(idCard.substring(i, i + 1)) * idCardWi[i];
		}
		int idCardMode = idCardWiSum % 11; //计算出校验码所在数组的位置
		String idCardLast = idCard.substring(17);//得到最后一位身份证号码
		//如果等于2，则说明校验码是10，身份证号码最后一位应该是X
		if (idCardMode == 2)
		{
			if ("X".equals(idCardLast) || "x".equals(idCardLast))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			//用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码
			if (idCardLast.equals(idCardY[idCardMode] + ""))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		//		}
		//		else
		//		{
		//			return false;
		//		}
	}
	
	/**
	 * 正则表达式检查
	 * @param regex
	 * @param src
	 * @return
	 */
	public static boolean matches(String regex, String src)
	{
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(src);
		return matcher.matches();
	}
	
	/***
	 * 检测银行卡卡号是否正确
	 * @param cardId
	 * @return
	 */
	public static boolean checkBankCard(String cardId)
	{
		if (cardId.length() > 19 || cardId.length() < 16)
		{
			return false;
		}
		int oddsum = 0; //奇数求和
		int evensum = 0; //偶数求和
		int allsum = 0;
		int cardNoLength = cardId.length();
		int lastNum = Integer.parseInt(cardId.substring(cardId.length() - 1, cardId.length()));
		cardId = cardId.substring(0, cardId.length() - 1);
		for (int i = cardNoLength - 1; i >= 1; i--)
		{
			String ch = cardId.substring(i - 1, i);
			int tmpVal = Integer.parseInt(ch);
			if (cardNoLength % 2 == 1)
			{
				if ((i % 2) == 0)
				{
					tmpVal *= 2;
					if (tmpVal >= 10)
					{
						tmpVal -= 9;
					}
					evensum += tmpVal;
				}
				else
				{
					oddsum += tmpVal;
				}
			}
			else
			{
				if ((i % 2) == 1)
				{
					tmpVal *= 2;
					if (tmpVal >= 10)
					{
						tmpVal -= 9;
					}
					evensum += tmpVal;
				}
				else
				{
					oddsum += tmpVal;
				}
			}
		}
		
		allsum = oddsum + evensum;
		allsum += lastNum;
		if ((allsum % 10) == 0)
			return true;
		else
			return false;
	}
	
	/**
	 * 清除html标签
	 * @param inputString
	 * @return
	 */
	public static String Html2Text(String inputString)
	{
		String htmlStr = inputString; //含html标签的字符串
		String textStr = "";
		Pattern p_script;
		Matcher m_script;
		Pattern p_style;
		Matcher m_style;
		Pattern p_html;
		Matcher m_html;
		
		try
		{
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
			String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式
			
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); //过滤script标签
			
			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); //过滤style标签
			
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); //过滤html标签
			
			textStr = htmlStr;
		}
		catch (Exception e)
		{
		}
		return textStr;//返回文本字符串
	}
	
	/**
	 * 保留2位小数
	 * @return
	 */
	public static String formatStr2(String src)
	{
		if (TextUtils.isEmpty(src) || src.equals("null"))
		{
			return "0.00";
		}
		else
		{
			BigDecimal b = new BigDecimal(src);
			double temp = b.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
			DecimalFormat df = new DecimalFormat("#0.00");
			return df.format(temp);
		}
	}
	
	/**
	 * 保留3位小数
	 * @return
	 */
	public static String formatStr3(String src)
	{
		if (TextUtils.isEmpty(src) || src.equals("null"))
		{
			return "0.000";
		}
		else
		{
			BigDecimal b = new BigDecimal(src);
			double temp = b.setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
			DecimalFormat df = new DecimalFormat("#0.000");
			return df.format(temp);
		}
	}
	
	/**
	 * 初始化交易密码验证规则
	 */
	public static RegexInfo initRegexInfo(Context context)
	{
		RegexInfo regexInfo = new RegexInfo();
		regexInfo.setRegisterFlage("false");
		regexInfo.setNewUserNameRegex((String)SharedPreferenceUtils.get(context,
			SharedPreferenceUtils.REGEXINFO_FILE_NAME,
			"newUserNameRegex",
			"^[A-Za-z][\\w]{5,17}$"));
		regexInfo.setUserNameRegexContent((String)SharedPreferenceUtils.get(context,
			SharedPreferenceUtils.REGEXINFO_FILE_NAME,
			"userNameRegexContent",
			"用户名6-18个字符，可使用字母、数字、下划线，需以字母开头"));
		regexInfo.setNewPasswordRegex((String)SharedPreferenceUtils.get(context,
			SharedPreferenceUtils.REGEXINFO_FILE_NAME,
			"newPasswordRegex",
			"^(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^.{6,18}$"));
		regexInfo.setPasswordRegexContent((String)SharedPreferenceUtils.get(context,
			SharedPreferenceUtils.REGEXINFO_FILE_NAME,
			"passwordRegexContent",
			"密码6-18个字符，至少包含数字、大写字母、小写字母、符号中的2种"));
		regexInfo.setTxPwdRegex((String)SharedPreferenceUtils.get(context,
			SharedPreferenceUtils.REGEXINFO_FILE_NAME,
			"txPwdRegex",
			"^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$"));
		regexInfo.setTxPwdContent((String)SharedPreferenceUtils.get(context,
			SharedPreferenceUtils.REGEXINFO_FILE_NAME,
			"txPwdContent",
			"交易密码由8-16位的字母+数字组成"));
		boolean isNetConfig =
			(Boolean)SharedPreferenceUtils.get(context, SharedPreferenceUtils.REGEXINFO_FILE_NAME, "isNetConfig", false);
		if (!isNetConfig)
		{
			context.startService(new Intent(context, RegisterInfoService.class));
		}
		DMApplication.getInstance().setRegexInfo(regexInfo);
		return DMApplication.getInstance().getRegexInfo();
	}
	
	/**
	 * 验证URL是否合法，必须带http\https才认为合法，URL可带端口
	 * 
	 * @param url
	 * @return
	 */
	public static boolean validateUrl(String url)
	{
		String regEx = "^(http|https|ftp|view|status)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-"
	           + "Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"   
	           + "2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}"   
	           + "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|"   
	           + "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-"   
	           + "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"   
	           + "-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/"   
	           + "[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*$";
		return matches(regEx, url);
	}
}
