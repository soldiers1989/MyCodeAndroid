package com.hxjr.p2p.ad5.service;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dm.utils.DMLog;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.BuildConfig;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.UpdataFileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 下载服务
 * @author  jiaohongyun
 * @date 2015年8月17日
 */
public class UpdateService extends Service
{
	public static final String BUNDLE_KEY_DOWNLOAD_URL = "download_url";
	
	public static final String BUNDLE_KEY_TITLE = "title";
	
	private static final String TAG = "UpdateService";
	
	private int progress;
	
	private String downloadUrl;
	
	private Context context;
	
	private boolean isForceToUpdate = false; //是否强制更新
	
	private boolean isCanCanceled = false; //是否已取消下载
	
	private String saveFileName = DMConstant.StringConstant.DEFAULT_SAVE_FILE_PATH;
	
	private UpdateBinder binder;
	
	private Context mContext = this;
	
	private Thread downLoadThread;
	
	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
				case 0:
					dlg.cancel();
					if (!isCanCanceled)
					{
						// 下载完毕
						DMLog.e("3");
						installApk();
						DMLog.e("4");
						//						sendBroadcast(new Intent(DMConstant.StringConstant.DOWN_LOAD_SUCCESS));
						//						SharedPreferenceUtils.put(mContext, SharedPreferenceUtils.KEY_HAVE_NEW_VERSION, false);
						ToastUtil.getInstant().show(getApplicationContext(), "下载完成！");
					}
					break;
				case 1: //更新进度条
					if (progress < 100)
					{
						progress_tv.setText(progress + "%");
						progress_pb.setProgress(progress);
					}
					else
					{
						DMLog.e("1");
						stopSelf();// 下载完毕, 停掉服务自身
						DMLog.e("2");
					}
					break;
			}
		}
	};
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		binder = new UpdateBinder();
		stopForeground(true);// 这个不确定是否有作用
	}
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return binder;
	}
	
	public class UpdateBinder extends Binder
	{
		public void start(Context context, String downloadUrl, boolean isForceToUpdate)
		{
			UpdateService.this.context = context;
			UpdateService.this.downloadUrl = downloadUrl;
			UpdateService.this.isForceToUpdate = isForceToUpdate;
			saveFileName = saveFileName + "com.hxjr.p2p.ad5.apk";
			File file = new File(saveFileName);
			if (file.exists())
			{
				file.delete();
			}
			if (downLoadThread == null || !downLoadThread.isAlive())
			{
				progress = 0;
				setUpNotification();
				// 下载
				startDownload();
			}
		}
	}
	
	private void startDownload()
	{
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	
	private TextView progress_tv; //显示进度百分比
	
//	private ImageView close_iv; //关闭对话框按钮
	
	private ProgressBar progress_pb; //进度条
	
	private AlertDialog dlg = null;
	
	/**
	 * 显示下载对话框
	 */
	private void setUpNotification()
	{
		View view = View.inflate(context, R.layout.update_alert, null);
		dlg = new AlertDialog.Builder(context).create();
		dlg.setView(view);
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.update_alert);
		progress_tv = (TextView)window.findViewById(R.id.progress_tv);
//		close_iv = (ImageView)window.findViewById(R.id.close_iv);
		progress_pb = (ProgressBar)window.findViewById(R.id.progress_pb);
//		close_iv.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				if (!isForceToUpdate)
//				{
//					dlg.dismiss();
//					isCanCanceled = true;
//					stopSelf();
//				}
//			}
//		});
		dlg.setCancelable(false);
		dlg.setCanceledOnTouchOutside(false);
	}
	
	/**
	 * 安装apk
	 */
	private void installApk()
	{
		File apkfile = new File(saveFileName);
		if (!apkfile.exists())
		{
			return;
		}
		if (apkfile == null || !apkfile.exists())
		{
			return;
		}
//		Intent intent = new Intent();
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.setAction(Intent.ACTION_VIEW);
//		intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
//				mContext.startActivity(intent);
		DMLog.e(context.getFilesDir()+"");
		DMLog.e(context.getExternalFilesDir(null)+"");
		DMLog.e(Environment.getExternalStorageDirectory()+"");
		Intent intent = new Intent(Intent.ACTION_VIEW);
		try {
			String[] command = {"chmod", "777", apkfile.toString()};
			ProcessBuilder builder = new ProcessBuilder(command);
			builder.start();
		} catch (IOException ignored) {
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			Uri contentUri = UpdataFileProvider.getUriForFile(getApplicationContext(),
					BuildConfig.APPLICATION_ID + ".fileProvider", apkfile);
			intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
		} else {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
		}
		mContext.startActivity(intent);
	}
	
	private Runnable mdownApkRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			File file = new File(DMConstant.StringConstant.DEFAULT_SAVE_FILE_PATH);
			if (!file.exists())
			{
				file.mkdirs();
			}
			String apkFile = saveFileName;
			File saveFile = new File(apkFile);
			//			if (saveFile.exists())  //在前面的步骤中已经删除过了。
			//			{
			//				saveFile.delete();
			//			}
			try
			{
				downloadUpdateFile(downloadUrl, saveFile);
				//				updateProgress();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	};
	
	/***
	 * 模拟测试使用，可忽略
	 */
	protected void updateProgress()
	{
		final Timer timer = new Timer();
		final TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				progress += 5;
				if (progress >= 100)
				{
					if (null != timer)
					{
						timer.cancel();
					}
					this.cancel();
				}
				else
				{
					mHandler.sendEmptyMessage(1);
				}
			}
		};
		timer.schedule(task, 1000, 1000);
	}
	
	private long lastTimeMillis = 0; //上一秒
	
	public long downloadUpdateFile(String downloadUrl, File saveFile) throws Exception
	{
		long currentSize = 0;
		long totalSize = 0; //已下载的大小
		long updateTotalSize = 0; //apk总大小
		
		HttpURLConnection httpConnection = null;
		InputStream is = null;
		FileOutputStream fos = null;
		
		try
		{
			URL url = new URL(downloadUrl);
			httpConnection = (HttpURLConnection)url.openConnection();
			httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
			if (currentSize > 0)
			{
				httpConnection.setRequestProperty("RANGE", "bytes=" + currentSize + "-");
			}
			httpConnection.setConnectTimeout(10000);
			httpConnection.setReadTimeout(20000);
			updateTotalSize = httpConnection.getContentLength();
			if (updateTotalSize == -1)
			{
				updateTotalSize = (long)(4.1 * 1024 * 1024);
			}
			int connCode = httpConnection.getResponseCode();
			if (200 == connCode)
			{
				is = httpConnection.getInputStream();
				fos = new FileOutputStream(saveFile, false);
				byte buffer[] = new byte[1024];
				int readsize = 0;
				while ((readsize = is.read(buffer)) > 0)
				{
					fos.write(buffer, 0, readsize);
					totalSize += readsize;
					long currentTimeMillis = System.currentTimeMillis();
					if (currentTimeMillis - lastTimeMillis > 1000)
					{
						progress = (int)(totalSize * 100 / updateTotalSize);
						if (progress > 100)
						{
							progress = 100;
						}
						// 通知更新进度
						mHandler.sendEmptyMessage(1);
					}
				}
				
				// 下载完成通知安装
				mHandler.sendEmptyMessage(0);
			}
			else
			{
				ToastUtil.getInstant().show(context, "网络异常");
				dlg.cancel();
				stopSelf();
			}
		}
		finally
		{
			if (httpConnection != null)
			{
				httpConnection.disconnect();
			}
			if (is != null)
			{
				is.close();
			}
			if (fos != null)
			{
				fos.close();
			}
		}
		return totalSize;
	}
}
