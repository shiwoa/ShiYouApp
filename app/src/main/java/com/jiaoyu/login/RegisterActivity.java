package com.jiaoyu.login;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.entity.PublicEntity;
import com.jiaoyu.entity.PublicEntityCallback;
import com.jiaoyu.entity.PublicStringEntity;
import com.jiaoyu.entity.PublicStringEntityCallback;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.utils.Address;
import com.jiaoyu.utils.LogUtils;
import com.jiaoyu.utils.ToastUtil;
import com.jiaoyu.utils.ValidateUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by bishuang on 2018/1/9.
 * 注册的类
 */

public class RegisterActivity extends BaseActivity{

    private TextView toLogin,title,yanzhengBtn,registerBtn,errorMessage; //去登陆 标题 获取验证码 注册 错误信息
    private EditText yanzhengNum,passWordEdit,confirmPassWordEdit; // 验证码
    private LinearLayout back; //返回
    private Boolean isCountdown = false; //标记是否发送验证码
    private EditText phone;
    private CheckBox checkBox;


    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_register;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        toLogin = (TextView) findViewById(R.id.reg_to_login);
        title = (TextView) findViewById(R.id.public_head_title);
        title.setText(R.string.register_title);
        back = (LinearLayout) findViewById(R.id.public_head_back);
        yanzhengBtn = (TextView) findViewById(R.id.yanzheng_btn);
        phone = (EditText) findViewById(R.id.phone);
        registerBtn = (TextView) findViewById(R.id.register_btn);
        errorMessage = (TextView) findViewById(R.id.login_error_toast);
        yanzhengNum = (EditText) findViewById(R.id.yanzheng_num);
        passWordEdit = (EditText) findViewById(R.id.pwd);
        confirmPassWordEdit = (EditText) findViewById(R.id.confirm_pwd);
        checkBox = (CheckBox) findViewById(R.id.check_box);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //获取验证码
        yanzhengBtn.setOnClickListener(view -> {
            String mobile = phone.getText().toString();
            if (!isCountdown) {
                if (TextUtils.isEmpty(mobile)) {
                    ToastUtil.showWarning(RegisterActivity.this,"请输入手机号");
                    return;
                }
                if (!ValidateUtil.isMobile(mobile)) {
                    ToastUtil.showWarning(RegisterActivity.this,"请输入正确的手机号");
                    return;
                }
                //手机注册,获取sgin值
                getSginData(mobile);
            }
        });
        //去登陆
        toLogin.setOnClickListener(view -> {
            openActivity(LoginActivity.class);
            RegisterActivity.this.finish();
        });
        //返回
        back.setOnClickListener(view -> RegisterActivity.this.finish());
        //注册
        registerBtn.setOnClickListener(view -> {
            String mobile = phone.getText().toString();
            errorMessage.setVisibility(View.GONE);
            String code = yanzhengNum.getText().toString();
            String passWord = passWordEdit.getText().toString();
            String confirm_passWord = confirmPassWordEdit.getText()
                    .toString();
            if (TextUtils.isEmpty(mobile)) {
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("请输入手机号");
                return;
            }
                if (TextUtils.isEmpty(code)) {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("请输入验证码");
                    return;
                }
            if (TextUtils.isEmpty(passWord)) {
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("请输入密码");
                return;
            }
            if (TextUtils.isEmpty(confirm_passWord)) {
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("请确认密码");
                return;
            }
            if (!ValidateUtil.isMobile(mobile)) {
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("请输入正确的手机号");
                return;
            }
            if (!(passWord.length() >= 6 && passWord.length() <= 18)||!ValidateUtil.isNumberOrLetter(passWord)) {
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("输入密码格式不正确");
                return;
            }
            if (!confirm_passWord.equals(passWord)) {
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("两次密码不对应");
                return;
            }
            boolean checked = checkBox.isChecked();
            if(!checked){
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("请阅读并遵守协议方可注册");
                return;
            }
                //联网注册的方法
                getRegisterNF(mobile,code,passWord,confirm_passWord);
        });
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }

    /**
     * 方法说明:注册的方法_手机验证码为ON 邮箱验证码为OF
     */
    private void getRegisterNF(final String mobile,String code,final String userPassword,String confirmPwd){
        Map<String,String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("userPassword", userPassword);
        map.put("confirmPwd", confirmPwd);
        map.put("mobileCheckCode", code); //手机验证码
        showLoading();
        OkHttpUtils.post().params(map).url(Address.REGISTER).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        cancelLoading();
                        String message = response.getMessage();
                        if(response.isSuccess()){
                            Intent intent = new Intent();
                            intent.setClass(RegisterActivity.this,LoginActivity.class);
                            intent.putExtra("userName", mobile);
                            intent.putExtra("passWord", userPassword);
                            startActivity(intent);
                            RegisterActivity.this.finish();
                        }else{
                            ToastUtil.showWarning(RegisterActivity.this,message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });

    }

    /**
     * 方法说明:获取sgin值的方法
     */
    private void getSginData(final String mobile) {
        Map<String, String> map = new HashMap<>();
        map.put("mobileType", "Android");
        map.put("mobile", mobile);
        showLoading();
        OkHttpUtils.post().params(map).url(Address.GET_SGIN).build().execute(new PublicStringEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicStringEntity response, int id) {
                if(!TextUtils.isEmpty(response.toString())){
                    try {
                        String message = response.getMessage();
                        if(response.isSuccess()){
                            String sgin = response.getEntity();
                            // 联网获取验证码的方法
                            getVerificationCode(mobile,sgin);
                        }else{
                            ToastUtil.showWarning(RegisterActivity.this,message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });

    }

    /**
     * 方法说明:获取验证码的接口
     */
    private void getVerificationCode(String mobile,String sgin) {
        Map<String,String> map = new HashMap<>();
        map.put("sendType", "register");
        map.put("mobile", mobile);
        map.put("mobileType", "Android");
        map.put("sign", sgin);
        OkHttpUtils.post().params(map).url(Address.GET_PHONE_CODE).build().execute(new PublicEntityCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(PublicEntity response, int id) {
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        cancelLoading();
                        String message = response.getMessage();
                        if (response.isSuccess()) {
                            ToastUtil.showWarning(RegisterActivity.this,message);
                            isCountdown = true;
                            //开启线程
                            startTheard();
                        }else{
                            ToastUtil.showWarning(RegisterActivity.this,message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });

    }

    /**
     * 方法说明:开启倒计时的线程
     */
    private void startTheard() {
        new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                yanzhengBtn.setText("重新获取" + millisUntilFinished/ 1000 + "秒");
            }

            @Override
            public void onFinish() {
                yanzhengBtn.setText("获取验证码");
                isCountdown = false;
            }
        }.start();
    }

}
