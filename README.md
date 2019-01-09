# 安卓QQ第三方登录

## 项目说明

  本项目集成封装了QQ第三方登录，大大减少了开发者编写【调用QQ登录】功能的代码量
  
  
## 引入本项目

  ### Step 1. 修改AndroidManifest文件
  
  
  **(1)** 将下列代码复制到 ```AndroidManifest.xml``` 文件的 ```<application></application>``` 中：
    
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
  
  
  ### Step 2. 引入Jar包
  
  这里需要引入腾讯的官方 SDK JAR 包，你可以点击这里下载 [QQ-SDK-JAR 包](http://qzonestyle.gtimg.cn/qzone/vas/opensns/res/doc/Android_SDK_V3.3.3.zip)  *(下载完成后只需要解压出里面 lib 文件夹里的 jar文件 即可)*
  
  
  或者你也可以直接使用本项目qq_sdk_jar文件夹里的jar包(这个jar包为腾讯官方于为2018年5月更新的版本，你也可以点击这里查看[最新官方SDK包版本](http://wiki.open.qq.com/wiki/mobile/SDK%E4%B8%8B%E8%BD%BD))
