package com.kxjsj.doctorassistant.Appxx.Mine;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.Group;
import android.text.TextUtils;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SScrollview;
import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Appxx.Mine.Login.AuthActivity;
import com.kxjsj.doctorassistant.Appxx.Mine.Wallet.WalletActivity;
import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.IDInfoActivity;
import com.kxjsj.doctorassistant.Component.BaseTitleActivity;
import com.kxjsj.doctorassistant.Constant.Session;
import com.kxjsj.doctorassistant.DialogAndPopWindow.PicDialog;
import com.kxjsj.doctorassistant.Glide.GlideLoader;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.kxjsj.doctorassistant.View.SettingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author bugaosuni
 *         Created by vange on 2017/10/18.
 */
public class UserInfoActivity extends BaseTitleActivity {

    @BindView(R.id.sscrollview)
    SScrollview sScrollview;
    @BindView(R.id.img)
    SettingView img;
    @BindView(R.id.nickname)
    SettingView nickname;
    @BindView(R.id.phone)
    SettingView phone;
    @BindView(R.id.sickcard)
    SettingView sickcard;
    @BindView(R.id.money)
    SettingView money;
    @BindView(R.id.password)
    SettingView password;
    @BindView(R.id.idinfo)
    SettingView idinfo;
    @BindView(R.id.group2)
    Group group2;
    private PicDialog dialog;
    private Session userInfo;

    @Override
    protected int getContentLayoutId() {
        return R.layout.userinfo_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setTitle("用户信息");
        sScrollview.setRefreshMode(true, true, false, false);
        userInfo = App.getUserInfo();
        if (userInfo == null) {
            K2JUtils.toast("登录失效，请重新登录");
            return;
        }
        if (userInfo.getType() != 0)
            group2.setVisibility(View.GONE);
        String imgUrl = userInfo.getImgUrl();
        GlideLoader.loadRound(img.getImageView(), imgUrl);
        nickname.setSubText(userInfo.getUsername());
        sickcard.setSubText(userInfo.getPatientNo());

        //dialog打开情况下的复活 操作
        if (savedInstanceState != null) {
            dialog = (PicDialog) getSupportFragmentManager().getFragment(savedInstanceState, "PicDialog");
        }
        if (dialog != null)
            dialog.setListener(result -> {
                GlideLoader.loadRound(img.getImageView(), result);
            });

    }


    @OnClick({R.id.money, R.id.img, R.id.nickname, R.id.phone, R.id.sickcard, R.id.idinfo, R.id.password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img:
                if (dialog == null) {
                    dialog = new PicDialog();
                    dialog.setListener(result -> {
                        GlideLoader.loadRound(img.getImageView(), result);
                    });
                }
                dialog.show(getSupportFragmentManager());
                break;
            case R.id.nickname:
                showChangeNick();
                break;
            case R.id.money:
                startActivity(new Intent(this, WalletActivity.class));
                break;
            case R.id.phone:
                K2JUtils.toast("这是您注册账号的手机号");
                break;
            case R.id.sickcard:
                K2JUtils.toast("需要更换绑定的病号吗？");
                break;
            case R.id.idinfo:
                startActivity(new Intent(this, IDInfoActivity.class));
                break;
            case R.id.password:
                startActivity(new Intent(this, AuthActivity.class));
                break;
        }
    }

    private void showChangeNick() {
        new MaterialDialog.Builder(this)
                .title("更改昵称")
                .content("当前：" + userInfo.getUsername())
                .input("请输入新的昵称", null, false, (dialog, input) -> {

                }).negativeText("取消")
                .positiveText("提交")
                .onPositive((dialog, which) -> {
                    String text = dialog.getInputEditText().getText().toString();
                    if (TextUtils.isEmpty(text) || text.equals(userInfo.getUsername())) {
                        K2JUtils.toast("昵称不能为空或相同");
                        return;
                    }
                    //TODO
                }).build().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (dialog != null&&dialog.isAdded())
            getSupportFragmentManager().putFragment(outState, "PicDialog", dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
