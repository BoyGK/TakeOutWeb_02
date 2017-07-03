package com.gkpoter.takeoutweb.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gkpoter.takeoutweb.R;
import com.gkpoter.takeoutweb.bean.BaseBean;
import com.gkpoter.takeoutweb.bean.UserBean;
import com.gkpoter.takeoutweb.interface_.MyCallBack;
import com.gkpoter.takeoutweb.util.HttpUtils;
import com.google.gson.Gson;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;

/**
 * Created by "GKpoter" on 2017/7/3.
 */

@ContentView(R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity {

    @ViewInject(R.id.register_NameOrPhone)
    EditText nameOrphone;
    @ViewInject(R.id.register_PassWord)
    EditText password;
    @ViewInject(R.id.register_code)
    EditText code;

    @ViewInject(R.id.register_SignIn)
    Button signIn;
    @ViewInject(R.id.register_getCode)
    Button getCode;

    RegisterCall call;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        call=new RegisterCall() {
            @Override
            public void success() {
                finish();
            }

            @Override
            public void error(String msg) {
                AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this)
                        .setTitle("提示")
                        .setMessage(msg)
                        .setPositiveButton("确定", null)
                        .create();
                alertDialog.show();
            }
        };
    }

    @Event(value = {R.id.register_SignIn, R.id.register_getCode})
    private void doClick(View view) {
        switch (view.getId()) {
            case R.id.register_getCode:
                requestCode();
                break;
            case R.id.register_SignIn:
                doRegister();
                break;
        }
    }

    //获取验证码
    private void requestCode() {
        if (TextUtils.isEmpty(nameOrphone.getText())) {
            AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("提示")
                    .setMessage("手机号不能为空")
                    .setPositiveButton("确定", null)
                    .create();
            alertDialog.show();
            return;
        }
        HashMap<String,String>map=new HashMap<>();
        map.put("phone",nameOrphone.getText().toString());
        HttpUtils.Get("v1/market/send_sms", map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                BaseBean bean=new Gson().fromJson(result,BaseBean.class);
                if (bean!=null&&bean.getRet()==0) {
                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("提示")
                            .setMessage("发送成功")
                            .setPositiveButton("确定", null)
                            .create();
                    alertDialog.show();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("提示")
                            .setMessage("发送失败，发送的频率太快")
                            .setPositiveButton("确定", null)
                            .create();
                    alertDialog.show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //注册
    private void doRegister() {
        if (TextUtils.isEmpty(nameOrphone.getText())) {
            AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("提示")
                    .setMessage("手机号不能为空")
                    .setPositiveButton("确定", null)
                    .create();
            alertDialog.show();
            return;
        }
        if (TextUtils.isEmpty(password.getText())) {
            AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("提示")
                    .setMessage("密码不能为空")
                    .setPositiveButton("确定", null)
                    .create();
            alertDialog.show();
            return;
        }
        if (TextUtils.isEmpty(code.getText())) {
            AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("提示")
                    .setMessage("店铺名称不能为空")
                    .setPositiveButton("确定", null)
                    .create();
            alertDialog.show();
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", nameOrphone.getText().toString());
        map.put("password", password.getText().toString());
        map.put("code", code.getText().toString());
        HttpUtils.Post("v1/market/register_by_phone", map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                UserBean user = new Gson().fromJson(result,UserBean.class);
                if (user!=null&&user.getRet()!=0) {
                    call.error(user.getMsg());
                } else {
                    call.success();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //注册回调接口
    private interface RegisterCall {
        void success();

        void error(String msg);
    }

}
