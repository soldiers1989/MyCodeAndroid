package com.dm.widgets.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.hxjr.p2p.ad5.R;

/**
 * 弹出框工具
 * @author  jiaohongyun
 * @date 2015年7月30日
 */
public class AlertDialogUtil
{
	
	/**
	 * 弹出有一个按钮确认的提示框
	 * @param context
	 * @param content 提示内容
	 * @return
	 */
	public static AlertDialog alert(Context context, String content)
	{
		return alert(context, content, null, null);
	}
	
	/**
	 * 弹出有一个按钮确认的提示框
	 * @param context
	 * @param content 提示内容
	 * @param btnStr 按钮文字
	 * @return
	 */
	public static AlertDialog alert(Context context, String content, String btnStr)
	{
		return alert(context, content, btnStr, null);
	}
	
	/**
	 * 弹出有一个按钮确认的提示框
	 * @param context
	 * @param content 提示内容
	 * @param listener 点击按钮要做的处理
	 * @return
	 */
	public static AlertDialog alert(Context context, String content, final AlertListener listener)
	{
		return alert(context, content, null, listener);
	}
	
	/**
	 * 弹出有一个按钮确认的提示框
	 * @param context
	 * @param content 提示内容
	 * @param btnStr 确定按钮显示文字
	 * @param listener
	 * @return
	 */
	public static AlertDialog alert(Context context, String content, String btnStr, final AlertListener listener)
	{
		View view = View.inflate(context, R.layout.alert, null);
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.setView(view);
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.alert);
		//		FontHelper.applyFont(context, window.getDecorView());
		TextView contentView = (TextView)window.findViewById(R.id.content);
		contentView.setText(Html.fromHtml(content));
		TextView btn_ok = (TextView)window.findViewById(R.id.btn_ok);
		if (btnStr != null && !btnStr.isEmpty())
		{
			btn_ok.setText(btnStr);
		}
		btn_ok.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (listener != null)
				{
					listener.doConfirm();
				}
				dlg.dismiss();
			}
		});
		dlg.setCancelable(true);
		dlg.setCanceledOnTouchOutside(true);
		return dlg;
	}
	
	/**
	 * 有两个按钮的确认对话框（按钮文字，点击操作都是默认的）
	 * @param context
	 * @param content 提示内容
	 * @return
	 */
	public static AlertDialog confirm(Context context, String content)
	{
		return confirm(context, content, null, null, null);
	}
	
	/**
	 * 有两个按钮的确认对话框（按钮使用默认文字）
	 * @param context
	 * @param content 提示内容
	 * @param listener 点击按钮的操作
	 * @return
	 */
	public static AlertDialog confirm(Context context, String content, final ConfirmListener listener)
	{
		return confirm(context, content, null, null, listener);
	}
	
	/**
	 * 有两个按钮的确认对话框
	 * @param context
	 * @param content 提示内容
	 * @param btnOkStr 确认按钮文字
	 * @param btnCancelStr 取消按钮文字
	 * @param listener 按钮被点击时的操作
	 * @return
	 */
	public static AlertDialog confirm(Context context, String content, String btnOkStr, String btnCancelStr,
		final ConfirmListener listener)
	{
		View view = View.inflate(context, R.layout.confirm, null);
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.setView(view);
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.confirm);
		//		FontHelper.applyFont(context, window.getDecorView());
		TextView contentView = (TextView)window.findViewById(R.id.content);
		contentView.setText(Html.fromHtml(content));
		TextView btn_ok = (TextView)window.findViewById(R.id.btn_ok);
		if (btnOkStr != null && !btnOkStr.isEmpty())
		{
			btn_ok.setText(btnOkStr);
		}
		btn_ok.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (listener != null)
				{
					listener.onOkClick();
				}
				dlg.dismiss();
			}
		});
		TextView btn_cancel = (TextView)window.findViewById(R.id.btn_cancel);
		if (btnCancelStr != null && !btnCancelStr.isEmpty())
		{
			btn_cancel.setText(btnCancelStr);
		}
		btn_cancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (listener != null)
				{
					listener.onCancelClick();
				}
				dlg.dismiss();
			}
		});
		dlg.setCancelable(false);
		dlg.setCanceledOnTouchOutside(false);
		return dlg;
	}
	
	/**
	 * 弹出带文字输入框的对话框
	 * @param context
	 * @param lab 输入框前面的标题
	 * @param listener 事件处理
	 * @return
	 */
	public static AlertDialog prompt(Context context, String lab, final PromptListener listener)
	{
		return prompt(context, lab, null, null, null, listener);
	}
	
	/**
	 * 
	 * @param context
	 * @param lab 输入框前面的标题
	 * @param inputType 输入框的输入类型，和xml中配置不同，一般要多个配合
	 * @param listener
	 * @return
	 */
	public static AlertDialog prompt(Context context, String lab, Integer inputType, final PromptListener listener)
	{
		return prompt(context, lab, null, null, inputType, listener);
	}
	
	/**
	 * 弹出带文字输入框的对话框
	 * @param context
	 * @param lab 输入框前面的标题
	 * @param btnOkStr 确定按钮文字
	 * @param btnCancelStr 取消按钮文字 
	 * @param listener 事件处理
	 * @return
	 */
	public static AlertDialog prompt(Context context, String lab, String btnOkStr, String btnCancelStr,
		final PromptListener listener)
	{
		return prompt(context, lab, btnOkStr, btnCancelStr, null, listener);
	}
	
	/**
	 * 弹出带文字输入框的对话框
	 * @param context
	 * @param lab 输入框前面的标题
	 * @param btnOkStr 确定按钮文字
	 * @param btnCancelStr 取消按钮文字 
	 * @param inputType 输入框的输入类型，和xml中配置不同，一般要多个配合
	 * @param listener 事件处理
	 * @return
	 */
	public static AlertDialog prompt(Context context, String lab, String btnOkStr, String btnCancelStr, Integer inputType,
		final PromptListener listener)
	{
		View view = View.inflate(context, R.layout.prompt, null);
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.setView(view);
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.prompt);
		TextView lab_text = (TextView)window.findViewById(R.id.prompt_title);
		lab_text.setText(lab);
		final EditText inputText = (EditText)window.findViewById(R.id.input_text);
		if (inputType != null)
		{
			inputText.setInputType(inputType);
		}
		else
		{
			//默认为密码输入框
			//			inputText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}
		TextView btn_ok = (TextView)window.findViewById(R.id.btn_ok);
		if (btnOkStr != null && !btnOkStr.isEmpty())
		{
			btn_ok.setText(btnOkStr);
		}
		btn_ok.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (listener != null)
				{
					listener.onOkClick(inputText);
				}
				dlg.dismiss();
			}
		});
		TextView btn_cancel = (TextView)window.findViewById(R.id.btn_cancel);
		if (btnCancelStr != null && !btnCancelStr.isEmpty())
		{
			btn_cancel.setText(btnCancelStr);
		}
		btn_cancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (listener != null)
				{
					listener.onCancelClick();
				}
				dlg.dismiss();
			}
		});
		dlg.setCancelable(true);
		dlg.setCanceledOnTouchOutside(true);
		return dlg;
	}
	
	/**
	 * 弹出带文字输入框的对话框，只有一个确定按钮
	 * @param context
	 * @param lab 输入框前面的标题
	 * @param btnOkStr 确定按钮文字
	 * @param btnCancelStr 取消按钮文字 
	 * @param inputType 输入框的输入类型，和xml中配置不同，一般要多个配合
	 * @param listener 事件处理
	 * @return
	 */
	public static AlertDialog promptOne(Context context, String title, Integer inputType, final PromptListener listener)
	{
		View view = View.inflate(context, R.layout.prompt_one_button, null);
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.setView(view);
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.prompt_one_button);
		TextView title_tv = (TextView)window.findViewById(R.id.prompt_title);
		title_tv.setText(title);
		window.findViewById(R.id.close_img).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (listener != null)
				{
					listener.onCancelClick();
				}
				dlg.dismiss();
			}
		});
		final EditText inputText = (EditText)window.findViewById(R.id.input_text);
		if (inputType != null)
		{
			inputText.setInputType(inputType);
		}
		else
		{
			//默认为密码输入框
			//inputText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}
		window.findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (listener != null)
				{
					listener.onOkClick(inputText);
				}
				dlg.dismiss();
			}
		});
		dlg.setCancelable(false);
		dlg.setCanceledOnTouchOutside(false);
		return dlg;
	}
	
	/**
	 * 针对密码管理弹出不消失
	 * @param context
	 * @param title
	 * @param inputType
	 * @param listener
	 * @return
	 */
	public static AlertDialog loginCancelColse(Context context, String title, Integer inputType, final PromptListener listener)
	{
		View view = View.inflate(context, R.layout.prompt_one_button, null);
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.setView(view);
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.prompt_one_button);
		TextView title_tv = (TextView)window.findViewById(R.id.prompt_title);
		title_tv.setText(title);
		window.findViewById(R.id.close_img).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (listener != null)
				{
					listener.onCancelClick();
				}
				dlg.dismiss();
			}
		});
		final EditText inputText = (EditText)window.findViewById(R.id.input_text);
		if (inputType != null)
		{
			inputText.setInputType(inputType);
		}
		else
		{
			//默认为密码输入框
			//inputText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}
		window.findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (listener != null)
				{
					listener.onOkClick(inputText);
				}
			}
		});
		dlg.setCancelable(false);
		dlg.setCanceledOnTouchOutside(false);
		return dlg;
	}
	
	public interface AlertListener
	{
		/**
		 * 点击确认按钮时需要做的处理
		 */
		void doConfirm();
	}
	
	public interface ConfirmListener
	{
		/**
		 * 当确认被点击时调用
		 */
		void onOkClick();
		
		/**
		 * 当取消被点击时调用
		 */
		void onCancelClick();
	}
	
	public interface PromptListener
	{
		/**
		 * 当确认被点击时调用
		 */
		 void onOkClick(EditText text);
		
		/**
		 * 当取消被点击时调用
		 */
		 void onCancelClick();
	}
}
