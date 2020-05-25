package com.bkk.qqlogin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MyLog";

    private Button btnLogin;
    private TextView tvMsg;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_main_activity_login:
                    if ("登录".equals(btnLogin.getText().toString())) {
                        showMsg("正在登录...");
                        QQLoginManager.login(MainActivity.this);
                    } else {
                        QQLoginManager.logout(MainActivity.this);
                        btnLogin.setText("登录");
                        showMsg("您已退出登录");
                    }
                    break;
                case R.id.btn_main_activity_check:
                    showMsg("正在检查登录状态...");
                    QQLoginManager.checkLogin(new QQLoginManager.QQCheckCallback() {
                        @Override
                        public void onCallback(boolean login, JSONObject json) {
                            Log.d(TAG, "onCheckCallback: login=" + login + "  msg=" + json.toString());
                            showMsg("检查登录状态结果: （true代表已登录，false代表未登录）\nlogin = " + login + "\nmsg = " + json.toString());

                            btnLogin.setText(login ? "退出登录" : "登录");
                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QQLoginManager.init(this, "1110529440");
        QQLoginManager.setQQLoginListener(new QQLoginManager.QQLoginListener() {
            @Override
            public void onQQLoginSuccess(JSONObject jsonObject) {
                showMsg(jsonObject.toString());
                btnLogin.setText("退出登录");
            }

            @Override
            public void onQQLoginCancel() {
                showMsg("登录取消");
            }

            @Override
            public void onQQLoginError(UiError uiError) {
                showMsg("登录出错：" + uiError.toString());
            }
        });

        tvMsg = findViewById(R.id.tv_main_activity_msg);
        showMsg("check按钮：检查登录状态\n登录按钮：拉起QQ登录");

        btnLogin = findViewById(R.id.btn_main_activity_login);
        btnLogin.setOnClickListener(onClickListener);

        Button btnCheck = findViewById(R.id.btn_main_activity_check);
        btnCheck.setOnClickListener(onClickListener);
    }

    private void showMsg(String msg) {
        tvMsg.setText(msg == null ? "" : msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        QQLoginManager.onActivityResultData(requestCode, resultCode, data);
    }
}
