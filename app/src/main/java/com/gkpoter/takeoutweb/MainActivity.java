package com.gkpoter.takeoutweb;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.gkpoter.takeoutweb.bean.UserBean;
import com.gkpoter.takeoutweb.bean.VersionBean;
import com.gkpoter.takeoutweb.interface_.MyCallBack;
import com.gkpoter.takeoutweb.ui.HomeActivity;
import com.gkpoter.takeoutweb.ui.LoginActivity;
import com.gkpoter.takeoutweb.util.DataUtils;
import com.gkpoter.takeoutweb.util.HttpUtils;
import com.gkpoter.takeoutweb.util.L;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.util.HashMap;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private VersionCall call = new VersionCall() {
        @Override
        public void listener(final String url, int code) {
            if (code == 1) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("版本更新")
                        .setMessage("必要组件更新!")
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateCall.update(url);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .create();
                alertDialog.show();
            } else if (code == 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("版本更新")
                        .setMessage("重要组件更新!")
                        .setCancelable(true)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateCall.update(url);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                            }
                        })
                        .create();
                alertDialog.show();
            }
        }
    };

    private UpdateCall updateCall = new UpdateCall() {
        @Override
        public void update(String url) {
            HttpUtils.Get(url, null, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {

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
    };

    private LoginCall call_l = new LoginCall() {
        @Override
        public void success(UserBean user) {
            DataUtils util = new DataUtils("userbean", getApplicationContext());
            util.clearData();
            util.saveData("username", user.getData().getUsername());
            util.saveData("phone", user.getData().getPhone());
            util.saveData("password", user.getData().getUsername());
            util.saveData("ak", user.getData().getAk());
            util.saveData("userPhoto", user.getData().getUserPhoto());
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }

        @Override
        public void error() {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        HashMap<String, String> map = new HashMap<>();
        HttpUtils.Get("v1/market/check_version", map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                VersionBean version = new Gson().fromJson(result, VersionBean.class);
                if (version != null && version.getRet() != -1) {
                    if (!getVersionCode(MainActivity.this).equals(version.getData().getVersionCode())) {
                        call.listener(version.getData().getUrl(), version.getData().getVersionCode());
                    } else {
                        L.i("not need update:" + version.getData().getVersionDescription());
                        doLogin();
                        //startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        //finish();
                    }
                } else {
                    L.i(version.getMsg());
                    doLogin();
                    //startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    //finish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, "网络错误!!!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //登录操作
    private void doLogin() {
        final DataUtils util = new DataUtils("userbean",getApplicationContext());
        String ak = util.getData("ak","");
        if(!ak.equals("")){
            HashMap<String,String>map=new HashMap<>();
            map.put("ak",ak);
            HttpUtils.Get("v1/market/get_user_info", map, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    UserBean bean = new Gson().fromJson(result,UserBean.class);
                    if(bean!=null&&bean.getRet()!=0){
                        call_l.error();
                    }else{
                        call_l.success(bean);
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
    }

    /**
     * get App versionCode
     *
     * @param context
     * @return
     */
    private String getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * get App versionName
     *
     * @param context
     * @return
     */
    private String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    //版本检测回调
    private interface VersionCall {
        void listener(String url, int code);
    }

    private interface UpdateCall {
        void update(String url);
    }

    //登陆回调接口
    private interface LoginCall {
        void success(UserBean user);

        void error();
    }

}
