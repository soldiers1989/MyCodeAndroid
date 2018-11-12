package com.hxjr.p2p.ad5.utils;

import android.os.Environment;

import com.dm.android.pay.PaymentType;

import java.io.File;

/**
 * 数字和对应字符常量定义
 */
public interface DMConstant
{
	interface Config
	{
		/**
		 * 使用协议http/https
		 */
		String PROTOCOL = "https://";

		/**
		 * 0使用http,1不用证书使用https,2需要证书的https
		 */
		int API_MODE = 1;

		/**
		 * 加密使用的Key
		 */
		String ENCRYPTKEY = "lisql2i13290xlj1";
//		String ENCRYPTKEY = "fdsjalk32903120o";//网关

		/**
		 * 平台服务器接口地址
		 */
//		String PLATA_URL = PROTOCOL + "112.95.233.249:5118/app/";  //苏
		String PLATA_URL = PROTOCOL + "101.200.139.68/app/";  //测试环境
//		String PLATA_URL = PROTOCOL + "www.todayfu.com/app/";

		/**
		 * 是否由托管方处理银行卡添加和删除操作
		 */
		boolean IS_BANKCARD_TG = false;

		/**
		 * 是否由托管方处理身份认证，手机认证，邮箱认证
		 */
		boolean IS_TG_VERIFY = false;

		/**
		 * 是否开启日志
		 */
		boolean IS_LOG = true;

		/**
		 * 是否使用欢迎指引页面
		 */
		boolean HAS_WELCOME = true;

		/**
		 * 银行所在地是否显示县
		 * 一般应该是true，目前只有双乾托管有这个限制，设置为false
		 */
		boolean SHOW_XIAN = true;

		String DOMAIN = "61.145.159.156";

		/**
		 * 托管支付方式
		 */
		PaymentType TG_PAYMENT_TYPE = PaymentType.YEEPAYTG;

		boolean bidIndex=true;

	}

	/**
	 * 数字常量
	 */
	interface DigitalConstant
	{
		/** 10 */
		int TEN = 10;

		/**  一万 */
		Double TENTHOUSAND = 10000.00d;

		/** 百万 */
		Double MILLION = 1000000.00d;

		/**  亿*/
		Double HUNDREDMILLION = 100000000.00d;

		int PAGE_SIZE = 20;
	}

	/**
	 * 字符数字常量
	 */
	interface StringConstant
	{
		/**
		 * 本地加密使用的字符串
		 */
		String ENCRYP_SEND = "b04ff3b45c9e5g8h";

		/**
		 * 默认存放文件下载的路径
		 */
		String DEFAULT_SAVE_FILE_PATH = Environment.getExternalStorageDirectory()+File.separator+"download"+File.separator;
	}

	/**
	 * 接口返回的Code
	 * @author  jiaohongyun
	 * @date 2015年10月16日
	 */
	interface ResultCode
	{
		/**
		 * 成功
		 */
		String SUCCESS = "000000";

	}

	/**
	 * 验证码类型
	 * @author  huangkaibo
	 * @date 2015年11月14日
	 */
	interface VerifyCodeType
	{
		String LOGIN_PWD_BY_MOBILE = "login_pwd_by_mobile";

		String LOGIN_PWD_BY_EMAIL = "login_pwd_by_email";

		String MOBILE = "mobile";

		String EMAIL = "email";
	}

	/**
	 * 接口名称
	 *
	 * @author jiaohongyun
	 *
	 */
	interface API_Url
	{
		String CSHKCG="http://101.200.139.68/user/credit/repaying.html?state=succeed";

		String CSHKSB="http://101.200.139.68/user/credit/repaying.html?state=fail";

		String HKSB="https://www.todayfu.com/user/credit/repaying.html?state=fail";

		String HKCG="https://www.todayfu.com/user/credit/repaying.html?state=succeed";

		String SYNCHRONIZATIONBALANCE=Config.PLATA_URL+"user/synchronizationBalance.htm";

		String RECHARGEVERIFYCODE=Config.PLATA_URL+"pay/sendESBSMS.htm";//获取充值验证码

