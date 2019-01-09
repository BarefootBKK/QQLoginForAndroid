# 安卓QQ第三方登录

## 项目说明

  本项目集成封装了QQ第三方登录，大大减少了开发者编写【调用QQ登录】功能的代码量
  
  
## 引入本项目

  ### 1. 修改AndroidManifest文件
  
  将下列代码复制到AndroidManifest.xml文件的 **<application></application>** 中：
    
  '''
  <!-- QQ登录开始 -->
  <activity
      android:name="com.tencent.tauth.AuthActivity"
      android:launchMode="singleTask"
      android:noHistory="true">
      <intent-filter>
          <action android:name="android.intent.action.VIEW" />

          <category android:name="android.intent.category.DEFAULT" />
          <category android:name="android.intent.category.BROWSABLE" />
          <!-- 这地方需要用你在开放平台申请的appid:tencent+appid -->
          <data android:scheme="tencent1108103648" />
      </intent-filter>
  </activity>
  <activity
      android:name="com.tencent.connect.common.AssistActivity"
      android:configChanges="orientation|keyboardHidden"
      android:screenOrientation="behind"
      android:theme="@android:style/Theme.Translucent.NoTitleBar" />
  <!-- QQ登录结束 -->
  '''
