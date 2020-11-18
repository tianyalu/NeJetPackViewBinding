package com.sty.ne.jetpack.viewbinding;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sty.ne.jetpack.viewbinding.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    //ActivityMainBinding == activity_main.xml
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_main);
        setContentView(binding.getRoot());

        initView();
    }

    private void initView() {
        binding.btn1.setText("张三");
        binding.btn2.setText("李四");
        binding.btn3.setText("王麻子");
    }
}