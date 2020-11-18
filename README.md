# JetPack之ViewBinding与DataBinding

[TOC]

## 一、理论

### 1.1 `ViewBinding`作用

通过视图绑定，可以更轻松地编写可与视图交互的代码。在模块中启用视图绑定之后，系统会为该模块中的每个`XML`布局文件生成一个绑定类，绑定类的实例包含对在相应布局中具有`ID`的所有视图的直接引用。

* 取代`ButterKnife`
* 代替`findViewById`
* 代替`Kotlin`的`synthetics`机制（该机制可能会使程序员误用其它布局的控件导致程序崩溃）

参考：[https://developer.android.google.cn/topic/libraries/view-binding](https://developer.android.google.cn/topic/libraries/view-binding)

### 1.2 `ViewBinding`与`findViewById`的区别

与使用`findViewById`相比，视图绑定具有以下显著的优势：

* **`Null`安全**：由于视图绑定会创建对视图的直接引用，因此不存在因视图`ID`无效而引发`Null`指针异常的风险；此外如果视图仅出现在布局的某些配置中，则绑定类中包含其引用的字段会使用`@Nullable`标记；
* **类型安全**：每个绑定类中的字段均具有与它们在`xml`文件中引用的视图相匹配的类型，这意味着不存在发生类型转换异常的风险。

### 1.3 `ViewBinding`与`DataBinding`的对比

视图绑定与数据绑定均会生成可用于直接引用视图的绑定类，但是，视图绑定旨在处理更简单的用例，与数据绑定相比，具有如下优势：

* **更快的编译速度**：视图绑定不需要处理注释，因此编译时间更短；
* **对布局代码没有侵入性，易于使用**：视图绑定不需要特别标记的`xml`布局文件，因此在应用中速度更快。在模块中启用视图绑定后，它会自动应用与该模块的所有布局。

反过来，与数据绑定相比，视图绑定也具有如下限制：

* 视图绑定不支持布局变量或布局表达式，因此不能用于直接在`xml`布局文件中生命动态界面内容；
* 视图绑定不支持双向数据绑定。

考虑到这些因素，可以在项目中同时使用视图绑定和数据绑定：在需要高级功能的布局中使用数据绑定，在不需要高级功能的布局中使用视图绑定。

## 二、实践

### 2.1 `ViewBinding`

#### 2.1.1 启用`ViewBinding`

视图绑定功能可按模块启用，在在魔鬼模块中启用视图绑定，需将`viewBinding`元素添加到其`build.gradle`文件中，如下所示：

```groovy
    defaultConfig {
				//...
        //viewBinding的依赖
        viewBinding {
            enabled = true
        }
    }
```

如果希望在生产绑定类时忽略某个布局文件，需将`tools:viewBindingIgnore="true"`属性添加到相应布局文件的根视图中：

```xml
<LinearLayout
              android:layout_width="match_parent"
              android:layout_height="wrap_content"
              tools:viewBindingIgnore="true">
  <!-- ... -->
</LinearLayout>
```

#### 2.1.2 用法

为魔鬼模块启用视图绑定功能后，系统会为该模块中包含的每个`xml`布局文件生成一个绑定类，每个绑定类均包含对根视图以及具有`ID`的所有视图的引用。系统会通过以下方式生成绑定类的名称：将`xml`文件的名称转换成驼峰式大小写，并在末尾添加`Binding`一词。

例如某个布局文件的名称为`result_profile.xml`:

```xml
<LinearLayout ... >
        <TextView android:id="@+id/name" />
        <ImageView android:cropToPadding="true" />
        <Button android:id="@+id/button"
            android:background="@drawable/rounded_button" />
    </LinearLayout>
```

所生成的绑定类的名称就为`ResultProfileBinding`。此类具有两个字段：一个是名为`name`的`TextView`，另一个是名为`button`的`Button`，该布局中的`ImageView`没有`ID`，因此绑定类中不存在对它的引用。

每个绑定类中还包含一个`getRoot()`方法，用于为相应布局文件的根视图 提供直接引用。在此示例中，`ResultProfileBinding`类中的`getRoot()`方法会返回`LinearLayout`根视图。

#### 2.1.3 在`Activity`中使用视图绑定

在`Activity`的`onCreate()`方法中执行以下步骤：

> 1. 调用生成的绑定类中包含的静态`inflate()`方法，此操作会创建该绑定类的实例以供`Activity`使用；
> 2. 通过调用`getRoot()`方法获取对根视图的引用；
> 3. 将根视图传递到`setContentView()`，使其成为屏幕上的活动视图。

```java
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
```

#### 2.1.4 在`Fragment`中使用视图绑定

在`Fragment`的`onCreateView()`方法中执行以下步骤：

> 1. 调用生成的绑定类中包含的静态`inflate()`方法，此操作会创建该绑定类的实例以供`Fragment`使用；
> 2. 通过调用`getRoot()`方法获取对根视图的引用；
> 3. 从`onCreateView()`方法返回根视图，使其成为屏幕上的活动视图。

```java
	  private FragmentBlankBinding blankBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // FragmentBlankBinding == fragment_blank.xml
        //return inflater.inflate(R.layout.fragment_blank, container, false);

        blankBinding = FragmentBlankBinding.inflate(getLayoutInflater());
        return blankBinding.getRoot();
    }

		// 使用
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        blankBinding.tvText.setText("哈哈哈");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        blankBinding = null;
    }
```

**注意**：`Fragment `的存在时间比其视图长。请务必在` Fragment` 的 `onDestroyView() `方法中清除对绑定类实例的所有引用。比如在`viewpager`中滑动时，触发`onDetach()`时，生命周期只会走到`onDestroyView()`，而不会走`onDestroy()`，这时，如果不把绑定类的引用置空的话，在`fragment`重新`onAttach()`时，会调用生命周期`onCreateView()`，生成新的绑定类实例，但`binding`的指向并没有改变，还是之前的`blankBinding`引用，这时我们再使用`binding`时，就无法更新UI了。

### 2.2 `Kotlin`中使用`ViewBinding`

`Kotlin`中有`synthetic`机制，是不需要写`findViewById`的，但是为什么要用`ViewBinding`呢？

因为`Kotlin`的`synthetic`机制不会检查控件的所属布局文件，有可能意外使用到了其他布局文件中的控件，造成`Null`异常；而由于视图绑定会创建对视图的直接引用，因此不存在因视图`ID`无效而引发`Null`指针异常的风险。

```kotlin
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
```

### 2.3 `DataBinding`之`BindingAdapter`

#### 2.3.1 `MainViewModel`

```java
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
```

#### 2.3.2 `activity_main_binding.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="vm"
            type="com.sty.ne.jetpack.databinding_adapter.MainViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:context=".MainBindingActivity">

        <ImageView
            android:id="@+id/iv_image"
            android:layout_width="match_parent"
            android:layout_height="300dp"
            imageUrl="@{vm.imageURL}"
            placeHolder="@{vm.placeHolder}"/>

    </LinearLayout>
</layout>
```

#### 2.3.3 `ImageBindingAdapter`

```java
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
```

#### 2.3.4 `MainBindingActivity`

```java
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
```