		String SETPWD=Config.PLATA_URL+"pay/setPassword.htm";//设置支付密码

		String REGISTER_THIRD=Config.PLATA_URL+"pay/payUserRegister.htm";//注册银行第三方

		String PAY_USER_REGISTERN=Config.PLATA_URL+"pay/payUserRegister.htm";//注册银行第三方 第三方注册数据接口

		String ADDBANKCARD=Config.PLATA_URL+"pay/bindCard.htm";//绑定银行卡

		String UPDATE_EXPERENCEBID=Config.PLATA_URL+"user/updateExperienceBid.htm";//更新体验表状态

		String START_IMG=Config.PLATA_URL+"start/bootImage.htm";//启动数据

		String BID_PUBLIC_LIST=Config.PLATA_URL+"bid/publics/bidList.htm";//分类子标

		String BID_PUBLIC_FL=Config.PLATA_URL+"bid/publics/bidProduct.htm";//获取标分类接口

		String USER_GETCODEIMG = Config.PLATA_URL + "platinfo/loginVerify.htm";// 获取登录验证码接口

		String USER_LOGIN = Config.PLATA_URL + "platinfo/login.htm";// 登录接口

		String CHECK_LOGIN_PWD = Config.PLATA_URL + "user/checkPwd.htm"; //检测登录密码是否正确

		String THIRD_LOGIN = Config.PLATA_URL + "platinfo/thirdPartyLogin.htm";// 第三方登录接口

		String USER_REGISTER = Config.PLATA_URL + "platinfo/register.htm";// 注册接口

		String USER_GETREGISTERCODEIMG = Config.PLATA_URL + "platinfo/registerVerify.htm";// 获取注册验证码接口

		String GET_VERIFY_CODE_NOLOGIN = Config.PLATA_URL + "platinfo/resetVerifyCodeNoLogin";// 获取字符验证码(未登录，找回密码用)

		String BID_PUBLICS_BIDLIST = Config.PLATA_URL + "bid/publics/bidList.htm";// 投资项目列表接口

		String CREDITOR_CREDITORLIST = Config.PLATA_URL + "creditor/publics/creditorList.htm";// 债权转让列表接口

		String BID_BID = Config.PLATA_URL + "bid/publics/bid.htm";// 标的详情信息

		String BID_BUYBID = Config.PLATA_URL + "bid/buyBid.htm";// 投标

		String CREDITOR_CREDITOR = Config.PLATA_URL + "creditor/publics/creditor.htm";// 债权的详情信息

		String CREDITOR_BUYCREDITOR = Config.PLATA_URL + "creditor/buyCreditor.htm";// 购买债权

		String CREDITOR_BUYCREDITOR_TG = Config.PLATA_URL + "creditor/bidExchange.htm";// (托管)购买债权 双乾

		String BID_REPAYLIST = Config.PLATA_URL + "bid/publics/repayList.htm";// 还款计划详情

		String BID_RECORDSLIST = Config.PLATA_URL + "bid/publics/bidRecordsList.htm";// 还款计划详情

		String BID_BIDITEM = Config.PLATA_URL + "bid/publics/bidItem.htm";// 借款方的企业信息

		String USER_USERINFO = Config.PLATA_URL + "user/user.htm";// 查询个人信息接口

		String USER_ACCOUNT = Config.PLATA_URL + "user/account.htm";// 查询账户信息

		String USER_TRANLOGLIST = Config.PLATA_URL + "user/tranRecordList.htm";// 交易记录列表接口

		String USER_MYCREDITORLIST = Config.PLATA_URL + "user/myCreditorList.htm";// 我的债权列表

		String USER_MYLOANLIST = Config.PLATA_URL + "user/myBidList.htm";// 我的借款列表

		String USER_REPAYINFO = Config.PLATA_URL + "user/repayInfo.htm";// 我的借款-->还款详情

		String USER_PAYMENT = Config.PLATA_URL + "user/payment.htm";// 我的借款-->还款

		String USER_PRE_REPAYMENT = Config.PLATA_URL + "user/prepayment.htm";// 我的借款-->提前还款

