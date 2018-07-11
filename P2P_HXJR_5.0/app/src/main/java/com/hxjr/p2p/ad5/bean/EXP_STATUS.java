package com.hxjr.p2p.ad5.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 体验金的状态
 * 
 * @author  huangkaibo
 * @date 2015年12月3日
 */
//'使用状态：已过期：''YGQ'',未使用：''WSY'',已委托：''YWT'',已投资：''YTZ'',已结清：''YJQ''',
public enum EXP_STATUS implements Parcelable
{
	WSY("WSY", "未使用"), YGQ("YGQ", "已过期"), YTZ("YTZ", "已投资"), YJQ("YJQ", "已结清"), YWT("YWT", "使用中");
	
	String name;
	
	String value;
	
	private EXP_STATUS(String name, String value)
	{
		this.name = name;
		this.value = value;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}
	
	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		// TODO Auto-generated method stub
		dest.writeInt(ordinal());
	}
	
	/** Parcelable.Creator needs by Android. */
	public static final Creator<EXP_STATUS> CREATOR = new Creator<EXP_STATUS>()
	{
		
		@Override
		public EXP_STATUS createFromParcel(Parcel source)
		{
			return EXP_STATUS.values()[source.readInt()];
		}
		
		@Override
		public EXP_STATUS[] newArray(int size)
		{
			return new EXP_STATUS[size];
		}
	};
}
