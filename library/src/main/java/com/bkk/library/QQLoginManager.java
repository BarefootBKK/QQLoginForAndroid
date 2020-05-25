package com.bkk.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;

public class QQLoginManager {
    private final static String KEY_OPEN_ID = "qq_open_id";
    private final static String KEY_ACCESS_TOKEN = "qq_access_token";
    private final static String KEY_EXPIRES_IN = "qq_expires_in";

    private Context context;
    private Tencent tencent;
    private String appId = "";
    private IUiListener requestListener;
    private QQLoginListener qqLoginListener;

    private static SharedPreferences loginInfoSP;
    private static QQLoginManager qqLoginManagerSingleton;

    public static void init(Context context, String appId) {
        if (null == qqLoginManagerSingleton) {
            qqLoginManagerSingleton = new QQLoginManager(context, appId);
        }
    }

    public static QQLoginManager getInstance() {
        return qqLoginManagerSingleton;
    }

    public static String getLocalOpenId(Context context) {
        initSP(context);
        return loginInfoSP.getString(KEY_OPEN_ID, "");
    }

    public static String getLocalAccessToken(Context context) {
        initSP(context);
        return loginInfoSP.getString(KEY_ACCESS_TOKEN, "");
    }

    public static Long getLocalExpiresIn(Context context) {
        initSP(context);
        return loginInfoSP.getLong(KEY_EXPIRES_IN, -1L);
    }

    public static void clearLoginInfo() {
        saveLoginInfo("", "", -1L);
    }

    public static void login(Activity activity) {
        login(activity, false);
    }

    public static void login(Activity activity, boolean forceLaunch) {
        qqLoginManagerSingleton.doLoginRequest(activity, forceLaunch);
    }

    public static void logout(Activity activity) {
        qqLoginManagerSingleton.doLogoutRequest(activity);
    }

    public static void checkLogin(QQCheckCallback callback) {
        Context context = qqLoginManagerSingleton.context;
        checkLogin(getLocalOpenId(context), getLocalAccessToken(context), getLocalExpiresIn(context), callback);
    }

    public static void checkLogin(String openId, String accessToken, long expiresIn,
                                  QQCheckCallback callback) {
        qqLoginManagerSingleton.doCheckLoginRequest(openId, accessToken, expiresIn, callback);
    }

    public static void setQQLoginListener(QQLoginListener qqLoginListener) {
        qqLoginManagerSingleton.qqLoginListener = qqLoginListener;
    }