		String USER_MESSAGELIST = Config.PLATA_URL + "user/messageList.htm";// 站内消息列表

		String USER_LOGINOUT = Config.PLATA_URL + "platinfo/logout.htm";// 退出登录

		String USER_SETWITPASSWORD = Config.PLATA_URL + "user/setTranPwd.htm";// 设置交易密码

		String USER_UPDATE_LOGIN_PWD = Config.PLATA_URL + "user/updatePwd.htm";// 修改登录密码

		String USER_LETTERS = Config.PLATA_URL + "user/letterList.htm";// 查询站内信

		String USER_LETTERS_STATUS = Config.PLATA_URL + "user/readLetter.htm";// 修改站内信状态接口

		String SYS_KEEPSESSION = Config.PLATA_URL + "timing.htm";// 定时查看是否在线接口

		String USER_SETUSERINFO = Config.PLATA_URL + "user/setUserInfo.htm";// 设置用户实名认证

		String USER_SETUSERINFO2 = Config.PLATA_URL + "user/setUserInfo2.htm";// 设置用户实名认证

		String USER_REPAYLIST_DETAIL = Config.PLATA_URL + "user/repayInfo.htm";// 我的借款还款详情

		String USER_SETUSERPHONE = Config.PLATA_URL + "user/setUserPhone.htm";// 绑定手机

		String USER_SETUSEREMAIL = Config.PLATA_URL + "user/setUserEmail.htm";// 设置用户邮箱地址

		String GET_EMAILCODE = Config.PLATA_URL + "platinfo/getEmailCode.htm";// 邮箱验证码下发

		String USER_UPDATETRANPWD = Config.PLATA_URL + "user/updateTranPwd.htm";//修改交易密码

		String USER_FORGOTTRANPWD = Config.PLATA_URL + "user/forgotTranPwd.htm";// 找回交易密码

		String GETMOBILECODE = Config.PLATA_URL + "platinfo/getMobileCode.htm";// 手机验证码下发

		String USER_SET_LOGIN_PWD = Config.PLATA_URL + "user/setLoginPwd.htm";//设置登录密码

		String USER_MYBANKLIST = Config.PLATA_URL + "user/myBankList.htm";// 用户银行列表

		String EDIT_BANKCARD = Config.PLATA_URL + "pay/editBankCard.htm";// 修改银行卡接口信息

		String PAYUSERREGISTER = Config.PLATA_URL + "pay/payUserRegister.htm";//注册第三方

		String USER_BANKLIST = Config.PLATA_URL + "user/bankList.htm";// 银行列表

		String SEARCHBANKADDRES=Config.PLATA_URL+"pay/searchBankAddress.htm";//搜索开户行地址

		String CITYS_LIST=Config.PLATA_URL+"pay/getCitys.htm";//城市列表

		String ADVS_GET = Config.PLATA_URL + "platinfo/advServlet.htm";// 广告图片

		String SEARCH_ADDRESS = Config.PLATA_URL + "pay/searchBankAddress.htm";//搜索地址

		String HOT_CITY = Config.PLATA_URL + "pay/hotAddress.htm";//热门城市

		String ADD_BANK_CARD = Config.PLATA_URL + "pay/bindCard.htm";//添加银行卡

		String DELETE_BANK_CARD = Config.PLATA_URL + "pay/deleteBankCard.htm";//删除银行卡

		String USER_FEE = Config.PLATA_URL + "user/fee.htm";//提现手续费和充值手续费

		String PAY_WITHDRAW = Config.PLATA_URL + "pay/withdraw.htm";//提现

		String PAY_CHARGE = Config.PLATA_URL + "pay/charge.htm";//充值;

		String CHARGE_VERIFY=Config.PLATA_URL+"user/chargeVerify.htm";//核对转账记录

		String EVALUATION = Config.PLATA_URL + "riskassessment/startApp.jsp";//风险评估测试

		//		String PAY_CHARGE = Config.PLATA_URL + "pay/allinpay/allinpayCharge.htm";//通联充值;

