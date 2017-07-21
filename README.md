本应用是智能老人护理系统移动端，主要用于子女监控老人信息，联系老人，提醒老人等功能

开发环境：Windows10 ，Android Studio2.3 
主要测试机：小米note Android7.1.2
NDK依赖：android-ndk64-r10b-windows-x86_64（注意：本项目运行依赖NDK）
Gradle依赖：  
    //v7依赖库
    compile 'com.android.support:appcompat-v7:25.2.0'
    //Design依赖库

    compile 'com.android.support:design:25.2.0'
    //约束布局
    compile 'com.android.support.constraint:constraint-layout:1.0.0-beta4'
    //Cardview卡片布局
    compile 'com.android.support:cardview-v7:25.2.0'
    //圆形头像
    compile 'de.hdodenhof:circleimageview:2.1.0'
    //图表库
    compile 'com.github.PhilJay:MPAndroidChart:v3.0.2'
    //图片加载
    compile 'com.github.bumptech.glide:glide:3.7.0'
    //网络请求
    compile 'com.squareup.okhttp3:okhttp:3.7.0'
    //json解析
    compile 'com.google.code.gson:gson:2.8.0'
    //Sqlite数据储存
    compile 'org.litepal.android:core:1.5.1'
    //水波纹背景
    compile 'com.skyfishjy.ripplebackground:library:1.0.1'
    //Eventbus事件总线
    compile 'de.greenrobot:eventbus:3.0.0-beta1'
    //本项目依赖Agora声网（基于WebRtc）SDK
    Agora Sdk
    //本项目依赖mqttv（MQTT）SDK
    mqttv Sdk

兼容机型：Android4.2-Android7.1
主要请求：MQTT，HTTP



UI及功能实现：本项目基于Android Material Design ，首页分为三个页面，第一页接受心跳传感器，体温传感器数据并动态展示到图表，提供一个发送短提醒的按钮；第二页是吃药提醒页面，提供添加按钮，修改后自动上传服务器，服务器刷新提醒，并在规定时间提醒老人，第三个页面是通话页面，提供拨打电话功能（暂时不展示视频）

注意：1.Android6.0及以上系统需要手动授予相关权限（在APP里面没有特意写出）
      2.MIUI等定制手机系统后台处理模式对于本应用的MQTTService有影响，如果系统强制杀死服务就无法接听终端打来的电话