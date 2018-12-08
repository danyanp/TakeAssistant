# 输入电话号码太慢——>图像识别(识别电话号码，记录电话号码)
---
> 想法：用一个ListView展示记录的电话号码
（细节：拍照识别，读出数字，提示弹框《是否要将****存入数据库》《是》《否》）
> 实际：将电话号码存入数据库
	 从数据库读取数据到ListView
## 功能： 
1. 识别并拨打电话

2. 识别保存电话号码
 
3. 识别并保存大量电话号码，依次拨打

4. 识别并发送短信


## UI界面：
1. 悬浮按钮
	 - 识别并拨打电话
	 - 识别保存电话号码 

> 百度OCR数字识别  返回json  如1.json
json 处理 返回 String


## 文件结构
```
app
├─ MainActivity.java
	├─ onCreate
		├─ 初始化recyclerView
		├─ 初始化initAccessToken
		├─ button点击事件，拍照
	├─ initrecycler方法（初始化布局）
	├─ initAccessToken方法（以license文件方式初始化）
	├─ checkTokenStatus方法，检测Token是否可用
	├─ alertText方法 弹窗
	├─ infoPopText方法 弹窗
	├─ onRequestPermissionsResult方法（请求权限）
	├─ requestPermission方法（自定义请求权限）
	├─ onActivityResult成功回调方法（识别成功返回）
	├─ setData方法（存入电话号码）
	├─ getJsonObjects方法（json字符串转对象）
	├─ onDestroy方法（释放内存资源）
├─ BaiduOCR.java
├─ FileUtil.java
├─ RecognizeService.java
```
```
└─takeassistant
    │  RecognizeService.java
    │
    ├─Activity
    │      MainActivity.java
    │      Myapplication.java
    │
    ├─Adaper
    │      NumAdaper.java
    │      OcrAdater.java
    │
    ├─bean
    │      OcrResult.java
    │
    ├─File
    │      FileUtil.java
    │
    └─ToolClass
            JsonHelper.java
```
```

├─layout
│      activity_main.xml
│      activity_main2.xml
│      app_bar_main2.xml
│      nav_header_main2.xml
│      ocr_item.xml
│
├─menu
│      activity_main_drawer.xml
│      main.xml
│
├─values
│      colors.xml
│      dimens.xml
│      strings.xml
│      styles.xml
│
└─values-v21
        styles.xml

```


## Debug:

### 问题1：拍照后，未识别到数字，添加上个数据到数据库（数据增加逻辑问题）
> 解决：拍照->数字识别->返回结果->（判断是否有数字）否 返回未识别到数字？是->(是否为电话号码)否 返回未识别到号码？是->setData存储数据

### 问题2：有无办法识别号码后直接拨打电话
> SetData方法中判断是否存在11位的电话号码
>> 存在->识别 不存在->重新调用识别方法（可以优化）

### 问题3：区分已打电话，和未打电话

### 问题4：item是否倒序！！！
> RecyclerView实现倒序列表：https://www.jianshu.com/p/2e647bf1573e

### 问题5：是否分开 “直接拨打电话” 和 “保存模式”

### 问题6：item的操作

### 问题7：重装软件后没有动态获取android.permission.CALL_PHONE权限

### 问题8：识别后判断电话号码，逻辑优化

### 问题9：动态权限问题
>

___
## 技术栈
### 1.百度COR Android—SDK
---
### 2.RecylerView类似ListView，ListView强大
	- RecylerView CardView（引入两个包）
		- implementation 'com.android.support:recyclerview-v7:27.1.1'
		- implementation 'com.android.support:cardview-v7:27.1.1'
>
>
>
>
>
---	
### 3.LitePal数据库（引入一个包）
	- implementation 'org.litepal.android:core:2.0.0'
		- 1.创建litepal.xml
		- 2.初始化
		- 3.增删改查
___

### 4.FloatingActionButton（FAB)
> FloatingActionButton（FAB）是 Android 5.0 新特性——Material Design中的一个控件。FloatingActionButton其实由3个单词组成, Floating:悬浮;Action:行为,Button:按钮。的确,FAB就是一个悬浮的按钮。

