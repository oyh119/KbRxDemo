package cn.zhaiyifan.interestingtitlebar;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by asus on 2016/8/16.
 */

public class CustomTitleBarUtils {
    CustomTitleBar titleBar;
    TextView titleView;
    LinearLayout leftLayout;
    LinearLayout rightLayout;
    View leftView;
    View rightView;
    View.OnClickListener leftClick;
    View.OnClickListener rightClick;
    String titleName;

    private CustomTitleBarUtils(CustomTitleBar titleBar) {
        this.titleBar = titleBar;
        leftLayout = findViewById(R.id.leftButtonArea);
        rightLayout = findViewById(R.id.rightButtonArea);
    }

    public static CustomTitleBarUtils getInstance(CustomTitleBar titleBar) {
       return  new CustomTitleBarUtils(titleBar);
    }

    public CustomTitleBarUtils setTitle(String name) {
        titleName = name;
        return this;
    }

    public CustomTitleBar getTitleBar() {
        return titleBar;
    }

    public void start() {
        sureViews();
    }

    public View getLeftView() {
        return leftView;
    }

    public View getRightView() {
        return rightView;
    }

    public View getTitleView() {
        return titleView;
    }

    public CustomTitleBarUtils setLeftView(View view) {
        leftLayout.removeAllViews();
        leftView = view;
        if (view != null) {
            leftLayout.addView(leftView);
        }
        return this;
    }

    public CustomTitleBarUtils setRightView(View view) {
        rightLayout.removeAllViews();
        if (view != null) {
            rightLayout.addView(rightView);
        }
        rightView = view;
        return this;
    }

    public CustomTitleBarUtils setLeftClick(View.OnClickListener click) {
        leftClick = click;
        return this;
    }

    public CustomTitleBarUtils setRightClick(View.OnClickListener click) {
        rightClick = click;
        return this;
    }

    public CustomTitleBarUtils setTitleView(TextView view) {
        titleView = view;
        return this;
    }

    private void sureViews() {
        if (titleView == null) {
            titleView = findViewById(R.id.bar_title);
        }
        if (leftView == null) {
            leftView = findViewById(R.id.bar_left_button);
        }
        if (rightView == null) {
            rightView = findViewById(R.id.bar_right_button);
        }
        if (leftClick != null && leftView != null) {
            leftView.setOnClickListener(leftClick);
        }
        if (rightClick != null && rightView != null) {
            rightView.setOnClickListener(rightClick);
        }
        if (!TextUtils.isEmpty(titleName)) {
            titleView.setText(titleName);
        }
    }

    private <T> T findViewById(int rId) {
        return (T) titleBar.findViewById(rId);
    }
}
