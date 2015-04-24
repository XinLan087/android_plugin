# android_plugin
Android Plugin Lib 
#Plugin README
【原理】
此插件是在此代码上继续开发：https://github.com/singwhatiwanna/dynamic-load-apk
具体原理请参考以上链接。

【工程说明】
>AndroidPlugin_MainHost 宿主程序，直接运行即可

>AndroidPlugin_PluginLib 插件类库，宿主程序会依赖，开发插件得是也会依赖此类库

>其他都是插件APK


【注意】
开发插件的时候，请勿将AndroidPlugoin_Lib插件JAR包编译到插件中，否则无法正常加载插件。



【功能支持】
=================
>A、Activity
>>1、普通Activity

>>2、FragmentActivity

>>3、ListActivity

>>4、默认增加了含有singleInstance,singleTask,singleTop的Activity，FragmentActivity支持

>>5、支持自定义Activity作为宿主代理程序

>>6、支持Activity在所有插件、宿主中查找

>B、Service

>>1、支持插件Service调用，一共12个，用完为止

>>2、支持Service的Bind功能

>>3、支持Service在所有插件、宿主中查找


    
>C、BroadcastReceiver
>>1、支持BroadcastReceiver

>>2、支持广播的动态注册，静态注册

>>3、支持静态广播，在未启动宿主程序的情况下，可以接收广播

>>4、支持广播内启动Activity

>>5、支持BroadcastReceiver在所有插件、宿主中查找

	
>D、Intent
>>1、支持Intent启动插件、或者宿主中的组件

>>2、支持action 等Intent的查找方式

>>3、支持AndroidManifest.xml配置的Intent-Filter格式，可以像普通APK程序一样查找执行

>E、Theme
>>1、支持插件的主题样式，不支持AndroidManifest.xml中设置Theme，但支持在代码中设置Theme，使用setTheme即可支持

>>2、支持自定义attr

>>3、支持自定义的style

	
>F、View
>>1、支持自定义View

>G、Fragment
>>1、支持Fragment

>H、ContentProvider
>>1、暂未支持

>I、so
>>1、支持X86、ARM的so加载

【不支持】
===================
>1、不支持Notification的自定义RemoteViews

>2、不支持ContentProvider

>3、不支持IntentService