		String INIT_TERMTYPE = Config.PLATA_URL + "platinfo/initTermType.htm";//协议接口需要传协议类型type字段 type字段值  ZC:注册|JK:借款|GYB:公益捐赠|ZQ:债权转让  JK:借款  需要传是否担保标标识  isDB isDB的值为 S或者F

		//"agreement"
		//		String INIT_RESISTER = Config.PLATA_URL + "platinfo/initResister.htm";

		String REGISTER_AGREEMENT = Config.PLATA_URL + "platinfo/agreement.htm";//注册协议

		String FORGET_PASS_VERIFY = Config.PLATA_URL + "platinfo/forgetPassVerify.htm";//获取忘记密码页面的验证码

		String FORGET_PASS = Config.PLATA_URL + "platinfo/forgetPass.htm";//忘记密码--验证手机号码是否已经注册

		String RESET_LOGIN_PWD = Config.PLATA_URL + "platinfo/resetLoginPwd.htm";//忘记密码--重置登录密码

		String PAY_USER_REGISTER = Config.PLATA_URL + "pay/payUserRegister.htm";//获取第三方托管注册

		String PAY_THIRD_WITHDRAW = Config.PLATA_URL + "pay/thirdWithdraw.htm";//第三方托管提现 这个没有使用

		String PAY_BIND_CARD = Config.PLATA_URL + "pay/bindCard.htm";//第三方托管绑定银行卡

//		String PAY_UNBIND_CARD = Config.PLATA_URL + "pay/unbindCard.htm";//第三方托管解除绑定银行卡
		String PAY_UNBIND_CARD = Config.PLATA_URL + "pay/service/yeepay/unBindCardServlet.htm";//第三方托管解除绑定银行卡

		String ARTICLE_LIST = Config.PLATA_URL + "platinfo/articleList.htm";//行业资讯或互联网金融

		String NOTICE_LIST = Config.PLATA_URL + "platinfo/noticeList.htm";//网站公告

		String ARTICLE_ITEM = Config.PLATA_URL + "platinfo/articleItem.htm";//行业资讯或互联网金融详情

		String NOTICE_ITEM = Config.PLATA_URL + "platinfo/noticeItem.htm";//网站公告详情

		String BID_COUNT = Config.PLATA_URL + "platinfo/getIndexStatic.htm";// 投资统计

		String RECOMMEND_BID_LIST = Config.PLATA_URL + "bid/publics/tjBidList.htm"; // 推荐标

		String CHECK_VERIFY_CODE = Config.PLATA_URL + "platinfo/resetCheckVerifyCode.htm"; // 验证码校验

		String UNUSE_REWARD_LIST = Config.PLATA_URL + "bid/publics/unUseAwardList.htm "; // 未使用奖励列表

		String EXPERIENCE_EXPECTED = Config.PLATA_URL + "user/tyjAmountLoan.htm"; // 未使用奖励列表

		//
		String MY_EXP_LIST = Config.PLATA_URL + "user/myTyjList.htm"; //体验金列表

		String MY_EXP_DETAIL = Config.PLATA_URL + "user/myExperienceItem.htm";// 体验金详情

		//		String MY_HB_LIST = Config.PLATA_URL + "user/myHbList.htm"; //红包列表

		//		String MY_JXQ_LIST = Config.PLATA_URL + "user/myJxqList.htm"; //加息券列表

		String MY_REWARD_LIST = Config.PLATA_URL + "user/myRewardList.htm";// 我的奖励列表

		String MY_REWARD_INFO = Config.PLATA_URL + "user/myAwardInfo.htm";// 我的奖励信息

		String GY_LOAD_LIST = Config.PLATA_URL + "bid/publics/gyLoanList.htm";// 公益标列表

		String GY_LOAN_DETAIL = Config.PLATA_URL + "bid/publics/gyLoanItem.htm";// 公益标详情

		String GY_LOAN_COUNT = Config.PLATA_URL + "bid/publics/gyLoanInfo.htm"; // 公益标信息(广告、统计信息)

		String GY_LOAN_RECORD = Config.PLATA_URL + "bid/publics/gyLoanRecordsList.htm";// 公益标投资记录

