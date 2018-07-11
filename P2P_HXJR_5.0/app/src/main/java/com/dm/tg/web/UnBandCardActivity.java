/*
 * 文 件 名:  UnBandCardActivity.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2015年1月9日
 */
package com.dm.tg.web;

import android.os.Bundle;

/**
 * 解除绑定银行卡
 * 
 * @author  jiaohongyun
 * @version  [版本号, 2015年1月9日]
 */
public class UnBandCardActivity extends BaseWebViewActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    
    /** {@inheritDoc} */
    
    @Override
    protected String setStartUrl()
    {
        return getIntent().getStringExtra("unbankBankUrl");
    }
}