package com.dm.android.pay;

import java.util.ArrayList;
import java.util.List;

public enum PaymentType
{
	ALLINPAY(100, "通联支付", ""), 
	SHUANGQIAN(200, "双乾支付", ""), 
	CHINAPNR(300, "汇付天下", ""), 
	TENPAY(400, "财付通", ""), 
	HUICHAO(500, "汇潮托管", ""), 
	FUYOU(600, "富有托管", ""), 
	BAOFU(700, "宝付支付", ""), 
	HUICHAOGATE(800, "汇潮网关支付",""),
	LIANLIANGATE(900, "连连支付", ""), 
	DINPAY(1000,"智付支付", ""), 
	CHINABANK(1100, "网银在线支付", ""), 
	KJTPAY(1200, "快捷通支付", ""), 
	HEEPAY(1300, "汇付宝支付", ""), 
	SINAPAY(1400, "新浪网关支付", ""),
	GFBPAY(1500,"国付宝支付", ""),
	BOCOM(1600,"交通银行支付", ""),
	BAOFUTG(1700,"宝付托管",""),
	LIANLIANAUTHGATE(1800, "连连认证支付", ""),
	YEEPAYTG(1900, "易宝支付托管", ""),
	HUANXUN(2000, "环迅支付托管", ""),
	UMPAY(1910,"联动优势托管",""),
	YINLIANPAY(2100,"银联支付",""),
	YIFUPAY(2300,"易宝支付",""),
	ZHONGXIN(3000,"中信银行托管",""),
	CIB(2900,"兴业银行支付代付",""),
	CIBQUICK(2901,"兴业银行快捷支付",""),
	KUAIQIAN(3100,"快钱快捷支付","");
																					
	private int institutionCode;
	
	private String chineseName;
	
	private String serverMode;	
	
	private PaymentType(int institutionCode, String chineseName, String serverMode)
	{
		this.institutionCode = institutionCode;
		this.chineseName = chineseName;
		this.serverMode = serverMode;
	}

	public int getInstitutionCode()
	{
		return institutionCode;
	}
	
	public String getChineseName()
	{
		return chineseName;
	}
	
	public String getServerMode()
	{
		return serverMode;
	}

	public void setServerMode(String serverMode)
	{
		this.serverMode = serverMode;
	}

	public static final String getDescription(int code)
	{
		for (PaymentType paymentInstitution : PaymentType.values())
		{
			if (paymentInstitution.getInstitutionCode() == code)
			{
				return paymentInstitution.getChineseName();
			}
		}
		return null;
	}
	
	public static final List<String> getCodes(String name)
	{
		List<String> list = new ArrayList<String>();
		for (PaymentType paymentInstitution : PaymentType.values())
		{
			if (paymentInstitution.getChineseName().contains(name))
			{
				list.add(paymentInstitution.getInstitutionCode() + "");
			}
		}
		return list;
	}
}
