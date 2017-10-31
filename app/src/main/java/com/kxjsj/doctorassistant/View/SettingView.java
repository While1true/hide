package com.kxjsj.doctorassistant.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.R;


/**
 *有什么属性才会添加相应控件，内存小，功能大
 * @author ck
 * @date 2017/10/18
 */
public class SettingView extends RelativeLayout {
    /**
     * 左边的titleview
     */
    TextView titleView;
    private float titlesize = getResources().getDimension(R.dimen.titlesize);
    private int titletextcolor = getResources().getColor(R.color.navi_unchecked);

    /**
     * 右边的textview
     *
     * @param context
     */
    TextView subTextView;
    private float subTextsize = getResources().getDimension(R.dimen.titlesize);
    private int subTextcolor = getResources().getColor(R.color.navi_unchecked);

    /**
     * 靠右的imageView
     *
     * @param context
     */
    ImageView imageView;

    /**
     * 靠右的开关
     *
     * @param context
     */
    Switch aSwitch;

    /**
     * drawpadding
     */
    private int padding=5;

    public SettingView(@NonNull Context context) {
        this(context, null);
    }

    public SettingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttras(attrs);
    }

    private void obtainAttras(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SettingView);
        float aFloat = typedArray.getFloat(R.styleable.SettingView_titlesize, 0);
        if (aFloat != 0) {
            titlesize = aFloat;
        }
        int color = typedArray.getColor(R.styleable.SettingView_titletextcolor, 0);
        if (color != 0) {
            titletextcolor = color;
        }

        float padding = typedArray.getDimension(R.styleable.SettingView_drawPadding, 0);
        if(padding!=0){
            this.padding= (int) padding;
        }

        CharSequence text = typedArray.getText(R.styleable.SettingView_titletext);
        if (!TextUtils.isEmpty(text)) {
            setTitleText(text);
        }

        float aFloat2 = typedArray.getFloat(R.styleable.SettingView_subTextsize, 0);
        if (aFloat2 != 0) {
            subTextsize = aFloat2;
        }
        int color2 = typedArray.getColor(R.styleable.SettingView_subTextcolor, 0);
        if (color2 != 0) {
            subTextcolor = color2;
        }
        CharSequence text2 = typedArray.getText(R.styleable.SettingView_subtitleText);
        if (!TextUtils.isEmpty(text2)) {
            setSubText(text2);
        }

        int resourceId = typedArray.getResourceId(R.styleable.SettingView_image, 0);
        if (resourceId != 0) {
            setRightImage(resourceId);
        }
        int resourceId1 = typedArray.getResourceId(R.styleable.SettingView_titleDrawable, 0);
        if (resourceId1 != 0) {
            setTitledrawable(resourceId1,0,0,0);
        }
        int resourceId2 = typedArray.getResourceId(R.styleable.SettingView_subtitleDrawable, 0);
        if (resourceId2 != 0) {
            setSubdrawable(0,0,resourceId2,0);
        }

        boolean show=typedArray.getBoolean(R.styleable.SettingView_showSwitch,false);
        if(show) {
            setShowSwitch(true);
        }
        CharSequence text1 = typedArray.getText(R.styleable.SettingView_switchOnText);
        CharSequence text3 = typedArray.getText(R.styleable.SettingView_switchOffText);
        if (!TextUtils.isEmpty(text1)&&!TextUtils.isEmpty(text3)) {
            setSwitchText(text1, text3);
        }

        typedArray.recycle();
    }

    public SettingView setTitleText(CharSequence title) {
        if (titleView == null) {
            titleView = creatTitleTextView();
        }
        titleView.setText(title);
        return this;
    }

    public SettingView setTitleColor(int color) {
        this.titletextcolor = color;
        if (titleView == null) {
            titleView = creatTitleTextView();
        }
        titleView.setTextColor(color);
        return this;
    }

    public SettingView setTitledrawable(int left, int top, int right, int bottom) {
        if (titleView == null) {
            titleView = creatTitleTextView();
        }
        titleView.setCompoundDrawables(generateDrawable(left), generateDrawable(top), generateDrawable(right), generateDrawable(bottom));
        return this;
    }

    public SettingView setSubText(CharSequence charSequence) {
        if (subTextView == null) {
            subTextView = creatSubTextView();
        }
        subTextView.setText(charSequence);
        return this;
    }

    public SettingView setSubTexColor(int color) {
        this.subTextcolor = color;
        if (subTextView == null) {
            subTextView = creatSubTextView();
        }
        subTextView.setTextColor(color);
        return this;
    }

    public SettingView setSubdrawable(int left, int top, int right, int bottom) {
        if (subTextView == null) {
            subTextView = creatSubTextView();
        }
        subTextView.setCompoundDrawables(generateDrawable(left), generateDrawable(top), generateDrawable(right), generateDrawable(bottom));
        return this;
    }

    public SettingView setSwitchListener(CompoundButton.OnCheckedChangeListener listener) {
        if (aSwitch == null) {
            aSwitch = creatRightSwitch();
        }
        aSwitch.setOnCheckedChangeListener(listener);

        return this;
    }

    public SettingView setShowSwitch(boolean show){
        if (aSwitch == null) {
            aSwitch = creatRightSwitch();
        }
        aSwitch.setVisibility(show?VISIBLE:GONE);
        return this;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SettingView setSwitchText(CharSequence on, CharSequence off) {
        if (aSwitch == null) {
            aSwitch = creatRightSwitch();
        }
        aSwitch.setShowText(true);
        aSwitch.setTextOn(on);
        aSwitch.setTextOff(off);
        return this;
    }

    public SettingView setdrawPadding(int padding){
        this.padding=padding;
        if(titleView!=null){
            titleView.setCompoundDrawablePadding(padding);
        }
        if(subTextView!=null){
            subTextView.setCompoundDrawablePadding(padding);
        }

        return this;

    }

    private Drawable generateDrawable(int drawableRes) {
        if (drawableRes <= 0) {
            return null;
        }
        Drawable drawable = getResources().getDrawable(drawableRes);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()
        );

        return drawable;
    }

    private TextView creatTitleTextView() {
        TextView titleView = new TextView(getContext());
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX,titlesize);
        titleView.setTextColor(titletextcolor);
        titleView.setCompoundDrawablePadding(padding);
        LayoutParams layoutParams = generateParams();
        layoutParams.addRule(CENTER_VERTICAL);
        layoutParams.addRule(ALIGN_PARENT_LEFT);
        titleView.setGravity(Gravity.CENTER);
        titleView.setLayoutParams(layoutParams);
        addView(titleView);

        return titleView;
    }

    public SettingView setRightImage(int res) {
        if (imageView == null) {
            imageView = creatRightImageView();
        }
        imageView.setImageResource(res);
        return this;
    }

    private TextView creatSubTextView() {
        TextView titleView = new TextView(getContext());
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX,subTextsize);
        titleView.setTextColor(subTextcolor);
        titleView.setCompoundDrawablePadding(padding);
        LayoutParams layoutParams = generateParams();
        layoutParams.addRule(CENTER_VERTICAL);
        layoutParams.addRule(ALIGN_PARENT_RIGHT);
        titleView.setGravity(Gravity.CENTER);
        titleView.setLayoutParams(layoutParams);
        addView(titleView);

        return titleView;
    }

    private ImageView creatRightImageView() {
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LayoutParams layoutParams = generateParams();
        layoutParams.addRule(CENTER_VERTICAL);
        layoutParams.addRule(ALIGN_PARENT_RIGHT);
        imageView.setLayoutParams(layoutParams);
        addView(imageView);

        return imageView;
    }

    private Switch creatRightSwitch() {
        Switch aswitch = new Switch(getContext());
        LayoutParams layoutParams = generateParams();
        aswitch.setTextOn("开");
        aswitch.setTextOff("关");
        layoutParams.addRule(CENTER_VERTICAL);
        layoutParams.addRule(ALIGN_PARENT_RIGHT);
        aswitch.setLayoutParams(layoutParams);
        addView(aswitch);
        return aswitch;
    }

    private LayoutParams generateParams() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return layoutParams;
    }


    public TextView getTitleView() {
        return titleView;
    }

    public float getTitlesize() {
        return titlesize;
    }

    public int getTitletextcolor() {
        return titletextcolor;
    }

    public TextView getSubTextView() {
        return subTextView;
    }

    public float getSubTextsize() {
        return subTextsize;
    }

    public int getSubTextcolor() {
        return subTextcolor;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Switch getaSwitch() {
        return aSwitch;
    }
}
