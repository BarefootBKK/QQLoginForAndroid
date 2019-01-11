# 安卓第三方QQ登录-项目说明

  本项目集成封装了QQ第三方登录，大大减少了开发者编写【调用QQ登录】功能的代码量
  
  
  
# 引入本项目

  
  
  
  ## Step 1. 引入SDK JAR包
  
  
  ***下载Jar包***
  
  
  你可以点击这里下载 [腾讯官方QQ-SDK-JAR 包](http://qzonestyle.gtimg.cn/qzone/vas/opensns/res/doc/Android_SDK_V3.3.3.zip)  *(下载完成后只需要解压出 libs 文件夹里的 jar文件 即可)*


  也可以直接使用本项目 ```qq_sdk_jar```文件夹里的 ```jar包```([点击直接下载](https://raw.githubusercontent.com/BarefootBKK/QQLoginForAndroid/master/qq_sdk_jar/open_sdk_r6008_lite.jar))  *(这个jar包为腾讯官方于2018年5月25日更新的版本，点击这里可以查看 [最新官方QQ-SDK包版本](http://wiki.open.qq.com/wiki/mobile/SDK%E4%B8%8B%E8%BD%BD) )*

  
  
 ***在项目里引入Jar包***
  
  
  这里给出一种引入Jar包的方法（其他方法也可以）
  
  以*Android Studio*为例：
  
  > 将下载好的jar包放到你的安卓项目的 ```app\libs``` 文件夹下

  > 在 *Android Studio* 中点击 ```File->Project Structure```，接下来选择 ```app->Dependencies```，这时候点击右上角的 ```+``` 号，选择 ```Jar dependency```，最后选择上一步 ```libs``` 文件夹里的 ```jar包``` 添加即可
  
  

  ## Step 2. 修改AndroidManifest文件
  
  
  **(1)** 将下列代码复制到 ```AndroidManifest.xml``` 文件的 ```<application></application>``` 中
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
  
  
  **(2)** 添加网络权限
  
  ```
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  ```
  
  
  ## Step 3. 引入QQLoginManager.java文件
  
  
  下载本项目的 ```QQLoginManager.java``` 文件，将其复制到自己的安卓项目中
  
  
  
  ## Step 4. 代码示例
  
  按照下列示例代码，模仿其添加到自己的Activity类中，之后你就可以在需要的地方（如点击某个按钮后）调用函数：```launchQQLogin()``` (如示例代码中：```qqLoginManager.launchQQLogin()```)，即可唤起QQ登录
  
  ```
  public class MainActivity extends AppCompatActivity implements QQLoginManager.QQLoginListener {

      private QQLoginManager qqLoginManager;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);
          // 实例化QQLoginManager, 传入你的app_id
          qqLoginManager = new QQLoginManager("app_id", this);
      }

      @Override
      protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
          // 回调
          qqLoginManager.onActivityResultData(requestCode, resultCode, data);
      }

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
  }
  ```
  
  
  ## 补充说明
  ### 函数onQQLoginSuccess：JSONObject里QQ用户信息json格式
  ```
  {
    "nickname":昵称,
    "gender":性别,
    "province":所在省份,
    "city":所在城市,
    "year":出生年,
    "constellation":星座,
    "figureurl":头像URL,
    "figureurl_1":头像URL,
    "figureurl_2":头像URL,
    "figureurl_qq_1":头像URL,
    "figureurl_qq_2":头像URL,
    "vip":是否为qq会员,
    "level":qq会员等级,
    "is_yellow_vip":是否为黄钻,
    "yellow_vip_level":黄钻等级,
    "is_yellow_year_vip":是否为黄钻年会员
  }
  ```
  
  ### APP_ID相关
  
  1. **APP_ID**需要在申请成为[腾讯开放平台开发者](http://open.qq.com/reg)后，
  【**新建应用**】获取
  
  2. **一个APP_ID只能应用于一个APP**，如果出现无法登录的情况，可能是因为当前APP_ID已经在其他APP中使用过了，这时需要在腾讯开发平台[新建一个应用](http://op.open.qq.com/manage_centerv2)，使用新的APP_ID
