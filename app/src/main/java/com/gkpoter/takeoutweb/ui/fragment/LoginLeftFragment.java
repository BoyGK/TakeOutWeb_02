package com.gkpoter.takeoutweb.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gkpoter.takeoutweb.R;
import com.gkpoter.takeoutweb.bean.UserBean;
import com.gkpoter.takeoutweb.interface_.MyCallBack;
import com.gkpoter.takeoutweb.ui.HomeActivity;
import com.gkpoter.takeoutweb.ui.RegisterActivity;
import com.gkpoter.takeoutweb.util.DataUtils;
import com.gkpoter.takeoutweb.util.HttpUtils;
import com.gkpoter.takeoutweb.util.L;
import com.google.gson.Gson;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;

/**
 * Created by "GKpoter" on 2017/7/3.
 */

@ContentView(R.layout.fragment_login_left)
public class LoginLeftFragment extends Fragment {

    @ViewInject(R.id.login_NameOrPhone)
    EditText nameOrphone;
    @ViewInject(R.id.login_PassWord)
    EditText password;
    @ViewInject(R.id.login_SIgnIn)
    Button login;
    @ViewInject(R.id.login_SignUp)
    TextView register;

    private LoginCall call;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  x.view().inject(this, inflater, container);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        call = new LoginCall() {
            @Override
            public void success(UserBean user) {
                L.i(user.getData().getAk());
                DataUtils util = new DataUtils("userbean", getActivity());
                util.clearData();
                util.saveData("username", user.getData().getUsername());
                util.saveData("phone", user.getData().getPhone());
                util.saveData("password", user.getData().getUsername());
                util.saveData("ak", user.getData().getAk());
                util.saveData("userPhoto", user.getData().getUserPhoto());
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finish();
            }

            @Override
            public void error(String msg) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage(msg)
                        .setPositiveButton("确定", null)
                        .create();
                alertDialog.show();
            }
        };
    }

    @Event(value = {R.id.login_SIgnIn, R.id.login_SignUp})
    private void doClick(View view) {
        switch (view.getId()) {
            case R.id.login_SIgnIn:
                doLogin();
                break;
            case R.id.login_SignUp:
                doRegister();
                break;
        }
    }

    private void doRegister() {
        startActivity(new Intent(getActivity(), RegisterActivity.class));
    }

    private void doLogin() {
        if (TextUtils.isEmpty(nameOrphone.getText())) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle("提示")
                    .setMessage("用户名或手机号不能为空")
                    .setPositiveButton("确定", null)
                    .create();
            alertDialog.show();
            return;
        }
        if (TextUtils.isEmpty(password.getText())) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle("提示")
                    .setMessage("密码不能为空")
                    .setPositiveButton("确定", null)
                    .create();
            alertDialog.show();
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", nameOrphone.getText().toString());
        map.put("password", password.getText().toString());
        HttpUtils.Post("v1/market/login", map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                UserBean user = new Gson().fromJson(result, UserBean.class);
                if (user != null && user.getRet() != 0) {
                    call.error(user.getMsg());
                } else {
                    call.success(user);
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

    //登陆回调接口
    private interface LoginCall {
        void success(UserBean user);

        void error(String msg);
    }

}
