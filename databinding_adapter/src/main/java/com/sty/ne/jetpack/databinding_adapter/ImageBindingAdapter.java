package com.sty.ne.jetpack.databinding_adapter;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import androidx.databinding.BindingAdapter;

/**
 * @Author: tian
 * @UpdateDate: 2020/11/17 9:56 PM
 */
public class ImageBindingAdapter {

    //placeHolder 占位图,没有加载出来之前的占位图
    //在设置任何属性时调用适配器，则可以将适配器的可选标志 requireAll 设置为 false
    @BindingAdapter(value = {"imageUrl", "placeHolder"}, requireAll = false)
    public static void loadUrl(ImageView imageView, String url, Drawable placeHolder) {
         //获取图片的逻辑...

        if(url != null) {
            Glide.with(imageView.getContext()).load(url).placeholder(placeHolder).into(imageView);
        }
    }
}
