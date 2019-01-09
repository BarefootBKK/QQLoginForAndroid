package com.example.qqlogin.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qqlogin.entity.User;
import com.example.qqlogin.manager.QQLoginManager;
import com.example.qqlogin.R;
import com.example.qqlogin.util.ToastUtil;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements QQLoginManager.QQLoginListener {

    private ImageView qqImageView;
    private TextView qqHintTextView;
    private QQLoginManager qqLoginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qqLoginManager = new QQLoginManager("app_id", this);
        ToastUtil.initToast(this);

        qqImageView = findViewById(R.id.main_qq_img);
        qqHintTextView = findViewById(R.id.main_hint);

        qqImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qqLoginManager.launchQQLogin();
                qqHintTextView.setText("正在发起QQ授权登录，请稍等...");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        qqLoginManager.onActivityResultData(requestCode, resultCode, data);
    }

    @Override
    public void onQQLoginSuccess(JSONObject jsonObject) {
        qqHintTextView.setText("点击调用QQ登录");
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra("extra_data", new User(jsonObject));
        startActivity(intent);
        ToastUtil.showToast(this, "登录成功");
    }

    @Override
    public void onQQLoginCancel() {
        qqHintTextView.setText("点击调用QQ登录");
        ToastUtil.showToast(this, "登录取消");
    }

    @Override
    public void onQQLoginError(UiError uiError) {
        qqHintTextView.setText("点击调用QQ登录");
        ToastUtil.showToast(this, "登录出错！");
    }
}
