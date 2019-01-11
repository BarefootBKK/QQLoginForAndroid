import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 这是一个QQ登录的管理类
 * 用法：
 * 1.首先implements QQLoginManager.QQLoginListener
 *
 * 2.实例化QQLoginManager，这里需要传入2个参数：app_id，Object
 *      示例：new QQLoginManager(你的app_id, this)
 *      这里的this，直接把当前Activity对象传过来就可以了
 *
 * 3.在onActivityResult()方法里回调登录结果，调用onActivityResultData(...)方法
 *
 * 4.最后调用QQ登录函数：launchQQLogin()，
 *  在重载的三个方法(onQQLoginSuccess(...)等)里执行你的操作
 */
/**
 * @author BarefootBKK
 * 2019/01/09
 */
public class QQLoginManager {

    private String app_id = "";
    private Tencent mTencent;
    private UserInfo mUserInfo;
    private LocalLoginListener localLoginListener;
    private QQLoginListener qqLoginListener;
    private Context mContext;
    private Activity mActivity;

    /**
     * 构造函数，包括app_id
     * @param app_id
     * @param o
     */
    public QQLoginManager(String app_id, Object o) {
        this.app_id = app_id;
        this.mContext = (Context) o;
        this.mActivity = (Activity) o;
        this.qqLoginListener = (QQLoginListener) o;
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        localLoginListener = new LocalLoginListener();
        if (mTencent == null) {
            mTencent = Tencent.createInstance(app_id, mContext);
        }
    }

    /**
     * 回调结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResultData(int requestCode, int resultCode, @Nullable Intent data) {
        mTencent.onActivityResultData(requestCode, resultCode, data, localLoginListener);
    }

    /**
     * 启动QQ登录
     */
    public void launchQQLogin() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(mActivity, "all", localLoginListener);
        } else {
            mTencent.logout(mContext);
            launchQQLogin();
        }
    }

    /**
     * 退出QQ登录
     */
    public void qqLogout() {
        if (mTencent.isSessionValid()) {
            mTencent.logout(mActivity);
        }
    }

    /**
     * QQ登录状态监听器
     */
    public interface QQLoginListener {
        void onQQLoginSuccess(JSONObject jsonObject);
        void onQQLoginCancel();
        void onQQLoginError(UiError uiError);
    }

    /**
     * 本地QQ登录监听器
     */
    private class LocalLoginListener implements IUiListener {

        private String openID;

        @Override
        public void onComplete(Object o) {
            initOpenIdAndToken(o);
            loadUserInfo();
        }

        @Override
        public void onError(UiError uiError) {
            qqLoginListener.onQQLoginError(uiError);
        }

        @Override
        public void onCancel() {
            qqLoginListener.onQQLoginCancel();
        }

        /**
         * 初始化openID和access_token
         * @param object
         */
        private void initOpenIdAndToken(Object object) {
            JSONObject jsonObject = (JSONObject) object;
            try {
                openID = jsonObject.getString("openid");
                String access_token = jsonObject.getString("access_token");
                String expires = jsonObject.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(access_token, expires);
            } catch (JSONException e) {
                qqLoginListener.onQQLoginError(null);
            }
        }

        /**
         * 加载用户信息
         */
        private void loadUserInfo() {
            QQToken qqToken = mTencent.getQQToken();
            mUserInfo = new UserInfo(mContext, qqToken);
            mUserInfo.getUserInfo(new IUiListener() {
                /**
                 * 登录成功
                 * @param o
                 */
                @Override
                public void onComplete(Object o) {
                    try {
                        JSONObject jsonObject = (JSONObject) o;
                        jsonObject.put("open_id", openID);
                        qqLoginListener.onQQLoginSuccess(jsonObject);
                    } catch (JSONException e) {
                        qqLoginListener.onQQLoginError(null);
                    }
                }

                /**
                 * 登录出错
                 * @param uiError
                 */
                @Override
                public void onError(UiError uiError) {
                    qqLoginListener.onQQLoginError(uiError);
                }

                /**
                 * 取消登录
                 */
                @Override
                public void onCancel() {
                    qqLoginListener.onQQLoginCancel();
                }
            });
        }
    }
}
