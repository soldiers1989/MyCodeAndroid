package com.hxjr.p2p.ad5.ui.discovery.spread;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMLog;
import com.dm.utils.ThreadsPool;
import com.dm.widgets.EncodingHandler;
import com.dm.widgets.utils.ToastUtil;
import com.google.zxing.WriterException;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.SpreadInfo;
import com.hxjr.p2p.ad5.bean.SpreadInfo.SpreadEntity;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.MainActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.HttpParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * 推广奖励
 * @author  huangkaibo
 * @date 2015年11月9日
 */
public class SpreadRewardActivity extends BaseActivity implements OnClickListener, PlatformActionListener
{
	private TextView link_invite_tv;
	
	private TextView my_spread;
	
	private TextView my_spread_code_tv;
	
	private TextView long_click_copy_tv;
	
	private SharePopupWindow sharePopupWindow;
	
	private static final String FILE_NAME = "/ic_launcher.png";
	
	private String share_image_url;
	
	private boolean hasShared = false; //是否有过分享到某个平台
	
	private SpreadInfo spreadInfo;
	
	private ClipData clipData;
	
	private ImageView qrImgImageView;
	
	private TextView way1_layout;
	
	private LinearLayout way2_layout;
	
	private boolean isShowQrImg = false;
	private String sindex;
	
	/**复制内容**/
	private ClipboardManager clipboardManager;
	