>1.FAB是Material Design (以下简称MD)中的一个控件.跟所有MD控件一样，要使用FAB，需要在gradle文件中先注册依赖：
```
implementation 'com.android.support:design:27.1.0'
```
>2.FAB的基本使用

>通过查看源码可知,FAB是 ImageView 的子类，因此它具备ImageView的全部属性。如果你只是进行最简单的操作,代码如下:
```
<android.support.design.widget.FloatingActionButton 
	android:id="@+id/fab" 
	android:layout_width="wrap_content" 
	android:layout_height="wrap_content" 
	android:src="@drawable/fab_up" 
/>
```
> 悬浮框轮子FloatingActionButton
：https://github.com/Clans/FloatingActionButton

```
<com.github.clans.fab.FloatingActionMenu
        android:id="@+id/menu"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_alignParentRight="true"
        fab:menu_fab_size="normal"
        fab:menu_showShadow="true"
        fab:menu_shadowColor="#66000000"
        fab:menu_shadowRadius="4dp"
        fab:menu_shadowXOffset="1dp"
        fab:menu_shadowYOffset="3dp"
        fab:menu_colorNormal="#DA4336"
        fab:menu_colorPressed="#E75043"
        fab:menu_colorRipple="#99FFFFFF"
        fab:menu_animationDelayPerItem="50"
        fab:menu_icon="@drawable/fab_add"
        fab:menu_buttonSpacing="0dp"
        fab:menu_labels_margin="0dp"
        fab:menu_labels_showAnimation="@anim/fab_slide_in_from_right"
        fab:menu_labels_hideAnimation="@anim/fab_slide_out_to_right"
        fab:menu_labels_paddingTop="4dp"
        fab:menu_labels_paddingRight="8dp"
        fab:menu_labels_paddingBottom="4dp"
        fab:menu_labels_paddingLeft="8dp"
        fab:menu_labels_padding="8dp"
        fab:menu_labels_textColor="#FFFFFF"
        fab:menu_labels_textSize="14sp"
        fab:menu_labels_cornerRadius="3dp"
        fab:menu_labels_colorNormal="#333333"
        fab:menu_labels_colorPressed="#444444"
        fab:menu_labels_colorRipple="#66FFFFFF"
        fab:menu_labels_showShadow="true"
        fab:menu_labels_singleLine="false"
        fab:menu_labels_ellipsize="none"
        fab:menu_labels_maxLines="-1"
        fab:menu_labels_style="@style/YourCustomLabelsStyle"
        fab:menu_labels_position="left"
        fab:menu_openDirection="up"
        fab:menu_backgroundColor="@android:color/transparent"
        fab:menu_fab_label="your_label_here"
        fab:menu_fab_show_animation="@anim/my_show_animation"
        fab:menu_fab_hide_animation="@anim/my_hide_animation">

        <com.github.clans.fab.FloatingActionButton
            android:id="@+id/menu_item"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/ic_star"
            fab:fab_size="mini"
            fab:fab_label="Menu item 1" />

    </com.github.clans.fab.FloatingActionMenu>
```
---

## 资料

- Android 控件 RecyclerView：https://www.jianshu.com/p/4f9591291365
- RecyclerView实现Item点击事件处理：https://www.jianshu.com/p/4e5631a5c9bc

- LitePal的使用：https://www.jianshu.com/p/9d0d00b69fe8
- 第三方数据库框架 - LitePal简介：https://www.jianshu.com/p/8035eb5da7a2
- 数据库利器 LitePal2.0使用介绍：https://www.jianshu.com/p/19731df3cd08


- android 字符串转换成JSON对象：https://blog.csdn.net/liufeifeinanfeng/article/details/79424247
- AlertDialog的六种创建方式：https://www.cnblogs.com/shen-hua/p/5709663.html
- AlertDialog入门与详解（多种实现示例：自定义布局等）：https://www.jianshu.com/p/cef3fb27c4e7

- Android开源项目-Easypermissions：https://www.jianshu.com/p/2b3661928e66

- Android仿知乎悬浮功能按钮FloatingActionButton效果:https://www.jb51.net/article/110878.htm
- Android 解决RecyclerView删除Item导致位置错乱的问题:https://www.cnblogs.com/zhujiabin/p/6737117.html

---
