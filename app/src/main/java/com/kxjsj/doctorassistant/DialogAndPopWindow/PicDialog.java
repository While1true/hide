package com.kxjsj.doctorassistant.DialogAndPopWindow;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kxjsj.doctorassistant.Component.BaseBottomSheetDialog;
import com.kxjsj.doctorassistant.Glide.MyOptions;
import com.kxjsj.doctorassistant.R;
import com.kxjsj.doctorassistant.Rx.MyObserver;
import com.kxjsj.doctorassistant.Rx.RxSchedulers;
import com.zhy.base.fileprovider.FileProvider7;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

import static android.app.Activity.RESULT_OK;

/**
 * Created by vange on 2017/10/18.
 */

public class PicDialog extends BaseBottomSheetDialog implements View.OnClickListener {
    public static String CAPTURE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/DCIM/Camera/";
    public static int RECENT_SIZE = 20;
    RecyclerView recyclerview;
    private MyAdapter adapter;
    private String filename;
    View view;
    ResultListener listener;

    public void setListener(ResultListener listener) {
        this.listener = listener;
        if (adapter != null) {
            adapter.setResultListener(listener);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.pic_dialog;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        this.view = view;
        setTitle("选择图片");

        if (!new File(CAPTURE_PATH).exists()) {
            new File(CAPTURE_PATH).mkdirs();
        }
        recyclerview = view.findViewById(R.id.recyclerview);
        view.findViewById(R.id.pic).setOnClickListener(this);
        view.findViewById(R.id.camera).setOnClickListener(this);
        initRecyclerview();

        getRecent().subscribe(new MyObserver<List<String>>(this) {
            @Override
            public void onNext(List<String> strings) {
                super.onNext(strings);
                if (adapter != null) {
                    adapter.setStrings(strings);
                    adapter.notifyDataSetChanged();
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

    private void initRecyclerview() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager manager2 = new GridLayoutManager(getContext(), 2);
        recyclerview.setLayoutManager((getContext().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) ? manager : manager2);
        adapter = new MyAdapter(this);
        adapter.setResultListener(listener);
        recyclerview.setAdapter(adapter);
    }

    /**
     * 获取相册的最近张照片
     *
     * @return
     */
    private Observable<List<String>> getRecent() {
        return Observable.create((ObservableOnSubscribe<List<String>>) e -> {

            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = getContext()
                    .getContentResolver();
            List<String> list = new ArrayList<String>(RECENT_SIZE);
            // 只查询jpeg和png的图片
            Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[]{"image/jpeg", "image/png", "image/bmp"},
                    MediaStore.Images.Media.DATE_ADDED + " DESC");
            while (mCursor.moveToNext()) {
                String path = mCursor.getString(mCursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                File file = new File(path);
                if (file.exists()) {
                    list.add(path);
                }
                if (list.size() > RECENT_SIZE)
                    break;
            }
            mCursor.close();
            e.onNext(list);
            e.onComplete();

        }).compose(RxSchedulers.compose());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pic:
                chosePic();
                break;
            case R.id.camera:
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 200);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.CAMERA)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        takePhoto();
                    } else {
                        Toast.makeText(getContext(), "需要相机权限，才能进行拍照", Toast.LENGTH_SHORT).show();
                    }
                }
            }
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
            File file = new File(CAPTURE_PATH, filename);
            Uri fileUri = FileProvider7.getUriForFile(getContext(), file);
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
                filename = getFilePathFromContentUri(uri, getContext().getContentResolver());

            }
            if (listener != null) {
                listener.onResult(CAPTURE_PATH + filename);
            }
            dismiss();
        }
        /**
         * 来自相册
         */
        else if (requestCode == 120 && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String file = getFilePathFromContentUri(uri, getContext().getContentResolver());
                if (listener != null) {
                    listener.onResult(file);
                }
                dismiss();

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
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        if (Build.BRAND.toLowerCase().equals("xiaomi")) {
            intent.setPackage("com.miui.gallery");
        }
        try {
            startActivityForResult(intent, 120);
        } catch (Exception e) {
            e.printStackTrace();
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(intent, 120);
        }
    }

    /**
     * 根据uri获取路径
     *
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


    private static class MyAdapter extends RecyclerView.Adapter {
        List<String> strings;
        ResultListener listener;
        WeakReference<PicDialog> dialogWeakReference;

        public MyAdapter(PicDialog dialog) {
            dialogWeakReference = new WeakReference<PicDialog>(dialog);
        }

        public void setStrings(List<String> strings) {
            this.strings = strings;
        }

        public void setResultListener(ResultListener listener) {
            this.listener = listener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageView itemView = new ImageView(parent.getContext());
            int i = dp2px(parent.getContext(), 150);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(i, i);
            itemView.setLayoutParams(layoutParams);
            return new RecyclerView.ViewHolder(itemView) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
        }

        private int dp2px(Context context, int dp) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Glide.with(holder.itemView.getContext())
                    .load(strings.get(position))
                    .apply(MyOptions.getCircleRequestOptions())
                    .transition(MyOptions.getTransitionCrossFade())
                    .into((ImageView) holder.itemView);
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onResult(strings.get(position));
                }
                if (dialogWeakReference.get() != null) {
                    dialogWeakReference.get().dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return strings == null ? 0 : strings.size();
        }
    }

    public interface ResultListener {
        void onResult(String result);
    }

}

