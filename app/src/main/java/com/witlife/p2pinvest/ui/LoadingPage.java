package com.witlife.p2pinvest.ui;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.witlife.p2pinvest.R;
import com.witlife.p2pinvest.util.UIUtils;

/**
 * Created by bruce on 24/08/2017.
 */

public abstract class LoadingPage extends FrameLayout {

    private static final int STATE_LOADING = 1;
    private static final int STATE_ERROR = 2;
    private static final int STATE_EMPTY = 3;
    private static final int STATE_SUCCESS = 4;

    private int state_current = STATE_LOADING;

    private View view_loading;
    private View view_error;
    private View view_empty;
    private View view_success;
    private LayoutParams params;
    private ResultState resultState;

    private Context context;

    public LoadingPage(@NonNull Context context) {
        this(context, null);
    }

    public LoadingPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingPage(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        init();
    }

    private void init(){
        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        if(view_loading == null){
            view_loading = View.inflate(getContext(), R.layout.page_loading, null);
            addView(view_loading, params);
        }

        if(view_empty == null){
            view_empty = View.inflate(getContext(), R.layout.page_empty, null);
            addView(view_empty, params);
        }

        if(view_error == null){
            view_error = View.inflate(getContext(), R.layout.page_error, null);
            addView(view_error, params);
        }
        
        showSavePage();
    }

    private void showSavePage() {
        UIUtils.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                showPage();
            }
        });
    }

    private void showPage() {
        view_loading.setVisibility(state_current == STATE_LOADING ? View.VISIBLE: View.GONE);
        view_empty.setVisibility(state_current == STATE_EMPTY ? View.VISIBLE: View.GONE);
        view_error.setVisibility(state_current == STATE_ERROR ? View.VISIBLE: View.GONE);

        if(view_success == null){
            view_success = View.inflate(context, layoutId(), null);
            addView(view_success, params);
        }
        view_success.setVisibility(state_current == STATE_SUCCESS ? View.VISIBLE: View.GONE);
    }

    public abstract int layoutId();

    public void showData(){

        if(TextUtils.isEmpty(url())){
            resultState = ResultState.SUCCESS;
            resultState.setContent("");
            loadImage();
            return;
        }

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url(), params(), new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String content) {
                if(TextUtils.isEmpty(content)){
                    //state_current = STATE_EMPTY;
                    resultState = ResultState.EMPTY;
                    resultState.setContent("");
                } else {
                    resultState = ResultState.SUCCESS;
                    resultState.setContent(content);
                }
                loadImage();
            }

            @Override
            public void onFailure(Throwable error, String content) {
                resultState = ResultState.ERROR;
                resultState.setContent("");
                loadImage();
            }
        });

    }

    private void loadImage() {
        switch (resultState){
            case ERROR:
                state_current = STATE_ERROR;
                break;
            case EMPTY:
                state_current = STATE_EMPTY;
                break;
            case SUCCESS:
                state_current = STATE_SUCCESS;
                break;
        }
        showSavePage();
        
        if(state_current == STATE_SUCCESS){
            onSuccess(resultState, view_success);
        }
    }

    protected abstract void onSuccess(ResultState resultState, View view_success);

    protected abstract RequestParams params();

    protected abstract String url();

    public enum ResultState{
        ERROR(2),EMPTY(3),SUCCESS(4);

        int state;
        ResultState(int state){
            this.state = state;
        }

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
