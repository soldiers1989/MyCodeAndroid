/*
 * 文 件 名:  DMTReceiver.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2015年1月6日
 */
package com.dm.tg.web;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 托管使用的接收广播
 * @author  jiaohongyun
 * @version  [版本号, 2015年1月6日]
 */
public class DMTReceiver extends BroadcastReceiver
{
    /**
     * 注册第三方帐号
     */
    public static final String REGISTER_ACTION = "com.dm.t.register";
    
    /**
     * 充值
     */
    public static final String RECHARGE_ACTION = "com.dm.t.recharge";
    
    /**
     * 提现
     */
    public static final String WITHDRAW_ACTION = "com.dm.t.withdraw";
    
    /**
     * 投标
     */
    public static final String BUYBID_ACTION = "com.dm.t.buybid";
    
    /**
     * 购买债权
     */
    public static final String BUYCRE_ACTION = "com.dm.t.buycre";
    
    /**
     * 绑定银行卡
     */
    public static final String BANDCARD_ACTION = "com.dm.t.bandcard";
    
    /**
     * 解除绑定银行卡
     */
    public static final String UNBANDCARD_ACTION = "com.dm.t.unbandcard";
    
    /**
     * 转账授权
     */
    public static final String DEBITAUTH_ACTION = "com.dm.t.debitauth";
    
    /** {@inheritDoc} */
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if (REGISTER_ACTION.equals(action))
        {
            //注册
            String url = intent.getStringExtra("thirdRegistUrlString");
            String cookies = intent.getStringExtra("cookies");
            Intent in = new Intent(context, RegisterActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra("thirdRegistUrlString", url);
            in.putExtra("cookies", cookies);
            context.startActivity(in);
        }
        else if (RECHARGE_ACTION.equals(action))
        {
            //充值
            String url = intent.getStringExtra("reChargeUrl");
            String cookies = intent.getStringExtra("cookies");
            Intent in = new Intent(context, RechargeActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra("reChargeUrl", url);
            in.putExtra("cookies", cookies);
            context.startActivity(in);
        }
        else if (WITHDRAW_ACTION.equals(action))
        {
            //提现
            String url = intent.getStringExtra("withdrawUrl");
            String cookies = intent.getStringExtra("cookies");
            Intent in = new Intent(context, WithdrawActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra("withdrawUrl", url);
            in.putExtra("cookies", cookies);
            context.startActivity(in);
        }
        else if (BUYBID_ACTION.equals(action))
        {
            //投标
            String url = intent.getStringExtra("buyBidUrl");
            String cookies = intent.getStringExtra("cookies");
            Intent in = new Intent(context, BuyBidActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra("buyBidUrl", url);
            in.putExtra("cookies", cookies);
            context.startActivity(in);
        }
        else if (BUYCRE_ACTION.equals(action))
        {
            //债权转让
            String url = intent.getStringExtra("buyCreUrl");
            String cookies = intent.getStringExtra("cookies");
            Intent in = new Intent(context, BuyCreActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra("buyCreUrl", url);
            in.putExtra("cookies", cookies);
            context.startActivity(in);
        }
        else if (BANDCARD_ACTION.equals(action))
        {
            //绑定银行卡
            String url = intent.getStringExtra("bankBankUrl");
            String cookies = intent.getStringExtra("cookies");
            Intent in = new Intent(context, BandCardActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra("bankBankUrl", url);
            in.putExtra("cookies", cookies);
            context.startActivity(in);
        }
        else if (UNBANDCARD_ACTION.equals(action))
        {
            //解除绑定银行卡
            String url = intent.getStringExtra("unbankBankUrl");
            String cookies = intent.getStringExtra("cookies");
            Intent in = new Intent(context, UnBandCardActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra("unbankBankUrl", url);
            in.putExtra("cookies", cookies);
            context.startActivity(in);
        }
        else if (DEBITAUTH_ACTION.equals(action))
        {
            //转账授权
            String url = intent.getStringExtra("debitAuthUrl");
            String cookies = intent.getStringExtra("cookies");
            Intent in = new Intent(context, DebitAuthActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra("debitAuthUrl", url);
            in.putExtra("cookies", cookies);
            context.startActivity(in);
        }
    }
}
