[2015-1-22]
1、增加静态广播，在宿主停止后，继续可以接收广播并调用
2、增加插件广播子类中，打开activity的方法

[2014-11-28]
1、增加startActivityForResult方法，不支持onActivityResult回调
2、重写Activity中的finish,startActivity,startActivityForResult,finishActivity4个方法，删除原启动Activity方法，挪到PluginManager类中
3、完成插件中service的启动，最多12个插件service限制
4、增加service启动activity方法，重写service中stopSelf方法，使用stopSelfProxy方法代替



[2014-11-19]
1、使用android的package name做唯一主键，对应到相关的插件系统中，实现插件升级唯一标识
2、使用package name的方式 处理so文件路径，动态加载到系统中
3、增加PluginManager类，统一对外接口，增加安装、卸载、列出插件等功能 



[2014-11-5]
1、增加动态加载SO的处理，使用DexClassLoader中的参数进行处理，并进行CPU类型判断应该加载哪种SO类库


[2014-10-31]
1、（未处理）使用android的package name做唯一主键，对应到相关的插件系统中，实现插件升级唯一标识
2、（未处理）使用package name的方式 处理so文件路径，动态加载到系统中 




[2014-10-31]
1、如果启动一个插件 ： PluginCache.getIntance(context).startPlugin(apkPath),会自动判断有没有主类等
2、如何通过Intent查找所有插件activity,PluginCache.getIntance(context).queryActivities(intent),查找的所有插件中的activity
3、如何启动一个插件activity，PluginCache.getIntance(context).startPluginIntentActivity(intent)，自动查找并启动结果中的第一个plugin activity
3、如何启动某个插件中的 一个插件activity，PluginCache.getIntance(context).startPluginIntentActivity(intent,apkPath)，自动查找并启动结果中的第一个plugin activity



[2014-10-20]
1、相同JAR包加载同样的自定义View转型异常问题得以解决，解决办法是：
	A、自定义LayoutInflater，使用自定义的ClassLoader加载View
	B、setContentView方法，传入layout id的时候，先使用LayoutInflater转成View，再进行设置
	C、具体实现请看 ClassLoaderAdapter#getLayoutInflater方法


[2014-10-20]
1、测试发现问题：
	A、2个插件程序加载同一个JAR包，并且进行调用，是可以正常使用的
	B、2个插件程序加载了同一个JAR包，并且在XML布局中使用JAR中的自定义View也是可以正常运行的
	C、2个插件程序加载了同一个JAR包，并且在XML布局文件中使用JAR中的自定义View，
		第一个插件名为C1,第二个插件名为C2，C2程序使用findViewById方法，将自定义View转换成类对象，如果是C2先启动，那么没问题；如果是C1先启动，C2后启动，那么就会出现自定义View转型异常。
		经过排查发现问题如下：
			setContentView将xml配置转换成为View对象的时候，使用的是第一次加载此View的ClassLoader，
			在代码中调用的，使用的是当前的ClassLoader；
			虽然包名、类名都一一致，但是不同的ClassLoader加载同一个View，也会被认为是不同对象，出现了转型异常；
			此问题还有待解决，附上日志：
		10-20 10:37:06.787: I/System.out(19804): MyTextView.MyTextView() testplugin_ref_customview1  从这个加载的  com.apkplugin.android.activity.HostClassLoader[dexPath=/storage/emulated/0/plugin/testplugin_ref_customview1.apk,libraryPath=null]
		10-20 10:37:11.005: I/System.out(19804): MyTextView.MyTextView() testplugin_ref_customview2  从这个加载的 com.apkplugin.android.activity.HostClassLoader[dexPath=/storage/emulated/0/plugin/testplugin_ref_customview1.apk,libraryPath=null]
		10-20 10:37:11.005: I/System.out(19804): MainActivity.onCreate() 代码中查看ClassLoader myTextView.classloader  com.apkplugin.android.activity.HostClassLoader[dexPath=/storage/emulated/0/plugin/testplugin_ref_customview2.apk,libraryPath=null]
	
		

[2014-10-09]
1、Fragment在插件中需要继承BasePluginFragment
2、BasePluginFragment通过setUserVisibleHint控制Fragment显示的调用
3、BasePluginFragment中的{@link FragmentPagerAdapter#MyPageChangeListener#onPageSelected(int)}进行调用
4、Fragment在插件中和ViewPager进行混合使用的时候，涉及到PageAdapter的情况下，必须使用com.apkplugin.android.fragment.FragmentPagerAdapter,因为宿主程序对此类进行了重写
5、如果是Fragment中嵌套了Fragment+ViewPager，需要使用getChildFragmentManager()进行构造，例如：GoogleMusicAdapter adapter = new GoogleMusicAdapter(
				getChildFragmentManager());
6、Fragment+ViewPager进行使用的时候，需要在代码最后设置监听器，防止被其他监听器覆盖，例如：indicator.setOnPageChangeListener(adapter.getPageChangeListener());

遗留问题：
	BasePluginFragment在Adapter中的缓存
	BasePluginFragment中的实际记录不一致处理

[2014-09-30]
1、如果宿主程序中的jar包已经加载过，请勿在将此JAR发布到插件的apk中
2、如果宿主程序中的jar包未加载过，在插件中均可同时加载


	
	
