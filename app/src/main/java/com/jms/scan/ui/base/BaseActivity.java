package com.jms.scan.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jms.scan.R;

public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {

    protected final static String TAG=BaseActivity.class.getSimpleName();
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = getRequestDg(this);
        injectView();
        getData();
        setListener();
    }

    protected abstract void injectView();

    protected abstract void getData();

    protected void setListener() {

    }

    protected void onKeyEv(View view) {

    }

    @Override
    public void onClick(View v) {
        onKeyEv(v);
    }

    protected void showLoading() {
        try {
            loadingDialog.show();
        } catch (Exception e) {
        }

    }

    protected void hideLoading() {
        try {
            loadingDialog.dismiss();
        } catch (Exception e) {
        }

    }
    public Dialog getRequestDg(Context context) {
        Dialog dgloading = new Dialog(context, R.style.loadingDialog);
        LinearLayout layout = new LinearLayout(context);
        layout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null);
        layout.addView(view);
        dgloading.setContentView(layout);
        dgloading.setCanceledOnTouchOutside(false);
        return dgloading;
    }
}
