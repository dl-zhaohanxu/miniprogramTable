package com.example.multiplicationtable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv_wx_miniprogram = findViewById(R.id.tv_wx_miniprogram);
        TextView tv_multiplication = findViewById(R.id.tv_multiplication);
        tv_wx_miniprogram.setOnClickListener(this);
        tv_multiplication.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

            case R.id.tv_wx_miniprogram: //跳转微信小程序
                jumpWechatMiniProgram();
                break;
            case R.id.tv_multiplication:
                Intent intent = new Intent(this,MultiplicationActivity.class);
                startActivity(intent);
                break;
        }
    }


    /**
     * 跳转微信小程序
     */
    private void jumpWechatMiniProgram() {
        if (CommonUtil.isPkgInstalled(this, "com.tencent.mm")) {
            String appId;
            if (isApkInDebug(getApplicationContext())) {
                appId = getString(R.string.weChat_key);
            } else {
                appId = getString(R.string.weChat_dev_key);
            }
            IWXAPI api = WXAPIFactory.createWXAPI(this, appId);
            //                    api.registerApp(appId);
            WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
            req.userName = "gh_dee3c914028f"; // 填小程序原始id
//				req.path = "/pages/index/index";                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
				req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// 可选打开 开发版，体验版和正式版
            api.sendReq(req);
        }
    }

    //判断当前应用是否是debug状态
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }

    }

    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
            String extraData =launchMiniProResp.extMsg; //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
        }
    }
}