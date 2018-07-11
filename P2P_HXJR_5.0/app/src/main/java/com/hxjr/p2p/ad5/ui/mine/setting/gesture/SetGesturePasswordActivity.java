package com.hxjr.p2p.ad5.ui.mine.setting.gesture;

import java.util.ArrayList;
import java.util.List;

import com.hxjr.p2p.ad5.DMApplication;
import com.hxjr.p2p.ad5.bean.LockPwd;
import com.hxjr.p2p.ad5.ui.BaseActivity;
import com.hxjr.p2p.ad5.ui.MainActivity;
import com.hxjr.p2p.ad5.utils.DMConstant;
import com.hxjr.p2p.ad5.utils.SharedPreferenceUtils;
import com.hxjr.p2p.ad5.R;
import com.dm.db.DbUtils;
import com.dm.db.exception.DbException;
import com.dm.db.sqlite.Selector;
import com.dm.utils.AppManager;
import com.dm.utils.DMLog;
import com.dm.utils.EncrypUtil;
import com.dm.utils.ThreadsPool;
import com.dm.widgets.LockPatternView;
import com.dm.widgets.LockPatternView.Cell;
import com.dm.widgets.LockPatternView.DisplayMode;
import com.dm.widgets.utils.LockPatternUtils;
import com.dm.widgets.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SetGesturePasswordActivity extends BaseActivity {
    private static final String LOG_TAG = SetGesturePasswordActivity.class.getCanonicalName();

    private static final int ID_EMPTY_MESSAGE = -1;

    private static final String KEY_UI_STAGE = "uiStage";

    private static final String KEY_PATTERN_CHOICE = "chosenPattern";

    private LockPatternView mLockPatternView;

    //  private Button mFooterRightButton;
    //  private Button mFooterLeftButton;
    protected TextView mHeaderText;

    protected List<Cell> mChosenPattern = null;

    private Stage mUiStage = Stage.Introduction;

    //  private View mPreviewViews[][] = new View[3][3];
    /**
     * The patten used during the help screen to show how to draw a pattern.
     */
    private final List<Cell> mAnimatePattern = new ArrayList<Cell>();

    /**
     * The states of the left footer button.
     */
    enum LeftButtonMode {
        Cancel(android.R.string.cancel, true), CancelDisabled(android.R.string.cancel, false), Retry(
                R.string.lockpattern_retry_button_text,
                true), RetryDisabled(R.string.lockpattern_retry_button_text, false), Gone(ID_EMPTY_MESSAGE, false);

        /**
         * @param text    The displayed text for this mode.
         * @param enabled Whether the button should be enabled.
         */
        LeftButtonMode(int text, boolean enabled) {
            this.text = text;
            this.enabled = enabled;
        }

        final int text;

        final boolean enabled;
    }

    /**
     * The states of the right button.
     */
    enum RightButtonMode {
        Continue(R.string.lockpattern_continue_button_text, true), ContinueDisabled(R.string
                .lockpattern_continue_button_text,
                false), Confirm(R.string.lockpattern_confirm_button_text,
                true), ConfirmDisabled(R.string.lockpattern_confirm_button_text, false), Ok(android.R.string.ok, true);

        /**
         * @param text    The displayed text for this mode.
         * @param enabled Whether the button should be enabled.
         */
        RightButtonMode(int text, boolean enabled) {
            this.text = text;
            this.enabled = enabled;
        }

        final int text;

        final boolean enabled;
    }

    /**
     * Keep track internally of where the user is in choosing a pattern.
     */
    protected enum Stage {

        Introduction(R.string.lockpattern_recording_intro_header, LeftButtonMode.Cancel, RightButtonMode
                .ContinueDisabled,
                ID_EMPTY_MESSAGE, true), HelpScreen(R.string.lockpattern_settings_help_how_to_record, LeftButtonMode
                .Gone,
                RightButtonMode.Ok, ID_EMPTY_MESSAGE, false), ChoiceTooShort(R.string
                .lockpattern_recording_incorrect_too_short,
                LeftButtonMode.Retry, RightButtonMode.ContinueDisabled, ID_EMPTY_MESSAGE,
                true), FirstChoiceValid(R.string.lockpattern_pattern_entered_header, LeftButtonMode.Retry,
                RightButtonMode.Continue, ID_EMPTY_MESSAGE, false), NeedToConfirm(R.string.lockpattern_need_to_confirm,
                LeftButtonMode.Cancel, RightButtonMode.ConfirmDisabled, ID_EMPTY_MESSAGE, true), ConfirmWrong(
                R.string.lockpattern_need_to_unlock_wrong, LeftButtonMode.Cancel, RightButtonMode.ConfirmDisabled,
                ID_EMPTY_MESSAGE, true), ChoiceConfirmed(R.string.lockpattern_pattern_confirmed_header,
                LeftButtonMode.Cancel, RightButtonMode.Confirm, ID_EMPTY_MESSAGE,
                false), VALIDOLDPWD(R.string.lockpattern_pattern_confirmed_header, LeftButtonMode.Cancel,
                RightButtonMode.Confirm, -1, true);

        /**
         * @param headerMessage  The message displayed at the top.
         * @param leftMode       The mode of the left button.
         * @param rightMode      The mode of the right button.
         * @param footerMessage  The footer message.
         * @param patternEnabled Whether the pattern widget is enabled.
         */
        Stage(int headerMessage, LeftButtonMode leftMode, RightButtonMode rightMode, int footerMessage, boolean
                patternEnabled) {
            this.headerMessage = headerMessage;
            this.leftMode = leftMode;
            this.rightMode = rightMode;
            this.footerMessage = footerMessage;
            this.patternEnabled = patternEnabled;
        }

        final int headerMessage;

        final LeftButtonMode leftMode;

        final RightButtonMode rightMode;

        final int footerMessage;

        final boolean patternEnabled;
    }

    private void showToast(CharSequence message) {
        ToastUtil.getInstant().show(this, message);
    }

    private boolean isSuccess = false;

    /**
     * 从哪个页面跳过来的
     */
    private String from;

    /**
     * 用户名
     */
    private String account;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 是否重置手势密码
     */
    private boolean isResetGesturePwd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DMLog.i(LOG_TAG, "onCreate");
        from = getIntent().getStringExtra("from");
        account = getIntent().getStringExtra("account");
        password = getIntent().getStringExtra("password");
        setContentView(R.layout.pwd_shoushi_setting_new);
        if (!TextUtils.isEmpty(from) && from.equals("login")) {
            findViewById(R.id.btn_back).setVisibility(View.GONE);//禁止返回
        }

        // 初始化演示动画
        mAnimatePattern.add(Cell.of(0, 0));
        mAnimatePattern.add(Cell.of(0, 1));
        mAnimatePattern.add(Cell.of(1, 1));
        mAnimatePattern.add(Cell.of(2, 1));
        mAnimatePattern.add(Cell.of(2, 2));

        mLockPatternView = (LockPatternView) this.findViewById(R.id.gesturepwd_create_lockview);
        mHeaderText = (TextView) findViewById(R.id.gesturepwd_create_text);
        mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
        mLockPatternView.setTactileFeedbackEnabled(true);

        mLockPatternView.setDisplayMode(DisplayMode.Correct);
        updateStage(Stage.Introduction);
        super.initView();
        isResetGesturePwd = getIntent().getBooleanExtra("isResetGesturePwd", false);
        if (isResetGesturePwd) {
            ((TextView) findViewById(R.id.title_text)).setText(getString(R.string.title_resetting_gesture_password));
        } else {
            ((TextView) findViewById(R.id.title_text)).setText(getString(R.string.title_setting_gesture_password));
        }

        // 设置返回按钮事件
        findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(from) && from.equals("login")) {
                    //临时保存上次登录的密码
                    String loginPwd = EncrypUtil.encrypt(password, DMConstant.StringConstant.ENCRYP_SEND);
                    SharedPreferenceUtils.put(SetGesturePasswordActivity.this, "loginTemp", "account", account);
                    SharedPreferenceUtils.put(SetGesturePasswordActivity.this, "loginTemp", "password", loginPwd);
                }
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_UI_STAGE, mUiStage.ordinal());
        if (mChosenPattern != null) {
            outState.putString(KEY_PATTERN_CHOICE, LockPatternUtils.patternToString(mChosenPattern));
        }
    }

    /**
     * {@inheritDoc}
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Runnable mClearPatternRunnable = new Runnable() {
        @Override
        public void run() {
            mLockPatternView.clearPattern();
        }
    };

    protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener
            () {

        @Override
        public void onPatternStart() {
            mLockPatternView.removeCallbacks(mClearPatternRunnable);
            patternInProgress();
        }

        @Override
        public void onPatternCleared() {
            mLockPatternView.removeCallbacks(mClearPatternRunnable);
        }

        @Override
        public void onPatternDetected(List<Cell> pattern) {
            if (pattern == null) {
                return;
            }
            // Log.i("way", "result = " + pattern.toString());
            if (mUiStage == Stage.NeedToConfirm || mUiStage == Stage.ConfirmWrong) {
                if (mChosenPattern == null) {
                    throw new IllegalStateException("null chosen pattern in stage 'need to confirm");
                }
                if (mChosenPattern.equals(pattern)) {
                    updateStage(Stage.ChoiceConfirmed);
                    //modea
                    saveChosenPatternAndFinish();
                } else {
                    updateStage(Stage.ConfirmWrong);
                    //mode
                    mChosenPattern = null;
                    mLockPatternView.clearPattern();
                    updateStage(Stage.Introduction);
                    ToastUtil.getInstant().show(SetGesturePasswordActivity.this, R.string.tow_pwd_error);
                }
            } else if (mUiStage == Stage.Introduction || mUiStage == Stage.ChoiceTooShort) {
                if (pattern.size() < LockPatternUtils.MIN_LOCK_PATTERN_SIZE) {
                    updateStage(Stage.ChoiceTooShort);
                } else {
                    mChosenPattern = new ArrayList<Cell>(pattern);
                    updateStage(Stage.FirstChoiceValid);

                    updateStage(Stage.NeedToConfirm);
                }
            } else {
                throw new IllegalStateException("Unexpected stage " + mUiStage + " when " + "entering the pattern.");
            }
        }

        @Override
        public void onPatternCellAdded(List<Cell> pattern) {

        }

        private void patternInProgress() {
            mHeaderText.setText(R.string.lockpattern_recording_inprogress);
        }
    };

    private void updateStage(Stage stage) {
        mUiStage = stage;
        if (stage == Stage.ChoiceTooShort) {
            mHeaderText.setText(getResources().getString(stage.headerMessage, LockPatternUtils.MIN_LOCK_PATTERN_SIZE));
        } else {
            mHeaderText.setText(stage.headerMessage);
        }

        // same for whether the patten is enabled
        if (stage.patternEnabled) {
            mLockPatternView.enableInput();
        } else {
            mLockPatternView.disableInput();
        }

        mLockPatternView.setDisplayMode(DisplayMode.Correct);

        switch (mUiStage) {
            case Introduction:
                mLockPatternView.clearPattern();
                break;
            case HelpScreen:
                mLockPatternView.setPattern(DisplayMode.Animate, mAnimatePattern);
                break;
            case ChoiceTooShort:
                mLockPatternView.setDisplayMode(DisplayMode.Wrong);
                postClearPatternRunnable();
                break;
            case FirstChoiceValid:
                break;
            case NeedToConfirm:
                mLockPatternView.clearPattern();
                //          updatePreviewViews();
                break;
            case ConfirmWrong:
                mLockPatternView.setDisplayMode(DisplayMode.Wrong);
                postClearPatternRunnable();
                break;
            case ChoiceConfirmed:
                break;
            case VALIDOLDPWD:
                break;
            default:
                break;
        }

    }

    // clear the wrong pattern unless they have started a new one
    // already
    private void postClearPatternRunnable() {
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
        mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
    }

    private DbUtils db;

    private LockPwd lockPwd;

    private void saveChosenPatternAndFinish() {
        isSuccess = true;
        saveLockPwdToDb();//将手势密码保存到数据库
        SharedPreferenceUtils.clear(this, "loginTemp");//删除可能存在的垃圾数据
        if (isResetGesturePwd) {
            showToast("手势密码重置成功");
        } else {
            showToast("手势密码设置成功");
        }
        if (!TextUtils.isEmpty(from) && from.equals("login")) {
            Activity main = AppManager.getAppManager().getActivity(MainActivity.class);
            if (DMApplication.toLoginValue == 3) {
                MainActivity.index = 3;
            } else if (DMApplication.toLoginValue == 2) {
                MainActivity.index = 2;
            }
            if (main == null) {//走到这里，表示是在解锁界面点击了使用其他账号，或者忘记了手势密码
                Intent intent = new Intent(SetGesturePasswordActivity.this, MainActivity.class);
                intent.putExtra("index", 3);
                startActivity(intent);
            }
        }
        finish();
    }

    /***
     * 将手势密码保存到数据库
     */
    private void saveLockPwdToDb() {
        final String pwd = LockPatternUtils.patternToString(mChosenPattern);
        if (db == null) {
            db = DbUtils.create(this);
        }
        ThreadsPool.executeOnExecutor(new Runnable() {
            @Override
            public void run() {
                try {
                    //将手势密码保存到数据库
                    db.createTableIfNotExist(LockPwd.class);
                    int userId = DMApplication.getInstance().getUserInfo().getId();
                    lockPwd = db.findFirst(Selector.from(LockPwd.class).where("userId", "=", userId));
                    if (lockPwd == null) {
                        lockPwd = new LockPwd();
                    }
                    String myPwd = EncrypUtil.encrypt(pwd, DMConstant.StringConstant.ENCRYP_SEND);
                    String loginPwd = EncrypUtil.encrypt(password, DMConstant.StringConstant.ENCRYP_SEND);
                    lockPwd.setPwd(myPwd);
                    lockPwd.setUserId(userId);
                    lockPwd.setAccount(account);
                    lockPwd.setLoginPwd(loginPwd);
                    lockPwd.setHasSetLockPwd(true);
                    db.saveOrUpdate(lockPwd);
                    SharedPreferenceUtils.put(SetGesturePasswordActivity.this, SharedPreferenceUtils.KEY_HAS_LOCKED,
                            true);
                } catch (DbException e) {
                    e.printStackTrace();
                } finally {
                    db.close();
                }
            }
        });
    }

    /**
     * 重新开始设置手势密码
     *
     * @param view
     */
    public void reStartSet(View view) {
        updateStage(Stage.Introduction);
    }

    /**
     * 取消设置手势密码
     *
     * @param view
     */
    public void cancel(View view) {
        Activity activity = AppManager.getAppManager().getActivity(MainActivity.class);
        if (activity == null) {
            //跳转到首页
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !TextUtils.isEmpty(from) && from.equals("login")) {
            //临时保存上次登录的密码
            String loginPwd = EncrypUtil.encrypt(password, DMConstant.StringConstant.ENCRYP_SEND);
            SharedPreferenceUtils.put(this, "loginTemp", "account", account);
            SharedPreferenceUtils.put(this, "loginTemp", "password", loginPwd);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
