package com.kxjsj.doctorassistant.Appxx.Mine.Wallet;

import android.os.Bundle;

import com.kxjsj.doctorassistant.Component.BaseTitleActivity;

/**
 * Created by vange on 2017/10/23.
 */

public class WalletActivity extends BaseTitleActivity {
    @Override
    protected int getContentLayoutId() {
        return 0;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
      setTitle("钱包");
    }
}
