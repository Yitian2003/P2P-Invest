package com.witlife.p2pinvest.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.witlife.p2pinvest.R;
import com.witlife.p2pinvest.bean.UserBean;
import com.witlife.p2pinvest.common.ActivityManager;
import com.witlife.p2pinvest.common.AppNetConfig;
import com.witlife.p2pinvest.common.BaseActivity;
import com.witlife.p2pinvest.util.BitmapUtils;
import com.witlife.p2pinvest.util.UIUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserInfoActivity extends BaseActivity {

    public static final String USER_INFO = "user_info";
    public static final int CAMERA = 1001;
    public static final int GALLERY = 1000;
    private static final int WRITE_EXTERNAL_STORAGE = 2000;

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.iv_user_icon)
    ImageView ivUserIcon;
    @Bind(R.id.tv_user_change)
    TextView tvUserChange;
    @Bind(R.id.btn_user_logout)
    Button btnUserLogout;

    private UserBean user;
    private Context context;

    @Override
    protected void initTitle() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("User Information");
        ivSetting.setVisibility(View.INVISIBLE);
    }

    @Override
    public void initData() {
        context = this;

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        if (bundle != null) {
            user = (UserBean) bundle.getSerializable(USER_INFO);
        }

        if(user.getImageurl() != null){
            Picasso.with(this)
                    .load(user.getImageurl()).transform(new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {

                    //rescale bitmap
                    Bitmap bitmap = BitmapUtils.zoom(source, UIUtils.dp2px(70), UIUtils.dp2px(70));

                    bitmap = BitmapUtils.circleBitmap(source);
                    source.recycle();
                    return bitmap;
                }

                @Override
                public String key() {
                    return "";
                }
            })
                    .into(ivUserIcon);
        } else {
            ivUserIcon.setImageResource(R.drawable.my_user_default);
        }

        ivUserIcon.setOnClickListener(new View.OnClickListener() {

            String[] items = new String[]{"Gallery", "Camera"};

            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(UserInfoActivity.this)
                        .setCancelable(false)
                        .setTitle("Choose Source")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent;
                                switch (i) {
                                    case 0:
                                        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(intent, GALLERY);
                                        break;

                                    case 1:
                                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent, CAMERA);
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().removeCurrent();
            }
        });

        btnUserLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. clear data in sp
                SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                sp.edit().clear().commit();
                //2, delete image in local
                File fileDir;
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    fileDir = getExternalFilesDir("");
                } else {
                    fileDir = getFilesDir();
                }
                File file = new File(fileDir, "icon.png");
                if(file.exists()){
                    file.delete();
                }
                //3. destroy all activity
                ((BaseActivity) context).removeAll();
                //((BaseActivity)getBaseContext()).removeAll();
                //4, reload to main fragment
                ((BaseActivity) context).startNewActivity(MainActivity.class, null);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap source = null;

        if (requestCode == CAMERA && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            source = (Bitmap) bundle.get("data");

            source = BitmapUtils.zoom(source, UIUtils.dp2px(70), UIUtils.dp2px(70));
            Bitmap bitmap = BitmapUtils.circleBitmap(source);
            source.recycle();
            ivUserIcon.setImageBitmap(bitmap);

            //updateImageToServer(bitmap);

            saveImage(bitmap);

        } else if (requestCode == GALLERY && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            String pathResult = getPath(imageUri);
            source = BitmapFactory.decodeFile(pathResult);

            source = BitmapUtils.zoom(source, UIUtils.dp2px(70), UIUtils.dp2px(70));
            Bitmap bitmap = BitmapUtils.circleBitmap(source);
            source.recycle();
            ivUserIcon.setImageBitmap(bitmap);

            //updateImageToServer(bitmap);

            saveImage(bitmap);
        }

    }


    private void updateImageToServer(Bitmap bitmap) {
        final String imageName = UUID.randomUUID().toString();
        final String encodedString = BitmapUtils.bitmapToString(bitmap);

        class UploadFileToServer extends AsyncTask<Void, Integer, String> {
            @Override
            protected String doInBackground(Void... voids) {
                return uploadFile();
            }

            @SuppressWarnings("deprecation")
            private String uploadFile() {
                String responseString = null;

                AsyncHttpClient client = new AsyncHttpClient();

                RequestParams params = new RequestParams();
                params.put("imageName", imageName);
                params.put("image", encodedString);
                params.put("phone", user.getPhone());

                client.post(AppNetConfig.UPLOADIMAGE, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String content) {
                        Log.i("TAG", content);
                    }

                    @Override
                    public void onFailure(Throwable error, String content) {
                        Log.i("TAG", content);
                    }
                });

                return null;
            }
        }
        UploadFileToServer server = new UploadFileToServer();
        server.execute();
    }

    //memory --> file
    private void saveImage(Bitmap bitmap) {
        File filesDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //path1: storage/sdcard/Android/data/package name/files
            filesDir = this.getExternalFilesDir("");
        } else {//internal storage
            //path: data/data/package name/files
            filesDir = this.getFilesDir();
        }

        File file = new File(filesDir, "icon.png");
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //get gallery file path according to different system
    @SuppressLint("NewApi")
    private String getPath(Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        //高于4.4.2的版本
        if (sdkVersion >= 19) {
            Log.e("TAG", "uri auth: " + uri.getAuthority());
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(this, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(this, contentUri, selection, selectionArgs);
            } else if (isMedia(uri)) {
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualimagecursor = this.managedQuery(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                return actualimagecursor.getString(actual_image_column_index);
            }


        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * uri路径查询字段
     *
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isMedia(Uri uri) {
        return "media".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private void checkPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
        } else {
            //callMethod();
        }
    }
}
