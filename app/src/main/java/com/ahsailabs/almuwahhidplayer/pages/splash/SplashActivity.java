package com.ahsailabs.almuwahhidplayer.pages.splash;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ahsailabs.alcore.activities.BaseSplashActivity;
import com.ahsailabs.alcore.api.APIConstant;
import com.ahsailabs.almuwahhidplayer.R;
import com.ahsailabs.almuwahhidplayer.pages.home.MainActivity;
import com.ahsailabs.alutils.CommonUtil;
import com.ahsailabs.alutils.HttpClientUtil;

public class SplashActivity extends BaseSplashActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImageIcon(R.drawable.splash_logo);
        setBackgroundPaneColor(R.color.colorPrimary);
        setTitleTextView(getString(R.string.app_name)+"\n"+getString(R.string.nav_header_subtitle), android.R.color.white);
        setBottomTextView(getString(R.string.app_name)+" v"+ CommonUtil.getVersionName(SplashActivity.this), android.R.color.white);
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
    protected HttpClientUtil.AuthType getAuthType() {
        return HttpClientUtil.AuthType.APIKEY;
    }

    @Override
    protected int getMinimumSplashTimeInMS() {
        return 3000;
    }
}
