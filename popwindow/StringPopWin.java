package com.winner.pickerview.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.winner.pickerview.R;
import com.winner.pickerview.view.LoopListener;
import com.winner.pickerview.view.LoopView;

import java.util.ArrayList;

/**
 * PopWindow for Date Pick
 */
public class StringPopWin extends PopupWindow implements OnClickListener {

    private static final int STYLE_DEFAULT = 0;
    private TextView cancelBtn;
    private TextView confirmBtn;
    private TextView titleTextView;
    private LoopView loopView1;
    private LoopView loopView2;
    private View pickerContainerV;
    private View contentView;// root view

    private int yearPos = 0;
    private int monthPos = 0;
    private Context mContext;
    private String textCancel;
    private String textConfirm;
    private String titleText;
    private int colorCancel;
    private int colorConfirm;
    private int colorTitleText;
    private int btnTextsize;
    private int viewTextSize;
    private int titleTextSize;
    private ArrayList<String> list1 = new ArrayList<>();
    private ArrayList<ArrayList<String>> list2 = new ArrayList<>();

    private boolean isShow = false;

    public static class Builder {

        // Required
        private Context context;
        private OnStringPickedListener listener;

        public Builder(Context context, OnStringPickedListener listener) {
            this.context = context;
            this.listener = listener;
        }

        public Builder(Context context, OnStringPickedListener listener, int style) {
            this.context = context;
            this.listener = listener;
            this.style = style;
        }

        private ArrayList<String> list1 = new ArrayList<>();
        private ArrayList<ArrayList<String>> list2 = new ArrayList<>();
        private String textCancel = "cancel";
        private String textConfirm = "confirm";
        private String titleText;
        private String dateChose = "";
        private int colorCancel = Color.parseColor("#00a0e9");
        private int colorConfirm = Color.parseColor("#00a0e9");
        private int colorTitleText = Color.parseColor("#333333");
        private int btnTextSize = 18;
        private int viewTextSize = 23;
        private int titleTextSize = 18;
        private boolean isShow;//中间标题是否显示，默认不显示
        private int style = STYLE_DEFAULT;


        public Builder isTitilShow(boolean isShow) {
            this.isShow = isShow;
            return this;
        }

        public Builder textCancel(String textCancel) {
            this.textCancel = textCancel;
            return this;
        }

        public Builder textConfirm(String textConfirm) {
            this.textConfirm = textConfirm;
            return this;
        }

        public Builder dateChose(String dateChose) {
            this.dateChose = dateChose;
            return this;
        }

        public Builder colorCancel(int colorCancel) {
            this.colorCancel = colorCancel;
            return this;
        }

        public Builder colorConfirm(int colorConfirm) {
            this.colorConfirm = colorConfirm;
            return this;
        }

        public Builder colorTitleText(int colorTitleText) {
            this.colorTitleText = colorTitleText;
            return this;
        }

        public Builder style(int style) {
            this.style = style;
            return this;
        }

        /**
         * set btn text btnTextSize
         *
         * @param textSize dp
         */
        public Builder btnTextSize(int textSize) {
            this.btnTextSize = textSize;
            return this;
        }

        public Builder viewTextSize(int textSize) {
            this.viewTextSize = textSize;
            return this;
        }

        public Builder titleText(String titleText) {
            this.titleText = titleText;
            return this;
        }

        public Builder titleTextSize(int textSize) {
            this.titleTextSize = textSize;
            return this;
        }

        public Builder setList1(ArrayList<String> list1) {
            this.list1 = list1;
            return this;
        }

        public Builder setList2(ArrayList<ArrayList<String>> list2) {
            this.list2 = list2;
            return this;
        }

        public StringPopWin build() {
            return new StringPopWin(this);
        }
    }

    //将设置属性
    private StringPopWin(Builder builder) {
        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        this.titleText = builder.titleText;
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        this.colorTitleText = builder.colorTitleText;
        this.btnTextsize = builder.btnTextSize;
        this.viewTextSize = builder.viewTextSize;
        this.titleTextSize = builder.titleTextSize;
        this.isShow = builder.isShow;
        this.list1 = builder.list1;
        this.list2 = builder.list2;
        initView(builder.style);
        setSelectedDate(builder.dateChose);
    }

