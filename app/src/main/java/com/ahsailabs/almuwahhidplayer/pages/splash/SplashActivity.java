package com.ahsailabs.almuwahhidplayer.pages.splash;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ahsailabs.almuwahhidplayer.R;
import com.ahsailabs.almuwahhidplayer.pages.home.MainActivity;
import com.zaitunlabs.zlcore.activities.BaseSplashActivity;
import com.zaitunlabs.zlcore.api.APIConstant;
import com.zaitunlabs.zlcore.utils.CommonUtil;

public class SplashActivity extends BaseSplashActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImageIcon(R.drawable.splash_logo);
        setBackgroundPaneColor(R.color.colorPrimary);
        setTitleTextView(getString(R.string.app_name)+"\n"+getString(R.string.nav_header_subtitle), android.R.color.white);
        setBottomTextView(getString(R.string.app_name)+" v"+CommonUtil.getVersionName(SplashActivity.this), android.R.color.white);
        ImageView logoView = findViewById(R.id.splashscreen_icon);
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) logoView.getLayoutParams();
        int widthHeigh = CommonUtil.getPixelFromDip2(this, 130);
        param.width = widthHeigh;
        param.height = widthHeigh;
        logoView.setLayoutParams(param);
    }

    @Override
    protected String getCheckVersionUrl() {
        return APIConstant.API_CHECK_VERSION;
    }

    @Override
    protected boolean doNextAction() {
        MainActivity.start(SplashActivity.this);
        return true;
    }

    @Override
    protected boolean isMeidIncluded() {
        return false;
    }

    @Override
    protected int getMinimumSplashTimeInMS() {
        return 3000;
    }
}