package com.kxjsj.doctorassistant.DialogAndPopWindow;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.PositionHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.StringHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.SBaseAdapter;
import com.kxjsj.doctorassistant.Component.BaseBottomSheetDialog;
import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.Glide.MyOptions;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.BaseBean;
import com.kxjsj.doctorassistant.Rx.MyObserver;
import com.kxjsj.doctorassistant.Rx.RxSchedulers;
import com.kxjsj.doctorassistant.Rx.Utils.RxBus;
import com.kxjsj.doctorassistant.Screen.OrentionUtils;
import com.kxjsj.doctorassistant.Utils.K2JUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhy.base.fileprovider.FileProvider7;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

import static android.app.Activity.RESULT_OK;

/**
 * Created by vange on 2017/10/18.
 */

public class PicDialog extends BaseBottomSheetDialog {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    Unbinder unbinder;
    private SAdapter adapter;
    private String filename;
    View view;

    @Override
    protected int getLayoutId() {
        return R.layout.pic_dialog;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        this.view=view;
        setTitle("选择图片");
        setData();
        getRecent20().subscribe(new MyObserver<List<String>>(this) {
            @Override
            public void onNext(List<String> strings) {
                super.onNext(strings);
                if(adapter!=null){
                    adapter.setBeanList(strings);
                    adapter.showItem();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                recyclerview.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void setData() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager manager2=new GridLayoutManager(getContext(),2);
        recyclerview.setLayoutManager(OrentionUtils.isPortrait(getContext())?manager:manager2);
        adapter = new SAdapter()
        .addType(R.layout.imageview, new StringHolder() {
            @Override
            public void onBind(SimpleViewHolder holder, String item,int position) {
                Glide.with(getContext())
                        .load(item)
                        .apply(MyOptions.getCircleRequestOptions())
                        .transition(MyOptions.getTransitionCrossFade())

                        .into((ImageView) holder.getView(R.id.imageview));
                holder.itemView.setOnClickListener(v -> {
                    RxBus.getDefault().post(new BaseBean(Constance.Rxbus.PIC, item));
                    dismiss();
                });
            }

            @Override
            public boolean istype(String item,int position) {
                return true;
            }
        });
        recyclerview.setAdapter(adapter);
    }

    /**
     * 获取相册的最近20张照片
     *
     * @return
     */
    private Observable<List<String>> getRecent20() {
        return Observable.create((ObservableOnSubscribe<List<String>>) e -> {

            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = getContext()
                    .getContentResolver();
            List<String> list = new ArrayList<String>(21);
            // 只查询jpeg和png的图片
            Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[]{"image/jpeg", "image/png", "image/bmp"},
                    MediaStore.Images.Media.DATE_ADDED + " DESC");
            while (mCursor.moveToNext()) {
                String path = mCursor.getString(mCursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                if (Constance.DEBUGTAG)
                    Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "initView: " + path);
                File file = new File(path);
                if (file.exists()) {
                    list.add(path);
                }
                if (list.size() > 20)
                    break;
            }
            mCursor.close();
            e.onNext(list);
            e.onComplete();

        }).compose(RxSchedulers.compose());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.pic, R.id.camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic:
                chosePic();
                break;
            case R.id.camera:

                new RxPermissions(getActivity())
                        .request(Manifest.permission.CAMERA)
                        .subscribe(new MyObserver<Boolean>(this) {
                            @Override
                            public void onNext(Boolean aBoolean) {
                                if (aBoolean) {
                                    takePhoto();
                                } else {
                                    K2JUtils.toast("缺少相机权限，请到设置打开");
                                }
                            }
                        });

                break;
        }
    }

    /**
     * 打开相机拍照
     */
    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {

            filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                    .format(new Date()) + ".png";
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            Uri fileUri =FileProvider7.getUriForFile(getContext(),file);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(takePictureIntent, 110);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 来自照相机
         */
        if (requestCode == 110 && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                filename= getFilePathFromContentUri(uri,getContext().getContentResolver());

            }
            RxBus.getDefault().post(new BaseBean<String>(Constance.Rxbus.PIC, filename));
            if (Constance.DEBUGTAG)
                Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onActivityResult1: " + Environment.getExternalStorageDirectory()+filename);
            dismiss();
        }
        /**
         * 来自相册
         */
        else if (requestCode == 120 && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String file = getFilePathFromContentUri(uri,getContext().getContentResolver());
                RxBus.getDefault().post(new BaseBean<String>(Constance.Rxbus.PIC, file));
                dismiss();
                if (Constance.DEBUGTAG)
                    Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onActivityResult2: " + file);

            }

        }
    }

    /**
     * 打开相册
     */
    private void chosePic() {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if(Build.VERSION.SDK_INT<19){
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }else{
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        try {
            startActivityForResult(intent, 120);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据uri获取路径
     * @param selectedVideoUri
     * @param contentResolver
     * @return
     */
    public static String getFilePathFromContentUri(Uri selectedVideoUri,
                                                   ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }
}

