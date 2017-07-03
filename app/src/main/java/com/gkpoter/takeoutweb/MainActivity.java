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

import com.gkpoter.takeoutweb.bean.VersionBean;
import com.gkpoter.takeoutweb.interface_.MyCallBack;
import com.gkpoter.takeoutweb.ui.LoginActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        HashMap<String, String> map = new HashMap<>();
        HttpUtils.Get("v1/restaurant/check_version", map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                VersionBean version = new Gson().fromJson(result, VersionBean.class);
                if (version != null && version.getRet() != -1) {
                    if (!getVersionCode(MainActivity.this).equals(version.getData().getVersionCode())) {
                        call.listener(version.getData().getUrl(), version.getData().getVersionCode());
                    } else {
                        L.i("not need update:" + version.getData().getVersionDescription());
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                } else {
                    L.i(version.getMsg());
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
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

}