	public void setHasShared(boolean hasShared)
	{
		this.hasShared = hasShared;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spread_reward);
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		if (bundle!=null) {
			sindex=bundle.getString("index");
		}
		initView();
		initListener();
		getData();
	}
	
	private void initListener()
	{
		findViewById(R.id.share_btn).setOnClickListener(this);
		my_spread.setOnClickListener(this);
		my_spread_code_tv.setOnClickListener(this);
		my_spread_code_tv.setOnLongClickListener(new OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				clipData = ClipData.newPlainText("spread_code", my_spread_code_tv.getText());
				clipboardManager.setPrimaryClip(clipData);
				ToastUtil.getInstant().show(SpreadRewardActivity.this, "复制成功");
				return false;
			}
		});
	}
	
	@Override
	protected void initView()
	{
		super.initView();
		((TextView)findViewById(R.id.title_text)).setText(R.string.discovery_spread_reward);
		my_spread = (TextView)findViewById(R.id.btn_right);
		my_spread.setVisibility(View.VISIBLE);
		setTitleRightIcon(my_spread);
		my_spread_code_tv = (TextView)findViewById(R.id.my_spread_code_tv);
		long_click_copy_tv = (TextView)findViewById(R.id.long_click_copy_tv);
		link_invite_tv = (TextView)findViewById(R.id.link_invite_tv);
		link_invite_tv.requestFocus();
		
		clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		
		// 二维码相关
		qrImgImageView = (ImageView)findViewById(R.id.qrImgImageView);
		way1_layout = (TextView)findViewById(R.id.way1_layout);
		way2_layout = (LinearLayout)findViewById(R.id.way2_layout);
		showQrImg();
	}
	
	/**
	 * 是否显示二维码扫描
	 */
	private void showQrImg()
	{
		if (isShowQrImg)
		{
			way1_layout.setVisibility(View.VISIBLE);
			way2_layout.setVisibility(View.VISIBLE);
		}
		else
		{
			way1_layout.setVisibility(View.GONE);
			way2_layout.setVisibility(View.GONE);
		}
	}
	
	private void getData()
	{
		initImagePath();
		postData();
	}
	
	private void postData()
	{
		HttpParams params = new HttpParams();
		//		params.put("", "");
		HttpUtil.getInstance().post(this, DMConstant.API_Url.MY_SPREAD_REWARD, params, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				if (null != result)
				{
					doAfterGetSpreadInfo(result);
					if (null != spreadInfo)
					{
						fillData();
					}
				}
			}
			
			@Override
			public void onFailure(Throwable t, Context context)
			{
				super.onFailure(t, context);
			}
			
			@Override
			public void onStart()
			{
				super.onStart();
			}
		});
	}
	
	/**
	 * 填充请求到的数据到界面
	 */
	protected void fillData()
	{
		my_spread_code_tv.setText(spreadInfo.getShareCode());
		link_invite_tv.setText("    " + spreadInfo.getShareMessage());
		String linkUrl = spreadInfo.getShareMessage().substring(spreadInfo.getShareMessage().indexOf("http"));
		//根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（200*200）
		try
		{
			Bitmap qrCodeBitmap = EncodingHandler.createQRCode(linkUrl, 200);
			qrImgImageView.setImageBitmap(qrCodeBitmap);
		}
		catch (WriterException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 解析推广数据
	 * @param result
	 */
	protected void doAfterGetSpreadInfo(JSONObject result)
	{
		try
		{
			if (result.has("code") && result.getString("code").equals("000000"))
			{
				JSONObject data = result.getJSONObject("data");
				spreadInfo = new SpreadInfo();
				spreadInfo.setShareCode(data.getString("tgm"));
				spreadInfo.setShareMessage(data.getString("msg"));
				spreadInfo.setCzjs(data.getString("czjs"));
				spreadInfo.setTghl(data.getString("tghl"));
				spreadInfo.setTgsx(data.getString("tgsx"));
				spreadInfo.setTzcs(data.getString("tzcs"));
				spreadInfo.setTzjl(data.getString("tzjl"));
				JSONObject spreadTotle = data.getJSONObject("spreadTotle");
				spreadInfo.setYqCount(spreadTotle.getInt("yqCount"));
				spreadInfo.setRewardTotal(spreadTotle.getDouble("rewardTotle"));
				spreadInfo.setRewardCxTotal(spreadTotle.getDouble("rewardCxtg"));
				spreadInfo.setRewardYxTotal(spreadTotle.getDouble("rewardYxtg"));
				
				if (data.has("spreadEntitys"))
				{
					JSONArray spreadEntitysJson = data.getJSONArray("spreadEntitys");
					if (null != spreadEntitysJson && spreadEntitysJson.length() > 0)
					{
						List<SpreadEntity> spreadEntitys = new ArrayList<SpreadEntity>(3);
						for (int i = 0; i < spreadEntitysJson.length(); i++)
						{
							JSONObject item = spreadEntitysJson.getJSONObject(i);
							SpreadEntity spreadEntity = spreadInfo.new SpreadEntity();
							spreadEntity.setUserName(item.getString("userName"));
							spreadEntity.setRegisterTime(item.getString("registerTime"));
							spreadEntitys.add(spreadEntity);
						}
						spreadInfo.setSpreadEntity(spreadEntitys);
					}
				}
			}
			else
			{
				ToastUtil.getInstant().show(this, result.getString("description"));
			}
		}
		catch (JSONException e)
		{
			DMLog.e(this.getClass().getSimpleName(), e.getMessage());
		}
	}
	
	/**
	 * 设置头部title里面右边TextView的icon图标
	 */
	protected void setTitleRightIcon(TextView textView)
	{
		Drawable right = getResources().getDrawable(R.drawable.pic_lb);
		right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
		textView.setCompoundDrawables(null, null, right, null);
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_right:
				Intent mySpreadIntent = new Intent(SpreadRewardActivity.this, MySpreadActivity.class);
				if (null != spreadInfo)
				{
					mySpreadIntent.putExtra("spreadInfo", spreadInfo);
				}
				startActivity(mySpreadIntent);
				break;
			case R.id.share_btn:
				showShare();
				break;
			case R.id.long_click_copy_tv:
				//				clipData = ClipData.newPlainText("spread_code", my_spread_code_tv.getText());
				//				clipboardManager.setPrimaryClip(clipData);
				//				ToastUtil.getInstant().show(this, "复制成功");
				break;
			default:
				break;
		}
	}
	
	private void showShare()
	{
		//		ShareSDK.initSDK(this);
		if (null == sharePopupWindow)
		{
			String content = spreadInfo.getShareMessage(); //"我的邀请码：" + spreadInfo.getShareCode();
			sharePopupWindow = new SharePopupWindow(SpreadRewardActivity.this);
			sharePopupWindow.setPlatformActionListener(this);
			ShareModel model = new ShareModel();
			model.setImageUrl(share_image_url);
			model.setTitle("装" + getString(R.string.app_name) + "吗？");
			model.setText(content);
			String shareUrl = spreadInfo.getShareMessage().substring(spreadInfo.getShareMessage().indexOf("http"));
			model.setUrl(shareUrl);
			sharePopupWindow.initShareParams(model);
			sharePopupWindow.showShareWindow();
		}
		// 显示窗口 (设置layout在PopupWindow中显示的位置)
		sharePopupWindow.showAtLocation(this.findViewById(R.id.invite_ll), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	
	@Override
	public void onCancel(Platform platform, int action)
	{
		ToastUtil.getInstant().show(this, "分享取消");
	}
	
	@SuppressLint("HandlerLeak")
	@Override
	public void onComplete(Platform platform, int action, HashMap<String, Object> res)
	{
		ToastUtil.getInstant().show(this, "分享成功");
	}
	
	@SuppressLint("HandlerLeak")
	@Override
	public void onError(Platform platform, int action, Throwable t)
	{
		ToastUtil.getInstant().show(this, "分享失败");
		t.printStackTrace();
	}
	
	/**
	* 把图片从drawable复制到sdcard中
	*/
	private void initImagePath()
	{
		ThreadsPool.executeOnExecutor(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
						&& Environment.getExternalStorageDirectory().exists())
					{
						share_image_url = Environment.getExternalStorageDirectory().getAbsolutePath() + FILE_NAME;
					}
					else
					{
						share_image_url = getApplication().getFilesDir().getAbsolutePath() + FILE_NAME;
					}
					File file = new File(share_image_url);
					if (file.exists())
					{
						file.delete();
					}
					file.createNewFile();
					Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
					FileOutputStream fos = new FileOutputStream(file);
					pic.compress(CompressFormat.PNG, 100, fos);
					fos.flush();
					fos.close();
				}
				catch (Throwable t)
				{
					t.printStackTrace();
					share_image_url = null;
				}
			}
		});
	}
	
	@Override
	protected void onDestroy()
	{
		if (hasShared)
		{
			ShareSDK.stopSDK(this);
		}
		if (sindex=="") {
			MainActivity.index = 2;
		}else{
			sindex="";
		}
		super.onDestroy();

	}
}
