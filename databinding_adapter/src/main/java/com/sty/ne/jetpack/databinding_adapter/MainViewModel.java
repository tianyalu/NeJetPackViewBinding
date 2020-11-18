package com.sty.ne.jetpack.databinding_adapter;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

/**
 * @Author: tian
 * @UpdateDate: 2020/11/17 9:55 PM
 */
public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<String> imageURL; //图片的URL
    private MutableLiveData<Drawable> placeHolder; //占位图

    private Context context;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
    }

    //暴露数据
    public MutableLiveData<String> getImageURL() {
        if(imageURL == null) {
            imageURL = new MutableLiveData<>();
        }
        return imageURL;
    }

    public MutableLiveData<Drawable> getPlaceHolder() {
        if(placeHolder == null) {
            placeHolder = new MutableLiveData<>();
            //默认图片
            placeHolder.setValue(context.getResources().getDrawable(R.drawable.ic_baseline_notifications_active_24));
        }
        return placeHolder;
    }

    //模拟请求的行为
    public void requestImage() {
        getImageURL().setValue(ImageRequest.IMG_URL);
    }
}
