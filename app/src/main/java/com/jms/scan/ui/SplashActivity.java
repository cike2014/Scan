package com.jms.scan.ui;

import android.content.Intent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.jms.scan.R;
import com.jms.scan.ui.base.BaseActivity;
import com.jms.scan.util.common.Constants;
import com.jms.scan.util.setting.SettingUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {


    @ViewInject(R.id.rl_view)
    private RelativeLayout mRlView;

    @Override
    protected void injectView() {
        x.view().inject(this);
    }

    @Override
    protected void getData() {
        AlphaAnimation startAnimation=new AlphaAnimation(0.3f, 1.0f);
        startAnimation.setDuration(2000);
        mRlView.setAnimation(startAnimation);
        startAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int uid=SettingUtils.getSharedPreferences(SplashActivity.this, Constants.UID, -1);
                if (uid == -1) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                SplashActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