    public static void onActivityResultData(int requestCode, int resultCode, @Nullable Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, qqLoginManagerSingleton.requestListener);
    }

    private static void initSP(Context context) {
        if (null == loginInfoSP) {
            loginInfoSP = context.getApplicationContext().getSharedPreferences("QQLoginSP", Context.MODE_PRIVATE);
        }
    }

    private static void saveLoginInfo(String openId, String accessToken, long expiresIn) {
        SharedPreferences.Editor editor = loginInfoSP.edit();
        editor.putString(KEY_OPEN_ID, openId);
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putLong(KEY_EXPIRES_IN, expiresIn);
        editor.apply();
    }

    private QQLoginManager(Context context, String appId) {
        this.context = context.getApplicationContext();
        this.appId = appId;
        init();
    }

    private void init() {
        initSP(context);
        tencent = Tencent.createInstance(appId, context);
        requestListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                setLoginResult((JSONObject) o);
                saveLoginInfo(getOpenId(), getAccessToken(), getExpiresIn());
                callbackUserInfo(qqLoginListener);
            }

            @Override
            public void onError(UiError uiError) {
                if (null != qqLoginListener) {
                    qqLoginListener.onQQLoginError(uiError);
                }
            }

            @Override
            public void onCancel() {
                if (null != qqLoginListener) {
                    qqLoginListener.onQQLoginCancel();
                }
            }
        };
    }

    private void callbackUserInfo(QQLoginListener qqLoginListener) {
        callbackUserInfo(qqLoginListener, null);
    }

    private void callbackUserInfo(QQCheckCallback checkCallback) {
        callbackUserInfo(null, checkCallback);
    }

    private void callbackUserInfo(final QQLoginListener qqLoginListener, final QQCheckCallback checkCallback) {
        UserInfo userInfo = new UserInfo(context, getQQToken());
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                if (null != qqLoginListener) {
                    qqLoginListener.onQQLoginSuccess((JSONObject) o);
                }
                if (null != checkCallback) {
                    checkCallback.onCallback(true, (JSONObject) o);
                }
            }

            @Override
            public void onError(UiError uiError) {
                if (null != qqLoginListener) {
                    qqLoginListener.onQQLoginError(uiError);
                }
                if (null != checkCallback) {
                    checkCallback.onError(uiError.toString());
                }
            }

            @Override
            public void onCancel() {
                if (null != qqLoginListener) {
                    qqLoginListener.onQQLoginCancel();
                }
                if (null != checkCallback) {
                    checkCallback.onCancel();
                }
            }
        });
    }

    private String getJsonStringValue(String key, JSONObject json, String def) {
        try {
            return json.getString(key);
        } catch (JSONException e) {
            return def;
        }
    }

    private Long getJsonLongValue(String key, JSONObject json, Long def) {
        try {
            return json.getLong(key);
        } catch (JSONException e) {
            return def;
        }
    }

    private Integer getJsonIntegerValue(String key, JSONObject json, Integer def) {
        try {
            return json.getInt(key);
        } catch (JSONException e) {
            return def;
        }
    }

    private Boolean getJsonBooleanValue(String key, JSONObject json, boolean def) {
        try {
            return json.getBoolean(key);
        } catch (JSONException e) {
            return def;
        }
    }

    private void doLoginRequest(Activity activity, boolean forcedLaunch) {
        if (forcedLaunch) {
            doLogoutRequest(activity);
        }

        if (!tencent.isSessionValid()) {
            tencent.login(activity, "all", requestListener);
        }
    }

    private void doLogoutRequest(Activity activity) {
        clearLoginInfo();
        if (tencent.isSessionValid()) {
            tencent.logout(activity.getApplicationContext());
        }
    }

    private void doCheckLoginRequest(String openId, String accessToken, long expiresIn,
                           final QQCheckCallback callback) {
        tencent.setOpenId(openId);
        tencent.setAccessToken(accessToken, String.valueOf(expiresIn));
        tencent.checkLogin(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Integer result = getJsonIntegerValue("ret", (JSONObject) o, null);
                if (null != result) {
                    if (result != 0) {
                        callback.onCallback(false, (JSONObject) o);
                    } else {
                        callbackUserInfo(callback);
                    }
                } else {
                    callback.onError("Json解析出错");
                }
            }

            @Override
            public void onError(UiError uiError) {
                callback.onError(uiError.errorDetail);
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });
    }

    private void setLoginResult(JSONObject json) {
        String openId = getJsonStringValue("openid", json, "");
        String accessToken = getJsonStringValue("access_token", json, "");
        long expiresIn = getJsonLongValue("expires_time", json, -1L);

        tencent.setOpenId(openId);
        tencent.setAccessToken(accessToken, String.valueOf(expiresIn));
    }

    public String getAppId() {
        return tencent.getAppId();
    }

    public String getAccessToken() {
        return tencent.getAccessToken();
    }

    public String getOpenId() {
        return tencent.getOpenId();
    }

    public long getExpiresIn() {
        return tencent.getExpiresIn();
    }

    public QQToken getQQToken() {
        return tencent.getQQToken();
    }

    public interface QQLoginListener {
        void onQQLoginSuccess(JSONObject jsonObject);
        void onQQLoginCancel();
        void onQQLoginError(UiError uiError);
    }

    public static abstract class QQCheckCallback {
        public abstract void onCallback(boolean login, JSONObject json);
        public void onError(String error) {}
        public void onCancel() {}
    }
}
