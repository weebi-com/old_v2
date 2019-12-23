package com.weebinatidi.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weebinatidi.R;


/**
 * Created by Zhujun Xiao on 2017/4/5.
 */
public class TitleLayout extends LinearLayout implements View.OnClickListener {

    private final ImageView ivBack;
    private final TextView title, status;

    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.activity_title, this);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        title = (TextView) findViewById(R.id.tv_title);
        status = (TextView) findViewById(R.id.tv_status);
        ivBack.setOnClickListener(this);

    }

    public void setTitle(String strTitle) {
        title.setText(strTitle);
    }

    public void setStatus(String statusText) {
        status.setText(statusText);
    }

    public void setBackKeyInvisible(boolean Invisible) {
        if (Invisible) {
            ivBack.setVisibility(View.INVISIBLE);
        } else {
            ivBack.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        ((Activity) getContext()).finish();
        Log.v("TAG", "点击");
    }
}
