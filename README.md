# 安卓第三方QQ登录-项目说明

  本项目集成封装了QQ第三方登录，大大减少了开发者编写【调用QQ登录】功能的代码量
  
  
  
# 引入本项目

  
  
  
  ## Step 1. 引入SDK JAR包
  
  
  - ***下载Jar包***
  
  
  你可以点击这里可以查看 [最新官方QQ-SDK包版本](http://wiki.open.qq.com/wiki/mobile/SDK%E4%B8%8B%E8%BD%BD) *(下载完成后只需要解压出 libs 文件夹里的 jar文件 即可)*


 也可以直接使用本项目 ```qq_sdk_jar```文件夹里的 ```jar包```([点击直接下载](https://raw.githubusercontent.com/BarefootBKK/QQLoginForAndroid/master/qq_sdk_jar/open_sdk_r8353806_lite.jar))  *(这个jar包为腾讯官方于2020年4月27日更新的版本)*

  
  
 - ***在项目里引入Jar包***
  
  
  这里给出一种引入Jar包的方法（其他方法也可以）
  
  以*Android Studio*为例：
  
  > 将下载好的jar包放到你的安卓项目的 ```app\libs``` 文件夹下

  > 在 *Android Studio* 中点击 ```File->Project Structure```，接下来选择 ```app->Dependencies```，这时候点击右上角的 ```+``` 号，选择 ```Jar dependency```，最后选择上一步 ```libs``` 文件夹里的 ```jar包``` 添加即可
  
  

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
  
  
  ## Step 3. 引入QQLoginManager.java文件
  
  
  下载本项目的 ```QQLoginManager.java``` 文件，将其复制到自己的安卓项目中
  
  
  
  ## Step 4. 代码示例
  
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
