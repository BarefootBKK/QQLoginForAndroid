package com.example.qqlogin.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qqlogin.R;
import com.example.qqlogin.entity.User;
import com.squareup.picasso.Picasso;

public class UserInfoActivity extends AppCompatActivity {
    private ImageView headImage;
    private TextView nickname;
    private TextView birthYear;
    private TextView gender;
    private TextView province;
    private TextView city;
    private TextView isYellowVIP;
    private TextView yellowVIPLevel;
    private TextView isYellowYearVIP;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        User user = getIntent().getParcelableExtra("extra_data");

        headImage = findViewById(R.id.head_img);
        nickname = findViewById(R.id.nickname);
        birthYear = findViewById(R.id.birth_year);
        gender = findViewById(R.id.gender);
        province = findViewById(R.id.province);
        city = findViewById(R.id.city);
        isYellowVIP = findViewById(R.id.is_yellow_vip);
        yellowVIPLevel = findViewById(R.id.yellow_vip_level);
        isYellowYearVIP = findViewById(R.id.is_yellow_year_vip);
        logoutButton = findViewById(R.id.logout);

        nickname.setText(user.getNickname());
        birthYear.setText(user.getBirthYear());
        gender.setText(user.getGender());
        province.setText(user.getProvince());
        city.setText(user.getCity());
        isYellowVIP.setText(user.getIsYellowVIP());
        yellowVIPLevel.setText(user.getYellowVIPLevel());
        isYellowYearVIP.setText(user.getIsYellowYearVIP());
        Picasso.get().load(user.getHeadImgUrl()).into(headImage);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.this.finish();
            }
        });
    }
}