		String GY_LOAN_BID = Config.PLATA_URL + "bid/gyLoanBid.htm";// 公益投标

		String USER_BID_RANK = Config.PLATA_URL + "platinfo/getUserBidRank.htm";// 土豪榜

		String MY_INVESTMENT_LIST = Config.PLATA_URL + "user/myCreditorInfo.htm";// 我的投资列表

		String MY_CREDITOR_ASSIGNMENT_LIST = Config.PLATA_URL + "user/creditAssignmentList.htm";// 债权转让列表

		String TRANSFER_CREDIT = Config.PLATA_URL + "creditor/transfer.htm";// 转让债权

		String CANCEL_CREDIT = Config.PLATA_URL + "creditor/cancelTransfer.htm";// 取消转让

		String MY_SPREAD_REWARD = Config.PLATA_URL + "user/mySpread.htm";//推广有奖

		String CHECK_AGREEMENT = Config.PLATA_URL + "bid/publics/agreementViewV2.htm";// 查看合同

		String CHECK_CREDITOR_AGREEMENT = Config.PLATA_URL + "bid/publics/zqzrAgreementViewV2.htm";// 查看我的债权转让合同

		String REGISTER_INFO = Config.PLATA_URL + "user/registerInfo.htm";//用户名和密码验证规则

		String AUTO_BID_CFG = Config.PLATA_URL + "platinfo/autoBidCfg.htm";//最低投标金额设置、投标金额的倍数设置
	}

	/**
	 * 托管广播使用的Action
	 * 以及托管相关配置
	 * 使用广播切换成非托管或其它托管时不用修改代码
	 * @author  jiaohongyun
	 * @version  [版本号, 2015年1月8日]
	 */
	interface TgAction
	{

		/**
		 * 注册帐号
		 */
		String REGISTER_ACTION = "com.dm.t.register";

		/**
		 * 充值
		 */
		String RECHARGE_ACTION = "com.dm.t.recharge";

		/**
		 * 提现
		 */
		String WITHDRAW_ACTION = "com.dm.t.withdraw";

		/**
		 * 投标
		 */
		String BUYBID_ACTION = "com.dm.t.buybid";

		/**
		 * 债权转让
		 */
		String BUYCRE_ACTION = "com.dm.t.buycre";

		/**
		 * 绑定银行卡
		 */
		String BANDCARD_ACTION = "com.dm.t.bandcard";

		/**
		 * 解除绑定银行卡
		 */
		String UNBANDCARD_ACTION = "com.dm.t.unbandcard";

		/**
		 * 转账授权
		 */
		String DEBITAUTH_ACTION = "com.dm.t.debitauth";
	}

	/**
	 * 广播使用的Actions
	 * @author  jiaohongyun
	 * @date 2015年10月17日
	 */
	interface BroadcastActions
	{
		/**
		 * 登录成功
		 */
		String USER_SESSION_LOGIN = "com.hxjr.p2p.ad5.action.login_success";

		/**
		 * 登录失败
		 */
		String USER_SESSION_LOGOUT = "com.hxjr.p2p.ad5.action.logout_success";

		/**
		 * 更新cookie
		 */
		String USER_SESSION_UPDATE_COOKIE = "com.hxjr.p2p.ad5.action.update_cookie";

		/**
		 * 我的投资-回款中，转让成功后刷新
		 */
		String MY_INVESTMENT_REPAYMENT = "com.hxjr.p2p.ad5.action.repayment_refresh";
	}

	/**
	 * 页面请求码
	 */
	interface RequestCodes
	{
		int REQUEST_CODE_SECURITY = 0x001;
	}

	/**
	 * 托管第三方web页面标题
	 * @author jiaohongyun
	 *
	 */
	interface TgWebTitle
	{
		String BUY_CRE = "债权购买";

		String DONATION_BID = "捐赠";

		String SOUQUAN = "授权";

		String BUY_BID = "投标";

		String WITHDRAW = "提现";

		String RECHARGE = "充值";

		String HUAN_KUAN = "还款";

		String TIQIAN_HUANKUAN = "提前还款";

		String BIND_CARD = "绑定银行卡";
	}
}
