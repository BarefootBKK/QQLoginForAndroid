# 安卓第三方QQ登录-项目说明

  本项目集成封装了QQ第三方登录，大大减少了开发者编写【调用QQ登录】功能的代码量
  
  
  
# 引入本项目

  
  
  
  ## Step 1. 添加依赖
  
#### build.gradle (project)

```
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

#### build.gradle (app)

```
implementation 'com.github.BarefootBKK:QQLoginForAndroid:1.0.1'
```

  ## Step 2. 修改AndroidManifest文件
  
  
  - 将下列代码复制到 ```AndroidManifest.xml``` 文件的 ```<application></application>``` 中
  *(注意将代码里的app_id完善)*：
    
  ```
    <activity
        android:name="com.tencent.tauth.AuthActivity"
        android:launchMode="singleTask"
        android:noHistory="true">
        <intent-filter>
            <action android:name="android.intent.action.VIEW" />

            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.BROWSABLE" />
            <!-- 这地方需要用你在开放平台申请的appid:tencent+appid -->
            <data android:scheme="tencent00000000" />
        </intent-filter>
    </activity>
    
    <activity
        android:name="com.tencent.connect.common.AssistActivity"
        android:configChanges="orientation|keyboardHidden"
        android:screenOrientation="behind"
        android:theme="@android:style/Theme.Translucent.NoTitleBar" />
  ```
  
  
  - 添加网络权限
  
  ```
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  ```
  
  
  ## Step 3. 代码示例
  
  按照下列示例代码，模仿其添加到自己的Activity类中，之后你就可以在需要的地方（如点击某个按钮后）调用函数：```QQLoginManager.login(activity)```，即可唤起QQ登录
  
  ```
  public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QQLoginManager.init(this, 你的appId);
        QQLoginManager.setQQLoginListener.setQQLoginListener(new QQLoginManager.QQLoginListener() {
            @Override
            public void onQQLoginSuccess(JSONObject jsonObject) {
                // 登录成功
            }

            @Override
            public void onQQLoginCancel() {
                // 登录取消
            }

            @Override
            public void onQQLoginError(UiError uiError) {
                // 登录出错
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 回调
        QQLoginManager.onActivityResultData(requestCode, resultCode, data);
    }
}
  ```
  
  ### 登录
```
// 登录1：如果用户已登录，则不会拉起QQ登录
QQLoginManager.login(activity);

// 登录2：无论用户是否已登录，都会强制拉起QQ登录
QQLoginManager.login(activity, true);
```

### 检查登录状态
```
QQLoginManager.checkLogin(new QQLoginManager.QQCheckCallback() {
    @Override
    public void onCallback(boolean login, JSONObject json) {
        // login=true：已登录；json=用户信息
        // login=false：未登录或登录已失效；json=检查详情
    }
});
```  
  ### 退出登录
```
QQLoginManager.logout(activity);
```
  
  
  ## 补充说明
  ### 函数onQQLoginSuccess：JSONObject里QQ用户信息json格式
  ```
  {
    "open_id":当前登录QQ唯一标识,
    "access_token": accessToken,
    "expires_in": accessToken的有效时间,
    "nickname":昵称,
    "gender":性别,
    "province":所在省份,
    "city":所在城市,
    "year":出生年,
    "constellation":星座,
    "figureurl":30X30的头像URL,
    "figureurl_1":50X50的头像URL,
    "figureurl_2":100X100的头像URL,
    "figureurl_qq_1":40X40的头像URL,
    "figureurl_qq_2":100X100的头像URL,
    "vip":是否为qq会员,
    "level":qq会员等级,
    "is_yellow_vip":是否为黄钻,
    "yellow_vip_level":黄钻等级,
    "is_yellow_year_vip":是否为黄钻年会员
  }
  ```
  
  ### 函数onQQLoginError：UiError类参数详情
  ```
  int errorCode: 错误码，具体错误码请参考腾讯官方API文档
  String errorDetail: 错误详情
  String errorMessage: 错误信息
  ```
  
  ### APP_ID相关
  
  - **APP_ID**需要在申请成为 [腾讯开放平台开发者](http://open.qq.com/reg) 后，
  【**新建应用**】获取
  
  - **一个APP_ID只能应用于一个APP**，如果出现无法登录的情况，可能是因为当前APP_ID已经在其他APP中使用过了，这时需要在 [腾讯开放平台](http://op.open.qq.com/manage_centerv2) 新建一个应用，使用新的APP_ID