    private OnStringPickedListener mListener;

    private void initView(int style) {

        contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_country_picker, null);
        cancelBtn = (TextView) contentView.findViewById(R.id.btn_cancel);
        confirmBtn = (TextView) contentView.findViewById(R.id.btn_confirm);
        titleTextView = (TextView) contentView.findViewById(R.id.select_date);
        loopView1 = (LoopView) contentView.findViewById(R.id.picker_country);
        loopView2 = (LoopView) contentView.findViewById(R.id.picker_city);

        pickerContainerV = contentView.findViewById(R.id.container_picker);

        cancelBtn.setText(textCancel);
        confirmBtn.setText(textConfirm);
        titleTextView.setText(titleText);
        cancelBtn.setTextColor(colorCancel);
        confirmBtn.setTextColor(colorConfirm);
        titleTextView.setTextColor(colorTitleText);
        cancelBtn.setTextSize(btnTextsize);
        confirmBtn.setTextSize(btnTextsize);
        titleTextView.setTextSize(titleTextSize);

        if (isShow) {
            titleTextView.setVisibility(View.VISIBLE);
        } else {
            titleTextView.setVisibility(View.GONE);
        }

        loopView1.setNotLoop();
        loopView2.setNotLoop();

        loopView1.setTextSize(viewTextSize);
        loopView2.setTextSize(viewTextSize);

        loopView1.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                yearPos = item;
                initMonthPickerViews(item);
            }
        });
        loopView2.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                monthPos = item;
            }
        });


        initYearPickerViews();
        initMonthPickerViews(0);

        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        contentView.setOnClickListener(this);

        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * Init year and month loop view,
     * Let the day loop view be handled separately
     */
    private void initYearPickerViews() {

        loopView1.setArrayList(list1);
        loopView1.setInitPosition(0);
    }

    private void initMonthPickerViews(int index) {

        loopView2.setArrayList(list2.get(index));
        loopView2.setInitPosition(0);
    }

    /**
     * set selected date position value when initView.
     */
    private void setSelectedDate(String dateStr) {

        if (!TextUtils.isEmpty(dateStr)) {

            if (dateStr.contains("-")) {
                for (int i = 0; i < list1.size(); i++) {
                    if (dateStr.split("-")[0].equals(list1.get(i))) {
                        loopView1.setInitPosition(i);
                        for (int j = 0; j < list2.get(i).size(); j++) {
                            if (dateStr.split("-")[1].equals(list2.get(i).get(j))) {
                                loopView2.setInitPosition(j);
                                loopView2.setArrayList(list2.get(i));
                            }
                        }
                    }
                }

            }
        }
    }

    /**
     * Show date picker popWindow
     */
    public void showPopWin(Activity activity) {

        if (null != activity) {

            TranslateAnimation trans = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);

            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
            trans.setDuration(400);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());

            pickerContainerV.startAnimation(trans);
        }
    }

    /**
     * Dismiss date picker popWindow
     */
    private void dismissPopWin() {

        TranslateAnimation trans = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);

        trans.setDuration(400);
        trans.setInterpolator(new AccelerateInterpolator());
        trans.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                dismiss();
            }
        });

        pickerContainerV.startAnimation(trans);
    }

    @Override
    public void onClick(View v) {

        if (v == contentView || v == cancelBtn) {
            dismissPopWin();
        } else if (v == confirmBtn) {
            if (null != mListener) {
                String year = list1.get(yearPos);
                String month = list2.get(yearPos).get(monthPos);
                StringBuffer sb = new StringBuffer();

                sb.append(year);
                sb.append("-");
                sb.append(month);
                mListener.onStringPickCompleted(year, month, sb.toString());
            }

            dismissPopWin();
        }
    }

    public interface OnStringPickedListener {

        void onStringPickCompleted(String year, String month, String dateDesc);
    }
}
