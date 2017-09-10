package com.witlife.p2pinvest.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.witlife.p2pinvest.R;
import com.witlife.p2pinvest.bean.UpdateBean;
import com.witlife.p2pinvest.common.AppNetConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {

    private static final int TO_MAIN = 1111;
    public static final int DOWNLOAD_VERSION_SUCCESS = 1112;
    public static final int DOWNLOAD_APK_SUCCESS = 1113;

    @Bind(R.id.tv_welcome_version)
    TextView tvWelcomeVersion;
    @Bind(R.id.rl_welcome)
    RelativeLayout rlWelcome;

    private long startTime;
    private UpdateBean bean;
    private ProgressDialog dialog;
    private File apkFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        setAnimation();

        updateApkFile();

        //handler.sendEmptyMessageDelayed(100, 3000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case DOWNLOAD_VERSION_SUCCESS:
                    String version = getVersion();
                    // update welcome page version
                    tvWelcomeVersion.setText(version);
                    // check the difference of current version and server version
                    if (version.equals(bean.getVersion()))
                    {
                        Toast.makeText(WelcomeActivity.this, "It is latest version", Toast.LENGTH_SHORT).show();
                        toMain();
                    } else {
                        new AlertDialog.Builder(WelcomeActivity.this)
                                .setTitle("Download Latest Version")
                                .setMessage(bean.getDesc())
                                .setPositiveButton("Download Now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // download apk
                                        downloadApk();
                                    }
                                })
                                .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        toMain();
                                    }
                                })
                                .show();
                    }
                    break;

                case TO_MAIN:
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                case DOWNLOAD_APK_SUCCESS:
                    Toast.makeText(WelcomeActivity.this, "Download Success", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    installApk();
                    finish();
                    break;
            }
        }
    };

    private void installApk() {
        Intent intent = new Intent("android.intent.action.INSTALL_PACKAGE");
        intent.setData(Uri.parse("file:" + apkFile.getAbsolutePath()));
        startActivity(intent);
    }

    private void downloadApk() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.show();

        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            filesDir = this.getExternalFilesDir("");
        } else {
            filesDir = this.getFilesDir();
        }
        apkFile = new File(filesDir, "update.apk");

        new Thread(){
            public void run(){
                URL url = null;
                HttpURLConnection connection = null;
                InputStream is = null;
                FileOutputStream fos = null;

                try {
                    url = new URL(bean.getApkUrl());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    
                    connection.connect();
                    
                    if(connection.getResponseCode() == 200){
                        dialog.setMax(connection.getContentLength());
                        is = connection.getInputStream();
                        fos = new FileOutputStream(apkFile);

                        byte[] buffer = new byte[1024];
                        int len;

                        while ((len = is.read(buffer)) != -1) {
                            dialog.incrementProgressBy(len);
                            fos.write(buffer, 0, len);
                        }

                        handler.sendEmptyMessage(DOWNLOAD_APK_SUCCESS);

                    } else {
                        Toast.makeText(WelcomeActivity.this, "download apk fail", Toast.LENGTH_SHORT).show();
                        toMain();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null){
                        connection.disconnect();
                    }
                    if (is != null){
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fos != null){
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    private String getVersion() {
        String version = "Unkown Version";
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return  version;
    }



    private void updateApkFile() {
        startTime = System.currentTimeMillis();

        // check internet connect status
        boolean connect = isConnect();

        if(!connect){ // not connect to the internet
            Toast.makeText(this, "No Internat Connected", Toast.LENGTH_SHORT).show();
            toMain();
        } else {// connceted
            // get app version
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(AppNetConfig.UPDATE, new AsyncHttpResponseHandler(){
                @Override
                public void onFailure(Throwable error, String content) {
                    Toast.makeText(WelcomeActivity.this, "Request to server fail", Toast.LENGTH_SHORT).show();
                    toMain();
                }

                @Override
                public void onSuccess(String content) {
                    Gson gson = new Gson();
                    bean = gson.fromJson(content, UpdateBean.class);

                    handler.sendEmptyMessage(DOWNLOAD_VERSION_SUCCESS);
                }
            });
        }
    }

    private void toMain() {
        long currentTime = System.currentTimeMillis();
        long delayTime = 3000 - currentTime + startTime;

        if(delayTime < 0){
            delayTime = 0;
        }

        handler.sendEmptyMessageDelayed(TO_MAIN, delayTime);
    }

    private boolean isConnect() {
        boolean connected = false;

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo != null){
            connected = networkInfo.isConnected();
        }
        return connected;
    }

    private void setAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());

        /*alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
*/
        rlWelcome.startAnimation(alphaAnimation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
