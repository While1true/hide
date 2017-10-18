package com.kxjsj.doctorassistant.DialogAndPopWindow;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.SBaseAdapter;
import com.ck.hello.nestrefreshlib.View.Adpater.SBaseMutilAdapter;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
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
    private SBaseAdapter adapter;
    private String filename;

    @Override
    protected int getLayoutId() {
        return R.layout.pic_dialog;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setData(null);
        getRecent20().subscribe(new MyObserver<List<String>>(this) {
            @Override
            public void onNext(List<String> strings) {
                super.onNext(strings);
                if(adapter!=null){
                    adapter.setBeanList(strings);
                    adapter.showItem();
                    adapter.notifyDataSetChanged();
                }
            }
        });


    }

    private void setData(final List<String> strings) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager manager2=new GridLayoutManager(getContext(),2);
        recyclerview.setLayoutManager(OrentionUtils.isPortrait(getContext())?manager:manager2);
        adapter = new SBaseAdapter<String>(strings, R.layout.imageview) {
            @Override
            protected void onBindView(SimpleViewHolder holder, String item, int position) {
                Glide.with(getContext())
                        .load(item)
                        .apply(MyOptions.getCircleRequestOptions())
                        .transition(MyOptions.getTransitionCrossFade())

                        .into((ImageView) holder.getView(R.id.imageview));
                holder.itemView.setOnClickListener(v -> {
                    RxBus.getDefault().post(new BaseBean(Constance.Rxbus.PIC, item));
                });
            }
        };
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
            List<String> list = new ArrayList<String>(20);
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

    @OnClick({R.id.pic, R.id.camera,R.id.close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                dismiss();
                break;
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

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {

            filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                    .format(new Date()) + ".png";
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            Uri fileUri = null;
            if (Build.VERSION.SDK_INT >= 24) {
                fileUri = FileProvider.getUriForFile(getContext(), "com.zhy.android7.fileprovider", file);
            } else {
                fileUri = Uri.fromFile(file);
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(takePictureIntent, 110);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constance.DEBUGTAG)
            Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", requestCode+"onActivityResult: "+resultCode+data.getData());
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
                Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onActivityResult1: " + filename);
        }
        /**
         * 来自相册
         */
        else if (requestCode == 120 && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String file = getFilePathFromContentUri(uri,getContext().getContentResolver());
                RxBus.getDefault().post(new BaseBean<String>(Constance.Rxbus.PIC, file));
                if (Constance.DEBUGTAG)
                    Log.i(Constance.DEBUG + "--" + getClass().getSimpleName() + "--", "onActivityResult2: " + file);

            }

        }
    }

    private void chosePic() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(intent, 120);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

