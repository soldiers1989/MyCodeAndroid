package com.hxjr.p2p.ad5.ui.mine.bank;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.http.HttpCallBack;
import com.dm.http.HttpUtil;
import com.dm.utils.DMJsonObject;
import com.dm.utils.DMLog;
import com.dm.widgets.utils.ToastUtil;
import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.R;
import com.hxjr.p2p.ad5.bean.Bank;
import com.hxjr.p2p.ad5.bean.UserInfo;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.ErrorUtil;
import com.hxjr.p2p.ad5.utils.FormatUtil;
import com.hxjr.p2p.ad5.utils.HttpParams;
import com.hxjr.p2p.ad5.utils.UIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ThirdBankCardActivity extends BaseActivity implements View.OnClickListener {
    private EditText openNameEt; //开户名

    private EditText cardNumEt; //卡号

    private TextView selectBankTv; //银行

    private TextView selectProvice;

    private TextView selectCitys;

    private PopupWindow popupWindow;

    private Button nextStep;

    private List<Bank> bankList;

    private TextView title;

    private ArrayList<Map<String, String>> provice;

    private ArrayList<Map<String, String>> citys;

    private ArrayList<Map<String, String>> bankNum;

    private String name;

    private EditText searchAdd;

    private EditText et_bankUnionCode;

    private Button deleteBtn;

    private static final int MSG_SEARCH = 1;

    /**
     * 需要提交的银行id
     */
    private int submitBankId;

    private String code;

    private String bankCodeNum;

    private boolean isSearch = false;

    private UserInfo userInfo;


    private ListView city_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_bank_card_activity);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBankList();
        getCitys();
    }

    /**
     * 查找银行列表
     */
    private void getBankList() {
        HttpParams params = new HttpParams();
        HttpUtil.getInstance().post(this, DMConstant.API_Url.USER_BANKLIST, params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String code = result.getString("code");
                    if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                        // 成功
                        JSONArray dataList = result.getJSONArray("data");
                        if (bankList == null) {
                            bankList = new ArrayList<Bank>();
                        }
                        bankList.clear();
                        for (int i = 0; i < dataList.length(); i++) {
                            DMJsonObject dmJsonObject = new DMJsonObject(dataList.get(i).toString());
                            Bank bank = new Bank(dmJsonObject);
                            bankList.add(bank);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * 获取地区信息
     */
    private void getCitys() {
        HttpParams params = new HttpParams();
        HttpUtil.getInstance().post(this, DMConstant.API_Url.CITYS_LIST, params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String code = result.getString("code");
                    if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                        // 成功
                        JSONArray dataList = result.getJSONArray("data");
                        if (provice == null) {
                            provice = new ArrayList<Map<String, String>>();
                        }
                        provice.clear();
                        for (int i = 0; i < dataList.length(); i++) {
                            Map<String, String> map = new HashMap<String, String>();
                            JSONObject jb = dataList.getJSONObject(i);
                            map.put("name", jb.getString("name"));
                            map.put("citys", jb.getString("citys"));
                            provice.add(map);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void getCitys(int position) {
        if (citys == null) {
            citys = new ArrayList<Map<String, String>>();
        }
        citys.clear();
        try {
            JSONArray jsonArray = new JSONArray(provice.get(position).get("citys"));
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                map.put("name", jsonObject.getString("name"));
                map.put("code", jsonObject.getString("code"));
                citys.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView() {
        //设置返回按钮事件
        ((ImageView) findViewById(R.id.btn_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //        ((TextView) findViewById(R.id.title_text)).setText(R.string.add_bank_card);
        title = (TextView) findViewById(R.id.title_text);
        openNameEt = (EditText) findViewById(R.id.open_name_et);
        openNameEt.setEnabled(false);
        cardNumEt = (EditText) findViewById(R.id.card_number_et);
        selectBankTv = (TextView) findViewById(R.id.select_bank_tv);
        selectProvice = (TextView) findViewById(R.id.select_province_tv);
        selectCitys = (TextView) findViewById(R.id.select_city_tv);
        et_bankUnionCode=(EditText) findViewById(R.id.et_bankUnionCode);
        searchAdd = (EditText) findViewById(R.id.searchAdd);
        deleteBtn = (Button) findViewById(R.id.delete_btn);
        city_listview = (ListView) findViewById(R.id.city_listview);
        nextStep = (Button) findViewById(R.id.next_step_btn);
        searchAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                DMLog.e("beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DMLog.e("onTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    deleteBtn.setVisibility(View.VISIBLE);
                    if (selectBankTv.getText().toString().trim().equals("选择银行")) {
                        ToastUtil.getInstant().show(ThirdBankCardActivity.this, "请选择银行");
                    } else {
                        if (TextUtils.isEmpty(code)) {
                            ToastUtil.getInstant().show(ThirdBankCardActivity.this, R.string.add_card_choice_citys);
                        } else {
                            mHandler.sendEmptyMessageDelayed(MSG_SEARCH, 800); //自动搜索功能 删除
                        }
                    }
                } else {
                    deleteBtn.setVisibility(View.GONE);
                    city_listview.setVisibility(View.GONE);
                    isSearch = false;
                }
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //搜索请求
            search(String.valueOf(searchAdd.getText()));
        }
    };

    private void search(String s) {
        if (!isSearch) {
            Toast.makeText(ThirdBankCardActivity.this, "搜索中。。。。", Toast.LENGTH_SHORT).show();
            HttpParams params = new HttpParams();
            params.put("cityCode", code);
            params.put("bankId", "" + submitBankId);
            params.put("name", s);
            HttpUtil.getInstance().post(this, DMConstant.API_Url.SEARCH_ADDRESS, params, new HttpCallBack() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        String code = result.getString("code");
                        if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                            // 成功
                            //                        DMLog.e(result.toString());
                            JSONArray jsonArray = result.getJSONArray("data");
                            if (jsonArray.length() > 0) {
                                if (bankNum == null) {
                                    bankNum = new ArrayList<Map<String, String>>();
                                } else {
                                    bankNum.clear();
                                }
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("name", jsonArray.getJSONObject(i).getString("name"));
                                    map.put("bankNum", jsonArray.getJSONObject(i).getString("bankNum"));
                                    bankNum.add(map);
                                }
                                showCitysList(bankNum);
                            }
                        } else {
                            ErrorUtil.showError(result);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void showCitysList(ArrayList<Map<String, String>> bankNuml) {
        city_listview.setAdapter(new BankNumBaseAdapter(bankNuml));
        city_listview.setVisibility(View.VISIBLE);
        city_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                name = parent.getAdapter().getItem(position).toString();
                if (name != null) {
                    searchAdd.setText(name);
                    bankCodeNum = bankNum.get(position).get("bankNum");
                    city_listview.setVisibility(View.GONE);
                    isSearch = true;
                    et_bankUnionCode.setText(bankCodeNum);
                    DMLog.e(bankCodeNum);
                }
            }
        });
    }

    private void initData() {
        userInfo = DMApplication.getInstance().getUserInfo();
        openNameEt.setText(null == userInfo ? "" : userInfo.getName()); //开户名默认是用户真实姓名
        if ("".equals( userInfo.getUsrCustId())) {
            title.setText("开通银行存管账户");
        } else {
            title.setText(R.string.add_bank_card);
        }
    }

    private void initListener() {
        nextStep.setOnClickListener(this);
        selectBankTv.setOnClickListener(this);
        selectProvice.setOnClickListener(this);
        selectCitys.setOnClickListener(this);
    }

    public void deleteText(View view) {
        searchAdd.setText("");
        city_listview.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_step_btn:
//                submitBankInfo();
                    submitAddBankInfo();
                break;
            case R.id.select_bank_tv: {
                UIHelper.hideSrfAndRun(this, new Runnable() {
                    @Override
                    public void run() {
                        showBankPopup(bankList, selectBankTv);
                    }
                });
                break;
            }
            case R.id.select_province_tv: {
                UIHelper.hideSrfAndRun(this, new Runnable() {
                    @Override
                    public void run() {
                        showProvicePopup(provice, selectProvice);
                    }
                });
                break;
            }
            case R.id.select_city_tv: {
                UIHelper.hideSrfAndRun(this, new Runnable() {
                    @Override
                    public void run() {
                        if (name == null) {
                            ToastUtil.getInstant().show(ThirdBankCardActivity.this, R.string.add_card_choice_provice);
                        } else {
                            showCitysPopup(citys, selectCitys);
                        }
                    }
                });
                break;
            }
        }
    }

    /**
     * 点击下一步，提交信息，完成添加银行卡
     */
    private void submitAddBankInfo() {
        if (!checkParams()) {
            return;
        }
        HttpParams params = new HttpParams();
        params.put("banknumber", cardNumEt.getText().toString().trim());
        params.put("bankId", "" + submitBankId);
        params.put("bankNum", bankCodeNum);
        HttpUtil.getInstance().post(this, DMConstant.API_Url.ADDBANKCARD, params, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String code = result.getString("code");
                    if (DMConstant.ResultCode.SUCCESS.equals(code)) {
                        // 成功
                        ToastUtil.getInstant().show(ThirdBankCardActivity.this, R.string.add_card_success);
                                finish();
                    } else {
                        ErrorUtil.showError(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 检测输入的内容
     */
    private boolean checkParams() {
        if (TextUtils.isEmpty(cardNumEt.getText().toString().trim())) {
            ToastUtil.getInstant().show(this, "请输入银行卡卡号");
            return false;
        }
        if (!FormatUtil.checkBankCard(cardNumEt.getText().toString().trim())) {
            ToastUtil.getInstant().show(this, "银行卡错误");
            return false;
        }
        if (selectBankTv.getText().toString().trim().equals("选择银行")) {
            ToastUtil.getInstant().show(this, "请选择银行");
            return false;
        }
        if (bankCodeNum == null) {
            if ("".equals(et_bankUnionCode.getText().toString())|| null==et_bankUnionCode.getText().toString()){
                ToastUtil.getInstant().show(this, "请搜索您银行卡开户行或手动填写银联号");
                return false;
            }else{
                bankCodeNum=et_bankUnionCode.getText().toString().trim();
                return true;
            }
        }
        return true;
    }

    /**
     * 显示银行卡popupWindow
     */
    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")
    private void showBankPopup(final List<Bank> bandCards, final TextView selectBankTv) {
        View contentView = LayoutInflater.from(ThirdBankCardActivity.this).inflate(R.layout.popup_window, null);

        int height = getPopupWindowHeight(selectBankTv, bandCards.size()); //获取悬浮窗的高度

        popupWindow = new PopupWindow(contentView, selectBankTv.getWidth(), height, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); //加该行代码后，点击界面其他位置popupWindow会消失
        //				popupWindow.setFocusable(true);
        ListView listView = (ListView) contentView.findViewById(R.id.popup_window_listview);
        listView.setAdapter(new BankBaseAdapter(bandCards));
        popupWindow.showAsDropDown(selectBankTv);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                popupWindow.dismiss();
                Bank bank = (Bank) parent.getAdapter().getItem(position);
                if (bank != null) {
                    selectBankTv.setText(bank.getBankName());
                    submitBankId = bank.getId();
                }
            }
        });
    }

    /**
     * 显示省份popupWindow
     */
    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")
    private void showProvicePopup(final ArrayList<Map<String, String>> provice, final TextView selectProvice) {
        View contentView = LayoutInflater.from(ThirdBankCardActivity.this).inflate(R.layout.popup_window, null);

        int height = getPopupWindowHeight(selectProvice, provice.size()); //获取悬浮窗的高度

        popupWindow = new PopupWindow(contentView, selectProvice.getWidth(), height, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); //加该行代码后，点击界面其他位置popupWindow会消失
        //				popupWindow.setFocusable(true);
        ListView listView = (ListView) contentView.findViewById(R.id.popup_window_listview);
        listView.setAdapter(new ProviceBaseAdapter(provice));
        popupWindow.showAsDropDown(selectProvice);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                popupWindow.dismiss();
                name = parent.getAdapter().getItem(position).toString();
                if (name != null) {
                    selectProvice.setText(name);
                    selectCitys.setText("市区");
                    if (citys != null)
                        citys.clear();
                }
                getCitys(position);
            }
        });
    }

    /**
     * 显示省份popupWindow
     */
    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")
    private void showCitysPopup(final ArrayList<Map<String, String>> citys, final TextView selectCitys) {
        View contentView = LayoutInflater.from(ThirdBankCardActivity.this).inflate(R.layout.popup_window, null);

        int height = getPopupWindowHeight(selectCitys, citys.size()); //获取悬浮窗的高度

        popupWindow = new PopupWindow(contentView, selectCitys.getWidth(), height, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); //加该行代码后，点击界面其他位置popupWindow会消失
        //				popupWindow.setFocusable(true);
        ListView listView = (ListView) contentView.findViewById(R.id.popup_window_listview);
        listView.setAdapter(new CitysBaseAdapter(citys));
        popupWindow.showAsDropDown(selectCitys);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                popupWindow.dismiss();
                name = parent.getAdapter().getItem(position).toString();
                if (name != null) {
                    selectCitys.setText(name);
                    code = citys.get(position).get("code");
                    DMLog.e(code);
                }
            }
        });
    }

    //    /**
    //     * 显示省份popupWindow
    //     */
    //    @SuppressLint("InflateParams")
    //    @SuppressWarnings("deprecation")
    //    private void showBankNumPopup(final ArrayList<Map<String, String>> bankNum, final EditText searchAdd) {
    //        View contentView = LayoutInflater.from(ThirdBankCardActivity.this).inflate(R.layout.popup_window, null);
    //
    //        int height = getPopupWindowHeight(searchAdd, bankNum.size()); //获取悬浮窗的高度
    //
    //        popupWindow = new PopupWindow(contentView, searchAdd.getWidth(), height, true);
    //        popupWindow.setBackgroundDrawable(new BitmapDrawable()); //加该行代码后，点击界面其他位置popupWindow会消失
    //        //				popupWindow.setFocusable(true);
    //        ListView listView = (ListView) contentView.findViewById(R.id.popup_window_listview);
    //        listView.setAdapter(new BankNumBaseAdapter(bankNum));
    //        popupWindow.showAsDropDown(searchAdd);
    //        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    //            @Override
    //            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
    //                popupWindow.dismiss();
    //                name = parent.getAdapter().getItem(position).toString();
    //                if (name != null) {
    //                    searchAdd.setText(name);
    //                    isSearch = true;
    //                    bankCodeNum = bankNum.get(position).get("bankNum");
    //                    DMLog.e(bankCodeNum);
    //                }
    //            }
    //        });
    //    }

    /**
     * 设置popupWindow的高度
     */
    private int getPopupWindowHeight(View parentView, int size) {
        int[] position = new int[2];
        parentView.getLocationInWindow(position);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels; //屏高
        int height = 0;
        if (size * parentView.getHeight() < 400) {
            height = size * parentView.getHeight();
        } else {
            height = screenHeight - position[1] - parentView.getHeight() - 10;
        }
        return height;
    }


    /**
     * 银行卡ListView的适配器
     */
    public class BankBaseAdapter extends BaseAdapter {
        private List<Bank> datas;

        public BankBaseAdapter(List<Bank> datas) {
            this.datas = datas;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(ThirdBankCardActivity.this).inflate(R.layout.popup_window_lv_item, null);
            TextView tv = (TextView) convertView.findViewById(R.id.item_tv);
            tv.setText(datas.get(position).getBankName());
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public int getCount() {
            return datas.size();
        }
    }

    /**
     * 省份ListView的适配器
     */
    public class ProviceBaseAdapter extends BaseAdapter {
        private ArrayList<Map<String, String>> datas;

        public ProviceBaseAdapter(ArrayList<Map<String, String>> datas) {
            this.datas = datas;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(ThirdBankCardActivity.this).inflate(R.layout.popup_window_lv_item, null);
            TextView tv = (TextView) convertView.findViewById(R.id.item_tv);
            tv.setText(datas.get(position).get("name"));
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position).get("name");
        }

        @Override
        public int getCount() {
            return datas.size();
        }
    }

    /**
     * 市区ListView的适配器
     */
    public class CitysBaseAdapter extends BaseAdapter {
        private ArrayList<Map<String, String>> datas;

        public CitysBaseAdapter(ArrayList<Map<String, String>> datas) {
            this.datas = datas;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(ThirdBankCardActivity.this).inflate(R.layout.popup_window_lv_item, null);
            TextView tv = (TextView) convertView.findViewById(R.id.item_tv);
            tv.setText(datas.get(position).get("name"));
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position).get("name");
        }

        @Override
        public int getCount() {
            return datas.size();
        }
    }

    /**
     * 市区ListView的适配器
     */
    public class BankNumBaseAdapter extends BaseAdapter {
        private ArrayList<Map<String, String>> datas;

        public BankNumBaseAdapter(ArrayList<Map<String, String>> datas) {
            this.datas = datas;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(ThirdBankCardActivity.this).inflate(R.layout.popup_window_lv_item, null);
            TextView tv = (TextView) convertView.findViewById(R.id.item_tv);
            tv.setText(datas.get(position).get("name"));
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position).get("name");
        }

        @Override
        public int getCount() {
            return datas.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != popupWindow && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
}
