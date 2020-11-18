package com.sty.ne.jetpack.databinding_adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.sty.ne.jetpack.databinding_adapter.databinding.ActivityMainBindingBindingImpl;

public class MainBindingActivity extends AppCompatActivity {
    private ActivityMainBindingBindingImpl binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_binding);

        //初始化ViewModel
        viewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MainViewModel.class);

        //绑定
        binding.setVm(viewModel);

        //setContentView(R.layout.activity_main_binding);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //触发请求 模拟
        viewModel.requestImage();
    }
}
