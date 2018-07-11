package com.hxjr.p2p.ad5.utils;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.StringUtils;
import com.dm.widgets.utils.AlertDialogUtil;
import com.dm.widgets.utils.AlertDialogUtil.AlertListener;
import com.dm.widgets.utils.AlertDialogUtil.ConfirmListener;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.UpdateInfoBean;
import com.hxjr.p2p.ad5.service.UpdateService;
import com.hxjr.p2p.ad5.service.UpdateService.UpdateBinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 版本更新管理
 * @author  tangjian
 * @date 2015年11月12日
 */
public class UpdateManager
{
	private Context mContext;
	
	private UpdateServiceConnection conn = null;
	
	private String downLoadUrl = null;
	
	private boolean isForceToUpdate = false;
	
	private static UpdateManager mUpdateManager;
	
	private UpdateManager()
	{
	}
	
	public static UpdateManager getInstance()
	{
		if (null == mUpdateManager)
		{
			mUpdateManager = new UpdateManager();
		}
		return mUpdateManager;
	}
	
	private UpdateInfoBean updateInfoBean;
	
	/**
	 * 检查更新
	 */
	public void checkForUpdate(Context context, final String nowVersion, final boolean isShowToast)
	{
		mContext = context;
		HttpParams params = new HttpParams();
		params.put("verType", 1 + "");
		HttpUtil.getInstance().post(context, DMConstant.API_Url.SYS_KEEPSESSION, params, new HttpCallBack()
		{
			@Override
			public void onSuccess(JSONObject result)
			{
				try
				{
					String code = result.getString("code");
					long cTime = System.currentTimeMillis();
					if (DMConstant.ResultCode.SUCCESS.equals(code))
					{
						// 成功                        
						JSONObject data = result.getJSONObject("data");
						long sTime = data.getLong("time");
						DMApplication.getInstance().diffTime = cTime - sTime;
						JSONArray versions = data.getJSONArray("version");
						JSONObject version = versions.length() > 0 ? versions.getJSONObject(0) : null;
						if (version == null)
						{
							//已经是最新版本
							if (isShowToast)
							{
								ToastUtil.getInstant().show(mContext, "您当前的版本已经是最新的了");
							}
						}
						else
						{
							updateInfoBean = new UpdateInfoBean(new DMJsonObject(version.toString().trim()
							                                                               ));
							downLoadUrl = updateInfoBean.getFileUrl();
							if (updateInfoBean.isNeedUpdate())
							{
								//							showUpdateDialog();
								if (updateInfoBean.isMustUpdate())
								{
									//强制更新
									mustUpdateToNewVersion(StringUtils.isEmptyOrNull(updateInfoBean.getNewVersion()) ? nowVersion
										: updateInfoBean.getNewVersion());
									isForceToUpdate = true;
								}
								else
								{
									selectToUpdateNewVersion(StringUtils.isEmptyOrNull(updateInfoBean.getNewVersion())
										? nowVersion : updateInfoBean.getNewVersion());
									isForceToUpdate = false;
								}
							}
							else
							{
								//已经是最新版本
								if (isShowToast)
								{
									ToastUtil.getInstant().show(mContext, "您当前的版本已经是最新的了");
								}
							}
						}
						
						String serviceTel = data.getString("tel");
						String kfQQ = data.getString("kfQQ");
						SharedPreferenceUtils.put(mContext, "consts", "tel", serviceTel);
						SharedPreferenceUtils.put(mContext, "consts", "kfQQ", kfQQ);
					}
					else
					{
						ErrorUtil.showError(result);
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
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
	 * 用户可选择是否更新
	 */
	private void selectToUpdateNewVersion(String versionName)
	{
		versionName = "";//客户要求不显示版本号
		AlertDialogUtil.confirm(mContext,
			mContext.getString(R.string.has_update_version, versionName),
			"更新",
			"取消",
			new ConfirmListener()
			{
				@Override
				public void onOkClick()
				{
					startService();
				}
				
				@Override
				public void onCancelClick()
				{
					SharedPreferenceUtils.put(mContext, SharedPreferenceUtils.KEY_HAVE_NEW_VERSION, true);
					SharedPreferenceUtils.put(mContext, SharedPreferenceUtils.KEY_NEW_VERSION_URL, downLoadUrl);
				}
			});
	}
	
	/**
	 * 强制更新到最新版本
	 */
	private void mustUpdateToNewVersion(String versionName)
	{
		versionName = "";//客户要求不显示版本号
		//有更新
		AlertDialog alert =
			AlertDialogUtil.alert(mContext, mContext.getString(R.string.has_update_version, versionName), new AlertListener()
			{
				@Override
				public void doConfirm()
				{
					startService();
					SharedPreferenceUtils.put(mContext, SharedPreferenceUtils.KEY_IS_UPDATE, true);
					DMApplication.getInstance().isUpdate=true;
				}
			});
		alert.setCancelable(false);
		alert.setCanceledOnTouchOutside(false);
	}
	
	private void startService()
	{
		Intent updateService = new Intent(mContext, UpdateService.class);
		mContext.startService(updateService);
		conn = new UpdateServiceConnection();
		mContext.bindService(updateService, conn, Context.BIND_AUTO_CREATE);
	}
	
	public class UpdateServiceConnection implements ServiceConnection
	{
		@Override
		public void onServiceDisconnected(ComponentName name)
		{
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			UpdateBinder binder = (UpdateBinder)service;
			binder.start(mContext, downLoadUrl, isForceToUpdate);
		}
	};
}
