package com.weebinatidi.ui.dummy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weebinatidi.R;


/**
 * Created by Zhujun Xiao on 2017/3/9.
 */
public class EditTextDialog extends Dialog {
    private EditText ipAddressFour, ipAddressThree, ipAddressTwo, ipAddressOne;
    private String ipAddressOneStr, ipAddressTwoStr, ipAddressThreeStr, ipAddressFourStr;
    private Button yes;//确定按钮
    private Button no;//取消按钮
    private TextView titleTv;//消息标题文本
    private TextView messageTv;//消息提示文本
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    //确定文本和取消文本的显示内容
    private String yesStr, noStr;
    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器
    private LinearLayout editLayout;
    public EditTextDialog(Context context) {
        super(context, R.style.ButtonDialog);
    }

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
            noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        if (titleStr != null) {
            titleTv.setText(titleStr);
        }
//        if (ipAddressOneStr != null){
//            ipAddressOne.setText(ipAddressOneStr);
//        }
        if (yesStr != null) {
            yes.setText(yesStr);
        }
        if (noStr != null) {
            no.setText(noStr);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        titleTv = (TextView) findViewById(R.id.title);
        editLayout = (LinearLayout) findViewById(R.id.ll_edit);
        ipAddressOne = (EditText) findViewById(R.id.et_ipaddressone);
        ipAddressTwo = (EditText) findViewById(R.id.et_ipaddresstwo);
        ipAddressThree = (EditText) findViewById(R.id.et_ipaddressthree);
        ipAddressFour = (EditText) findViewById(R.id.et_ipaddressfour);
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
    }

    public String getIpAddressOneStr() {
        return ipAddressOne.getText().toString();
    }

    public void setIpAddressOneStr(String ipOne) {
        ipAddressOne.setText(ipOne);
    }

    public String getIpAddressTwoStr() {
        return ipAddressTwo.getText().toString();
    }

    public void setIpAddressTwoStr(String ipTwo) {
        ipAddressTwo.setText(ipTwo);
    }

    public String getIpAddressThreeStr() {
        return ipAddressThree.getText().toString();
    }

    public void setIpAddressThreeStr(String ipThree) {
        ipAddressThree.setText(ipThree);
    }

    public String getIpAddressFourStr() {
        return ipAddressFour.getText().toString();
    }

    public void setIpAddressFourStr(String ipFour) {
        ipAddressFour.setText(ipFour);
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        public void onYesClick();
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }
}
