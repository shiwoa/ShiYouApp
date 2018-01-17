package com.jiaoyu.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoyu.base.BaseActivity;
import com.jiaoyu.entity.PublicEntity;
import com.jiaoyu.entity.PublicEntityCallback;
import com.jiaoyu.shiyou.R;
import com.jiaoyu.utils.Address;
import com.jiaoyu.utils.SharedPreferencesUtils;
import com.jiaoyu.utils.ToastUtil;
import com.jiaoyu.utils.ValidateUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by bishuang on 2018/1/9.
 * 登陆的类
 */

public class LoginActivity extends BaseActivity{

    private TextView toRegister,title,forget,logon,errorMessage; //去注册 标题 忘记密码 登陆
    private LinearLayout back; //返回
    private EditText phone,pwd; //手机号 密码


    /**
     * 初始化布局资源文件
     */
    @Override
    protected int initContentView() {
        return R.layout.act_login;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        toRegister = (TextView) findViewById(R.id.login_to_register);
        title = (TextView) findViewById(R.id.public_head_title);
        title.setText(R.string.login_title);
        back = (LinearLayout) findViewById(R.id.public_head_back);
        forget = (TextView) findViewById(R.id.login_forget_pwd);
        phone = (EditText) findViewById(R.id.phone);
        pwd = (EditText) findViewById(R.id.pwd);
        logon = (TextView) findViewById(R.id.login);
        errorMessage = (TextView) findViewById(R.id.login_error_toast);
    }

    /**
     * 初始化监听事件
     */
    @Override
    protected void addListener() {
        //登录
        logon.setOnClickListener(view -> {
            String userName = phone.getText().toString(); //用户手机号
            String passWord = pwd.getText().toString(); //用户密码
            if (TextUtils.isEmpty(userName)) {
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("请输入手机号");
                return;
            }
            if (TextUtils.isEmpty(passWord)) {
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("请输入密码");
                return;
            }
            if (!ValidateUtil.isMobile(userName)) {
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("请输入正确的手机号");
                return;
            }
            if (!(passWord.length() >= 6 && passWord.length() <= 18)||!ValidateUtil.isNumberOrLetter(passWord)) {
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("输入密码格式不正确");
                return;
            }
            // 联网登录的方法
            getLogin(userName, passWord);
        });
        //忘记密码
        forget.setOnClickListener(view -> openActivity(ForgetPwdActivity.class));
        //去注册
        toRegister.setOnClickListener(view ->{
            openActivity(RegisterActivity.class);
            LoginActivity.this.finish();
          });
        //返回
        back.setOnClickListener(view -> LoginActivity.this.finish());
    }

    /**
     * 数据加载失败重新加载
     */
    @Override
    protected void onDataReload() {

    }

    //  登陆的方法
    public void getLogin(String account, String userPassword) {
        Map<String, String> map = new HashMap<>();
        map.put("account", account);
        map.put("userPassword", userPassword);
        showLoading();
        OkHttpUtils.get().params(map).url(Address.LOGIN).build().execute(
                new PublicEntityCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PublicEntity response, int id) {
                        cancelLoading();
                        if (!TextUtils.isEmpty(response.toString())) {
                            try {
                                String message = response.getMessage();
                                if (response.isSuccess()) {
                                    //登陆成功的方法
                                    LoginScuessMethod(response);
                                } else {
                                    ToastUtil.showWarning(LoginActivity.this,message);
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                });
    }

    /**
     * 方法说明:登陆成功的方法
     */
    private void LoginScuessMethod(PublicEntity publicEntity) {

        int userId = publicEntity.getEntity().getId();
        //判断用户是否存在其他地方登录标记
        String memTime = publicEntity.getEntity().getMemTime();
        SharedPreferencesUtils.setParam(this, "userId", userId);
        SharedPreferencesUtils.setParam(this, "memTime", memTime);
        LoginActivity.this.finish();
    }
}
