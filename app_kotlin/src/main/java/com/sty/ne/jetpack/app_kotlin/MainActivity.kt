package com.sty.ne.jetpack.app_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sty.ne.jetpack.app_kotlin.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_home.*

//这个机制能够保证Kotlin不需要写findViewById
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        //setContentView(R.layout.activity_main)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
//        btn1.text = "张三"
//        btn2.text = "李四"
//        btn3.text = "王麻子"

        //隐患，无法避免程序员出错(错误的用到了别的布局控件)
        // java.lang.IllegalStateException: tv_text must not be null
        //解决这个隐患，需要用到ViewBinding
//        tv_text.text = "风云"

        //----------viewBinding
        binding.btn1.text = "张三"
        binding.btn2.text = "李四"
        binding.btn3.text = "王麻子"
        //binding.tv_text = "风云" //编译不会通过
    }
}
