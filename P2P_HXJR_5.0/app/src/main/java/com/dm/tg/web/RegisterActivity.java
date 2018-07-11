/*
 * 文 件 名:  RegisterActivity.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2015年1月6日
 */
package com.dm.tg.web;

import android.os.Bundle;

/**
 * 注册第三方帐号
 * @author  jiaohongyun
 * @version  [版本号, 2015年1月6日]
 */
public class RegisterActivity extends BaseWebViewActivity
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
        return getIntent().getStringExtra("thirdRegistUrlString");
    }
}
